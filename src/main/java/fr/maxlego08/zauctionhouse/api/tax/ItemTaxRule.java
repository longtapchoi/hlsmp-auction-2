package fr.maxlego08.zauctionhouse.api.tax;

import fr.maxlego08.zauctionhouse.api.rules.Rule;
import org.bukkit.inventory.ItemStack;

public interface ItemTaxRule {
   String getName();

   int getPriority();

   TaxType getTaxType();

   TaxAmountType getAmountType();

   double getAmount();

   Rule getRule();

   boolean matches(ItemStack var1);
}
