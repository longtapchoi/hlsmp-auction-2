package fr.maxlego08.zauctionhouse.utils;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class PerformanceDebug {
   private final AuctionPlugin plugin;
   private final Logger logger;

   public PerformanceDebug(AuctionPlugin var1) {
      this.plugin = var1;
      this.logger = var1.getLogger();
   }

   public boolean isEnabled() {
      return this.plugin.getConfiguration().isEnablePerformanceDebug();
   }

   public void measure(String var1, Runnable var2) {
      if (!this.isEnabled()) {
         var2.run();
      } else {
         long var3 = System.nanoTime();
         var2.run();
         long var5 = System.nanoTime();
         this.logPerformance(var1, var3, var5, (String)null);
      }
   }

   public <T> T measure(String var1, Supplier<T> var2) {
      if (!this.isEnabled()) {
         return (T)var2.get();
      } else {
         long var3 = System.nanoTime();
         Object var5 = var2.get();
         long var6 = System.nanoTime();
         this.logPerformance(var1, var3, var6, (String)null);
         return (T)var5;
      }
   }

   public <T> T measureWithContext(String var1, Supplier<T> var2, Supplier<String> var3) {
      if (!this.isEnabled()) {
         return (T)var2.get();
      } else {
         long var4 = System.nanoTime();
         Object var6 = var2.get();
         long var7 = System.nanoTime();
         this.logPerformance(var1, var4, var7, (String)var3.get());
         return (T)var6;
      }
   }

   public long start() {
      return this.isEnabled() ? System.nanoTime() : -1L;
   }

   public void end(String var1, long var2) {
      if (var2 >= 0L) {
         long var4 = System.nanoTime();
         this.logPerformance(var1, var2, var4, (String)null);
      }
   }

   public void end(String var1, long var2, String var4) {
      if (var2 >= 0L) {
         long var5 = System.nanoTime();
         this.logPerformance(var1, var2, var5, var4);
      }
   }

   private void logPerformance(String var1, long var2, long var4, String var6) {
      if (this.plugin.getConfiguration().getPerformanceDebug().shouldLog(var1)) {
         double var7 = (double)(var4 - var2) / (double)1000000.0F;
         StringBuilder var9 = new StringBuilder();
         var9.append("[Performance] ").append(var1);
         if (var7 >= (double)1000.0F) {
            double var10 = var7 / (double)1000.0F;
            var9.append(" took ").append(String.format("%.3f", var10)).append("s");
         } else {
            var9.append(" took ").append(String.format("%.3f", var7)).append("ms");
         }

         if (var6 != null && !var6.isEmpty()) {
            var9.append(" (").append(var6).append(")");
         }

         if (var7 > (double)100.0F) {
            this.logger.warning(var9.toString());
         } else {
            this.logger.info(var9.toString());
         }

      }
   }
}
