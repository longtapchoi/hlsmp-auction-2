package fr.maxlego08.zauctionhouse.api.rules;

import java.util.ArrayList;
import java.util.List;

public record Rules(boolean enabled, List<Rule> rules) implements Rule {
   public Rules(boolean enabled, List<Rule> rules) {
      this.enabled = enabled;
      this.rules = List.copyOf(rules);
   }

   public boolean matches(ItemRuleContext var1) {
      if (!this.enabled) {
         return false;
      } else {
         for(Rule var3 : this.rules) {
            if (var3.matches(var1)) {
               return true;
            }
         }

         return false;
      }
   }

   public Rules withEnabled(boolean var1) {
      return new Rules(var1, this.rules);
   }

   public Rules withAddedRule(Rule var1) {
      ArrayList var2 = new ArrayList(this.rules);
      var2.add(var1);
      return new Rules(this.enabled, var2);
   }

   public boolean isValid() {
      return this.rules.isEmpty() ? false : this.rules.stream().anyMatch(Rule::isValid);
   }

   public static Rules emptyDisabled() {
      return new Rules(false, List.of());
   }
}
