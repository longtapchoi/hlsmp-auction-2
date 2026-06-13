package fr.maxlego08.zauctionhouse.libs.folialib.wrapper.task;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.plugin.Plugin;

public class WrappedFoliaTask implements WrappedTask {
   private static final Class<? extends ScheduledTask> ASYNC_TASK_CLASS;
   private final ScheduledTask task;
   private final boolean async;

   public WrappedFoliaTask(ScheduledTask var1) {
      this.task = var1;
      if (ASYNC_TASK_CLASS == null) {
         this.async = false;
      } else {
         this.async = ASYNC_TASK_CLASS.isAssignableFrom(var1.getClass());
      }

   }

   public void cancel() {
      this.task.cancel();
   }

   public boolean isCancelled() {
      return this.task.isCancelled();
   }

   public Plugin getOwningPlugin() {
      return this.task.getOwningPlugin();
   }

   public boolean isAsync() {
      return this.async;
   }

   static {
      Class var0 = null;

      try {
         var0 = Class.forName("io.papermc.paper.threadedregions.scheduler.FoliaAsyncScheduler.AsyncScheduledTask");
      } catch (ClassNotFoundException var2) {
      }

      ASYNC_TASK_CLASS = var0;
   }
}
