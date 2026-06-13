package fr.maxlego08.zauctionhouse.rule.loaders;

import fr.maxlego08.zauctionhouse.api.rules.Rule;
import fr.maxlego08.zauctionhouse.api.rules.RuleConfigHelper;
import fr.maxlego08.zauctionhouse.api.rules.loader.RuleLoader;
import fr.maxlego08.zauctionhouse.api.rules.loader.RuleLoaderRegistry;
import fr.maxlego08.zauctionhouse.rule.rules.AndRule;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"unchecked", "rawtypes"})
public class AndRuleLoader implements RuleLoader {
   private final RuleLoaderRegistry registry;

   public AndRuleLoader(RuleLoaderRegistry var1) {
      this.registry = var1;
   }

   public String getType() {
      return "and";
   }

   public Rule load(Map<?, ?> var1) {
      List var2 = RuleConfigHelper.getMapList(var1, "rules");
      if (var2.isEmpty()) {
         return null;
      } else {
         ArrayList var3 = new ArrayList();

         for(Map var5 : var2) {
            Rule var6 = this.registry.loadRule(var5);
            if (var6 != null) {
               var3.add(var6);
            }
         }

         if (var3.isEmpty()) {
            return null;
         } else if (var3.size() == 1) {
            return (Rule)var3.getFirst();
         } else {
            return new AndRule(var3);
         }
      }
   }
}
