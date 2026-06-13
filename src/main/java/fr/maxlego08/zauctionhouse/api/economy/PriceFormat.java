package fr.maxlego08.zauctionhouse.api.economy;

public enum PriceFormat {
   PRICE_RAW,
   PRICE_WITHOUT_DECIMAL,
   PRICE_WITH_DECIMAL_FORMAT,
   PRICE_WITH_REDUCTION;

   // $FF: synthetic method
   private static PriceFormat[] $values() {
      return new PriceFormat[]{PRICE_RAW, PRICE_WITHOUT_DECIMAL, PRICE_WITH_DECIMAL_FORMAT, PRICE_WITH_REDUCTION};
   }
}
