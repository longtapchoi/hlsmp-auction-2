package fr.maxlego08.zauctionhouse.rule.loaders;

import fr.maxlego08.zauctionhouse.api.rules.Rule;
import fr.maxlego08.zauctionhouse.api.rules.RuleConfigHelper;
import fr.maxlego08.zauctionhouse.api.rules.loader.RuleLoader;
import fr.maxlego08.zauctionhouse.rule.rules.MaterialContainsRule;
import java.util.List;
import java.util.Map;

public class MaterialContainsRuleLoader implements RuleLoader {
   public String getType() {
      return "material-contains";
   }

   public Rule load(Map<?, ?> var1) {
      List var2 = RuleConfigHelper.getStringList(var1, "patterns");
      return var2.isEmpty() ? null : new MaterialContainsRule(var2);
   }
}
