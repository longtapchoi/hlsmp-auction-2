package fr.maxlego08.zauctionhouse.rule.loaders;

import fr.maxlego08.zauctionhouse.api.rules.Rule;
import fr.maxlego08.zauctionhouse.api.rules.RuleConfigHelper;
import fr.maxlego08.zauctionhouse.api.rules.loader.RuleLoader;
import fr.maxlego08.zauctionhouse.rule.rules.AndRule;
import fr.maxlego08.zauctionhouse.rule.rules.ModelDataRangeRule;
import fr.maxlego08.zauctionhouse.rule.rules.ModelDataRule;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class CustomModelDataRuleLoader implements RuleLoader {
   public String getType() {
      return "custom-model-data";
   }

   public Rule load(Map<?, ?> var1) {
      ArrayList<fr.maxlego08.zauctionhouse.rule.rules.ModelDataRangeRule> var2 = new ArrayList<>();
      List<Map<?, ?>> var3 = RuleConfigHelper.getMapList(var1, "ranges");
      if (!var3.isEmpty()) {
         for(Map<?, ?> var5 : var3) {
            int var6 = RuleConfigHelper.getInt(var5, "min", 0);
            int var7 = RuleConfigHelper.getInt(var5, "max", Integer.MAX_VALUE);
            var2.add(new ModelDataRangeRule(var6, var7));
         }
      }

      List var8 = RuleConfigHelper.getIntegerList(var1, "values");
      if (!var8.isEmpty()) {
         var2.add(new ModelDataRule(new HashSet(var8)));
      }

      if (var2.isEmpty()) {
         return null;
      } else {
         return (Rule)(var2.size() == 1 ? (Rule)var2.getFirst() : new AndRule(var2));
      }
   }
}
