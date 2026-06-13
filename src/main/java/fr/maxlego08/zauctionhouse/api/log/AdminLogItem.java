package fr.maxlego08.zauctionhouse.api.log;

import fr.maxlego08.zauctionhouse.api.storage.dto.LogDTO;
import java.util.List;
import org.bukkit.inventory.ItemStack;

public record AdminLogItem(LogDTO log, List<ItemStack> itemStacks) {
   public boolean hasMultipleItems() {
      return this.itemStacks != null && this.itemStacks.size() > 1;
   }

   public ItemStack getFirstItem() {
      return this.itemStacks != null && !this.itemStacks.isEmpty() ? (ItemStack)this.itemStacks.getFirst() : null;
   }
}
