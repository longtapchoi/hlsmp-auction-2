package fr.maxlego08.zauctionhouse.tax;

import fr.maxlego08.zauctionhouse.api.rules.Rule;
import fr.maxlego08.zauctionhouse.api.tax.ItemTaxRule;
import fr.maxlego08.zauctionhouse.api.tax.TaxAmountType;
import fr.maxlego08.zauctionhouse.api.tax.TaxType;
import fr.maxlego08.zauctionhouse.rule.ZItemRuleContext;
import org.bukkit.inventory.ItemStack;

public record ZItemTaxRule(String name, int priority, TaxType taxType, TaxAmountType amountType, double amount, Rule rule) implements ItemTaxRule {
   public String getName() {
      return this.name;
   }

   public int getPriority() {
      return this.priority;
   }

   public TaxType getTaxType() {
      return this.taxType;
   }

   public TaxAmountType getAmountType() {
      return this.amountType;
   }

   public double getAmount() {
      return this.amount;
   }

   public Rule getRule() {
      return this.rule;
   }

   public boolean matches(ItemStack var1) {
      return this.rule != null && var1 != null ? this.rule.matches(new ZItemRuleContext(var1)) : false;
   }
}
