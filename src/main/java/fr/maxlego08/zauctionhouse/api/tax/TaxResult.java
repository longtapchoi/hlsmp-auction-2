package fr.maxlego08.zauctionhouse.api.tax;

import java.math.BigDecimal;

public record TaxResult(BigDecimal taxAmount, double taxPercentage, BigDecimal originalPrice, BigDecimal finalPrice, boolean isBypassed, boolean isReduced, double reductionPercentage) {
   public static TaxResult bypassed(BigDecimal var0) {
      return new TaxResult(BigDecimal.ZERO, (double)0.0F, var0, var0, true, false, (double)0.0F);
   }

   public static TaxResult disabled(BigDecimal var0) {
      return new TaxResult(BigDecimal.ZERO, (double)0.0F, var0, var0, false, false, (double)0.0F);
   }

   public boolean hasTax() {
      return this.taxAmount.compareTo(BigDecimal.ZERO) > 0;
   }
}
