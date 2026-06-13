package fr.maxlego08.zauctionhouse.tax;

import fr.maxlego08.zauctionhouse.api.tax.ItemTaxRule;
import fr.maxlego08.zauctionhouse.api.tax.TaxAmountType;
import fr.maxlego08.zauctionhouse.api.tax.TaxConfiguration;
import fr.maxlego08.zauctionhouse.api.tax.TaxReduction;
import fr.maxlego08.zauctionhouse.api.tax.TaxResult;
import fr.maxlego08.zauctionhouse.api.tax.TaxType;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ZTaxConfiguration implements TaxConfiguration {
   private final boolean enabled;
   private final TaxType taxType;
   private final TaxAmountType amountType;
   private final double amount;
   private final String bypassPermission;
   private final List<TaxReduction> reductions;
   private final boolean itemRulesEnabled;
   private final List<ItemTaxRule> itemRules;

   public ZTaxConfiguration(boolean var1, TaxType var2, TaxAmountType var3, double var4, String var6, List<TaxReduction> var7, boolean var8, List<ItemTaxRule> var9) {
      this.enabled = var1;
      this.taxType = var2;
      this.amountType = var3;
      this.amount = var4;
      this.bypassPermission = var6;
      this.reductions = var7 != null ? var7 : Collections.emptyList();
      this.itemRulesEnabled = var8;
      this.itemRules = var9 != null ? var9.stream().sorted(Comparator.comparingInt(ItemTaxRule::getPriority).reversed()).toList() : Collections.emptyList();
   }

   public static ZTaxConfiguration disabled() {
      return new ZTaxConfiguration(false, TaxType.SELL, TaxAmountType.PERCENTAGE, (double)0.0F, (String)null, Collections.emptyList(), false, Collections.emptyList());
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public TaxType getTaxType() {
      return this.taxType;
   }

   public TaxAmountType getAmountType() {
      return this.amountType;
   }

   public double getAmount() {
      return this.amount;
   }

   public String getBypassPermission() {
      return this.bypassPermission;
   }

   public List<TaxReduction> getReductions() {
      return this.reductions;
   }

   public boolean hasItemRules() {
      return this.itemRulesEnabled && !this.itemRules.isEmpty();
   }

   public List<ItemTaxRule> getItemRules() {
      return this.itemRules;
   }

   public TaxResult calculateSellTax(Player var1, BigDecimal var2, ItemStack var3) {
      if (!this.enabled) {
         return TaxResult.disabled(var2);
      } else {
         if (this.hasItemRules() && var3 != null) {
            ItemTaxRule var4 = this.findMatchingItemRule(var3);
            if (var4 != null) {
               TaxType var5 = var4.getTaxType();
               if (var5 == TaxType.SELL || var5 == TaxType.BOTH) {
                  return this.calculateTax(var1, var2, var4.getAmountType(), var4.getAmount());
               }

               if (this.taxType != TaxType.SELL && this.taxType != TaxType.BOTH) {
                  return TaxResult.disabled(var2);
               }
            }
         }

         if (this.taxType != TaxType.SELL && this.taxType != TaxType.BOTH) {
            return TaxResult.disabled(var2);
         } else {
            return this.calculateTax(var1, var2, this.amountType, this.amount);
         }
      }
   }

   public TaxResult calculatePurchaseTax(Player var1, BigDecimal var2, ItemStack var3) {
      if (!this.enabled) {
         return TaxResult.disabled(var2);
      } else {
         if (this.hasItemRules() && var3 != null) {
            ItemTaxRule var4 = this.findMatchingItemRule(var3);
            if (var4 != null) {
               TaxType var5 = var4.getTaxType();
               if (var5 == TaxType.PURCHASE || var5 == TaxType.BOTH || var5 == TaxType.CAPITALISM) {
                  return this.calculatePurchaseTaxInternal(var1, var2, var4.getAmountType(), var4.getAmount(), var5);
               }

               if (this.taxType != TaxType.PURCHASE && this.taxType != TaxType.BOTH && this.taxType != TaxType.CAPITALISM) {
                  return TaxResult.disabled(var2);
               }
            }
         }

         if (this.taxType != TaxType.PURCHASE && this.taxType != TaxType.BOTH && this.taxType != TaxType.CAPITALISM) {
            return TaxResult.disabled(var2);
         } else {
            return this.calculatePurchaseTaxInternal(var1, var2, this.amountType, this.amount, this.taxType);
         }
      }
   }

   private TaxResult calculatePurchaseTaxInternal(Player var1, BigDecimal var2, TaxAmountType var3, double var4, TaxType var6) {
      TaxResult var7 = this.calculateTax(var1, var2, var3, var4);
      if (!var7.isBypassed() && var7.hasTax()) {
         if (var6 == TaxType.CAPITALISM) {
            BigDecimal var9 = var2.add(var7.taxAmount());
            return new TaxResult(var7.taxAmount(), var7.taxPercentage(), var2, var9, false, var7.isReduced(), var7.reductionPercentage());
         } else {
            BigDecimal var8 = var2.subtract(var7.taxAmount());
            return new TaxResult(var7.taxAmount(), var7.taxPercentage(), var2, var8, false, var7.isReduced(), var7.reductionPercentage());
         }
      } else {
         return var7;
      }
   }

   private TaxResult calculateTax(Player var1, BigDecimal var2, TaxAmountType var3, double var4) {
      if (this.canBypass(var1)) {
         return TaxResult.bypassed(var2);
      } else {
         BigDecimal var6;
         double var7;
         if (var3 == TaxAmountType.PERCENTAGE) {
            var7 = var4;
            var6 = var2.multiply(BigDecimal.valueOf(var4 / (double)100.0F));
         } else {
            var6 = BigDecimal.valueOf(var4);
            var7 = var2.compareTo(BigDecimal.ZERO) > 0 ? var6.divide(var2, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100L)).doubleValue() : (double)0.0F;
         }

         TaxReduction var9 = this.getReduction(var1);
         double var10 = (double)0.0F;
         boolean var12 = false;
         if (var9 != null) {
            var10 = var9.percentage();
            var12 = true;
            var6 = var6.multiply(BigDecimal.valueOf(var9.getMultiplier()));
            var7 *= var9.getMultiplier();
         }

         var6 = var6.setScale(2, RoundingMode.HALF_UP);
         BigDecimal var13 = var2.subtract(var6);
         return new TaxResult(var6, var7, var2, var13, false, var12, var10);
      }
   }

   public boolean canBypass(Player var1) {
      return this.bypassPermission != null && !this.bypassPermission.isEmpty() ? var1.hasPermission(this.bypassPermission) : false;
   }

   public TaxReduction getReduction(Player var1) {
      for(TaxReduction var3 : this.reductions) {
         if (var1.hasPermission(var3.permission())) {
            return var3;
         }
      }

      return null;
   }

   private ItemTaxRule findMatchingItemRule(ItemStack var1) {
      for(ItemTaxRule var3 : this.itemRules) {
         if (var3.matches(var1)) {
            return var3;
         }
      }

      return null;
   }
}
