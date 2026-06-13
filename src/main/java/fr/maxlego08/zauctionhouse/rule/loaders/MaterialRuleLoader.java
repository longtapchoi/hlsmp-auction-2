package fr.maxlego08.zauctionhouse.rule.loaders;

import fr.maxlego08.zauctionhouse.api.rules.Rule;
import fr.maxlego08.zauctionhouse.api.rules.RuleConfigHelper;
import fr.maxlego08.zauctionhouse.api.rules.loader.RuleLoader;
import fr.maxlego08.zauctionhouse.rule.rules.MaterialRule;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.bukkit.Material;

public class MaterialRuleLoader implements RuleLoader {
   public String getType() {
      return "material";
   }

   public Rule load(Map<?, ?> var1) {
      List<String> var2 = RuleConfigHelper.getStringList(var1, "materials");
      if (var2.isEmpty()) {
         return null;
      } else {
         Set<Material> var3 = var2.stream().map((var0) -> {
            try {
               return Material.valueOf(var0.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException var2x) {
               return null;
            }
         }).filter(Objects::nonNull).collect(java.util.stream.Collectors.<Material>toSet());
         return var3.isEmpty() ? null : new MaterialRule(var3);
      }
   }
}
