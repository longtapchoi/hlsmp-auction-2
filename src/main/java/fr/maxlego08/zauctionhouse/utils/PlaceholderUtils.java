package fr.maxlego08.zauctionhouse.utils;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class PlaceholderUtils {
   private static final ConcurrentHashMap<String, CacheEntry> CACHE = new ConcurrentHashMap();

   protected String papi(String var1, CommandSender var2) {
      String var10000;
      if (var2 instanceof Player var3) {
         var10000 = PlaceholderUtils.PapiHelper.papi(var1, var3);
      } else {
         var10000 = var1;
      }

      return var10000;
   }

   protected List<String> papi(List<String> var1, Player var2) {
      return PlaceholderUtils.PapiHelper.papi(var1, var2);
   }

   private static class CacheEntry {
      String value;
      long timeStamp;

      public CacheEntry(String var1, long var2) {
         this.value = var1;
         this.timeStamp = var2;
      }

      public boolean isValid() {
         return System.currentTimeMillis() - this.timeStamp < 100L;
      }
   }

   public static class PapiHelper {
      public static String papi(String var0, Player var1) {
         if (var0 == null) {
            return null;
         } else if (var1 == null) {
            return var0;
         } else if (!var0.contains("%")) {
            return var0;
         } else {
            String var2 = var0 + ";" + String.valueOf(var1.getUniqueId());
            CacheEntry var3 = (CacheEntry)PlaceholderUtils.CACHE.get(var2);
            if (var3 != null && var3.isValid()) {
               return var3.value;
            } else {
               String var4 = PlaceholderAPI.setPlaceholders(var1, var0).replace("%player%", var1.getName());
               PlaceholderUtils.CACHE.put(var2, new CacheEntry(var4, System.currentTimeMillis()));
               return var4;
            }
         }
      }

      public static List<String> papi(List<String> var0, Player var1) {
         return var1 == null ? var0 : (List)var0.stream().map((var1x) -> papi(var1x, var1)).collect(Collectors.toList());
      }
   }
}
