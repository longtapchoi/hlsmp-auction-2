package fr.maxlego08.zauctionhouse.api.rules;

import java.util.List;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public interface ItemRuleContext {
   ItemStack getItemStack();

   Material getMaterial();

   String getDisplayName();

   boolean hasDisplayName();

   List<String> getLore();

   boolean hasLore();

   int getCustomModelData();

   boolean hasCustomModelData();
}
