package fr.maxlego08.zauctionhouse.rule.rules;

import fr.maxlego08.zauctionhouse.api.rules.ItemRuleContext;
import fr.maxlego08.zauctionhouse.api.rules.Rule;

public class ModelDataRangeRule implements Rule {
   private final int min;
   private final int max;

   public ModelDataRangeRule(int var1, int var2) {
      this.min = var1;
      this.max = var2;
   }

   public boolean matches(ItemRuleContext var1) {
      if (!var1.hasCustomModelData()) {
         return false;
      } else {
         int var2 = var1.getCustomModelData();
         return var2 >= this.min && var2 <= this.max;
      }
   }

   public boolean isValid() {
      return this.min <= this.max;
   }
}
