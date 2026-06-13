package fr.maxlego08.zauctionhouse.buttons.admin.history;

import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.inventories.Inventories;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AdminHistoryMainPurchasedButton extends TargetHelper {
   public AdminHistoryMainPurchasedButton(Plugin var1) {
      super((AuctionPlugin)var1);
   }

   public @Nullable ItemStack getCustomItemStack(@NotNull Player var1, boolean var2, @NotNull Placeholders var3) {
      this.getTargetName(var1).ifPresent((var1x) -> var3.register("target", var1x));
      this.getTargetUniqueId(var1).ifPresent((var2x) -> {
         List var3x = this.plugin.getAuctionManager().getPurchasedItems(var2x);
         var3.register("purchased-items", String.valueOf(var3x.size()));
         var3.register("s", var3x.size() > 1 ? "s" : "");
      });
      return super.getCustomItemStack(var1, var2, var3);
   }

   public void onClick(@NotNull Player var1, @NotNull InventoryClickEvent var2, @NotNull InventoryEngine var3, int var4, @NotNull Placeholders var5) {
      this.plugin.getInventoriesLoader().openInventory(var1, Inventories.ADMIN_PURCHASED_ITEMS);
   }
}
