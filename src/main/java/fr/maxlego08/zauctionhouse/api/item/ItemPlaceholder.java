package fr.maxlego08.zauctionhouse.api.item;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public enum ItemPlaceholder {
   ECONOMY_NAME("economy-name"),
   ECONOMY_DISPLAY_NAME("economy-display-name"),
   SELLER("seller"),
   STATUS("status"),
   PRICE("price"),
   PRICE_RAW("price-price-raw"),
   PRICE_WITH_DECIMAL_FORMAT("price-price-with-decimal-format"),
   PRICE_WITH_REDUCTION("price-price-with-reduction"),
   PRICE_WITHOUT_DECIMAL("price-price-without-decimal"),
   TIME_REMAINING("time-remaining"),
   FORMATTED_EXPIRE_DATE("formatted-expire-date"),
   ITEM_COUNT("item_count");

   private static final Set<ItemPlaceholder> ALL = Set.copyOf(EnumSet.allOf(ItemPlaceholder.class));
   private final String key;
   private final String pattern;

   private ItemPlaceholder(String var3) {
      this.key = var3;
      this.pattern = "%" + var3 + "%";
   }

   public static Set<ItemPlaceholder> all() {
      return ALL;
   }

   public static Set<ItemPlaceholder> detect(List<String> var0) {
      EnumSet var1 = EnumSet.noneOf(ItemPlaceholder.class);

      for(String var3 : var0) {
         if (var3.contains("%")) {
            for(ItemPlaceholder var7 : values()) {
               if (var3.contains(var7.pattern)) {
                  var1.add(var7);
               }
            }
         }
      }

      return Set.copyOf(var1);
   }

   public String getKey() {
      return this.key;
   }

   // $FF: synthetic method
   private static ItemPlaceholder[] $values() {
      return new ItemPlaceholder[]{ECONOMY_NAME, ECONOMY_DISPLAY_NAME, SELLER, STATUS, PRICE, PRICE_RAW, PRICE_WITH_DECIMAL_FORMAT, PRICE_WITH_REDUCTION, PRICE_WITHOUT_DECIMAL, TIME_REMAINING, FORMATTED_EXPIRE_DATE, ITEM_COUNT};
   }
}
