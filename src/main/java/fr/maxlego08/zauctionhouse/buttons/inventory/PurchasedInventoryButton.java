package fr.maxlego08.zauctionhouse.buttons.inventory;

import fr.maxlego08.menu.api.button.Button;
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

public class PurchasedInventoryButton extends Button {
   private final AuctionPlugin plugin;

   public PurchasedInventoryButton(Plugin var1) {
      this.plugin = (AuctionPlugin)var1;
   }

   public boolean hasPermission() {
      return true;
   }

   public boolean checkPermission(Player var1, InventoryEngine var2, Placeholders var3) {
      List var4 = this.plugin.getAuctionManager().getPurchasedItems(var1);
      return super.checkPermission(var1, var2, var3) && !var4.isEmpty();
   }

   public void onClick(Player var1, InventoryClickEvent var2, InventoryEngine var3, int var4, Placeholders var5) {
      super.onClick(var1, var2, var3, var4, var5);
      this.plugin.getInventoriesLoader().openInventory(var1, Inventories.PURCHASED_ITEMS);
   }

   public ItemStack getCustomItemStack(@NotNull Player var1, boolean var2, @NotNull Placeholders var3) {
      List var4 = this.plugin.getAuctionManager().getPurchasedItems(var1);
      var3.register("purchased-items", String.valueOf(var4.size()));
      var3.register("s", var4.size() > 1 ? "s" : "");
      return this.getItemStack().build(var1, false, var3);
   }

   public boolean isPermanent() {
      return true;
   }
}
