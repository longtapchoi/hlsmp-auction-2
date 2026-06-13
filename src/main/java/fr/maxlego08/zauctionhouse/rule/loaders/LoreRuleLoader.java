package fr.maxlego08.zauctionhouse.rule.loaders;

import fr.maxlego08.zauctionhouse.api.rules.Rule;
import fr.maxlego08.zauctionhouse.api.rules.RuleConfigHelper;
import fr.maxlego08.zauctionhouse.api.rules.loader.RuleLoader;
import fr.maxlego08.zauctionhouse.rule.rules.LoreContainsRule;
import fr.maxlego08.zauctionhouse.rule.rules.LoreEqualsRule;
import java.util.List;
import java.util.Map;

public class LoreRuleLoader implements RuleLoader {
   public String getType() {
      return "lore";
   }

   public Rule load(Map<?, ?> var1) {
      String var2 = RuleConfigHelper.getString(var1, "mode");
      List var3 = RuleConfigHelper.getStringList(var1, "values");
      if (var3.isEmpty()) {
         return null;
      } else {
         return (Rule)("EQUALS".equalsIgnoreCase(var2) ? new LoreEqualsRule(var3) : new LoreContainsRule(var3));
      }
   }
}
