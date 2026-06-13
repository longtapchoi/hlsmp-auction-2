package fr.maxlego08.zauctionhouse.rule.loaders;

import fr.maxlego08.zauctionhouse.api.rules.Rule;
import fr.maxlego08.zauctionhouse.api.rules.RuleConfigHelper;
import fr.maxlego08.zauctionhouse.api.rules.loader.RuleLoader;
import fr.maxlego08.zauctionhouse.rule.rules.NameContainsRule;
import fr.maxlego08.zauctionhouse.rule.rules.NameEqualsRule;
import java.util.List;
import java.util.Map;

public class NameRuleLoader implements RuleLoader {
   public String getType() {
      return "name";
   }

   public Rule load(Map<?, ?> var1) {
      String var2 = RuleConfigHelper.getString(var1, "mode");
      List var3 = RuleConfigHelper.getStringList(var1, "values");
      if (var3.isEmpty()) {
         return null;
      } else if ("EQUALS".equalsIgnoreCase(var2)) {
         boolean var4 = RuleConfigHelper.getBoolean(var1, "ignore-case", true);
         return new NameEqualsRule(var3, var4);
      } else {
         return new NameContainsRule(var3);
      }
   }
}
