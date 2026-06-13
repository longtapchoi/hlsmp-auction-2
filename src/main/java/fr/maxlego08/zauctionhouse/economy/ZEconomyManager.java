package fr.maxlego08.zauctionhouse.economy;

import fr.maxlego08.menu.api.MenuItemStack;
import fr.maxlego08.menu.api.utils.TypedMapAccessor;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.economy.AuctionEconomy;
import fr.maxlego08.zauctionhouse.api.economy.EconomyManager;
import fr.maxlego08.zauctionhouse.api.economy.NumberFormatReduction;
import fr.maxlego08.zauctionhouse.api.economy.PriceFormat;
import fr.maxlego08.zauctionhouse.api.event.events.AuctionLoadEconomyEvent;
import fr.maxlego08.zauctionhouse.api.item.ItemType;
import fr.maxlego08.zauctionhouse.api.rules.Rule;
import fr.maxlego08.zauctionhouse.api.tax.ItemTaxRule;
import fr.maxlego08.zauctionhouse.api.tax.TaxAmountType;
import fr.maxlego08.zauctionhouse.api.tax.TaxConfiguration;
import fr.maxlego08.zauctionhouse.api.tax.TaxReduction;
import fr.maxlego08.zauctionhouse.api.tax.TaxType;
import fr.maxlego08.zauctionhouse.libs.currencies.Currencies;
import fr.maxlego08.zauctionhouse.libs.currencies.CurrencyProvider;
import fr.maxlego08.zauctionhouse.tax.ZItemTaxRule;
import fr.maxlego08.zauctionhouse.tax.ZTaxConfiguration;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

@SuppressWarnings({"unchecked", "rawtypes"})
public class ZEconomyManager implements EconomyManager {
   private final AuctionPlugin plugin;
   private final Set<AuctionEconomy> economies = new HashSet();
   private final Map<ItemType, AuctionEconomy> defaultEconomies = new HashMap();
   private final List<NumberFormatReduction> priceReductions = new ArrayList();
   private DecimalFormat priceDecimalFormat;

   public ZEconomyManager(AuctionPlugin var1) {
      this.plugin = var1;
   }

   public Collection<AuctionEconomy> getEconomies() {
      return Collections.unmodifiableCollection(this.economies);
   }

   public boolean registerEconomy(AuctionEconomy var1) {
      Optional var2 = this.getEconomy(var1.getName());
      return var2.isEmpty() && this.economies.add(var1);
   }

   public boolean removeEconomy(AuctionEconomy var1) {
      return this.economies.remove(var1);
   }

   public Optional<AuctionEconomy> getEconomy(String var1) {
      return this.economies.stream().filter((var1x) -> var1x.getName().equalsIgnoreCase(var1)).findFirst();
   }

   public void loadEconomies() {
      this.economies.clear();
      File var1 = new File(this.plugin.getDataFolder(), "economies.yml");
      if (!var1.exists()) {
         this.plugin.saveFile("economies.yml", false);
      }

      AuctionLoadEconomyEvent var2 = new AuctionLoadEconomyEvent(this.plugin, this);
      var2.callEvent();
      YamlConfiguration var3 = YamlConfiguration.loadConfiguration(var1);

      for(Map var5 : ((FileConfiguration)var3).getMapList("economies")) {
         this.loadEconomy(var1, new TypedMapAccessor(var5));
      }

      this.loadDefaultEconomies(var3);
      this.loadConfiguration(var3);
   }

   private void loadConfiguration(FileConfiguration var1) {
      String var2 = var1.getString("price-decimal-format", "#,###.#");
      if (var2.isEmpty()) {
         this.plugin.getLogger().severe("Price decimal format is not set, skip it...");
      } else {
         this.priceDecimalFormat = new DecimalFormat(var2);
         this.priceReductions.clear();

         for(Map var4 : var1.getMapList("price-reductions")) {
            TypedMapAccessor var5 = new TypedMapAccessor(var4);
            String var6 = var5.getString("format");
            String var7 = var5.getString("max-amount");
            String var8 = var5.getString("display", "%amount%");
            if (var6 != null && !var6.isEmpty()) {
               if (var7 != null && !var7.isEmpty()) {
                  try {
                     this.priceReductions.add(new NumberFormatReduction(var6, new BigDecimal(var7), var8));
                  } catch (NumberFormatException var10) {
                     this.plugin.getLogger().warning("Invalid price reduction max-amount '" + var7 + "' for format '" + var6 + "', skipping...");
                  }
               } else {
                  this.plugin.getLogger().severe("Price reduction max amount is not set, skip it...");
               }
            } else {
               this.plugin.getLogger().severe("Price reduction format is not set, skip it...");
            }
         }

      }
   }

   private void loadDefaultEconomies(FileConfiguration var1) {
      this.defaultEconomies.clear();

      for(ItemType var5 : ItemType.values()) {
         String var6 = var1.getString("default-economy." + var5.name().toLowerCase(), (String)null);
         if (var6 == null) {
            Logger var10000 = this.plugin.getLogger();
            String var10001 = var5.name();
            var10000.severe("Default economy for " + var10001 + " is not set! Please configure 'default-economy." + var5.name().toLowerCase() + "' in economies.yml");
         } else {
            Optional var7 = this.getEconomy(var6);
            if (var7.isEmpty()) {
               this.plugin.getLogger().severe("Default economy '" + var6 + "' for " + var5.name() + " was not found! Make sure the economy '" + var6 + "' is enabled in economies.yml");
            } else {
               this.defaultEconomies.put(var5, (AuctionEconomy)var7.get());
            }
         }
      }

      for(ItemType var11 : ItemType.values()) {
         if (!this.defaultEconomies.containsKey(var11)) {
            this.plugin.getLogger().severe("========================================");
            this.plugin.getLogger().severe("WARNING: No default economy for " + var11.name() + "!");
            this.plugin.getLogger().severe("Players will NOT be able to sell/buy items.");
            this.plugin.getLogger().severe("Please check your economies.yml configuration.");
            this.plugin.getLogger().severe("========================================");
         }
      }

   }

   public AuctionEconomy getDefaultEconomy(ItemType var1) {
      return (AuctionEconomy)this.defaultEconomies.get(var1);
   }

   public DecimalFormat getPriceDecimalFormat() {
      return this.priceDecimalFormat;
   }

   public List<NumberFormatReduction> getPriceReductions() {
      return this.priceReductions;
   }

   public String format(PriceFormat var1, Number var2) {
      String var10000;
      switch (var1) {
         case PRICE_WITH_REDUCTION -> var10000 = this.getDisplayBalance(var2);
         case PRICE_WITH_DECIMAL_FORMAT -> var10000 = this.priceDecimalFormat.format(var2);
         case PRICE_WITHOUT_DECIMAL -> var10000 = String.valueOf(var2.longValue());
         default -> var10000 = var2.toString();
      }

      return var10000;
   }

   protected String getDisplayBalance(Number var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("number cannot be null");
      } else {
         BigDecimal var2 = this.toBigDecimal(var1);

         for(NumberFormatReduction var4 : this.priceReductions) {
            if (var4 != null && var4.maxAmount() != null) {
               BigDecimal var5 = var4.maxAmount();
               if (var2.compareTo(var5) < 0) {
                  String var6 = var4.display();
                  String var7 = var4.format();
                  if (var6 != null && !var6.isEmpty()) {
                     String var8 = this.formatAmount(var2, var5, var7);
                     return var6.replace("%amount%", var8);
                  }

                  this.plugin.getLogger().severe("Display text is null or empty for format '" + var7 + "' in economy module config.yml");
               }
            }
         }

         return var2.toPlainString();
      }
   }

   private BigDecimal toBigDecimal(Number var1) {
      if (var1 instanceof BigDecimal var2) {
         return var2;
      } else {
         return !(var1 instanceof Long) && !(var1 instanceof Integer) && !(var1 instanceof Short) && !(var1 instanceof Byte) ? BigDecimal.valueOf(var1.doubleValue()) : BigDecimal.valueOf(var1.longValue());
      }
   }

   private String formatAmount(BigDecimal var1, BigDecimal var2, String var3) {
      if (var3 != null && !var3.isEmpty()) {
         if (var3.indexOf(35) >= 0) {
            DecimalFormat var7 = new DecimalFormat(var3);
            return var7.format(var1);
         } else {
            BigDecimal var4 = BigDecimal.valueOf(1000L);
            BigDecimal var5;
            if (var2.compareTo(var4) == 0) {
               var5 = var4;
            } else {
               var5 = var2.divide(var4, 2, RoundingMode.HALF_UP);
            }

            BigDecimal var6 = var1.divide(var5, 2, RoundingMode.HALF_UP);
            return String.format(var3, var6);
         }
      } else {
         return var1.toPlainString();
      }
   }

   public String format(AuctionEconomy var1, Number var2) {
      String var3 = var1.format(this.format(var1.getPriceFormat(), var2), var2.longValue());
      if (var3.contains(":")) {
         var3 = this.plugin.getInventoriesLoader().getInventoryManager().getFontImage().replace(var3);
      }

      return var3;
   }

   private void loadEconomy(File var1, TypedMapAccessor var2) {
      String var3 = var2.getString("name", (String)null);
      if (var3 == null) {
         this.plugin.getLogger().severe("An economy present in economies.yml is active but doesn’t have a name, please correct that!");
      } else if (!var2.getBoolean("is-enable", false)) {
         this.plugin.getLogger().info("Economy '" + var3 + "' is not active, skip it...");
      } else {
         String var4 = var2.getString("display-name", var3);
         if (var4 == null) {
            this.plugin.getLogger().severe("Economy '" + var3 + "' is active but doesn’t have a display name, please correct that!");
         } else {
            String var5 = var2.getString("symbol", "$");
            if (var5 == null) {
               this.plugin.getLogger().severe("Economy '" + var3 + "' is active but doesn’t have a symbol, please correct that!");
            } else {
               String var6 = var2.getString("format", "%price%$");
               if (var6 == null) {
                  this.plugin.getLogger().severe("Economy '" + var3 + "' is active but doesn’t have a format, please correct that!");
               } else {
                  String var7 = var2.getString("permission", (String)null);
                  String var8 = var2.getString("deposit-reason", "Sale of x%amount% %item% (zAuctionHouse)");
                  if (var8 == null) {
                     this.plugin.getLogger().severe("Economy '" + var3 + "' is active but doesn’t have a deposit reason, please correct that!");
                  } else {
                     String var9 = var2.getString("withdraw-reason", "Purchase of x%amount% %item% (zAuctionHouse)");
                     if (var9 == null) {
                        this.plugin.getLogger().severe("Economy '" + var3 + "' is active but doesn’t have a withdraw reason, please correct that!");
                     } else {
                        boolean var10 = var2.getBoolean("auto-claim", true);
                        boolean var11 = var2.getBoolean("must-be-online", false);
                        String var12 = var2.getString("price-format", PriceFormat.PRICE_RAW.name());
                        if (var12 == null) {
                           this.plugin.getLogger().severe("Economy '" + var3 + "' is active but doesn’t have a price format, please correct that!");
                        } else {
                           PriceFormat var13 = this.findPriceFormat(var12);
                           if (var13 == null) {
                              this.plugin.getLogger().severe("Economy '" + var3 + "' is active but doesn’t have a valid price format, please correct that!");
                           } else {
                              String var14 = var2.getString("type", "VAULT");
                              if (var14 == null) {
                                 this.plugin.getLogger().severe("Economy '" + var3 + "' is active but doesn’t have a type, please correct that!");
                              } else {
                                 Currencies var15 = this.findCurrencies(var14);
                                 if (var15 == null) {
                                    this.plugin.getLogger().severe("Economy '" + var3 + "' is active but doesn’t have a valid type, please correct that!");
                                 } else {
                                    Object var10000;
                                    switch (var15) {
                                       case ITEM:
case ZESSENTIALS:
                                       case ECOBITS:
                                       case COINSENGINE:
                                       case REDISECONOMY:
                                       case EXCELLENTEECONOMY:
                                          String var17 = var2.getString("currency-name", (String)null);
                                          if (var17 == null) {
                                             this.plugin.getLogger().severe("Economy '" + var3 + "' is active but doesn’t have a currency name, please correct that!");
                                             var10000 = null;
                                          } else {
                                             var10000 = var15.createProvider(var17);
                                          }
                                          break;
                                       default:
                                          var10000 = var15.createProvider();
                                    }

                                    Object var16 = var10000;
                                    if (var16 == null) {
                                       this.plugin.getLogger().severe("Impossible to create the currency provider for the economy '" + var3 + "'.");
                                    } else {
                                       EnumMap var22 = new EnumMap(ItemType.class);
                                       if (var2.contains("max-prices")) {
                                          var22 = this.loadPrices(var2.getObject("max-prices"), "max-prices for economy '" + var3 + "'");
                                       }

                                       EnumMap var23 = new EnumMap(ItemType.class);
                                       if (var2.contains("min-prices")) {
                                          var23 = this.loadPrices(var2.getObject("min-prices"), "min-prices for economy '" + var3 + "'");
                                       }

                                       TaxConfiguration var19 = this.loadTaxConfiguration(var3, var2);
                                       ZAuctionEconomy var20 = new ZAuctionEconomy(this.plugin, (CurrencyProvider)var16, var3, var4, var6, var5, var7, var8, var9, var13, var23, var22, var10, var11, var19);
                                       this.economies.add(var20);
                                       this.plugin.getLogger().info("Economy '" + var3 + "' loaded successfully!");
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private Currencies findCurrencies(String var1) {
      try {
         return Currencies.valueOf(var1.toUpperCase());
      } catch (Exception var3) {
         return null;
      }
   }

   private PriceFormat findPriceFormat(String var1) {
      try {
         return PriceFormat.valueOf(var1.toUpperCase());
      } catch (Exception var3) {
         return null;
      }
   }

   private EnumMap<ItemType, BigDecimal> loadPrices(Object var1, String var2) {
      EnumMap var3 = new EnumMap(ItemType.class);
      if (var1 instanceof Number var4) {
         for(ItemType var9 : ItemType.values()) {
            var3.put(var9, this.toBigDecimal(var4));
         }
      } else if (var1 instanceof Map var5) {
         var5.forEach((var3x, var4x) -> {
            if (var3x instanceof String var5) {
               if (var4x instanceof Number var6) {
                  ItemType var7 = ItemType.valueOf(var5.toUpperCase());
                  var3.put(var7, this.toBigDecimal(var6));
                  return;
               }
            }

            this.plugin.getLogger().severe("Impossible to find the price format for " + var2 + " (map)");
         });
      } else {
         this.plugin.getLogger().severe("Impossible to find the price format for " + var2 + " (type)");
      }

      return var3;
   }

   private TaxConfiguration loadTaxConfiguration(String var1, TypedMapAccessor var2) {
      if (!var2.contains("tax")) {
         return ZTaxConfiguration.disabled();
      } else {
         Object var3 = var2.getObject("tax");
         if (!(var3 instanceof Map)) {
            return ZTaxConfiguration.disabled();
         } else {
            Map var4 = (Map)var3;
            TypedMapAccessor var5 = new TypedMapAccessor(var4);
            boolean var6 = var5.getBoolean("enabled", false);
            if (!var6) {
               return ZTaxConfiguration.disabled();
            } else {
               String var7 = var5.getString("type", "SELL");

               TaxType var8;
               try {
                  var8 = TaxType.valueOf(var7.toUpperCase());
               } catch (IllegalArgumentException var27) {
                  this.plugin.getLogger().warning("Invalid tax type '" + var7 + "' for economy '" + var1 + "', defaulting to SELL");
                  var8 = TaxType.SELL;
               }

               String var9 = var5.getString("amount-type", "PERCENTAGE");

               TaxAmountType var10;
               try {
                  var10 = TaxAmountType.valueOf(var9.toUpperCase());
               } catch (IllegalArgumentException var26) {
                  this.plugin.getLogger().warning("Invalid tax amount type '" + var9 + "' for economy '" + var1 + "', defaulting to PERCENTAGE");
                  var10 = TaxAmountType.PERCENTAGE;
               }

               double var11 = var5.getDouble("amount", (double)0.0F);
               String var13 = var5.getString("bypass-permission", (String)null);
               ArrayList var14 = new ArrayList();
               if (var5.contains("reductions")) {
                  Object var15 = var5.getObject("reductions");
                  if (var15 instanceof List) {
                     for(Object var18 : (List)var15) {
                        if (var18 instanceof Map) {
                           Map var19 = (Map)var18;
                           TypedMapAccessor var20 = new TypedMapAccessor(var19);
                           String var21 = var20.getString("permission", (String)null);
                           double var22 = var20.getDouble("percentage", (double)0.0F);
                           if (var21 != null && !var21.isEmpty() && var22 > (double)0.0F) {
                              var14.add(new TaxReduction(var21, var22));
                           }
                        }
                     }
                  }
               }

               boolean var28 = false;
               ArrayList var29 = new ArrayList();
               if (var5.contains("item-rules")) {
                  Object var30 = var5.getObject("item-rules");
                  if (var30 instanceof Map) {
                     Map var31 = (Map)var30;
                     TypedMapAccessor var32 = new TypedMapAccessor(var31);
                     var28 = var32.getBoolean("enabled", false);
                     if (var28 && var32.contains("rules")) {
                        Object var33 = var32.getObject("rules");
                        if (var33 instanceof List) {
                           for(Object var23 : (List)var33) {
                              if (var23 instanceof Map) {
                                 Map var24 = (Map)var23;
                                 ItemTaxRule var25 = this.loadItemTaxRule(var1, var24);
                                 if (var25 != null) {
                                    var29.add(var25);
                                 }
                              }
                           }
                        }
                     }
                  }
               }

               this.plugin.getLogger().info("Tax configuration loaded for economy '" + var1 + "': type=" + String.valueOf(var8) + ", amountType=" + String.valueOf(var10) + ", amount=" + var11 + ", reductions=" + var14.size() + ", itemRules=" + var29.size());
               return new ZTaxConfiguration(var6, var8, var10, var11, var13, var14, var28, var29);
            }
         }
      }
   }

   private ItemTaxRule loadItemTaxRule(String var1, Map<String, Object> var2) {
      TypedMapAccessor var3 = new TypedMapAccessor(var2);
      String var4 = var3.getString("name", (String)null);
      if (var4 == null) {
         this.plugin.getLogger().warning("Item tax rule for economy '" + var1 + "' has no name, skipping");
         return null;
      } else {
         int var5 = var3.getInt("priority", 0);
         String var6 = var3.getString("type", "SELL");

         TaxType var7;
         try {
            var7 = TaxType.valueOf(var6.toUpperCase());
         } catch (IllegalArgumentException var16) {
            this.plugin.getLogger().warning("Invalid tax type '" + var6 + "' for item rule '" + var4 + "', defaulting to SELL");
            var7 = TaxType.SELL;
         }

         String var8 = var3.getString("amount-type", "PERCENTAGE");

         TaxAmountType var9;
         try {
            var9 = TaxAmountType.valueOf(var8.toUpperCase());
         } catch (IllegalArgumentException var15) {
            this.plugin.getLogger().warning("Invalid tax amount type '" + var8 + "' for item rule '" + var4 + "', defaulting to PERCENTAGE");
            var9 = TaxAmountType.PERCENTAGE;
         }

         double var10 = var3.getDouble("amount", (double)0.0F);
         Object var12 = var3.getObject("rule");
         if (var12 instanceof Map) {
            Map var13 = (Map)var12;
            Rule var14 = this.plugin.getRuleLoaderRegistry().loadRule(var13);
            if (var14 == null) {
               this.plugin.getLogger().warning("Item tax rule '" + var4 + "' has an invalid rule configuration, skipping");
               return null;
            } else {
               return new ZItemTaxRule(var4, var5, var7, var9, var10, var14);
            }
         } else {
            this.plugin.getLogger().warning("Item tax rule '" + var4 + "' has no valid rule configuration, skipping");
            return null;
         }
      }
   }
}
