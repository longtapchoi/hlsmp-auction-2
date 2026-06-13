package fr.maxlego08.zauctionhouse.rule.loaders;

import fr.maxlego08.zauctionhouse.api.rules.Rule;
import fr.maxlego08.zauctionhouse.api.rules.RuleConfigHelper;
import fr.maxlego08.zauctionhouse.api.rules.loader.RuleLoader;
import fr.maxlego08.zauctionhouse.rule.rules.MaterialSuffixRule;
import java.util.List;
import java.util.Map;

public class MaterialSuffixRuleLoader implements RuleLoader {
   public String getType() {
      return "material-suffix";
   }

   public Rule load(Map<?, ?> var1) {
      List var2 = RuleConfigHelper.getStringList(var1, "suffixes");
      return var2.isEmpty() ? null : new MaterialSuffixRule(var2);
   }
}
