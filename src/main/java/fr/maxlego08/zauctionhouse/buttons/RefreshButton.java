package fr.maxlego08.zauctionhouse.buttons;

import fr.maxlego08.menu.api.MenuItemStack;
import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCache;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCacheKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public class RefreshButton extends Button {
   private final AuctionPlugin plugin;
   private final MenuItemStack loadingItemStack;

   public RefreshButton(AuctionPlugin var1, MenuItemStack var2) {
      this.plugin = var1;
      this.loadingItemStack = var2;
   }

   public boolean isPermanent() {
      return true;
   }

   public ItemStack getCustomItemStack(@NotNull Player var1, boolean var2, @NotNull Placeholders var3) {
      PlayerCache var4 = this.plugin.getAuctionManager().getCache(var1);
      return (Boolean)var4.get(PlayerCacheKey.REFRESH_LOADING) ? this.loadingItemStack.build(var1, false, var3) : this.getItemStack().build(var1, false, var3);
   }

   public void onClick(@NonNull Player var1, @NonNull InventoryClickEvent var2, @NonNull InventoryEngine var3, int var4, @NonNull Placeholders var5) {
      super.onClick(var1, var2, var3, var4, var5);
      PlayerCache var6 = this.plugin.getAuctionManager().getCache(var1);
      if (!(Boolean)var6.get(PlayerCacheKey.REFRESH_LOADING)) {
         var6.set(PlayerCacheKey.REFRESH_LOADING, true);
         ItemStack var7 = this.loadingItemStack.build(var1);

         for(Integer var9 : this.getSlots()) {
            var3.getSpigotInventory().setItem(var9, var7);
         }

         this.plugin.getScheduler().runAsync((var3x) -> {
            var6.remove(PlayerCacheKey.ITEMS_LISTED);
            this.plugin.getAuctionManager().getItemsListedForSale(var1);
            this.plugin.getScheduler().runAtEntity(var1, (var3y) -> {
               var6.set(PlayerCacheKey.REFRESH_LOADING, false);
               this.plugin.getInventoriesLoader().getInventoryManager().updateInventory(var1);
            });
         });
      }
   }
}
