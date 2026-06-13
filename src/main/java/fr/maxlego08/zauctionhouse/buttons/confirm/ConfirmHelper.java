package fr.maxlego08.zauctionhouse.buttons.confirm;

import fr.maxlego08.menu.api.Inventory;
import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.zauctionhouse.api.AuctionManager;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCache;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCacheKey;
import fr.maxlego08.zauctionhouse.api.item.Item;
import fr.maxlego08.zauctionhouse.api.item.ItemStatus;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jspecify.annotations.NonNull;

public abstract class ConfirmHelper extends Button {
   protected final AuctionPlugin plugin;
   private final ItemStatus previous;
   private final ItemStatus next;

   public ConfirmHelper(AuctionPlugin var1, ItemStatus var2, ItemStatus var3) {
      this.plugin = var1;
      this.previous = var2;
      this.next = var3;
   }

   public void onInventoryClose(@NonNull Player var1, @NonNull InventoryEngine var2) {
      super.onInventoryClose(var1, var2);
      AuctionManager var3 = this.plugin.getAuctionManager();
      PlayerCache var4 = var3.getCache(var1);
      Item var5 = (Item)var4.get(PlayerCacheKey.ITEM_SHOW);
      if (var5 != null) {
         if (var5.getStatus() == this.previous) {
            var5.setStatus(this.next);
            this.plugin.getAuctionClusterBridge().notifyItemStatusChange(var5, this.previous, this.next).exceptionally((var1x) -> {
               this.plugin.getLogger().warning("Failed to notify item status change on inventory close: " + var1x.getMessage());
               return null;
            });
            var3.clearPlayersCache(PlayerCacheKey.ITEMS_LISTED, PlayerCacheKey.ITEMS_SEARCH);
            var3.updateListedItems(var5, true, var1);
         }

      }
   }

   public void onBackClick(@NonNull Player var1, @NonNull InventoryClickEvent var2, @NonNull InventoryEngine var3, @NonNull List<Inventory> var4, @NonNull Inventory var5, int var6) {
      super.onBackClick(var1, var2, var3, var4, var5, var6);
      AuctionManager var7 = this.plugin.getAuctionManager();
      PlayerCache var8 = var7.getCache(var1);
      Item var9 = (Item)var8.get(PlayerCacheKey.ITEM_SHOW);
      if (var9 != null) {
         var9.setStatus(this.next);
         this.plugin.getAuctionClusterBridge().notifyItemStatusChange(var9, this.previous, this.next).exceptionally((var1x) -> {
            this.plugin.getLogger().warning("Failed to notify item status change on back click: " + var1x.getMessage());
            return null;
         });
         var7.clearPlayerCache(var1, PlayerCacheKey.ITEMS_LISTED);
         var7.updateListedItems(var9, true, var1);
      }
   }

   public void onClick(@NonNull Player var1, @NonNull InventoryClickEvent var2, @NonNull InventoryEngine var3, int var4, @NonNull Placeholders var5) {
      super.onClick(var1, var2, var3, var4, var5);
      AuctionManager var6 = this.plugin.getAuctionManager();
      Item var7 = (Item)var6.getCache(var1).get(PlayerCacheKey.ITEM_SHOW);
      if (var7 == null) {
         var6.openMainAuction(var1);
      } else {
         this.onPostClick(var1, var2, var3, var4, var5, var6, var7);
      }
   }

   protected abstract void onPostClick(@NonNull Player var1, @NonNull InventoryClickEvent var2, @NonNull InventoryEngine var3, int var4, @NonNull Placeholders var5, AuctionManager var6, Item var7);
}
