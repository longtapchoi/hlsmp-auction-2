package fr.maxlego08.zauctionhouse.rule.rules;

import fr.maxlego08.zauctionhouse.api.rules.ItemRuleContext;
import fr.maxlego08.zauctionhouse.api.rules.Rule;
import java.util.List;
import java.util.Locale;

public class LoreEqualsRule implements Rule {
   private final List<String> loreLines;

   public LoreEqualsRule(List<String> var1) {
      this.loreLines = var1.stream().map((var0) -> var0.toLowerCase(Locale.ROOT)).toList();
   }

   public boolean matches(ItemRuleContext var1) {
      if (!var1.hasLore()) {
         return false;
      } else {
         for(String var3 : var1.getLore()) {
            String var4 = var3.toLowerCase(Locale.ROOT);
            if (this.loreLines.contains(var4)) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean isValid() {
      return !this.loreLines.isEmpty();
   }
}
