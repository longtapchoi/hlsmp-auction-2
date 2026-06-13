package fr.maxlego08.zauctionhouse.libs.folialib.wrapper.task;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class WrappedBukkitTask implements WrappedTask {
   private final BukkitTask task;

   public WrappedBukkitTask(BukkitTask var1) {
      this.task = var1;
   }

   public void cancel() {
      this.task.cancel();
   }

   public boolean isCancelled() {
      return this.task.isCancelled();
   }

   public Plugin getOwningPlugin() {
      return this.task.getOwner();
   }

   public boolean isAsync() {
      return !this.task.isSync();
   }
}
