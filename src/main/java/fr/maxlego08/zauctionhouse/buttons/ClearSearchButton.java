package fr.maxlego08.zauctionhouse.buttons;

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

public class ClearSearchButton extends Button {
   private final AuctionPlugin plugin;

   public ClearSearchButton(AuctionPlugin var1) {
      this.plugin = var1;
   }

   public boolean isPermanent() {
      return true;
   }

   public boolean hasPermission() {
      return true;
   }

   public boolean checkPermission(@NotNull Player var1, @NotNull InventoryEngine var2, @NotNull Placeholders var3) {
      PlayerCache var4 = this.plugin.getAuctionManager().getCache(var1);
      String var5 = (String)var4.get(PlayerCacheKey.SEARCH_QUERY);
      return super.checkPermission(var1, var2, var3) && var5 != null && !var5.isEmpty();
   }

   public ItemStack getCustomItemStack(@NotNull Player var1, boolean var2, @NotNull Placeholders var3) {
      PlayerCache var4 = this.plugin.getAuctionManager().getCache(var1);
      String var5 = (String)var4.get(PlayerCacheKey.SEARCH_QUERY);
      if (var5 != null && !var5.isEmpty()) {
         var3.register("search_query", var5);
         return this.getItemStack().build(var1, false, var3);
      } else {
         return null;
      }
   }

   public void onClick(@NonNull Player var1, @NonNull InventoryClickEvent var2, @NonNull InventoryEngine var3, int var4, @NonNull Placeholders var5) {
      super.onClick(var1, var2, var3, var4, var5);
      this.plugin.getAuctionManager().clearSearch(var1);
      this.plugin.getAuctionManager().openMainAuction(var1);
   }
}
