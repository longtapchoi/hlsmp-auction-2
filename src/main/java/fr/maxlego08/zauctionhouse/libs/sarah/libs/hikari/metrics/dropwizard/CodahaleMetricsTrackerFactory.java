package fr.maxlego08.zauctionhouse.libs.sarah.libs.hikari.metrics.dropwizard;

import com.codahale.metrics.MetricRegistry;
import fr.maxlego08.zauctionhouse.libs.sarah.libs.hikari.metrics.IMetricsTracker;
import fr.maxlego08.zauctionhouse.libs.sarah.libs.hikari.metrics.MetricsTrackerFactory;
import fr.maxlego08.zauctionhouse.libs.sarah.libs.hikari.metrics.PoolStats;

public final class CodahaleMetricsTrackerFactory implements MetricsTrackerFactory {
   private final MetricRegistry registry;

   public CodahaleMetricsTrackerFactory(MetricRegistry var1) {
      this.registry = var1;
   }

   public MetricRegistry getRegistry() {
      return this.registry;
   }

   public IMetricsTracker create(String var1, PoolStats var2) {
      return new CodaHaleMetricsTracker(var1, var2, this.registry);
   }
}
