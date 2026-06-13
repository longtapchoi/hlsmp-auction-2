package fr.maxlego08.zauctionhouse.buttons;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.zauctionhouse.api.AuctionManager;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.configuration.Configuration;
import fr.maxlego08.zauctionhouse.api.item.ItemType;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings({"unchecked", "rawtypes"})
public class SellLimitButton extends Button {
   private final AuctionPlugin plugin;
   private final List<ItemType> itemTypes;

   public SellLimitButton(AuctionPlugin var1, List<ItemType> var2) {
      this.plugin = var1;
      this.itemTypes = var2;
   }

   public boolean hasSpecialRender() {
      return true;
   }

   public void onRender(Player var1, InventoryEngine var2) {
      Configuration var3 = this.plugin.getConfiguration();
      AuctionManager var4 = this.plugin.getAuctionManager();
      int var5 = 0;

      for(ItemType var7 : this.itemTypes) {
         var5 += var3.getPermission().getLimit(var7, var1);
      }

      int var11 = var4.getPlayerSellingItems(var1).size();
      int var12 = Math.max(0, var5 - var11);
      ArrayList var8 = new ArrayList(this.getSlots());
      var8.forEach((var1x) -> {
         var2.getSpigotInventory().setItem(var1x, (ItemStack)null);
         var2.removeItem(var1x);
      });
      int var9 = Math.min(var12, var8.size());

      for(int var10 = 0; var10 < var9; ++var10) {
         var2.addItem((Integer)var8.get(var10), this.getItemStack().build(var1, false, new Placeholders()));
      }

   }
}
