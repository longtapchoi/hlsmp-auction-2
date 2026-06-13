package fr.maxlego08.zauctionhouse.rule.rules;

import fr.maxlego08.zauctionhouse.api.rules.ItemRuleContext;
import fr.maxlego08.zauctionhouse.api.rules.Rule;
import java.util.List;

public class AndRule implements Rule {
   private final List<Rule> children;

   public AndRule(List<Rule> var1) {
      this.children = var1;
   }

   public boolean matches(ItemRuleContext var1) {
      for(Rule var3 : this.children) {
         if (!var3.matches(var1)) {
            return false;
         }
      }

      return !this.children.isEmpty();
   }

   public boolean isValid() {
      return this.children.isEmpty() ? false : this.children.stream().allMatch(Rule::isValid);
   }
}
