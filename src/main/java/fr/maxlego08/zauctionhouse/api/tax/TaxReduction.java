package fr.maxlego08.zauctionhouse.api.tax;

public record TaxReduction(String permission, double percentage) {
   public double getMultiplier() {
      return (double)1.0F - this.percentage / (double)100.0F;
   }
}
