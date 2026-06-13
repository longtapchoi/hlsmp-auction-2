package fr.maxlego08.zauctionhouse.buttons.list;

import fr.maxlego08.menu.api.button.PaginateButton;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.zauctionhouse.api.AuctionManager;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.configuration.records.ItemLoreConfiguration;
import fr.maxlego08.zauctionhouse.api.item.ItemStatus;
import java.util.List;
import java.util.Set;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NonNull;

public class SellingItemsButton extends PaginateButton {
   private final AuctionPlugin plugin;
   private final int emptySlot;

   public SellingItemsButton(Plugin var1, int var2) {
      this.plugin = (AuctionPlugin)var1;
      this.emptySlot = var2;
   }

   public void onRender(Player var1, InventoryEngine var2) {
      AuctionManager var3 = this.plugin.getAuctionManager();
      List var4 = var3.getPlayerSellingItems(var1);
      if (var4.isEmpty()) {
         if (this.emptySlot != -1) {
            var2.addItem(this.emptySlot, this.getCustomItemStack(var1, false, new Placeholders()));
         }
      } else {
         ItemLoreConfiguration var5 = this.plugin.getConfiguration().getItemLore();
         List var6 = var5.sellingLore();
         List var7 = var5.beingPurchasedLore();
         Set var8 = var5.sellingPlaceholders();
         Set var9 = var5.beingPurchasedPlaceholders();
         this.paginate(var4, var2, (var7x, var8x) -> {
            boolean var9x = var8x.getStatus() == ItemStatus.AVAILABLE;
            var2.addItem(var7x, var8x.buildItemStack(var1, var9x ? var6 : var7, var9x ? var8 : var9)).setClick((var3) -> this.plugin.getAuctionManager().getRemoveService().removeSellingItem(var1, var8x));
         });
      }
   }

   public int getPaginationSize(@NonNull Player var1) {
      return this.plugin.getAuctionManager().getPlayerSellingItems(var1).size();
   }
}
