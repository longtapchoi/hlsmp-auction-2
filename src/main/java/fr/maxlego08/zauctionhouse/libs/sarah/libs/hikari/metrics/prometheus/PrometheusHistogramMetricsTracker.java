package fr.maxlego08.zauctionhouse.libs.sarah.libs.hikari.metrics.prometheus;

import fr.maxlego08.zauctionhouse.libs.sarah.libs.hikari.metrics.IMetricsTracker;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import io.prometheus.client.Histogram;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class PrometheusHistogramMetricsTracker implements IMetricsTracker {
   private static final Counter CONNECTION_TIMEOUT_COUNTER = ((Counter.Builder)((Counter.Builder)((Counter.Builder)Counter.build().name("hikaricp_connection_timeout_total")).labelNames(new String[]{"pool"})).help("Connection timeout total count")).create();
   private static final Histogram ELAPSED_ACQUIRED_HISTOGRAM = registerHistogram("hikaricp_connection_acquired_nanos", "Connection acquired time (ns)", (double)1000.0F);
   private static final Histogram ELAPSED_BORROWED_HISTOGRAM = registerHistogram("hikaricp_connection_usage_millis", "Connection usage (ms)", (double)1.0F);
   private static final Histogram ELAPSED_CREATION_HISTOGRAM = registerHistogram("hikaricp_connection_creation_millis", "Connection creation (ms)", (double)1.0F);
   private final Counter.Child connectionTimeoutCounterChild;
   private static final Map<CollectorRegistry, PrometheusMetricsTrackerFactory.RegistrationStatus> registrationStatuses = new ConcurrentHashMap();
   private final String poolName;
   private final HikariCPCollector hikariCPCollector;
   private final Histogram.Child elapsedAcquiredHistogramChild;
   private final Histogram.Child elapsedBorrowedHistogramChild;
   private final Histogram.Child elapsedCreationHistogramChild;

   private static Histogram registerHistogram(String var0, String var1, double var2) {
      return ((Histogram.Builder)((Histogram.Builder)((Histogram.Builder)Histogram.build().name(var0)).labelNames(new String[]{"pool"})).help(var1)).exponentialBuckets(var2, (double)2.0F, 11).create();
   }

   PrometheusHistogramMetricsTracker(String var1, CollectorRegistry var2, HikariCPCollector var3) {
      this.registerMetrics(var2);
      this.poolName = var1;
      this.hikariCPCollector = var3;
      this.connectionTimeoutCounterChild = (Counter.Child)CONNECTION_TIMEOUT_COUNTER.labels(new String[]{var1});
      this.elapsedAcquiredHistogramChild = (Histogram.Child)ELAPSED_ACQUIRED_HISTOGRAM.labels(new String[]{var1});
      this.elapsedBorrowedHistogramChild = (Histogram.Child)ELAPSED_BORROWED_HISTOGRAM.labels(new String[]{var1});
      this.elapsedCreationHistogramChild = (Histogram.Child)ELAPSED_CREATION_HISTOGRAM.labels(new String[]{var1});
   }

   private void registerMetrics(CollectorRegistry var1) {
      if (registrationStatuses.putIfAbsent(var1, PrometheusMetricsTrackerFactory.RegistrationStatus.REGISTERED) == null) {
         CONNECTION_TIMEOUT_COUNTER.register(var1);
         ELAPSED_ACQUIRED_HISTOGRAM.register(var1);
         ELAPSED_BORROWED_HISTOGRAM.register(var1);
         ELAPSED_CREATION_HISTOGRAM.register(var1);
      }

   }

   public void recordConnectionAcquiredNanos(long var1) {
      this.elapsedAcquiredHistogramChild.observe((double)var1);
   }

   public void recordConnectionUsageMillis(long var1) {
      this.elapsedBorrowedHistogramChild.observe((double)var1);
   }

   public void recordConnectionCreatedMillis(long var1) {
      this.elapsedCreationHistogramChild.observe((double)var1);
   }

   public void recordConnectionTimeout() {
      this.connectionTimeoutCounterChild.inc();
   }

   public void close() {
      this.hikariCPCollector.remove(this.poolName);
      CONNECTION_TIMEOUT_COUNTER.remove(new String[]{this.poolName});
      ELAPSED_ACQUIRED_HISTOGRAM.remove(new String[]{this.poolName});
      ELAPSED_BORROWED_HISTOGRAM.remove(new String[]{this.poolName});
      ELAPSED_CREATION_HISTOGRAM.remove(new String[]{this.poolName});
   }
}
