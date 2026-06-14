package fr.maxlego08.zauctionhouse.api.configuration.records;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import org.bukkit.configuration.file.FileConfiguration;

public record PerformanceConfiguration(int parallelSortThreshold, int parallelCategoryThreshold, int parallelism, long checkAvailabilityTimeoutMs, long lockItemTimeoutMs, long unlockItemTimeoutMs, long notifyStatusChangeTimeoutMs, long notifyItemActionTimeoutMs) {
   public static final int DEFAULT_PARALLEL_SORT_THRESHOLD = 10000;
   public static final int DEFAULT_PARALLEL_CATEGORY_THRESHOLD = 5000;
   public static final int DEFAULT_PARALLELISM = -1;
   public static final long DEFAULT_TIMEOUT_MS = 5000L;

   public static PerformanceConfiguration of(AuctionPlugin var0, FileConfiguration var1) {
      int var2 = var1.getInt("performance.cache.parallel-sort-threshold", 10000);
      int var3 = var1.getInt("performance.cache.parallel-category-threshold", 5000);
      int var4 = var1.getInt("performance.cache.parallelism", -1);
      long var5 = var1.getLong("performance.cluster-timeout.check-availability-ms", 5000L);
      long var7 = var1.getLong("performance.cluster-timeout.lock-item-ms", 5000L);
      long var9 = var1.getLong("performance.cluster-timeout.unlock-item-ms", 3000L);
      long var11 = var1.getLong("performance.cluster-timeout.notify-status-change-ms", 3000L);
      long var13 = var1.getLong("performance.cluster-timeout.notify-item-action-ms", 5000L);
      return new PerformanceConfiguration(var2, var3, var4, var5, var7, var9, var11, var13);
   }

   public int getEffectiveParallelism() {
      return this.parallelism <= 0 ? Math.max(2, Runtime.getRuntime().availableProcessors() - 1) : this.parallelism;
   }
}
