package fr.maxlego08.zauctionhouse.hooks.slimefun;

import fr.maxlego08.zauctionhouse.api.rules.Rule;
import fr.maxlego08.zauctionhouse.api.rules.RuleConfigHelper;
import fr.maxlego08.zauctionhouse.api.rules.loader.RuleLoader;
import java.util.List;
import java.util.Map;

public class SlimefunRuleLoader implements RuleLoader {
   public String getType() {
      return "slimefun";
   }

   public List<String> getAliases() {
      return List.of("slime-fun", "slime_fun", "sf");
   }

   public Rule load(Map<?, ?> var1) {
      List var2 = RuleConfigHelper.getStringList(var1, "items");
      return var2.isEmpty() ? null : new SlimefunRule(var2);
   }
}
