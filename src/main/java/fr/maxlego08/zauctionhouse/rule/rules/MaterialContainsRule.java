package fr.maxlego08.zauctionhouse.rule.rules;

import fr.maxlego08.zauctionhouse.api.rules.ItemRuleContext;
import fr.maxlego08.zauctionhouse.api.rules.Rule;
import java.util.List;
import java.util.Locale;

public class MaterialContainsRule implements Rule {
   private final List<String> patterns;

   public MaterialContainsRule(List<String> var1) {
      this.patterns = var1.stream().map((var0) -> var0.toUpperCase(Locale.ROOT)).toList();
   }

   public boolean matches(ItemRuleContext var1) {
      String var2 = var1.getMaterial().name();

      for(String var4 : this.patterns) {
         if (var2.contains(var4)) {
            return true;
         }
      }

      return false;
   }

   public boolean isValid() {
      return !this.patterns.isEmpty();
   }
}
