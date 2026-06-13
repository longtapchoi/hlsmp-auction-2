package fr.maxlego08.zauctionhouse.libs.folialib.util;

import java.util.function.Consumer;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public class ImplementationTestsUtil {
   private static final boolean IS_CANCELLED_SUPPORTED;
   private static final boolean IS_TASK_CONSUMERS_SUPPORTED;
   private static final boolean IS_ASYNC_TELEPORT_SUPPORTED;

   public static boolean isCancelledSupported() {
      return IS_CANCELLED_SUPPORTED;
   }

   public static boolean isTaskConsumersSupported() {
      return IS_TASK_CONSUMERS_SUPPORTED;
   }

   public static boolean isAsyncTeleportSupported() {
      return IS_ASYNC_TELEPORT_SUPPORTED;
   }

   static {
      boolean var0 = false;

      try {
         Class var1 = BukkitTask.class;
         var1.getDeclaredMethod("isCancelled");
         var0 = true;
      } catch (NoSuchMethodException var6) {
      }

      IS_CANCELLED_SUPPORTED = var0;
      boolean var7 = false;

      try {
         Class var2 = BukkitScheduler.class;
         var2.getDeclaredMethod("runTask", Plugin.class, Consumer.class);
         var7 = true;
      } catch (NoSuchMethodException var5) {
      }

      IS_TASK_CONSUMERS_SUPPORTED = var7;
      boolean var8 = false;

      try {
         Class var3 = Entity.class;
         var3.getDeclaredMethod("teleportAsync", Location.class, PlayerTeleportEvent.TeleportCause.class);
         var8 = true;
      } catch (NoSuchMethodException var4) {
      }

      IS_ASYNC_TELEPORT_SUPPORTED = var8;
   }
}
