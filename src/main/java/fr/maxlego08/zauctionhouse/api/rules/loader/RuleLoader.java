package fr.maxlego08.zauctionhouse.api.rules.loader;

import fr.maxlego08.zauctionhouse.api.rules.Rule;
import java.util.List;
import java.util.Map;

public interface RuleLoader {
   String getType();

   default List<String> getAliases() {
      return List.of();
   }

   Rule load(Map<?, ?> var1);
}
