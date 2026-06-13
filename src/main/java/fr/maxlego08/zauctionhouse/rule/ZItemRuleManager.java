package fr.maxlego08.zauctionhouse.rule;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.event.events.RuleLoadEvent;
import fr.maxlego08.zauctionhouse.api.rules.ItemRuleManager;
import fr.maxlego08.zauctionhouse.api.rules.Rule;
import fr.maxlego08.zauctionhouse.api.rules.Rules;
import fr.maxlego08.zauctionhouse.api.rules.loader.RuleLoaderRegistry;
import fr.maxlego08.zauctionhouse.rule.rules.AndRule;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings({"unchecked", "rawtypes"})
public class ZItemRuleManager implements ItemRuleManager {
   private final AuctionPlugin plugin;
   private final RuleLoaderRegistry ruleLoaderRegistry;
   private Rules blacklist = Rules.emptyDisabled();
   private Rules whitelist = Rules.emptyDisabled();

   public ZItemRuleManager(AuctionPlugin var1, RuleLoaderRegistry var2) {
      this.plugin = var1;
      this.ruleLoaderRegistry = var2;
   }

   public boolean isBlacklisted(ItemStack var1) {
      ZItemRuleContext var2 = new ZItemRuleContext(var1);
      return this.blacklist.matches(var2);
   }

   public boolean isWhitelisted(ItemStack var1) {
      ZItemRuleContext var2 = new ZItemRuleContext(var1);
      return this.whitelist.matches(var2);
   }

   public boolean isAllowed(ItemStack var1) {
      ZItemRuleContext var2 = new ZItemRuleContext(var1);
      if (this.whitelist.enabled() && this.whitelist.matches(var2)) {
         return true;
      } else {
         return !this.blacklist.enabled() || !this.blacklist.matches(var2);
      }
   }

   public Rules blacklistRules() {
      return this.blacklist;
   }

   public Rules whitelistRules() {
      return this.whitelist;
   }

   public boolean isBlacklistEnabled() {
      return this.blacklist.enabled();
   }

   public void setBlacklistEnabled(boolean var1) {
      this.blacklist = this.blacklist.withEnabled(var1);
   }

   public boolean isWhitelistEnabled() {
      return this.whitelist.enabled();
   }

   public void setWhitelistEnabled(boolean var1) {
      this.whitelist = this.whitelist.withEnabled(var1);
   }

   public void addBlacklistRule(Rule var1) {
      this.blacklist = this.blacklist.withAddedRule(var1);
   }

   public void addWhitelistRule(Rule var1) {
      this.whitelist = this.whitelist.withAddedRule(var1);
   }

   public void setBlacklistRules(Rules var1) {
      this.blacklist = var1;
   }

   public void setWhitelistRules(Rules var1) {
      this.whitelist = var1;
   }

   public void loadRules() {
      File var1 = new File(this.plugin.getDataFolder(), "rules.yml");
      if (!var1.exists()) {
         this.plugin.saveFile("rules.yml", false);
      }

      RuleLoadEvent var2 = new RuleLoadEvent(this);
      var2.callEvent();
      YamlConfiguration var3 = YamlConfiguration.loadConfiguration(var1);
      this.blacklist = this.loadRuleSet(var3, "blacklist", false);
      this.whitelist = this.loadRuleSet(var3, "whitelist", true);
   }

   private Rules loadRuleSet(FileConfiguration var1, String var2, boolean var3) {
      boolean var4 = var1.getBoolean(var2 + ".enabled", var3);
      List var5 = var1.getMapList(var2 + ".rules");
      if (var5.isEmpty()) {
         return new Rules(var4, List.of());
      } else {
         ArrayList var6 = new ArrayList();

         for(Map var8 : var5) {
            List var9 = this.ruleLoaderRegistry.loadRules(var8);
            if (!var9.isEmpty()) {
               if (var9.size() == 1) {
                  var6.add((Rule)var9.getFirst());
               } else {
                  var6.add(new AndRule(var9));
               }
            }
         }

         return new Rules(var4, var6);
      }
   }
}
