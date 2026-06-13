package fr.maxlego08.zauctionhouse.api.hooks.itemcontent;

import java.util.List;
import org.bukkit.inventory.ItemStack;

public interface ItemContentManager {
   void registerProvider(ItemContentProvider var1);

   void unregisterProvider(String var1);

   boolean isContainer(ItemStack var1);

   List<ItemStack> getContents(ItemStack var1);

   List<ItemStack> getContainers(List<ItemStack> var1);

   boolean containsContainer(List<ItemStack> var1);
}
