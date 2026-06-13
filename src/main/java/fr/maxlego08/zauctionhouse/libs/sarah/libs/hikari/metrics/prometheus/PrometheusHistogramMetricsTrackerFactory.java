package fr.maxlego08.zauctionhouse.libs.sarah.libs.hikari.metrics.prometheus;

import fr.maxlego08.zauctionhouse.libs.sarah.libs.hikari.metrics.IMetricsTracker;
import fr.maxlego08.zauctionhouse.libs.sarah.libs.hikari.metrics.MetricsTrackerFactory;
import fr.maxlego08.zauctionhouse.libs.sarah.libs.hikari.metrics.PoolStats;
import io.prometheus.client.Collector;
import io.prometheus.client.CollectorRegistry;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PrometheusHistogramMetricsTrackerFactory implements MetricsTrackerFactory {
   private static final Map<CollectorRegistry, PrometheusMetricsTrackerFactory.RegistrationStatus> registrationStatuses = new ConcurrentHashMap();
   private final HikariCPCollector collector;
   private final CollectorRegistry collectorRegistry;

   public PrometheusHistogramMetricsTrackerFactory() {
      this(CollectorRegistry.defaultRegistry);
   }

   public PrometheusHistogramMetricsTrackerFactory(CollectorRegistry var1) {
      this.collector = new HikariCPCollector();
      this.collectorRegistry = var1;
   }

   public IMetricsTracker create(String var1, PoolStats var2) {
      this.registerCollector(this.collector, this.collectorRegistry);
      this.collector.add(var1, var2);
      return new PrometheusHistogramMetricsTracker(var1, this.collectorRegistry, this.collector);
   }

   private void registerCollector(Collector var1, CollectorRegistry var2) {
      if (registrationStatuses.putIfAbsent(var2, PrometheusMetricsTrackerFactory.RegistrationStatus.REGISTERED) == null) {
         var1.register(var2);
      }

   }
}
