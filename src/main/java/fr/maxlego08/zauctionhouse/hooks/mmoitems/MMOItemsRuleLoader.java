package fr.maxlego08.zauctionhouse.hooks.mmoitems;

import fr.maxlego08.zauctionhouse.api.rules.Rule;
import fr.maxlego08.zauctionhouse.api.rules.RuleConfigHelper;
import fr.maxlego08.zauctionhouse.api.rules.loader.RuleLoader;
import java.util.List;
import java.util.Map;

public class MMOItemsRuleLoader implements RuleLoader {
   public String getType() {
      return "mmoitems";
   }

   public List<String> getAliases() {
      return List.of("mmo-items", "mmo_items");
   }

   public Rule load(Map<?, ?> var1) {
      List var2 = RuleConfigHelper.getStringList(var1, "items");
      return var2.isEmpty() ? null : new MMOItemsRule(var2);
   }
}
