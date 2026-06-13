package fr.maxlego08.zauctionhouse.libs.sarah.libs.hikari.metrics.prometheus;

import fr.maxlego08.zauctionhouse.libs.sarah.libs.hikari.metrics.PoolStats;
import io.prometheus.client.Collector;
import io.prometheus.client.GaugeMetricFamily;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

class HikariCPCollector extends Collector {
   private static final List<String> LABEL_NAMES = Collections.singletonList("pool");
   private final Map<String, PoolStats> poolStatsMap = new ConcurrentHashMap();

   public List<Collector.MetricFamilySamples> collect() {
      return Arrays.asList(this.createGauge("hikaricp_active_connections", "Active connections", PoolStats::getActiveConnections), this.createGauge("hikaricp_idle_connections", "Idle connections", PoolStats::getIdleConnections), this.createGauge("hikaricp_pending_threads", "Pending threads", PoolStats::getPendingThreads), this.createGauge("hikaricp_connections", "The number of current connections", PoolStats::getTotalConnections), this.createGauge("hikaricp_max_connections", "Max connections", PoolStats::getMaxConnections), this.createGauge("hikaricp_min_connections", "Min connections", PoolStats::getMinConnections));
   }

   void add(String var1, PoolStats var2) {
      this.poolStatsMap.put(var1, var2);
   }

   void remove(String var1) {
      this.poolStatsMap.remove(var1);
   }

   private GaugeMetricFamily createGauge(String var1, String var2, Function<PoolStats, Integer> var3) {
      GaugeMetricFamily var4 = new GaugeMetricFamily(var1, var2, LABEL_NAMES);
      this.poolStatsMap.forEach((var2x, var3x) -> var4.addMetric(Collections.singletonList(var2x), (double)(Integer)var3.apply(var3x)));
      return var4;
   }
}
