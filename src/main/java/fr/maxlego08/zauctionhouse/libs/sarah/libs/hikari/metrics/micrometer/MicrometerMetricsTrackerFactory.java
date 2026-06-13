package fr.maxlego08.zauctionhouse.libs.sarah.libs.hikari.metrics.micrometer;

import fr.maxlego08.zauctionhouse.libs.sarah.libs.hikari.metrics.IMetricsTracker;
import fr.maxlego08.zauctionhouse.libs.sarah.libs.hikari.metrics.MetricsTrackerFactory;
import fr.maxlego08.zauctionhouse.libs.sarah.libs.hikari.metrics.PoolStats;
import io.micrometer.core.instrument.MeterRegistry;

public class MicrometerMetricsTrackerFactory implements MetricsTrackerFactory {
   private final MeterRegistry registry;

   public MicrometerMetricsTrackerFactory(MeterRegistry var1) {
      this.registry = var1;
   }

   public IMetricsTracker create(String var1, PoolStats var2) {
      return new MicrometerMetricsTracker(var1, var2, this.registry);
   }
}
