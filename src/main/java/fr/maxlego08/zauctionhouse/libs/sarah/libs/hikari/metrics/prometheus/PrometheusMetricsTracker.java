package fr.maxlego08.zauctionhouse.libs.sarah.libs.hikari.metrics.prometheus;

import fr.maxlego08.zauctionhouse.libs.sarah.libs.hikari.metrics.IMetricsTracker;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import io.prometheus.client.Summary;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

class PrometheusMetricsTracker implements IMetricsTracker {
   private static final Counter CONNECTION_TIMEOUT_COUNTER = ((Counter.Builder)((Counter.Builder)((Counter.Builder)Counter.build().name("hikaricp_connection_timeout_total")).labelNames(new String[]{"pool"})).help("Connection timeout total count")).create();
   private static final Summary ELAPSED_ACQUIRED_SUMMARY = createSummary("hikaricp_connection_acquired_nanos", "Connection acquired time (ns)");
   private static final Summary ELAPSED_USAGE_SUMMARY = createSummary("hikaricp_connection_usage_millis", "Connection usage (ms)");
   private static final Summary ELAPSED_CREATION_SUMMARY = createSummary("hikaricp_connection_creation_millis", "Connection creation (ms)");
   private static final Map<CollectorRegistry, PrometheusMetricsTrackerFactory.RegistrationStatus> registrationStatuses = new ConcurrentHashMap();
   private final String poolName;
   private final HikariCPCollector hikariCPCollector;
   private final Counter.Child connectionTimeoutCounterChild;
   private final Summary.Child elapsedAcquiredSummaryChild;
   private final Summary.Child elapsedUsageSummaryChild;
   private final Summary.Child elapsedCreationSummaryChild;

   PrometheusMetricsTracker(String var1, CollectorRegistry var2, HikariCPCollector var3) {
      this.registerMetrics(var2);
      this.poolName = var1;
      this.hikariCPCollector = var3;
      this.connectionTimeoutCounterChild = (Counter.Child)CONNECTION_TIMEOUT_COUNTER.labels(new String[]{var1});
      this.elapsedAcquiredSummaryChild = (Summary.Child)ELAPSED_ACQUIRED_SUMMARY.labels(new String[]{var1});
      this.elapsedUsageSummaryChild = (Summary.Child)ELAPSED_USAGE_SUMMARY.labels(new String[]{var1});
      this.elapsedCreationSummaryChild = (Summary.Child)ELAPSED_CREATION_SUMMARY.labels(new String[]{var1});
   }

   private void registerMetrics(CollectorRegistry var1) {
      if (registrationStatuses.putIfAbsent(var1, PrometheusMetricsTrackerFactory.RegistrationStatus.REGISTERED) == null) {
         CONNECTION_TIMEOUT_COUNTER.register(var1);
         ELAPSED_ACQUIRED_SUMMARY.register(var1);
         ELAPSED_USAGE_SUMMARY.register(var1);
         ELAPSED_CREATION_SUMMARY.register(var1);
      }

   }

   public void recordConnectionAcquiredNanos(long var1) {
      this.elapsedAcquiredSummaryChild.observe((double)var1);
   }

   public void recordConnectionUsageMillis(long var1) {
      this.elapsedUsageSummaryChild.observe((double)var1);
   }

   public void recordConnectionCreatedMillis(long var1) {
      this.elapsedCreationSummaryChild.observe((double)var1);
   }

   public void recordConnectionTimeout() {
      this.connectionTimeoutCounterChild.inc();
   }

   private static Summary createSummary(String var0, String var1) {
      return ((Summary.Builder)((Summary.Builder)((Summary.Builder)Summary.build().name(var0)).labelNames(new String[]{"pool"})).help(var1)).quantile((double)0.5F, 0.05).quantile(0.95, 0.01).quantile(0.99, 0.001).maxAgeSeconds(TimeUnit.MINUTES.toSeconds(5L)).ageBuckets(5).create();
   }

   public void close() {
      this.hikariCPCollector.remove(this.poolName);
      CONNECTION_TIMEOUT_COUNTER.remove(new String[]{this.poolName});
      ELAPSED_ACQUIRED_SUMMARY.remove(new String[]{this.poolName});
      ELAPSED_USAGE_SUMMARY.remove(new String[]{this.poolName});
      ELAPSED_CREATION_SUMMARY.remove(new String[]{this.poolName});
   }
}
