package fr.maxlego08.zauctionhouse.hooks.executableblocks;

import fr.maxlego08.zauctionhouse.api.rules.Rule;
import fr.maxlego08.zauctionhouse.api.rules.RuleConfigHelper;
import fr.maxlego08.zauctionhouse.api.rules.loader.RuleLoader;
import java.util.List;
import java.util.Map;

public class ExecutableBlocksRuleLoader implements RuleLoader {
   public String getType() {
      return "executableblocks";
   }

   public List<String> getAliases() {
      return List.of("executable-blocks", "executable_blocks", "eb");
   }

   public Rule load(Map<?, ?> var1) {
      List var2 = RuleConfigHelper.getStringList(var1, "items");
      return var2.isEmpty() ? null : new ExecutableBlocksRule(var2);
   }
}
