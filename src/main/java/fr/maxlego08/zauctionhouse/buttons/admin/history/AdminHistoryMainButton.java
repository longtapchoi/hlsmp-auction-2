package fr.maxlego08.zauctionhouse.buttons.admin.history;

import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AdminHistoryMainButton extends TargetHelper {
   public AdminHistoryMainButton(Plugin var1) {
      super((AuctionPlugin)var1);
   }

   public void onInventoryOpen(@NotNull Player var1, @NotNull InventoryEngine var2, @NotNull Placeholders var3) {
      this.getTargetName(var1).ifPresent((var1x) -> var3.register("target", var1x));
      super.onInventoryOpen(var1, var2, var3);
   }

   public @Nullable ItemStack getCustomItemStack(@NotNull Player var1, boolean var2, @NotNull Placeholders var3) {
      this.getTargetName(var1).ifPresent((var1x) -> var3.register("target", var1x));
      return super.getCustomItemStack(var1, var2, var3);
   }
}
