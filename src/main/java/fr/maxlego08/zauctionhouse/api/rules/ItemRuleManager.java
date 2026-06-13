package fr.maxlego08.zauctionhouse.api.rules;

import org.bukkit.inventory.ItemStack;

public interface ItemRuleManager {
   boolean isBlacklisted(ItemStack var1);

   boolean isWhitelisted(ItemStack var1);

   boolean isAllowed(ItemStack var1);

   Rules blacklistRules();

   Rules whitelistRules();

   boolean isBlacklistEnabled();

   void setBlacklistEnabled(boolean var1);

   boolean isWhitelistEnabled();

   void setWhitelistEnabled(boolean var1);

   void addBlacklistRule(Rule var1);

   void addWhitelistRule(Rule var1);

   void setBlacklistRules(Rules var1);

   void setWhitelistRules(Rules var1);

   void loadRules();
}
