package fr.maxlego08.zauctionhouse.category;

import fr.maxlego08.zauctionhouse.api.AuctionManager;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.category.Category;
import fr.maxlego08.zauctionhouse.api.category.CategoryManager;
import fr.maxlego08.zauctionhouse.api.item.Item;
import fr.maxlego08.zauctionhouse.api.item.StorageType;
import fr.maxlego08.zauctionhouse.api.item.items.AuctionItem;
import fr.maxlego08.zauctionhouse.api.rules.Rule;
import fr.maxlego08.zauctionhouse.api.rules.loader.RuleLoaderRegistry;
import fr.maxlego08.zauctionhouse.rule.ZItemRuleContext;
import fr.maxlego08.zauctionhouse.utils.PerformanceDebug;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class ZCategoryManager implements CategoryManager {
   private final AuctionPlugin plugin;
   private final RuleLoaderRegistry ruleLoaderRegistry;
   private final PerformanceDebug performanceDebug;
   private final Map<String, Category> categories = new LinkedHashMap();
   private final Map<String, Long> categoryCountCache = new ConcurrentHashMap();
   private List<Category> sortedCategories = List.of();
   private Category miscCategory;
   private boolean enabled = true;
   private String allCategoryName = "#0c1719Auction House";

   public ZCategoryManager(AuctionPlugin var1, RuleLoaderRegistry var2) {
      this.plugin = var1;
      this.ruleLoaderRegistry = var2;
      this.performanceDebug = new PerformanceDebug(var1);
   }

   public void loadCategories() {
      this.categories.clear();
      this.invalidateCategoryCountCache();
      File var1 = new File(this.plugin.getDataFolder(), "categories.yml");
      if (!var1.exists()) {
         this.plugin.saveFile("categories.yml", false);
      }

      if (var1.exists()) {
         this.loadCategoriesFromFile(var1);
      }

      File var2 = new File(this.plugin.getDataFolder(), "categories");
      if (var2.exists() && var2.isDirectory()) {
         File[] var3 = var2.listFiles((var0, var1x) -> var1x.endsWith(".yml"));
         if (var3 != null) {
            for(File var7 : var3) {
               this.loadCategoriesFromFile(var7);
            }
         }
      }

      if (this.miscCategory == null) {
         this.miscCategory = ZCategory.miscellaneous("misc", "&8Miscellaneous");
         this.categories.put("misc", this.miscCategory);
         this.plugin.getLogger().warning("No 'misc' category found, creating default one");
      }

      this.sortedCategories = new ArrayList(this.categories.values());
      if (this.isEnabled()) {
         this.plugin.getAuctionManager().getItems(StorageType.LISTED).forEach(this::applyCategories);
      }

      this.plugin.getLogger().info("Loaded " + this.categories.size() + " categories");
   }

   private void loadCategoriesFromFile(File var1) {
      try {
         YamlConfiguration var2 = YamlConfiguration.loadConfiguration(var1);
         if (var2.contains("settings")) {
            ConfigurationSection var3 = var2.getConfigurationSection("settings");
            if (var3 != null) {
               this.enabled = var3.getBoolean("enabled", true);
               this.allCategoryName = var3.getString("all-category-name", "#0c1719Auction House");
            }
         }

         for(String var4 : var2.getKeys(false)) {
            if (!var4.equals("settings") && !var4.equals("dynamic-categories") && !var4.equals("custom-items-support")) {
               ConfigurationSection var5 = var2.getConfigurationSection(var4);
               if (var5 != null) {
                  try {
                     Category var6 = this.loadCategory(var4, var5);
                     this.categories.put(var4.toLowerCase(Locale.ROOT), var6);
                     if (var6.isMiscellaneous()) {
                        this.miscCategory = var6;
                     }
                  } catch (Exception var7) {
                     this.plugin.getLogger().log(Level.WARNING, "Failed to load category '" + var4 + "' from " + var1.getName(), var7);
                  }
               }
            }
         }
      } catch (Exception var8) {
         this.plugin.getLogger().log(Level.SEVERE, "Failed to load categories from " + var1.getName(), var8);
      }

   }

   private Category loadCategory(String var1, ConfigurationSection var2) {
      String var3 = var2.getString("display-name", var1);
      List var4 = var2.getMapList("rules");
      if (var4.isEmpty() && var1.equalsIgnoreCase("misc")) {
         return ZCategory.miscellaneous(var1, var3);
      } else {
         List var5 = this.loadRules(var4);
         if (var5.isEmpty()) {
            this.plugin.getLogger().warning("No rules found for category '" + var1 + "'");
         }

         List var6 = var2.getMapList("banned-rules");
         List var7 = this.loadRules(var6);
         return new ZCategory(var1, var3, var5, var7, false);
      }
   }

   private List<Rule> loadRules(List<Map<?, ?>> var1) {
      return this.ruleLoaderRegistry.loadRulesFromList(var1);
   }

   public List<Category> getCategories() {
      return this.sortedCategories;
   }

   public Optional<Category> getCategory(String var1) {
      return Optional.ofNullable((Category)this.categories.get(var1.toLowerCase(Locale.ROOT)));
   }

   public Category getCategoryFor(ItemStack var1) {
      if (var1 == null) {
         return this.miscCategory;
      } else {
         ZItemRuleContext var2 = new ZItemRuleContext(var1);

         for(Category var4 : this.sortedCategories) {
            if (!var4.isMiscellaneous() && var4.matches(var2)) {
               return var4;
            }
         }

         return this.miscCategory;
      }
   }

   public List<Category> getCategoriesFor(ItemStack var1) {
      if (var1 == null) {
         return List.of(this.miscCategory);
      } else {
         ZItemRuleContext var2 = new ZItemRuleContext(var1);
         List var3 = this.sortedCategories.stream().filter((var1x) -> !var1x.isMiscellaneous() && var1x.matches(var2)).toList();
         return var3.isEmpty() ? List.of(this.miscCategory) : var3;
      }
   }

   public boolean matches(ItemStack var1, Category var2) {
      if (var2 == null) {
         return false;
      } else {
         ZItemRuleContext var3 = new ZItemRuleContext(var1);
         return var2.matches(var3);
      }
   }

   public Category getMiscCategory() {
      return this.miscCategory;
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public int getCategoryCount() {
      return this.categories.size();
   }

   public void applyCategories(Item var1) {
      long var2 = this.performanceDebug.start();
      if (var1 != null && this.isEnabled()) {
         var1.getCategories().clear();
         HashSet var4 = new HashSet();
         if (var1 instanceof AuctionItem) {
            AuctionItem var5 = (AuctionItem)var1;

            for(ItemStack var7 : var5.getItemStacks()) {
               List var8 = this.getCategoriesFor(var7);
               var4.addAll(var8);
            }
         }

         var1.setCategories(var4);
         PerformanceDebug var10000 = this.performanceDebug;
         int var10003 = var1.getId();
         var10000.end("applyCategories", var2, "itemId=" + var10003 + ", categories=" + var4.size());
      } else {
         this.performanceDebug.end("applyCategories", var2, "skipped");
      }
   }

   public long getItemCountForCategory(String var1) {
      return !this.isEnabled() ? 0L : (Long)this.categoryCountCache.computeIfAbsent(var1.toLowerCase(Locale.ROOT), this::computeCategoryCount);
   }

   public void invalidateCategoryCountCache() {
      this.categoryCountCache.clear();
   }

   public String getAllCategoryName() {
      return this.allCategoryName;
   }

   private long computeCategoryCount(String var1) {
      long var2 = this.performanceDebug.start();
      AuctionManager var4 = this.plugin.getAuctionManager();
      List<? extends fr.maxlego08.zauctionhouse.api.item.Item> var5 = var4.getItems(StorageType.LISTED);
      long var6;
      if (var1.equals("all")) {
         var6 = (long)var5.size();
      } else {
         int var8 = 0;

         for(Item var10 : var5) {
            if (var10.hasCategory(var1)) {
               ++var8;
            }
         }

         var6 = (long)var8;
      }

      PerformanceDebug var10000 = this.performanceDebug;
      String var10001 = "computeCategoryCount[" + var1 + "]";
      int var10003 = var5.size();
      var10000.end(var10001, var2, "total=" + var10003 + ", count=" + var6);
      return var6;
   }
}
