package fr.maxlego08.zauctionhouse.buttons.sell;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.messages.Message;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;

public class SellCancelButton extends Button {
   private final AuctionPlugin plugin;

   public SellCancelButton(Plugin var1) {
      this.plugin = (AuctionPlugin)var1;
   }

   public void onClick(Player var1, InventoryClickEvent var2, InventoryEngine var3, int var4, Placeholders var5) {
      super.onClick(var1, var2, var3, var4, var5);
      var1.closeInventory();
      this.plugin.getAuctionManager().message(var1, Message.SELL_INVENTORY_CANCELLED);
   }
}
