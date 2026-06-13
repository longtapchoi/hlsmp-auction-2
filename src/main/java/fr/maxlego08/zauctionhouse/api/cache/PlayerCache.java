package fr.maxlego08.zauctionhouse.api.cache;

import java.util.function.Supplier;

public interface PlayerCache {
   <T> void set(PlayerCacheKey var1, T var2);

   <T> T get(PlayerCacheKey var1);

   <T> T get(PlayerCacheKey var1, T var2);

   boolean has(PlayerCacheKey var1);

   void remove(PlayerCacheKey var1);

   void remove(PlayerCacheKey... var1);

   <T> T getOrCompute(PlayerCacheKey var1, Supplier<T> var2);
}
