package fr.maxlego08.zauctionhouse.services;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.configuration.records.SalesNotificationConfiguration;
import fr.maxlego08.zauctionhouse.api.economy.AuctionEconomy;
import fr.maxlego08.zauctionhouse.api.economy.EconomyManager;
import fr.maxlego08.zauctionhouse.api.history.ItemLog;
import fr.maxlego08.zauctionhouse.api.inventories.Inventories;
import fr.maxlego08.zauctionhouse.api.messages.Message;
import fr.maxlego08.zauctionhouse.api.services.AuctionHistoryService;
import fr.maxlego08.zauctionhouse.api.storage.StorageManager;
import fr.maxlego08.zauctionhouse.api.storage.dto.LogDTO;
import fr.maxlego08.zauctionhouse.storage.repository.repositories.LogRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;
import org.bukkit.entity.Player;

public class HistoryService extends AuctionService implements AuctionHistoryService {
   private final AuctionPlugin plugin;

   public HistoryService(AuctionPlugin var1) {
      this.plugin = var1;
   }

   public CompletableFuture<List<ItemLog>> getSalesHistory(UUID var1) {
      StorageManager var2 = this.plugin.getStorageManager();
      EconomyManager var3 = this.plugin.getEconomyManager();
      return CompletableFuture.supplyAsync(() -> {
         List<LogDTO> var3L = var2.selectSalesHistory(var1);
         List<? extends fr.maxlego08.zauctionhouse.api.item.Item> var4 = var2.selectItems(var3L.stream().map(LogDTO::item_id).toList());
         ArrayList<ItemLog> var5 = new ArrayList<>();
         var3L.forEach((var3x) -> {
            Optional<? extends fr.maxlego08.zauctionhouse.api.item.Item> var4x = var4.stream().filter((itemX) -> itemX.getId() == var3x.item_id()).findFirst();
            var4x.ifPresentOrElse((item2) -> var5.add(new ItemLog(var3x, item2)), () -> this.plugin.getLogger().warning("Item not found for log ID: " + var3x.id()));
         });
         return var5;
      }, this.plugin.getExecutorService());
   }

   public CompletableFuture<List<LogDTO>> getUnreadSales(UUID var1) {
      return CompletableFuture.supplyAsync(() -> {
         LogRepository var2 = (LogRepository)this.plugin.getStorageManager().with(LogRepository.class);
         return var2.selectUnreadSales(var1);
      }, this.plugin.getExecutorService());
   }

   public CompletableFuture<Void> markSalesAsRead(List<Integer> var1) {
      return CompletableFuture.runAsync(() -> {
         LogRepository var2 = (LogRepository)this.plugin.getStorageManager().with(LogRepository.class);
         var2.markAsRead(var1);
      }, this.plugin.getExecutorService());
   }

   public void handlePlayerJoin(Player var1) {
      SalesNotificationConfiguration var2 = this.plugin.getConfiguration().getSalesNotificationConfiguration();
      if (var2.enabled()) {
         this.getUnreadSales(var1.getUniqueId()).thenAccept((var3) -> {
            if (!var3.isEmpty()) {
               BigDecimal var4 = (BigDecimal)var3.stream().map(LogDTO::price).reduce(BigDecimal.ZERO, BigDecimal::add);
               int var5 = var3.size();
               List var6 = var3.stream().map(LogDTO::id).toList();
               this.markSalesAsRead(var6);
               long var7 = var2.delayTicks();
               Runnable var9 = () -> {
                  if (var1.isOnline()) {
                     String var5x = this.formatTotalEarned(var3, var4);
                     this.message(this.plugin, var1, Message.SALES_NOTIFICATION, new Object[]{"%count%", String.valueOf(var5), "%total%", var5x});
                  }

               };
               if (var7 <= 0L) {
                  this.plugin.getScheduler().runAtEntity(var1, (var1x) -> var9.run());
               } else {
                  this.plugin.getScheduler().runLater((var1x) -> var9.run(), var7);
               }

            }
         }).exceptionally((var2x) -> {
            Logger var10000 = this.plugin.getLogger();
            String var10001 = var1.getName();
            var10000.warning("Failed to fetch unread sales for player " + var10001 + ": " + var2x.getMessage());
            return null;
         });
      }
   }

   public void openHistoryInventory(Player var1) {
      this.openHistoryInventory(var1, 1);
   }

   public void openHistoryInventory(Player var1, int var2) {
      this.plugin.getInventoriesLoader().openInventory(var1, Inventories.HISTORY, var2);
   }

   private String formatTotalEarned(List<LogDTO> var1, BigDecimal var2) {
      EconomyManager var3 = this.plugin.getEconomyManager();
      if (!var1.isEmpty()) {
         String var4 = ((LogDTO)var1.getFirst()).economy_name();
         if (var4 != null) {
            Optional var5 = var3.getEconomy(var4);
            if (var5.isPresent()) {
               return var3.format((AuctionEconomy)((AuctionEconomy)var5.get()), var2);
            }
         }
      }

      return var2.toString();
   }
}
