package fr.maxlego08.zauctionhouse.rule.rules;

import fr.maxlego08.zauctionhouse.api.rules.ItemRuleContext;
import fr.maxlego08.zauctionhouse.api.rules.Rule;
import java.util.List;
import java.util.Locale;

public class NameEqualsRule implements Rule {
   private final List<String> names;
   private final boolean ignoreCase;

   public NameEqualsRule(List<String> var1, boolean var2) {
      this.ignoreCase = var2;
      this.names = var2 ? var1.stream().map((var0) -> var0.toLowerCase(Locale.ROOT)).toList() : var1;
   }

   public boolean matches(ItemRuleContext var1) {
      if (!var1.hasDisplayName()) {
         return false;
      } else {
         String var2 = var1.getDisplayName();
         if (this.ignoreCase) {
            var2 = var2.toLowerCase(Locale.ROOT);
         }

         return this.names.contains(var2);
      }
   }

   public boolean isValid() {
      return !this.names.isEmpty();
   }
}
