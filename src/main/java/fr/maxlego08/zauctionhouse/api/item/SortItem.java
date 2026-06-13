package fr.maxlego08.zauctionhouse.api.item;

import java.util.Comparator;

public enum SortItem {
   DECREASING_DATE,
   DECREASING_PRICE,
   ASCENDING_DATE,
   ASCENDING_PRICE;

   public SortItem next() {
      return values()[(this.ordinal() + 1) % values().length];
   }

   public Comparator<Item> getComparator() {
      Comparator var10000;
      switch (this.ordinal()) {
         case 0 -> var10000 = Comparator.comparing(Item::getExpiredAt).reversed();
         case 1 -> var10000 = Comparator.comparing(Item::getPrice).reversed();
         case 2 -> var10000 = Comparator.comparing(Item::getExpiredAt);
         case 3 -> var10000 = Comparator.comparing(Item::getPrice);
         default -> throw new MatchException((String)null, (Throwable)null);
      }

      return var10000;
   }

   // $FF: synthetic method
   private static SortItem[] $values() {
      return new SortItem[]{DECREASING_DATE, DECREASING_PRICE, ASCENDING_DATE, ASCENDING_PRICE};
   }
}
