package fr.maxlego08.zauctionhouse.buttons;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.zauctionhouse.ZAuctionPlugin;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCache;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCacheKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public class SearchButton extends Button {
   private final AuctionPlugin plugin;

   public SearchButton(AuctionPlugin var1) {
      this.plugin = var1;
   }

   public boolean isPermanent() {
      return true;
   }

   public ItemStack getCustomItemStack(@NotNull Player var1, boolean var2, @NotNull Placeholders var3) {
      PlayerCache var4 = this.plugin.getAuctionManager().getCache(var1);
      String var5 = (String)var4.get(PlayerCacheKey.SEARCH_QUERY);
      boolean var6 = var5 != null && !var5.isBlank();
      var3.register("search_query", var6 ? var5 : "None");
      var3.register("search_active", var6 ? "true" : "false");
      return this.getItemStack().build(var1, false, var3);
   }

   public void onClick(@NonNull Player var1, @NonNull InventoryClickEvent var2, @NonNull InventoryEngine var3, int var4, @NonNull Placeholders var5) {
      super.onClick(var1, var2, var3, var4, var5);
      PlayerCache var6 = this.plugin.getAuctionManager().getCache(var1);
      String var7 = (String)var6.get(PlayerCacheKey.SEARCH_QUERY);
      if (var2.isRightClick()) {
         if (var7 != null) {
            this.plugin.getAuctionManager().clearSearch(var1);
            this.plugin.getAuctionManager().openMainAuction(var1);
         }

      } else {
         AuctionPlugin var9 = this.plugin;
         if (var9 instanceof ZAuctionPlugin) {
            ZAuctionPlugin var8 = (ZAuctionPlugin)var9;
            var8.getSignSearchListener().startSearch(var1);
         }

      }
   }
}
