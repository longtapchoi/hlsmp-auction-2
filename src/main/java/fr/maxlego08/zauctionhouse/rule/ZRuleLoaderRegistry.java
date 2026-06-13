package fr.maxlego08.zauctionhouse.rule;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.rules.Rule;
import fr.maxlego08.zauctionhouse.api.rules.RuleConfigHelper;
import fr.maxlego08.zauctionhouse.api.rules.loader.RuleLoader;
import fr.maxlego08.zauctionhouse.api.rules.loader.RuleLoaderRegistry;
import fr.maxlego08.zauctionhouse.hooks.craftengine.CraftEngineRuleLoader;
import fr.maxlego08.zauctionhouse.hooks.denizen.DenizenRuleLoader;
import fr.maxlego08.zauctionhouse.hooks.executableblocks.ExecutableBlocksRuleLoader;
import fr.maxlego08.zauctionhouse.hooks.executableitems.ExecutableItemsRuleLoader;
import fr.maxlego08.zauctionhouse.hooks.headdatabase.HeadDatabaseRuleLoader;
import fr.maxlego08.zauctionhouse.hooks.itemsadder.ItemsAdderRuleLoader;
import fr.maxlego08.zauctionhouse.hooks.mmoitems.MMOItemsRuleLoader;
import fr.maxlego08.zauctionhouse.hooks.nexo.NexoRuleLoader;
import fr.maxlego08.zauctionhouse.hooks.nova.NovaRuleLoader;
import fr.maxlego08.zauctionhouse.hooks.oraxen.OraxenRuleLoader;
import fr.maxlego08.zauctionhouse.hooks.slimefun.SlimefunRuleLoader;
import fr.maxlego08.zauctionhouse.rule.loaders.AndRuleLoader;
import fr.maxlego08.zauctionhouse.rule.loaders.CustomModelDataRuleLoader;
import fr.maxlego08.zauctionhouse.rule.loaders.LoreRuleLoader;
import fr.maxlego08.zauctionhouse.rule.loaders.MaterialContainsRuleLoader;
import fr.maxlego08.zauctionhouse.rule.loaders.MaterialPrefixRuleLoader;
import fr.maxlego08.zauctionhouse.rule.loaders.MaterialRuleLoader;
import fr.maxlego08.zauctionhouse.rule.loaders.MaterialSuffixRuleLoader;
import fr.maxlego08.zauctionhouse.rule.loaders.NameRuleLoader;
import fr.maxlego08.zauctionhouse.rule.loaders.TagRuleLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class ZRuleLoaderRegistry implements RuleLoaderRegistry {
   private final Map<String, RuleLoader> loaders = new HashMap();
   private final AuctionPlugin plugin;

   public ZRuleLoaderRegistry(AuctionPlugin var1) {
      this.plugin = var1;
   }

   public void registerDefaultLoaders() {
      this.register(new MaterialRuleLoader());
      this.register(new MaterialSuffixRuleLoader());
      this.register(new MaterialPrefixRuleLoader());
      this.register(new MaterialContainsRuleLoader());
      this.register(new TagRuleLoader(this.plugin));
      this.register(new NameRuleLoader());
      this.register(new LoreRuleLoader());
      this.register(new CustomModelDataRuleLoader());
      this.register(new AndRuleLoader(this));
   }

   public void registerItemsAdderLoader() {
      this.register(new ItemsAdderRuleLoader());
   }

   public void registerOraxenLoader() {
      this.register(new OraxenRuleLoader());
   }

   public void registerNexoLoader() {
      this.register(new NexoRuleLoader());
   }

   public void registerMMOItemsLoader() {
      this.register(new MMOItemsRuleLoader());
   }

   public void registerExecutableItemsLoader() {
      this.register(new ExecutableItemsRuleLoader());
   }

   public void registerSlimefunLoader() {
      this.register(new SlimefunRuleLoader());
   }

   public void registerHeadDatabaseLoader() {
      this.register(new HeadDatabaseRuleLoader());
   }

   public void registerNovaLoader() {
      this.register(new NovaRuleLoader());
   }

   public void registerDenizenLoader() {
      this.register(new DenizenRuleLoader());
   }

   public void registerCraftEngineLoader() {
      this.register(new CraftEngineRuleLoader());
   }

   public void registerExecutableBlocksLoader() {
      this.register(new ExecutableBlocksRuleLoader());
   }

   public void register(RuleLoader var1) {
      String var2 = var1.getType().toLowerCase(Locale.ROOT);
      this.loaders.put(var2, var1);

      for(String var4 : var1.getAliases()) {
         this.loaders.put(var4.toLowerCase(Locale.ROOT), var1);
      }

   }

   public void unregister(String var1) {
      RuleLoader var2 = (RuleLoader)this.loaders.remove(var1.toLowerCase(Locale.ROOT));
      if (var2 != null) {
         for(String var4 : var2.getAliases()) {
            this.loaders.remove(var4.toLowerCase(Locale.ROOT));
         }
      }

   }

   public Optional<RuleLoader> getLoader(String var1) {
      return Optional.ofNullable((RuleLoader)this.loaders.get(var1.toLowerCase(Locale.ROOT)));
   }

   public List<RuleLoader> getLoaders() {
      return List.copyOf(new HashSet(this.loaders.values()));
   }

   public Rule loadRule(Map<?, ?> var1) {
      String var2 = RuleConfigHelper.getString(var1, "type");
      return var2 == null ? null : (Rule)this.getLoader(var2).map((var1x) -> var1x.load(var1)).orElse((Object)null);
   }

   public List<Rule> loadRules(Map<?, ?> var1) {
      Rule var2 = this.loadRule(var1);
      return var2 != null ? List.of(var2) : List.of();
   }

   public List<Rule> loadRulesFromList(List<Map<?, ?>> var1) {
      ArrayList var2 = new ArrayList();

      for(Map var4 : var1) {
         Rule var5 = this.loadRule(var4);
         if (var5 != null) {
            if (!var5.isValid()) {
               this.plugin.getLogger().warning("Invalid rule: " + String.valueOf(var4.get("type")));
            } else {
               var2.add(var5);
            }
         }
      }

      return var2;
   }
}
