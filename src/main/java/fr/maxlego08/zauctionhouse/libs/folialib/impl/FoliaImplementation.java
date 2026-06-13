package fr.maxlego08.zauctionhouse.libs.folialib.impl;

import fr.maxlego08.zauctionhouse.libs.folialib.FoliaLib;
import fr.maxlego08.zauctionhouse.libs.folialib.enums.EntityTaskResult;
import fr.maxlego08.zauctionhouse.libs.folialib.util.InvalidTickDelayNotifier;
import fr.maxlego08.zauctionhouse.libs.folialib.util.TimeConverter;
import fr.maxlego08.zauctionhouse.libs.folialib.wrapper.task.WrappedFoliaTask;
import fr.maxlego08.zauctionhouse.libs.folialib.wrapper.task.WrappedTask;
import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import io.papermc.paper.threadedregions.scheduler.RegionScheduler;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class FoliaImplementation implements PlatformScheduler {
   private final FoliaLib foliaLib;
   private final Plugin plugin;
   private final GlobalRegionScheduler globalRegionScheduler;
   private final RegionScheduler regionScheduler;
   private final AsyncScheduler asyncScheduler;
   private final InvalidTickDelayNotifier tickNotifier;

   public FoliaImplementation(FoliaLib var1) {
      this.foliaLib = var1;
      this.plugin = var1.getPlugin();
      this.globalRegionScheduler = this.plugin.getServer().getGlobalRegionScheduler();
      this.regionScheduler = this.plugin.getServer().getRegionScheduler();
      this.asyncScheduler = this.plugin.getServer().getAsyncScheduler();
      this.tickNotifier = this.foliaLib.getInvalidTickDelayNotifier();
   }

   public boolean isOwnedByCurrentRegion(@NotNull Location var1) {
      return this.plugin.getServer().isOwnedByCurrentRegion(var1);
   }

   public boolean isOwnedByCurrentRegion(@NotNull Location var1, int var2) {
      return this.plugin.getServer().isOwnedByCurrentRegion(var1, var2);
   }

   public boolean isOwnedByCurrentRegion(@NotNull Block var1) {
      return this.plugin.getServer().isOwnedByCurrentRegion(var1);
   }

   public boolean isOwnedByCurrentRegion(@NotNull World var1, int var2, int var3) {
      return this.plugin.getServer().isOwnedByCurrentRegion(var1, var2, var3);
   }

   public boolean isOwnedByCurrentRegion(@NotNull World var1, int var2, int var3, int var4) {
      return this.plugin.getServer().isOwnedByCurrentRegion(var1, var2, var3, var4);
   }

   public boolean isOwnedByCurrentRegion(@NotNull Entity var1) {
      return this.plugin.getServer().isOwnedByCurrentRegion(var1);
   }

   public boolean isGlobalTickThread() {
      return this.plugin.getServer().isGlobalTickThread();
   }

   public @NotNull CompletableFuture<Void> runNextTick(@NotNull Consumer<WrappedTask> var1) {
      CompletableFuture var2 = new CompletableFuture();
      this.globalRegionScheduler.run(this.plugin, (var3) -> {
         var1.accept(this.wrapTask(var3));
         var2.complete((Object)null);
      });
      return var2;
   }

   public @NotNull CompletableFuture<Void> runAsync(@NotNull Consumer<WrappedTask> var1) {
      CompletableFuture var2 = new CompletableFuture();
      this.asyncScheduler.runNow(this.plugin, (var3) -> {
         var1.accept(this.wrapTask(var3));
         var2.complete((Object)null);
      });
      return var2;
   }

   public WrappedTask runLater(@NotNull Runnable var1, long var2) {
      var2 = this.ensureValidDuration(var2);
      return this.wrapTask(this.globalRegionScheduler.runDelayed(this.plugin, (var1x) -> var1.run(), var2));
   }

   public @NotNull CompletableFuture<Void> runLater(@NotNull Consumer<WrappedTask> var1, long var2) {
      CompletableFuture var4 = new CompletableFuture();
      var2 = this.ensureValidDuration(var2);
      this.globalRegionScheduler.runDelayed(this.plugin, (var3) -> {
         var1.accept(this.wrapTask(var3));
         var4.complete((Object)null);
      }, var2);
      return var4;
   }

   public WrappedTask runLater(@NotNull Runnable var1, long var2, TimeUnit var4) {
      return this.runLater(var1, TimeConverter.toTicks(var2, var4));
   }

   public @NotNull CompletableFuture<Void> runLater(@NotNull Consumer<WrappedTask> var1, long var2, TimeUnit var4) {
      return this.runLater(var1, TimeConverter.toTicks(var2, var4));
   }

   public WrappedTask runLaterAsync(@NotNull Runnable var1, long var2) {
      return this.runLaterAsync(var1, TimeConverter.toMillis(var2), TimeUnit.MILLISECONDS);
   }

   public @NotNull CompletableFuture<Void> runLaterAsync(@NotNull Consumer<WrappedTask> var1, long var2) {
      return this.runLaterAsync(var1, TimeConverter.toMillis(var2), TimeUnit.MILLISECONDS);
   }

   public WrappedTask runLaterAsync(@NotNull Runnable var1, long var2, TimeUnit var4) {
      return this.wrapTask(this.asyncScheduler.runDelayed(this.plugin, (var1x) -> var1.run(), var2, var4));
   }

   public @NotNull CompletableFuture<Void> runLaterAsync(@NotNull Consumer<WrappedTask> var1, long var2, TimeUnit var4) {
      CompletableFuture var5 = new CompletableFuture();
      this.asyncScheduler.runDelayed(this.plugin, (var3) -> {
         var1.accept(this.wrapTask(var3));
         var5.complete((Object)null);
      }, var2, var4);
      return var5;
   }

   public WrappedTask runTimer(@NotNull Runnable var1, long var2, long var4) {
      var2 = this.ensureValidDuration(var2);
      var4 = this.ensureValidDuration(var4);
      return this.wrapTask(this.globalRegionScheduler.runAtFixedRate(this.plugin, (var1x) -> var1.run(), var2, var4));
   }

   public void runTimer(@NotNull Consumer<WrappedTask> var1, long var2, long var4) {
      var2 = this.ensureValidDuration(var2);
      var4 = this.ensureValidDuration(var4);
      this.globalRegionScheduler.runAtFixedRate(this.plugin, (var2x) -> var1.accept(this.wrapTask(var2x)), var2, var4);
   }

   public WrappedTask runTimer(@NotNull Runnable var1, long var2, long var4, TimeUnit var6) {
      return this.runTimer(var1, TimeConverter.toTicks(var2, var6), TimeConverter.toTicks(var4, var6));
   }

   public void runTimer(@NotNull Consumer<WrappedTask> var1, long var2, long var4, TimeUnit var6) {
      this.runTimer(var1, TimeConverter.toTicks(var2, var6), TimeConverter.toTicks(var4, var6));
   }

   public WrappedTask runTimerAsync(@NotNull Runnable var1, long var2, long var4) {
      return this.runTimerAsync(var1, TimeConverter.toMillis(var2), TimeConverter.toMillis(var4), TimeUnit.MILLISECONDS);
   }

   public void runTimerAsync(@NotNull Consumer<WrappedTask> var1, long var2, long var4) {
      this.runTimerAsync(var1, TimeConverter.toMillis(var2), TimeConverter.toMillis(var4), TimeUnit.MILLISECONDS);
   }

   public WrappedTask runTimerAsync(@NotNull Runnable var1, long var2, long var4, TimeUnit var6) {
      return this.wrapTask(this.asyncScheduler.runAtFixedRate(this.plugin, (var1x) -> var1.run(), var2, var4, var6));
   }

   public void runTimerAsync(@NotNull Consumer<WrappedTask> var1, long var2, long var4, TimeUnit var6) {
      this.asyncScheduler.runAtFixedRate(this.plugin, (var2x) -> var1.accept(this.wrapTask(var2x)), var2, var4, var6);
   }

   public @NotNull CompletableFuture<Void> runAtLocation(Location var1, @NotNull Consumer<WrappedTask> var2) {
      CompletableFuture var3 = new CompletableFuture();
      this.regionScheduler.run(this.plugin, var1, (var3x) -> {
         var2.accept(this.wrapTask(var3x));
         var3.complete((Object)null);
      });
      return var3;
   }

   public WrappedTask runAtLocationLater(Location var1, @NotNull Runnable var2, long var3) {
      var3 = this.ensureValidDuration(var3);
      return this.wrapTask(this.regionScheduler.runDelayed(this.plugin, var1, (var1x) -> var2.run(), var3));
   }

   public @NotNull CompletableFuture<Void> runAtLocationLater(Location var1, @NotNull Consumer<WrappedTask> var2, long var3) {
      CompletableFuture var5 = new CompletableFuture();
      var3 = this.ensureValidDuration(var3);
      this.regionScheduler.runDelayed(this.plugin, var1, (var3x) -> {
         var2.accept(this.wrapTask(var3x));
         var5.complete((Object)null);
      }, var3);
      return var5;
   }

   public WrappedTask runAtLocationLater(Location var1, @NotNull Runnable var2, long var3, TimeUnit var5) {
      return this.runAtLocationLater(var1, var2, TimeConverter.toTicks(var3, var5));
   }

   public @NotNull CompletableFuture<Void> runAtLocationLater(Location var1, @NotNull Consumer<WrappedTask> var2, long var3, TimeUnit var5) {
      return this.runAtLocationLater(var1, var2, TimeConverter.toTicks(var3, var5));
   }

   public WrappedTask runAtLocationTimer(Location var1, @NotNull Runnable var2, long var3, long var5) {
      var3 = this.ensureValidDuration(var3);
      var5 = this.ensureValidDuration(var5);
      return this.wrapTask(this.regionScheduler.runAtFixedRate(this.plugin, var1, (var1x) -> var2.run(), var3, var5));
   }

   public void runAtLocationTimer(Location var1, @NotNull Consumer<WrappedTask> var2, long var3, long var5) {
      var3 = this.ensureValidDuration(var3);
      var5 = this.ensureValidDuration(var5);
      this.regionScheduler.runAtFixedRate(this.plugin, var1, (var2x) -> var2.accept(this.wrapTask(var2x)), var3, var5);
   }

   public WrappedTask runAtLocationTimer(Location var1, @NotNull Runnable var2, long var3, long var5, TimeUnit var7) {
      return this.runAtLocationTimer(var1, var2, TimeConverter.toTicks(var3, var7), TimeConverter.toTicks(var5, var7));
   }

   public void runAtLocationTimer(Location var1, @NotNull Consumer<WrappedTask> var2, long var3, long var5, TimeUnit var7) {
      this.runAtLocationTimer(var1, var2, TimeConverter.toTicks(var3, var7), TimeConverter.toTicks(var5, var7));
   }

   public @NotNull CompletableFuture<EntityTaskResult> runAtEntity(Entity var1, @NotNull Consumer<WrappedTask> var2) {
      CompletableFuture var3 = new CompletableFuture();
      ScheduledTask var4 = var1.getScheduler().run(this.plugin, (var3x) -> {
         var2.accept(this.wrapTask(var3x));
         var3.complete(EntityTaskResult.SUCCESS);
      }, (Runnable)null);
      if (var4 == null) {
         var3.complete(EntityTaskResult.SCHEDULER_RETIRED);
      }

      return var3;
   }

   public @NotNull CompletableFuture<EntityTaskResult> runAtEntityWithFallback(Entity var1, @NotNull Consumer<WrappedTask> var2, Runnable var3) {
      CompletableFuture var4 = new CompletableFuture();
      ScheduledTask var5 = var1.getScheduler().run(this.plugin, (var3x) -> {
         var2.accept(this.wrapTask(var3x));
         var4.complete(EntityTaskResult.SUCCESS);
      }, () -> {
         var3.run();
         var4.complete(EntityTaskResult.ENTITY_RETIRED);
      });
      if (var5 == null) {
         var4.complete(EntityTaskResult.SCHEDULER_RETIRED);
      }

      return var4;
   }

   public WrappedTask runAtEntityLater(Entity var1, @NotNull Runnable var2, long var3) {
      return this.runAtEntityLater(var1, var2, (Runnable)null, var3);
   }

   public WrappedTask runAtEntityLater(Entity var1, @NotNull Runnable var2, Runnable var3, long var4) {
      var4 = this.ensureValidDuration(var4);
      return this.wrapTask(var1.getScheduler().runDelayed(this.plugin, (var1x) -> var2.run(), var3, var4));
   }

   public @NotNull CompletableFuture<Void> runAtEntityLater(Entity var1, @NotNull Consumer<WrappedTask> var2, long var3) {
      return this.runAtEntityLater(var1, (Consumer)var2, (Runnable)null, var3);
   }

   public @NotNull CompletableFuture<Void> runAtEntityLater(Entity var1, @NotNull Consumer<WrappedTask> var2, Runnable var3, long var4) {
      CompletableFuture var6 = new CompletableFuture();
      final Runnable var3Final;
      if (var3 != null) {
         final Runnable var3Orig = var3;
         var3Final = () -> {
            var3Orig.run();
            var6.complete(null);
         };
      } else {
         var3Final = null;
      }

      var4 = this.ensureValidDuration(var4);
      var1.getScheduler().runDelayed(this.plugin, (var3x) -> {
         var2.accept(this.wrapTask(var3x));
         var6.complete(null);
      }, var3Final, var4);
      return var6;
   }

   public WrappedTask runAtEntityLater(Entity var1, @NotNull Runnable var2, long var3, TimeUnit var5) {
      return this.runAtEntityLater(var1, var2, TimeConverter.toTicks(var3, var5));
   }

   public @NotNull CompletableFuture<Void> runAtEntityLater(Entity var1, @NotNull Consumer<WrappedTask> var2, long var3, TimeUnit var5) {
      return this.runAtEntityLater(var1, var2, TimeConverter.toTicks(var3, var5));
   }

   public WrappedTask runAtEntityTimer(Entity var1, @NotNull Runnable var2, long var3, long var5) {
      return this.runAtEntityTimer(var1, var2, (Runnable)null, var3, var5);
   }

   public WrappedTask runAtEntityTimer(Entity var1, @NotNull Runnable var2, Runnable var3, long var4, long var6) {
      var4 = this.ensureValidDuration(var4);
      var6 = this.ensureValidDuration(var6);
      return this.wrapTask(var1.getScheduler().runAtFixedRate(this.plugin, (var1x) -> var2.run(), var3, var4, var6));
   }

   public void runAtEntityTimer(Entity var1, @NotNull Consumer<WrappedTask> var2, long var3, long var5) {
      this.runAtEntityTimer(var1, (Consumer)var2, (Runnable)null, var3, var5);
   }

   public void runAtEntityTimer(Entity var1, @NotNull Consumer<WrappedTask> var2, Runnable var3, long var4, long var6) {
      var4 = this.ensureValidDuration(var4);
      var6 = this.ensureValidDuration(var6);
      var1.getScheduler().runAtFixedRate(this.plugin, (var2x) -> var2.accept(this.wrapTask(var2x)), var3, var4, var6);
   }

   public WrappedTask runAtEntityTimer(Entity var1, @NotNull Runnable var2, long var3, long var5, TimeUnit var7) {
      return this.runAtEntityTimer(var1, var2, TimeConverter.toTicks(var3, var7), TimeConverter.toTicks(var5, var7));
   }

   public void runAtEntityTimer(Entity var1, @NotNull Consumer<WrappedTask> var2, long var3, long var5, TimeUnit var7) {
      this.runAtEntityTimer(var1, var2, TimeConverter.toTicks(var3, var7), TimeConverter.toTicks(var5, var7));
   }

   public void cancelTask(WrappedTask var1) {
      var1.cancel();
   }

   public void cancelAllTasks() {
      this.globalRegionScheduler.cancelTasks(this.plugin);
      this.asyncScheduler.cancelTasks(this.plugin);
   }

   public List<WrappedTask> getAllTasks() {
      try {
         return (List)this.getAllScheduledTasks().stream().filter((var1) -> var1.getOwningPlugin().equals(this.plugin)).map(this::wrapTask).collect(Collectors.toList());
      } catch (Exception var2) {
         var2.printStackTrace();
         return null;
      }
   }

   public List<WrappedTask> getAllServerTasks() {
      try {
         return (List)this.getAllScheduledTasks().stream().map(this::wrapTask).collect(Collectors.toList());
      } catch (Exception var2) {
         var2.printStackTrace();
         return null;
      }
   }

   private @NotNull List<ScheduledTask> getAllScheduledTasks() {
      try {
         Class var1 = this.globalRegionScheduler.getClass();
         Field var2 = var1.getDeclaredField("tasksByDeadline");
         boolean var3 = var2.isAccessible();
         var2.setAccessible(true);
         Long2ObjectOpenHashMap var4 = (Long2ObjectOpenHashMap)var2.get(this.globalRegionScheduler);
         var2.setAccessible(var3);
         Class var5 = this.asyncScheduler.getClass();
         Field var6 = var5.getDeclaredField("tasks");
         var3 = var6.isAccessible();
         var6.setAccessible(true);
         Set var7 = (Set)var6.get(this.asyncScheduler);
         var6.setAccessible(var3);
         ArrayList<ScheduledTask> var8 = new ArrayList<>();
         ObjectIterator var9 = var4.values().iterator();

         while(var9.hasNext()) {
            List var10 = (List)var9.next();
            var8.addAll(var10);
         }

         ArrayList var12 = new ArrayList(var8.size() + var7.size());
         var12.addAll(var8);
         var12.addAll(var7);
         return var12;
      } catch (NoSuchFieldException | IllegalAccessException var_ex) {
         throw new RuntimeException("Failed to get scheduled tasks", var_ex);
      }
   }

   public Player getPlayer(String var1) {
      return this.plugin.getServer().getPlayer(var1);
   }

   public Player getPlayerExact(String var1) {
      return this.plugin.getServer().getPlayerExact(var1);
   }

   public Player getPlayer(UUID var1) {
      return this.plugin.getServer().getPlayer(var1);
   }

   public CompletableFuture<Boolean> teleportAsync(Entity var1, Location var2) {
      return var1.teleportAsync(var2);
   }

   public CompletableFuture<Boolean> teleportAsync(Entity var1, Location var2, PlayerTeleportEvent.TeleportCause var3) {
      return var1.teleportAsync(var2, var3);
   }

   public WrappedTask wrapTask(@NotNull Object var1) {
      Objects.requireNonNull(var1, "nativeTask");
      if (!(var1 instanceof ScheduledTask)) {
         throw new IllegalArgumentException("The nativeTask provided must be a ScheduledTask. Got: " + var1.getClass().getName() + " instead.");
      } else {
         return new WrappedFoliaTask((ScheduledTask)var1);
      }
   }

   private long ensureValidDuration(long var1) {
      if (var1 <= 0L) {
         this.tickNotifier.notifyOnce(var1);
         return 1L;
      } else {
         return var1;
      }
   }
}
