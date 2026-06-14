package fr.maxlego08.zauctionhouse.api.rules.loader;

import fr.maxlego08.zauctionhouse.api.rules.Rule;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface RuleLoaderRegistry {
   void registerDefaultLoaders();

   void register(RuleLoader var1);

   void unregister(String var1);

   Optional<RuleLoader> getLoader(String var1);

   List<RuleLoader> getLoaders();

   Rule loadRule(Map<?, ?> var1);

   List<Rule> loadRules(Map<?, ?> var1);

   List<Rule> loadRulesFromList(List<Map<?, ?>> var1);
}
