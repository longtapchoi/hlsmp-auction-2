package fr.maxlego08.zauctionhouse.api.hooks.itemcontent;

import java.util.List;
import org.bukkit.inventory.ItemStack;

public interface ItemContentProvider {
   String getName();

   default int getPriority() {
      return 100;
   }

   boolean isContainer(ItemStack var1);

   List<ItemStack> getContents(ItemStack var1);
}
