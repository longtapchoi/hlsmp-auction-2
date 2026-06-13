package fr.maxlego08.zauctionhouse.rule.loaders;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.rules.Rule;
import fr.maxlego08.zauctionhouse.api.rules.RuleConfigHelper;
import fr.maxlego08.zauctionhouse.api.rules.loader.RuleLoader;
import fr.maxlego08.zauctionhouse.rule.rules.TagRule;
import java.util.List;
import java.util.Map;

public class TagRuleLoader implements RuleLoader {
   private final AuctionPlugin auctionPlugin;

   public TagRuleLoader(AuctionPlugin var1) {
      this.auctionPlugin = var1;
   }

   public String getType() {
      return "material-tag";
   }

   public List<String> getAliases() {
      return List.of("tag");
   }

   public Rule load(Map<?, ?> var1) {
      List var2 = RuleConfigHelper.getStringList(var1, "tags");
      if (var2.isEmpty()) {
         String var3 = RuleConfigHelper.getString(var1, "tag");
         if (var3 != null) {
            var2 = List.of(var3);
         }
      }

      return var2.isEmpty() ? null : new TagRule(this.auctionPlugin, var2);
   }
}
