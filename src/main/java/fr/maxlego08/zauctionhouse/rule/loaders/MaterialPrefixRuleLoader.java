package fr.maxlego08.zauctionhouse.rule.loaders;

import fr.maxlego08.zauctionhouse.api.rules.Rule;
import fr.maxlego08.zauctionhouse.api.rules.RuleConfigHelper;
import fr.maxlego08.zauctionhouse.api.rules.loader.RuleLoader;
import fr.maxlego08.zauctionhouse.rule.rules.MaterialPrefixRule;
import java.util.List;
import java.util.Map;

public class MaterialPrefixRuleLoader implements RuleLoader {
   public String getType() {
      return "material-prefix";
   }

   public Rule load(Map<?, ?> var1) {
      List var2 = RuleConfigHelper.getStringList(var1, "prefixes");
      return var2.isEmpty() ? null : new MaterialPrefixRule(var2);
   }
}
