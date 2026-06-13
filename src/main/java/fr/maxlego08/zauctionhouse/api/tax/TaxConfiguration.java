package fr.maxlego08.zauctionhouse.api.tax;

import java.math.BigDecimal;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface TaxConfiguration {
   boolean isEnabled();

   TaxType getTaxType();

   TaxAmountType getAmountType();

   double getAmount();

   String getBypassPermission();

   List<TaxReduction> getReductions();

   boolean hasItemRules();

   List<ItemTaxRule> getItemRules();

   TaxResult calculateSellTax(Player var1, BigDecimal var2, ItemStack var3);

   TaxResult calculatePurchaseTax(Player var1, BigDecimal var2, ItemStack var3);

   boolean canBypass(Player var1);

   TaxReduction getReduction(Player var1);
}
