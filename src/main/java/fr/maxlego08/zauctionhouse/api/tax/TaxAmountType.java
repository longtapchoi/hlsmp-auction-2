package fr.maxlego08.zauctionhouse.api.tax;

public enum TaxAmountType {
   PERCENTAGE,
   FIXED;

   // $FF: synthetic method
   private static TaxAmountType[] $values() {
      return new TaxAmountType[]{PERCENTAGE, FIXED};
   }
}
