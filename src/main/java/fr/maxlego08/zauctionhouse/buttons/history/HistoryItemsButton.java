package fr.maxlego08.zauctionhouse.buttons.history;

import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.utils.LoreType;
import fr.maxlego08.menu.api.utils.MetaUpdater;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.zauctionhouse.api.AuctionManager;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.button.LoadingButton;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCache;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCacheKey;
import fr.maxlego08.zauctionhouse.api.configuration.Configuration;
import fr.maxlego08.zauctionhouse.api.history.HistorySortType;
import fr.maxlego08.zauctionhouse.api.history.ItemLog;
import fr.maxlego08.zauctionhouse.api.item.Item;
import fr.maxlego08.zauctionhouse.api.item.items.AuctionItem;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NonNull;

public class HistoryItemsButton extends LoadingButton {
   public HistoryItemsButton(Plugin var1, int var2) {
      super((AuctionPlugin)var1, var2);
   }

   public void onRender(Player var1, InventoryEngine var2) {
      AuctionManager var3 = this.plugin.getAuctionManager();
      PlayerCache var4 = var3.getCache(var1);
      if (!var4.has(PlayerCacheKey.HISTORY_DATA)) {
         if (this.loadingSlot != -1) {
            var2.addItem(this.loadingSlot, this.getCustomItemStack(var1, false, new Placeholders()));
         }

         Boolean var6 = (Boolean)var4.get(PlayerCacheKey.HISTORY_LOADING, false);
         if (!var6) {
            var4.set(PlayerCacheKey.HISTORY_LOADING, true);
            var3.getHistoryService().getSalesHistory(var1.getUniqueId()).thenAccept((var3x) -> {
               if (var1.isOnline()) {
                  var4.set(PlayerCacheKey.HISTORY_DATA, var3x);
                  var4.set(PlayerCacheKey.HISTORY_LOADING, false);
                  this.plugin.getScheduler().runAtEntity(var1, (var2w) -> {
                     if (var1.isOnline()) {
                        this.plugin.getAuctionManager().getHistoryService().openHistoryInventory(var1);
                     }

                  });
               }
            }).exceptionally((var2x) -> {
               var4.set(PlayerCacheKey.HISTORY_LOADING, false);
               this.plugin.getLogger().severe("Failed to load history: " + var2x.getMessage());
               return null;
            });
         }

      } else {
         List<ItemLog> var5 = var4.get(PlayerCacheKey.HISTORY_DATA);
         this.displayHistory(var1, var2, var5);
      }
   }

   private void displayHistory(Player var1, InventoryEngine var2, List<ItemLog> var3) {
      if (var3 != null && !var3.isEmpty()) {
         Configuration var4 = this.plugin.getConfiguration();
         SimpleDateFormat var5 = var4.getDateFormat();
         List<String> var6 = var4.getItemLore().historyLore();
         PlayerCache var7 = this.plugin.getAuctionManager().getCache(var1);
         HistorySortType var8 = var7.get(PlayerCacheKey.HISTORY_SORT, HistorySortType.DATE_DESC);
         List<ItemLog> var9 = var3.stream().sorted(var8.getComparator()).toList();
         this.paginate(var9, var2, (var4x, var5x) -> {
            ItemStack var6x = this.createDisplayItem(var5x, var5, var6);
            var2.addItem(var4x, var6x);
         });
      } else {
         var2.buildButton(this.getElseButton(), new Placeholders());
      }
   }

   public int getPaginationSize(@NonNull Player var1) {
      PlayerCache var2 = this.plugin.getAuctionManager().getCache(var1);
      List<ItemLog> var3 = var2.get(PlayerCacheKey.HISTORY_DATA);
      return var3 != null ? var3.size() : 0;
   }

   private ItemStack createDisplayItem(ItemLog var1, SimpleDateFormat var2, List<String> var3) {
      Item var6 = var1.item();
      ItemStack var4;
      if (var6 instanceof AuctionItem var5) {
         var4 = var5.getItemStack();
      } else {
         var4 = new ItemStack(Material.PAPER);
      }

      var4 = var4.clone();
      ItemMeta var9 = var4.getItemMeta();
      if (var9 == null) {
         return var4;
      } else {
         Placeholders var10 = new Placeholders();
         var10.register("buyer", var1.item().getBuyerName());
         var10.register("seller", var1.item().getSellerName());
         var10.register("price", var1.item().getFormattedPrice());
         var10.register("date", var2.format(var1.log().created_at()));
         MetaUpdater var7 = this.plugin.getInventoriesLoader().getInventoryManager().getMeta();
         Stream<String> var10002 = var3.stream();
         Objects.requireNonNull(var10);
         var7.updateLore(var9, var10002.map(var10::parse).toList(), LoreType.APPEND);
         var4.setItemMeta(var9);
         return var4;
      }
   }
}
