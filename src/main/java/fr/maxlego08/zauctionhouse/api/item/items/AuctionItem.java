package fr.maxlego08.zauctionhouse.api.item.items;

import fr.maxlego08.zauctionhouse.api.item.Item;
import java.util.List;
import org.bukkit.inventory.ItemStack;

public interface AuctionItem extends Item {
   List<ItemStack> getItemStacks();

   default ItemStack getItemStack() {
      List<ItemStack> itemStacks = this.getItemStacks();
      return itemStacks.isEmpty() ? null : (ItemStack)itemStacks.getFirst();
   }

   String getItemsAsString();
}
