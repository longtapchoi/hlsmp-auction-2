package fr.maxlego08.zauctionhouse.buttons.admin;

import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.utils.LoreType;
import fr.maxlego08.menu.api.utils.MetaUpdater;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.button.LoadingButton;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCache;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCacheKey;
import fr.maxlego08.zauctionhouse.api.configuration.Configuration;
import fr.maxlego08.zauctionhouse.api.filter.DateFilter;
import fr.maxlego08.zauctionhouse.api.messages.Message;
import fr.maxlego08.zauctionhouse.api.storage.dto.TransactionDTO;
import fr.maxlego08.zauctionhouse.api.transaction.TransactionStatus;
import fr.maxlego08.zauctionhouse.storage.repository.repositories.TransactionRepository;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NonNull;

@SuppressWarnings({"unchecked", "rawtypes"})
public class AdminTransactionsButton extends LoadingButton {
   public AdminTransactionsButton(Plugin var1, int var2) {
      super((AuctionPlugin)var1, var2);
   }

   public void onRender(Player var1, InventoryEngine var2) {
      Optional var3 = this.getTarget(var1);
      if (var3.isEmpty()) {
         this.plugin.getAuctionManager().message(var1, Message.ADMIN_TARGET_REQUIRED);
      } else {
         PlayerCache var4 = this.plugin.getAuctionManager().getCache(var1);
         Boolean var5 = (Boolean)var4.get(PlayerCacheKey.ADMIN_TRANSACTIONS_LOADING, false);
         if (var5) {
            this.showLoadingItem(var2, var1);
         } else {
            Object var6 = (List)var4.get(PlayerCacheKey.ADMIN_TRANSACTIONS_DATA);
            if ((var6 == null || ((List)var6).isEmpty()) && !var4.has(PlayerCacheKey.ADMIN_TRANSACTIONS_DATA)) {
               this.loadTransactionsAsync(var1, (UUID)var3.get(), var2);
            } else {
               if (var6 == null) {
                  var6 = new ArrayList();
               }

               List var7 = this.applyFilters(var4, (List)var6);
               if (var7.isEmpty()) {
                  var2.buildButton(this.getElseButton(), new Placeholders());
               } else {
                  this.paginate(var7, var2, (var2x, var3x) -> {
                     ItemStack var4 = this.buildTransactionItemStack(var3x);
                     var2.addItem(var2x, var4);
                  });
               }
            }
         }
      }
   }

   public int getPaginationSize(@NonNull Player var1) {
      PlayerCache var2 = this.plugin.getAuctionManager().getCache(var1);
      List var3 = (List)var2.get(PlayerCacheKey.ADMIN_TRANSACTIONS_DATA);
      return var3 == null ? 0 : this.applyFilters(var2, var3).size();
   }

   private Optional<UUID> getTarget(Player var1) {
      return Optional.ofNullable((UUID)this.plugin.getAuctionManager().getCache(var1).get(PlayerCacheKey.ADMIN_TARGET_UUID));
   }

   private void loadTransactionsAsync(Player var1, UUID var2, InventoryEngine var3) {
      PlayerCache var4 = this.plugin.getAuctionManager().getCache(var1);
      var4.set(PlayerCacheKey.ADMIN_TRANSACTIONS_LOADING, true);
      this.showLoadingItem(var3, var1);
      CompletableFuture.supplyAsync(() -> ((TransactionRepository)this.plugin.getStorageManager().with(TransactionRepository.class)).selectByPlayer(var2), this.plugin.getExecutorService()).thenAccept((var3x) -> {
         var4.set(PlayerCacheKey.ADMIN_TRANSACTIONS_DATA, var3x);
         var4.set(PlayerCacheKey.ADMIN_TRANSACTIONS_LOADING, false);
         this.plugin.getScheduler().runAtEntity(var1, (var2) -> {
            if (var1.isOnline()) {
               this.plugin.getInventoriesLoader().getInventoryManager().updateInventory(var1);
            }

         });
      }).exceptionally((var2x) -> {
         var4.set(PlayerCacheKey.ADMIN_TRANSACTIONS_LOADING, false);
         this.plugin.getLogger().severe("Failed to load transactions: " + var2x.getMessage());
         return null;
      });
   }

   private void showLoadingItem(InventoryEngine var1, Player var2) {
      var1.addItem(this.loadingSlot, this.getCustomItemStack(var2, false, new Placeholders()));
   }

   private List<TransactionDTO> applyFilters(PlayerCache var1, List<TransactionDTO> var2) {
      TransactionStatus var3 = (TransactionStatus)var1.get(PlayerCacheKey.ADMIN_TRANSACTIONS_STATUS_FILTER);
      DateFilter var4 = (DateFilter)var1.get(PlayerCacheKey.ADMIN_TRANSACTIONS_DATE_FILTER, DateFilter.ALL);
      return var2.stream().filter((var1x) -> var3 == null || var1x.status() == var3).filter((var1x) -> var4.matches(var1x.created_at())).toList();
   }

   private ItemStack buildTransactionItemStack(TransactionDTO var1) {
      Material var2 = var1.status() == TransactionStatus.PENDING ? Material.GOLD_BLOCK : Material.EMERALD_BLOCK;
      Configuration var3 = this.plugin.getConfiguration();
      SimpleDateFormat var4 = var3.getDateFormat();
      ItemStack var5 = new ItemStack(var2);
      ItemMeta var6 = var5.getItemMeta();
      MetaUpdater var7 = this.plugin.getInventoriesLoader().getInventoryManager().getMeta();
      String var8 = var1.status() == TransactionStatus.PENDING ? "#FFD700" : "#00FF00";
      String var9 = var1.value() != null && var1.value().signum() >= 0 ? "+" : "";
      String var10 = var1.value() != null ? var1.value().toPlainString() : "0";
      var7.updateDisplayName(var6, var8 + "<bold>" + var1.status().name() + " #92ffff- #2CCED2" + var9 + var10 + " " + var1.economy_name(), (Player)null);
      ArrayList var11 = new ArrayList();
      var11.add("#8c8c8c• #92ffffStatus: #2CCED2" + var1.status().name());
      var11.add("#8c8c8c• #92ffffEconomy: #2CCED2" + var1.economy_name());
      var11.add("#8c8c8c• #92ffffBefore: #2CCED2" + String.valueOf(var1.before()));
      var11.add("#8c8c8c• #92ffffAfter: #2CCED2" + String.valueOf(var1.after()));
      var11.add("#8c8c8c• #92ffffValue: #2CCED2" + var9 + var10);
      String var10001 = var4.format(var1.created_at());
      var11.add("#8c8c8c• #92ffffDate: #2CCED2" + var10001);
      var7.updateLore(var6, var11, LoreType.REPLACE);
      var5.setItemMeta(var6);
      return var5;
   }
}
