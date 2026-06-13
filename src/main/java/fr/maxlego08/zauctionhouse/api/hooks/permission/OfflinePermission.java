package fr.maxlego08.zauctionhouse.api.hooks.permission;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import org.bukkit.OfflinePlayer;

public interface OfflinePermission {
   CompletableFuture<List<OfflinePermissionResult>> hasPermissions(OfflinePlayer var1, Set<String> var2);
}
