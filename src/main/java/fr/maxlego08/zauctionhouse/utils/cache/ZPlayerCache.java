package fr.maxlego08.zauctionhouse.utils.cache;

import fr.maxlego08.zauctionhouse.api.cache.PlayerCache;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCacheKey;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

public class ZPlayerCache implements PlayerCache {
   private final Map<PlayerCacheKey, Object> cache = new EnumMap(PlayerCacheKey.class);

   public <T> void set(PlayerCacheKey var1, T var2) {
      if (var2 != null && !var1.getRawType().isInstance(var2)) {
         String var10002 = String.valueOf(var1);
         throw new IllegalArgumentException("Invalid type for key " + var10002 + ": expected " + String.valueOf(var1.getType().getType()));
      } else {
         this.cache.put(var1, var2);
      }
   }

   public <T> T get(PlayerCacheKey var1) {
      return (T)this.get(var1, var1.getFallback());
   }

   public <T> T get(PlayerCacheKey var1, T var2) {
      return (T)this.cache.getOrDefault(var1, var2);
   }

   public boolean has(PlayerCacheKey var1) {
      return this.cache.containsKey(var1);
   }

   public void remove(PlayerCacheKey var1) {
      this.cache.remove(var1);
   }

   public void remove(PlayerCacheKey... var1) {
      for(PlayerCacheKey var5 : var1) {
         this.cache.remove(var5);
      }

   }

   public <T> T getOrCompute(PlayerCacheKey var1, Supplier<T> var2) {
      if (this.has(var1)) {
         return (T)this.get(var1);
      } else {
         Object var3 = var2.get();
         this.set(var1, var3);
         return (T)var3;
      }
   }
}
