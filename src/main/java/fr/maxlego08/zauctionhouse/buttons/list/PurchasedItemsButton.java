package fr.maxlego08.zauctionhouse.buttons.list;

import fr.maxlego08.menu.api.button.PaginateButton;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.zauctionhouse.api.AuctionManager;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.configuration.records.ItemLoreConfiguration;
import java.util.List;
import java.util.Set;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NonNull;

public class PurchasedItemsButton extends PaginateButton {
   private final AuctionPlugin plugin;
   private final int emptySlot;

   public PurchasedItemsButton(Plugin var1, int var2) {
      this.plugin = (AuctionPlugin)var1;
      this.emptySlot = var2;
   }

   public void onRender(Player var1, InventoryEngine var2) {
      AuctionManager var3 = this.plugin.getAuctionManager();
      List<? extends fr.maxlego08.zauctionhouse.api.item.Item> var4 = var3.getPurchasedItems(var1);
      if (var4.isEmpty()) {
         if (this.emptySlot != -1) {
            var2.addItem(this.emptySlot, this.getCustomItemStack(var1, false, new Placeholders()));
         }
      } else {
         ItemLoreConfiguration var5 = this.plugin.getConfiguration().getItemLore();
         List<String> var6 = var5.purchasedLore();
         Set<fr.maxlego08.zauctionhouse.api.item.ItemPlaceholder> var7 = var5.purchasedPlaceholders();
         this.paginate(var4, var2, (var5x, var6x) -> var2.addItem(var5x, var6x.buildItemStack(var1, var6, var7)).setClick((var3x) -> var3.getRemoveService().removePurchasedItem(var1, var6x)));
      }
   }

   public int getPaginationSize(@NonNull Player var1) {
      return this.plugin.getAuctionManager().getPurchasedItems(var1).size();
   }
}
