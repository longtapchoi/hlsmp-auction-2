package fr.maxlego08.zauctionhouse.hooks.permissions;

import fr.maxlego08.zauctionhouse.api.hooks.permission.OfflinePermission;
import fr.maxlego08.zauctionhouse.api.hooks.permission.OfflinePermissionResult;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedPermissionData;
import org.bukkit.OfflinePlayer;

public class LuckPermsOfflinePermission implements OfflinePermission {
   private final LuckPerms luckPerms = LuckPermsProvider.get();

   public CompletableFuture<List<OfflinePermissionResult>> hasPermissions(OfflinePlayer var1, Set<String> var2) {
      return this.luckPerms.getUserManager().loadUser(var1.getUniqueId()).thenApply((var2x) -> {
         if (var2x == null) {
            return var2.stream().map((var0) -> new OfflinePermissionResult(var0, false)).toList();
         } else {
            CachedPermissionData var3 = var2x.getCachedData().getPermissionData(this.luckPerms.getContextManager().getStaticQueryOptions());
            return var2.stream().map((var1) -> new OfflinePermissionResult(var1, var3.checkPermission(var1).asBoolean())).toList();
         }
      });
   }
}
