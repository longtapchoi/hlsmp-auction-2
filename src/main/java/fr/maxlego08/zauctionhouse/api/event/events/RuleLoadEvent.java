package fr.maxlego08.zauctionhouse.api.event.events;

import fr.maxlego08.zauctionhouse.api.event.AuctionEvent;
import fr.maxlego08.zauctionhouse.api.rules.ItemRuleManager;

public class RuleLoadEvent extends AuctionEvent {
   private final ItemRuleManager itemRuleManager;

   public RuleLoadEvent(ItemRuleManager var1) {
      this.itemRuleManager = var1;
   }

   public ItemRuleManager getItemRuleManager() {
      return this.itemRuleManager;
   }
}
