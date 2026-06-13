package fr.maxlego08.zauctionhouse.api.history;

import java.util.Comparator;

@SuppressWarnings({"unchecked", "rawtypes"})
public enum HistorySortType {
   DATE_DESC("Newest First", Comparator.comparing((var0) -> ((ItemLog)var0).log().created_at()).reversed()),
   DATE_ASC("Oldest First", Comparator.comparing((var0) -> ((ItemLog)var0).log().created_at())),
   PRICE_DESC("Highest Price", Comparator.comparing((var0) -> ((ItemLog)var0).log().price()).reversed()),
   PRICE_ASC("Lowest Price", Comparator.comparing((var0) -> ((ItemLog)var0).log().price())),
   BUYER_ASC("Buyer A-Z", Comparator.comparing((var0) -> ((ItemLog)var0).item().getBuyerName() != null ? ((ItemLog)var0).item().getBuyerName() : "")),
   BUYER_DESC("Buyer Z-A", Comparator.comparing((var0) -> ((ItemLog)var0).item().getBuyerName() != null ? ((ItemLog)var0).item().getBuyerName() : "").reversed());

   private final String defaultDisplayName;
   private final Comparator<ItemLog> comparator;

   private HistorySortType(String var3, Comparator<ItemLog> var4) {
      this.defaultDisplayName = var3;
      this.comparator = var4;
   }

   public String getDefaultDisplayName() {
      return this.defaultDisplayName;
   }

   public Comparator<ItemLog> getComparator() {
      return this.comparator;
   }

   // $FF: synthetic method
   private static HistorySortType[] $values() {
      return new HistorySortType[]{DATE_DESC, DATE_ASC, PRICE_DESC, PRICE_ASC, BUYER_ASC, BUYER_DESC};
   }
}
