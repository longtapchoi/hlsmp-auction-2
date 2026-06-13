package fr.maxlego08.zauctionhouse.libs.sarah.libs.hikari.metrics.dropwizard;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.health.HealthCheck.Result;
import fr.maxlego08.zauctionhouse.libs.sarah.libs.hikari.HikariConfig;
import fr.maxlego08.zauctionhouse.libs.sarah.libs.hikari.pool.HikariPool;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import java.util.SortedMap;
import java.util.concurrent.TimeUnit;

public final class CodahaleHealthChecker {
   public static void registerHealthChecks(HikariPool var0, HikariConfig var1, HealthCheckRegistry var2) {
      Properties var3 = var1.getHealthCheckProperties();
      MetricRegistry var4 = (MetricRegistry)var1.getMetricRegistry();
      long var5 = Long.parseLong(var3.getProperty("connectivityCheckTimeoutMs", String.valueOf(var1.getConnectionTimeout())));
      var2.register(MetricRegistry.name(var1.getPoolName(), new String[]{"pool", "ConnectivityCheck"}), new ConnectivityHealthCheck(var0, var5));
      long var7 = Long.parseLong(var3.getProperty("expected99thPercentileMs", "0"));
      if (var4 != null && var7 > 0L) {
         SortedMap var9 = var4.getTimers((var1x, var2x) -> var1x.equals(MetricRegistry.name(var1.getPoolName(), new String[]{"pool", "Wait"})));
         if (!var9.isEmpty()) {
            Timer var10 = (Timer)((Map.Entry)var9.entrySet().iterator().next()).getValue();
            var2.register(MetricRegistry.name(var1.getPoolName(), new String[]{"pool", "Connection99Percent"}), new Connection99Percent(var10, var7));
         }
      }

   }

   private CodahaleHealthChecker() {
   }

   private static class ConnectivityHealthCheck extends HealthCheck {
      private final HikariPool pool;
      private final long checkTimeoutMs;

      ConnectivityHealthCheck(HikariPool var1, long var2) {
         this.pool = var1;
         this.checkTimeoutMs = var2 > 0L && var2 != 2147483647L ? var2 : TimeUnit.SECONDS.toMillis(10L);
      }

      protected HealthCheck.Result check() {
         try {
            Connection var1 = this.pool.getConnection(this.checkTimeoutMs);

            HealthCheck.Result var2;
            try {
               var2 = Result.healthy();
            } catch (Throwable var5) {
               if (var1 != null) {
                  try {
                     var1.close();
                  } catch (Throwable var4) {
                     var5.addSuppressed(var4);
                  }
               }

               throw var5;
            }

            if (var1 != null) {
               var1.close();
            }

            return var2;
         } catch (SQLException var6) {
            return Result.unhealthy(var6);
         }
      }
   }

   private static class Connection99Percent extends HealthCheck {
      private final Timer waitTimer;
      private final long expected99thPercentile;

      Connection99Percent(Timer var1, long var2) {
         this.waitTimer = var1;
         this.expected99thPercentile = var2;
      }

      protected HealthCheck.Result check() {
         long var1 = TimeUnit.NANOSECONDS.toMillis(Math.round(this.waitTimer.getSnapshot().get99thPercentile()));
         return var1 <= this.expected99thPercentile ? Result.healthy() : Result.unhealthy("99th percentile connection wait time of %dms exceeds the threshold %dms", new Object[]{var1, this.expected99thPercentile});
      }
   }
}
