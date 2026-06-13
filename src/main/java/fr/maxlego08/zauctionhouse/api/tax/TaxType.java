package fr.maxlego08.zauctionhouse.api.tax;

public enum TaxType {
   SELL,
   PURCHASE,
   BOTH,
   CAPITALISM;

   // $FF: synthetic method
   private static TaxType[] $values() {
      return new TaxType[]{SELL, PURCHASE, BOTH, CAPITALISM};
   }
}
