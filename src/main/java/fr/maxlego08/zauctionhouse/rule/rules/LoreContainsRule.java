package fr.maxlego08.zauctionhouse.rule.rules;

import fr.maxlego08.zauctionhouse.api.rules.ItemRuleContext;
import fr.maxlego08.zauctionhouse.api.rules.Rule;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LoreContainsRule implements Rule {
   private final List<String> needles;

   public LoreContainsRule(List<String> var1) {
      this.needles = var1.stream().map((var0) -> var0.toLowerCase(Locale.ROOT)).toList();
   }

   public boolean matches(ItemRuleContext var1) {
      if (!var1.hasLore()) {
         return false;
      } else {
         String var2 = (String)var1.getLore().stream().map((var0) -> var0.toLowerCase(Locale.ROOT)).collect(Collectors.joining(" "));
         Stream var10000 = this.needles.stream();
         Objects.requireNonNull(var2);
         return var10000.anyMatch(var2::contains);
      }
   }

   public boolean isValid() {
      return !this.needles.isEmpty();
   }
}
