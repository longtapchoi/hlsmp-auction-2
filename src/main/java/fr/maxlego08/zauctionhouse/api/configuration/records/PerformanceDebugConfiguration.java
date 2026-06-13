package fr.maxlego08.zauctionhouse.api.configuration.records;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import java.util.List;
import org.bukkit.configuration.file.FileConfiguration;

public record PerformanceDebugConfiguration(FilterMode mode, List<String> operations) {
   public static PerformanceDebugConfiguration of(AuctionPlugin var0, FileConfiguration var1) {
      String var2 = var1.getString("performance-debug.filter.mode", "DISABLED");

      FilterMode var3;
      try {
         var3 = PerformanceDebugConfiguration.FilterMode.valueOf(var2.toUpperCase());
      } catch (IllegalArgumentException var5) {
         var0.getLogger().warning("Invalid performance-debug filter mode: " + var2 + ". Using DISABLED.");
         var3 = PerformanceDebugConfiguration.FilterMode.DISABLED;
      }

      List var4 = var1.getStringList("performance-debug.filter.operations");
      return new PerformanceDebugConfiguration(var3, var4);
   }

   public boolean shouldLog(String var1) {
      boolean var10000;
      switch (this.mode.ordinal()) {
         case 0 -> var10000 = this.matchesAny(var1);
         case 1 -> var10000 = !this.matchesAny(var1);
         case 2 -> var10000 = true;
         default -> throw new MatchException((String)null, (Throwable)null);
      }

      return var10000;
   }

   private boolean matchesAny(String var1) {
      for(String var3 : this.operations) {
         if (var3.endsWith("*")) {
            String var4 = var3.substring(0, var3.length() - 1);
            if (var1.startsWith(var4)) {
               return true;
            }
         } else if (var1.equals(var3)) {
            return true;
         }
      }

      return false;
   }

   public static enum FilterMode {
      WHITELIST,
      BLACKLIST,
      DISABLED;

      // $FF: synthetic method
      private static FilterMode[] $values() {
         return new FilterMode[]{WHITELIST, BLACKLIST, DISABLED};
      }
   }
}
