package fr.maxlego08.zauctionhouse.utils.cache;

import java.util.concurrent.ConcurrentHashMap;

public class SimpleCache<K, V> {
   private final ConcurrentHashMap<K, V> cache = new ConcurrentHashMap();

   public V get(K var1, Loader<V> var2) {
      return (V)this.cache.computeIfAbsent(var1, (var2x) -> {
         Object var3 = var2.load();
         if (var3 == null) {
            throw new IllegalStateException("Cache loader returned null for key: " + String.valueOf(var1));
         } else {
            return var3;
         }
      });
   }

   @FunctionalInterface
   public interface Loader<V> {
      V load();
   }
}
