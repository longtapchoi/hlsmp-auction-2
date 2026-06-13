package fr.maxlego08.zauctionhouse.api.configuration.records;

import fr.maxlego08.menu.api.utils.TypedMapAccessor;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.hooks.permission.OfflinePermission;
import fr.maxlego08.zauctionhouse.api.hooks.permission.OfflinePermissionResult;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

@SuppressWarnings({"unchecked", "rawtypes"})
public record ExpirationConfiguration(long defaultExpiration, boolean enablePermission, Map<String, Long> expirations) {
   public static ExpirationConfiguration of(AuctionPlugin var0, FileConfiguration var1, String var2) {
      long var3 = var1.getLong(var2 + "default-expiration");
      boolean var5 = var1.getBoolean(var2 + "permission.enable");
      HashMap var6 = new HashMap();

      for(Map var8 : var1.getMapList(var2 + "permission.permissions")) {
         TypedMapAccessor var9 = new TypedMapAccessor(var8);
         String var10 = var9.getString("permission");
         long var11 = var9.getLong("expiration");
         if (var10 == null) {
            var0.getLogger().info("The permission is null for '" + var2 + "' !, you need to fix that ");
         } else {
            var6.put(var10, var11);
         }
      }

      return new ExpirationConfiguration(var3, var5, var6);
   }

   public long getExpiration(Player var1) {
      long var2 = this.defaultExpiration;
      if (this.enablePermission) {
         for(Map.Entry var5 : this.expirations.entrySet()) {
            if (var1.hasPermission((String)var5.getKey())) {
               var2 = Math.max(var2, (Long)var5.getValue());
            }
         }
      }

      return var2;
   }

   public CompletableFuture<Long> getExpiration(OfflinePermission var1, OfflinePlayer var2) {
      long var3 = this.defaultExpiration;
      return this.enablePermission ? var1.hasPermissions(var2, this.expirations.keySet()).handle((var3x, var4) -> {
         if (var4 == null && var3x != null) {
            long var5 = var3;

            for(OfflinePermissionResult var8 : var3x) {
               if (var8.result()) {
                  var5 = Math.max(var5, (Long)this.expirations.get(var8.permission()));
               }
            }

            return var5;
         } else {
            return var3;
         }
      }) : CompletableFuture.completedFuture(var3);
   }
}
