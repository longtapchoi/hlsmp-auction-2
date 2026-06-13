package fr.maxlego08.zauctionhouse.rule.rules;

import fr.maxlego08.zauctionhouse.api.rules.ItemRuleContext;
import fr.maxlego08.zauctionhouse.api.rules.Rule;
import java.util.Set;

public class ModelDataRule implements Rule {
   private final Set<Integer> modelData;

   public ModelDataRule(Set<Integer> var1) {
      this.modelData = var1;
   }

   public boolean matches(ItemRuleContext var1) {
      return !var1.hasCustomModelData() ? false : this.modelData.contains(var1.getCustomModelData());
   }

   public boolean isValid() {
      return !this.modelData.isEmpty();
   }
}
