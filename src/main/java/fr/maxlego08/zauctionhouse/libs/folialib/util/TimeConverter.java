package fr.maxlego08.zauctionhouse.libs.folialib.util;

import java.util.concurrent.TimeUnit;

public class TimeConverter {
   public static long toTicks(long var0, TimeUnit var2) {
      return var2.toMillis(var0) / 50L;
   }

   public static long toMillis(long var0) {
      return var0 * 50L;
   }
}
