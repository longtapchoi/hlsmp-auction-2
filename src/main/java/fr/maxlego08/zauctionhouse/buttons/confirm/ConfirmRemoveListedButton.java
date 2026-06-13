package fr.maxlego08.zauctionhouse.buttons.confirm;

import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.zauctionhouse.api.AuctionManager;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.item.Item;
import fr.maxlego08.zauctionhouse.api.item.ItemStatus;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NonNull;

public class ConfirmRemoveListedButton extends ConfirmHelper {
   public ConfirmRemoveListedButton(Plugin var1) {
      super((AuctionPlugin)var1, ItemStatus.IS_REMOVE_CONFIRM, ItemStatus.AVAILABLE);
   }

   protected void onPostClick(@NonNull Player var1, @NonNull InventoryClickEvent var2, @NonNull InventoryEngine var3, int var4, @NonNull Placeholders var5, AuctionManager var6, Item var7) {
      var6.getRemoveService().removeListedItem(var1, var7);
   }
}
