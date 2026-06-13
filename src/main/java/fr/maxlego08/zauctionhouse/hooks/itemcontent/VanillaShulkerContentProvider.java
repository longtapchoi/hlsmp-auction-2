package fr.maxlego08.zauctionhouse.hooks.itemcontent;

import fr.maxlego08.zauctionhouse.api.hooks.itemcontent.ItemContentProvider;
import fr.maxlego08.zauctionhouse.utils.ShulkerHelper;
import java.util.List;
import org.bukkit.inventory.ItemStack;

public class VanillaShulkerContentProvider implements ItemContentProvider {
   public String getName() {
      return "Vanilla";
   }

   public int getPriority() {
      return 100;
   }

   public boolean isContainer(ItemStack var1) {
      return ShulkerHelper.isShulkerBox(var1);
   }

   public List<ItemStack> getContents(ItemStack var1) {
      return ShulkerHelper.getShulkerContent(var1);
   }
}
