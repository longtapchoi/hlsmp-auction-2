package fr.maxlego08.zauctionhouse.utils.cache;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.category.Category;
import fr.maxlego08.zauctionhouse.api.configuration.records.PerformanceConfiguration;
import fr.maxlego08.zauctionhouse.api.item.Item;
import fr.maxlego08.zauctionhouse.api.item.ItemStatus;
import fr.maxlego08.zauctionhouse.api.item.SortItem;
import fr.maxlego08.zauctionhouse.api.utils.IntArrayList;
import fr.maxlego08.zauctionhouse.api.utils.IntList;
import fr.maxlego08.zauctionhouse.utils.PerformanceDebug;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

public class SortedItemsCache {
   private final AuctionPlugin plugin;
   private final PerformanceDebug performanceDebug;
   private final Supplier<Collection<Item>> itemsSupplier;
   private final AtomicReference<Map<SortItem, IntList>> sortedAllItems = new AtomicReference<>(new ConcurrentHashMap<>());
   private final AtomicReference<Map<String, IntList>> sortedByCategoryItems = new AtomicReference<>(new ConcurrentHashMap<>());
   private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
   private final AtomicBoolean dirty = new AtomicBoolean(true);
   private final AtomicBoolean rebuildInProgress = new AtomicBoolean(false);
   private final AtomicReference<CompletableFuture<Void>> ongoingRebuild = new AtomicReference<>(null);
   private volatile long lastRebuildTime = 0L;
   private final int parallelSortThreshold;
   private final int parallelCategoryThreshold;
   private final ForkJoinPool forkJoinPool;

   public SortedItemsCache(AuctionPlugin var1, Supplier<Collection<Item>> var2) {
      this.plugin = var1;
      this.performanceDebug = new PerformanceDebug(var1);
      this.itemsSupplier = var2;
      PerformanceConfiguration var3 = var1.getConfiguration().getPerformance();
      this.parallelSortThreshold = var3.parallelSortThreshold();
      this.parallelCategoryThreshold = var3.parallelCategoryThreshold();
      this.forkJoinPool = new ForkJoinPool(var3.getEffectiveParallelism());
   }

   public IntList getSortedIds(SortItem var1) {
      this.triggerRebuildIfNeeded();
      Map var2 = (Map)this.sortedAllItems.get();
      IntList var3 = (IntList)var2.get(var1);
      return (IntList)(var3 != null ? var3.clone() : new IntArrayList());
   }

   public IntList getSortedIds(Category var1, SortItem var2) {
      if (var1 == null) {
         return this.getSortedIds(var2);
      } else {
         this.triggerRebuildIfNeeded();
         String var3 = this.buildCacheKey(var1.getId(), var2);
         Map var4 = (Map)this.sortedByCategoryItems.get();
         IntList var5 = (IntList)var4.get(var3);
         return (IntList)(var5 != null ? var5.clone() : new IntArrayList());
      }
   }

   private void triggerRebuildIfNeeded() {
      if (this.dirty.get() && this.rebuildInProgress.compareAndSet(false, true)) {
         this.plugin.getScheduler().runAsync((var1) -> {
            try {
               this.rebuildCache();
            } finally {
               this.rebuildInProgress.set(false);
            }

         });
      }

   }

   public IntList getPage(SortItem var1, int var2, int var3) {
      IntList var4 = this.getSortedIds(var1);
      return this.getSubList(var4, var2, var3);
   }

   public IntList getPage(Category var1, SortItem var2, int var3, int var4) {
      IntList var5 = this.getSortedIds(var1, var2);
      return this.getSubList(var5, var3, var4);
   }

   public void invalidate() {
      this.dirty.set(true);
   }

   public void rebuild() {
      this.rebuildCache();
   }

   public void rebuildAsync() {
      this.plugin.getScheduler().runAsync((var1) -> this.rebuildCache());
   }

   public int getTotalCount(SortItem var1) {
      this.triggerRebuildIfNeeded();
      Map var2 = (Map)this.sortedAllItems.get();
      IntList var3 = (IntList)var2.get(var1);
      return var3 != null ? var3.size() : 0;
   }

   public int getTotalCount(Category var1, SortItem var2) {
      if (var1 == null) {
         return this.getTotalCount(var2);
      } else {
         this.triggerRebuildIfNeeded();
         String var3 = this.buildCacheKey(var1.getId(), var2);
         Map var4 = (Map)this.sortedByCategoryItems.get();
         IntList var5 = (IntList)var4.get(var3);
         return var5 != null ? var5.size() : 0;
      }
   }

   public long getLastRebuildTime() {
      return this.lastRebuildTime;
   }

   public boolean isDirty() {
      return this.dirty.get();
   }

   public CompletableFuture<Void> ensureCacheValidAsync() {
      if (!this.dirty.get()) {
         return CompletableFuture.<Void>completedFuture(null);
      } else {
         CompletableFuture var1 = (CompletableFuture)this.ongoingRebuild.get();
         if (var1 != null && !var1.isDone()) {
            return var1;
         } else {
            CompletableFuture var2 = new CompletableFuture();
            if (this.ongoingRebuild.compareAndSet(var1, var2)) {
               this.plugin.getScheduler().runAsync((var2x) -> {
                  try {
                     this.rebuildCache();
                     var2.complete(null);
                  } catch (Exception var7) {
                     var2.completeExceptionally(var7);
                  } finally {
                     this.ongoingRebuild.compareAndSet(var2, null);
                  }

               });
               return var2;
            } else {
               CompletableFuture var3 = (CompletableFuture)this.ongoingRebuild.get();
               return var3 != null ? var3 : CompletableFuture.<Void>completedFuture(null);
            }
         }
      }
   }

   private void ensureCacheValid() {
      if (this.dirty.get()) {
         this.rebuildCache();
      }

   }

   private void rebuildCache() {
      long var1 = this.performanceDebug.start();
      this.lock.writeLock().lock();

      try {
         if (this.dirty.get()) {
            Collection var3 = (Collection)this.itemsSupplier.get();
            int var4 = var3.size();
            ArrayList<Item> var5 = new ArrayList<>(var4);
            HashMap<String, List<Item>> var6 = new HashMap<>();

            for(Item var8 : var3) {
               if (var8.getStatus() == ItemStatus.AVAILABLE && !var8.isExpired()) {
                  var5.add(var8);
                  Set<fr.maxlego08.zauctionhouse.api.category.Category> var9 = var8.getCategories();
                  if (var9 != null) {
                     for(Category var11 : var9) {
                        var6.computeIfAbsent(var11.getId(), (var0) -> new ArrayList<>()).add(var8);
                     }
                  }
               }
            }

            int var24 = var5.size();
            int var25 = var6.size();
            ConcurrentHashMap var26 = new ConcurrentHashMap();
            ConcurrentHashMap var27 = new ConcurrentHashMap();
            if (var24 == 0) {
               this.sortedAllItems.set(var26);
               this.sortedByCategoryItems.set(var27);
               this.dirty.set(false);
               this.lastRebuildTime = System.currentTimeMillis();
               this.performanceDebug.end("SortedItemsCache.rebuild", var1, "items=0");
               return;
            }

            Item[] var28 = (Item[])var5.toArray(new Item[0]);
            if (var24 >= this.parallelSortThreshold) {
               Arrays.parallelSort(var28, SortItem.ASCENDING_DATE.getComparator());
            } else {
               Arrays.sort(var28, SortItem.ASCENDING_DATE.getComparator());
            }

            int[] var12 = this.extractIdsToArray(var28);
            int[] var13 = this.reverseArray(var12);
            var26.put(SortItem.ASCENDING_DATE, this.wrapArray(var12));
            var26.put(SortItem.DECREASING_DATE, this.wrapArray(var13));
            if (var24 >= this.parallelSortThreshold) {
               Arrays.parallelSort(var28, SortItem.ASCENDING_PRICE.getComparator());
            } else {
               Arrays.sort(var28, SortItem.ASCENDING_PRICE.getComparator());
            }

            int[] var14 = this.extractIdsToArray(var28);
            int[] var15 = this.reverseArray(var14);
            var26.put(SortItem.ASCENDING_PRICE, this.wrapArray(var14));
            var26.put(SortItem.DECREASING_PRICE, this.wrapArray(var15));
            if (var25 > 2 && var24 >= this.parallelCategoryThreshold) {
               try {
                  this.forkJoinPool.submit(() -> var6.entrySet().parallelStream().forEach((var2x) -> this.buildCategorySortedLists(var2x.getKey(), var2x.getValue(), var27))).get();
               } catch (Exception var22) {
                  for(Map.Entry<String, List<Item>> var18 : var6.entrySet()) {
                     this.buildCategorySortedLists(var18.getKey(), var18.getValue(), var27);
                  }
               }
            } else {
               for(Map.Entry<String, List<Item>> var17 : var6.entrySet()) {
                  this.buildCategorySortedLists(var17.getKey(), var17.getValue(), var27);
               }
            }

            this.sortedAllItems.set(var26);
            this.sortedByCategoryItems.set(var27);
            this.dirty.set(false);
            this.lastRebuildTime = System.currentTimeMillis();
            this.performanceDebug.end("SortedItemsCache.rebuild", var1, "items=" + var24 + ", categories=" + var25);
            return;
         }

         this.performanceDebug.end("SortedItemsCache.rebuild", var1, "skipped (already rebuilt)");
      } finally {
         this.lock.writeLock().unlock();
      }

   }

   private void buildCategorySortedLists(String var1, List<Item> var2, Map<String, IntList> var3) {
      int var4 = var2.size();
      if (var4 != 0) {
         Item[] var5 = (Item[])var2.toArray(new Item[0]);
         if (var4 >= this.parallelSortThreshold) {
            Arrays.parallelSort(var5, SortItem.ASCENDING_DATE.getComparator());
         } else {
            Arrays.sort(var5, SortItem.ASCENDING_DATE.getComparator());
         }

         int[] var6 = this.extractIdsToArray(var5);
         int[] var7 = this.reverseArray(var6);
         var3.put(this.buildCacheKey(var1, SortItem.ASCENDING_DATE), this.wrapArray(var6));
         var3.put(this.buildCacheKey(var1, SortItem.DECREASING_DATE), this.wrapArray(var7));
         if (var4 >= this.parallelSortThreshold) {
            Arrays.parallelSort(var5, SortItem.ASCENDING_PRICE.getComparator());
         } else {
            Arrays.sort(var5, SortItem.ASCENDING_PRICE.getComparator());
         }

         int[] var8 = this.extractIdsToArray(var5);
         int[] var9 = this.reverseArray(var8);
         var3.put(this.buildCacheKey(var1, SortItem.ASCENDING_PRICE), this.wrapArray(var8));
         var3.put(this.buildCacheKey(var1, SortItem.DECREASING_PRICE), this.wrapArray(var9));
      }
   }

   private int[] extractIdsToArray(Item[] var1) {
      int[] var2 = new int[var1.length];

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2[var3] = var1[var3].getId();
      }

      return var2;
   }

   private int[] reverseArray(int[] var1) {
      int var2 = var1.length;
      int[] var3 = new int[var2];

      for(int var4 = 0; var4 < var2; ++var4) {
         var3[var4] = var1[var2 - 1 - var4];
      }

      return var3;
   }

   private IntList wrapArray(int[] var1) {
      IntArrayList var2 = new IntArrayList(var1.length);

      for(int var6 : var1) {
         var2.add(var6);
      }

      return var2;
   }

   private String buildCacheKey(String var1, SortItem var2) {
      return var1 + ":" + var2.name();
   }

   private IntList getSubList(IntList var1, int var2, int var3) {
      if (var1 != null && !var1.isEmpty()) {
         int var4 = var1.size();
         if (var2 >= var4) {
            return new IntArrayList();
         } else {
            int var5 = Math.max(0, var2);
            int var6 = Math.min(var4, var2 + var3);
            IntArrayList var7 = new IntArrayList(var6 - var5);

            for(int var8 = var5; var8 < var6; ++var8) {
               var7.add(var1.getInt(var8));
            }

            return var7;
         }
      } else {
         return new IntArrayList();
      }
   }

   public void shutdown() {
      this.forkJoinPool.shutdown();

      try {
         if (!this.forkJoinPool.awaitTermination(10L, TimeUnit.SECONDS)) {
            this.plugin.getLogger().warning("ForkJoinPool did not terminate within 10 seconds, forcing shutdown");
            this.forkJoinPool.shutdownNow();
            if (!this.forkJoinPool.awaitTermination(5L, TimeUnit.SECONDS)) {
               this.plugin.getLogger().severe("ForkJoinPool did not terminate properly after forced shutdown");
            }
         }
      } catch (InterruptedException var2) {
         this.plugin.getLogger().warning("ForkJoinPool shutdown interrupted, forcing shutdown");
         this.forkJoinPool.shutdownNow();
         Thread.currentThread().interrupt();
      }

   }
}
