package fr.maxlego08.zauctionhouse.api.filter;

import java.util.Date;

public enum DateFilter {
   ALL(0),
   TODAY(1),
   THIS_WEEK(7),
   THIS_MONTH(30),
   THIS_YEAR(365);

   private final int days;

   private DateFilter(int var3) {
      this.days = var3;
   }

   public int getDays() {
      return this.days;
   }

   public boolean matches(Date var1) {
      if (this.days == 0) {
         return true;
      } else {
         long var2 = System.currentTimeMillis() - (long)this.days * 24L * 60L * 60L * 1000L;
         return var1.getTime() >= var2;
      }
   }

   public DateFilter next() {
      DateFilter[] var1 = values();
      return var1[(this.ordinal() + 1) % var1.length];
   }

   public String getDisplayName() {
      String var10000;
      switch (this.ordinal()) {
         case 0 -> var10000 = "All Time";
         case 1 -> var10000 = "Today";
         case 2 -> var10000 = "This Week";
         case 3 -> var10000 = "This Month";
         case 4 -> var10000 = "This Year";
         default -> throw new MatchException((String)null, (Throwable)null);
      }

      return var10000;
   }

   // $FF: synthetic method
   private static DateFilter[] $values() {
      return new DateFilter[]{ALL, TODAY, THIS_WEEK, THIS_MONTH, THIS_YEAR};
   }
}
