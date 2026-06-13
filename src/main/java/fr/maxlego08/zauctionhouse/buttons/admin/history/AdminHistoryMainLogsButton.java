package fr.maxlego08.zauctionhouse.buttons.admin.history;

import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCache;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCacheKey;
import fr.maxlego08.zauctionhouse.api.inventories.Inventories;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AdminHistoryMainLogsButton extends TargetHelper {
   public AdminHistoryMainLogsButton(Plugin var1) {
      super((AuctionPlugin)var1);
   }

   public @Nullable ItemStack getCustomItemStack(@NotNull Player var1, boolean var2, @NotNull Placeholders var3) {
      this.getTargetName(var1).ifPresent((var1x) -> var3.register("target", var1x));
      return super.getCustomItemStack(var1, var2, var3);
   }

   public void onClick(@NotNull Player var1, @NotNull InventoryClickEvent var2, @NotNull InventoryEngine var3, int var4, @NotNull Placeholders var5) {
      PlayerCache var6 = this.plugin.getAuctionManager().getCache(var1);
      var6.remove(PlayerCacheKey.ADMIN_LOGS_DATA, PlayerCacheKey.ADMIN_LOGS_LOADING, PlayerCacheKey.ADMIN_LOGS_TYPE_FILTER, PlayerCacheKey.ADMIN_LOGS_DATE_FILTER);
      var6.set(PlayerCacheKey.CURRENT_PAGE, 1);
      this.plugin.getInventoriesLoader().openInventory(var1, Inventories.ADMIN_LOGS);
   }
}
