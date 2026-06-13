package fr.maxlego08.zauctionhouse.utils.cache;

import java.util.concurrent.ConcurrentHashMap;

public class ExpiringCache<K, V> {
   private final ConcurrentHashMap<K, CacheEntry<V>> cache = new ConcurrentHashMap<>();
   private final long expiryDurationMillis;

   public ExpiringCache(long var1) {
      this.expiryDurationMillis = var1;
   }

   public V get(K var1, Loader<V> var2) {
      return (this.cache.compute(var1, (var2x, var3) -> {
         long var4 = System.currentTimeMillis();
         if (var3 != null && var3.expiryTime >= var4) {
            return var3;
         } else {
            V var6 = var2.load();
            return new CacheEntry<>(var6, var4 + this.expiryDurationMillis);
         }
      }).value;
   }

   public void clear(K var1) {
      this.cache.remove(var1);
   }

   private static record CacheEntry<V>(V value, long expiryTime) {
   }

   @FunctionalInterface
   public interface Loader<V> {
      V load();
   }
}
