package fr.maxlego08.zauctionhouse.buttons.shulker;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCache;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCacheKey;
import fr.maxlego08.zauctionhouse.api.hooks.itemcontent.ItemContentManager;
import fr.maxlego08.zauctionhouse.api.item.Item;
import fr.maxlego08.zauctionhouse.api.item.items.AuctionItem;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public class ShulkerNavigationButton extends Button {
   private final AuctionPlugin plugin;
   private final boolean isNext;

   public ShulkerNavigationButton(Plugin var1, boolean var2) {
      this.plugin = (AuctionPlugin)var1;
      this.isNext = var2;
   }

   public boolean hasPermission() {
      return true;
   }

   public boolean checkPermission(@NonNull Player var1, @NonNull InventoryEngine var2, @NonNull Placeholders var3) {
      ItemContentManager var4 = this.plugin.getItemContentManager();
      PlayerCache var5 = this.plugin.getAuctionManager().getCache(var1);
      Item var6 = (Item)var5.get(PlayerCacheKey.ITEM_SHOW);
      if (var6 instanceof AuctionItem var7) {
         List var8 = var4.getContainers(var7.getItemStacks());
         if (var8.size() <= 1) {
            return false;
         } else {
            int var9 = (Integer)var5.get(PlayerCacheKey.SHULKER_INDEX, 0);
            if (this.isNext) {
               return var9 < var8.size() - 1;
            } else {
               return var9 > 0;
            }
         }
      } else {
         return false;
      }
   }

   public void onClick(@NotNull Player var1, @NotNull InventoryClickEvent var2, @NotNull InventoryEngine var3, int var4, @NotNull Placeholders var5) {
      super.onClick(var1, var2, var3, var4, var5);
      ItemContentManager var6 = this.plugin.getItemContentManager();
      PlayerCache var7 = this.plugin.getAuctionManager().getCache(var1);
      Item var8 = (Item)var7.get(PlayerCacheKey.ITEM_SHOW);
      if (var8 instanceof AuctionItem var9) {
         List var10 = var6.getContainers(var9.getItemStacks());
         if (!var10.isEmpty()) {
            int var11 = (Integer)var7.get(PlayerCacheKey.SHULKER_INDEX, 0);
            if (this.isNext) {
               var11 = Math.min(var11 + 1, var10.size() - 1);
            } else {
               var11 = Math.max(var11 - 1, 0);
            }

            var7.set(PlayerCacheKey.SHULKER_INDEX, var11);
            List var12 = var6.getContents((ItemStack)var10.get(var11));
            var7.set(PlayerCacheKey.SHULKER_ITEMS, var12);
            this.plugin.getInventoriesLoader().getInventoryManager().updateInventory(var1);
         }
      }
   }
}
