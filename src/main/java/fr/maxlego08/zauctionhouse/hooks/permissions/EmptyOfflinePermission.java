package fr.maxlego08.zauctionhouse.hooks.permissions;

import fr.maxlego08.zauctionhouse.api.hooks.permission.OfflinePermission;
import fr.maxlego08.zauctionhouse.api.hooks.permission.OfflinePermissionResult;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import org.bukkit.OfflinePlayer;

public class EmptyOfflinePermission implements OfflinePermission {
   public CompletableFuture<List<OfflinePermissionResult>> hasPermissions(OfflinePlayer var1, Set<String> var2) {
      return CompletableFuture.completedFuture(var2.stream().map((var0) -> new OfflinePermissionResult(var0, false)).toList());
   }
}
