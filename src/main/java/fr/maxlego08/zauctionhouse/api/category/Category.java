package fr.maxlego08.zauctionhouse.api.category;

import fr.maxlego08.zauctionhouse.api.rules.ItemRuleContext;
import fr.maxlego08.zauctionhouse.api.rules.Rule;
import java.util.List;

public interface Category {
   String getId();

   String getDisplayName();

   List<Rule> getRules();

   List<Rule> getBannedRules();

   boolean isMiscellaneous();

   boolean matches(ItemRuleContext var1);
}
