package fr.maxlego08.zauctionhouse.libs.folialib.impl;

import fr.maxlego08.zauctionhouse.libs.folialib.FoliaLib;
import fr.maxlego08.zauctionhouse.libs.folialib.util.ImplementationTestsUtil;
import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class PaperImplementation extends SpigotImplementation {
   private final FoliaLib foliaLib;
   private Method teleportAsyncMethod;

   public PaperImplementation(FoliaLib var1) {
      super(var1);
      this.foliaLib = var1;
      if (ImplementationTestsUtil.isAsyncTeleportSupported()) {
         try {
            this.teleportAsyncMethod = Entity.class.getMethod("teleportAsync", Location.class, PlayerTeleportEvent.TeleportCause.class);
         } catch (NoSuchMethodException var3) {
            throw new RuntimeException("Failed to initialize PaperImplementation", var3);
         }
      }

   }

   public CompletableFuture<Boolean> teleportAsync(Entity var1, Location var2) {
      return this.teleportAsync(var1, var2, TeleportCause.PLUGIN);
   }

   public CompletableFuture<Boolean> teleportAsync(Entity var1, Location var2, PlayerTeleportEvent.TeleportCause var3) {
      if (!ImplementationTestsUtil.isAsyncTeleportSupported()) {
         return super.teleportAsync(var1, var2, var3);
      } else {
         try {
            return (CompletableFuture)this.teleportAsyncMethod.invoke(var1, var2, var3);
         } catch (Exception var5) {
            var5.printStackTrace();
            return super.teleportAsync(var1, var2, var3);
         }
      }
   }
}
