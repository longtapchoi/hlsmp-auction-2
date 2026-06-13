package fr.maxlego08.zauctionhouse.libs.folialib.impl;

import fr.maxlego08.zauctionhouse.libs.folialib.FoliaLib;
import fr.maxlego08.zauctionhouse.libs.folialib.enums.EntityTaskResult;
import fr.maxlego08.zauctionhouse.libs.folialib.type.Ref;
import fr.maxlego08.zauctionhouse.libs.folialib.util.FoliaLibOptions;
import fr.maxlego08.zauctionhouse.libs.folialib.util.TimeConverter;
import fr.maxlego08.zauctionhouse.libs.folialib.wrapper.task.WrappedBukkitTask;
import fr.maxlego08.zauctionhouse.libs.folialib.wrapper.task.WrappedTask;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings({"unchecked", "rawtypes"})
public class SpigotImplementation implements PlatformScheduler {
   private final FoliaLib foliaLib;
   private final FoliaLibOptions options;
   private final Plugin plugin;
   private final @NotNull BukkitScheduler scheduler;

   public SpigotImplementation(FoliaLib var1) {
      this.foliaLib = var1;
      this.options = var1.getOptions();
      this.plugin = var1.getPlugin();
      this.scheduler = this.plugin.getServer().getScheduler();
   }

   public boolean isOwnedByCurrentRegion(@NotNull Location var1) {
      return this.plugin.getServer().isPrimaryThread();
   }

   public boolean isOwnedByCurrentRegion(@NotNull Location var1, int var2) {
      return this.plugin.getServer().isPrimaryThread();
   }

   public boolean isOwnedByCurrentRegion(@NotNull Block var1) {
      return this.plugin.getServer().isPrimaryThread();
   }

   public boolean isOwnedByCurrentRegion(@NotNull World var1, int var2, int var3) {
      return this.plugin.getServer().isPrimaryThread();
   }

   public boolean isOwnedByCurrentRegion(@NotNull World var1, int var2, int var3, int var4) {
      return this.plugin.getServer().isPrimaryThread();
   }

   public boolean isOwnedByCurrentRegion(@NotNull Entity var1) {
      return this.plugin.getServer().isPrimaryThread();
   }

   public boolean isGlobalTickThread() {
      return this.plugin.getServer().isPrimaryThread();
   }

   public @NotNull CompletableFuture<Void> runNextTick(@NotNull Consumer<WrappedTask> var1) {
      CompletableFuture var2 = new CompletableFuture();
      this.scheduler.runTask(this.plugin, (var3) -> {
         var1.accept(this.wrapTask(var3));
         var2.complete((Object)null);
      });
      return var2;
   }

   public @NotNull CompletableFuture<Void> runAsync(@NotNull Consumer<WrappedTask> var1) {
      CompletableFuture var2 = new CompletableFuture();
      this.scheduler.runTaskAsynchronously(this.plugin, (var3) -> {
         var1.accept(this.wrapTask(var3));
         var2.complete((Object)null);
      });
      return var2;
   }

   public WrappedTask runLater(@NotNull Runnable var1, long var2) {
      return this.wrapTask(this.scheduler.runTaskLater(this.plugin, var1, var2));
   }

   public @NotNull CompletableFuture<Void> runLater(@NotNull Consumer<WrappedTask> var1, long var2) {
      CompletableFuture var4 = new CompletableFuture();
      this.scheduler.runTaskLater(this.plugin, (var3) -> {
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
      return this.wrapTask(this.scheduler.runTaskLaterAsynchronously(this.plugin, var1, var2));
   }

   public @NotNull CompletableFuture<Void> runLaterAsync(@NotNull Consumer<WrappedTask> var1, long var2) {
      CompletableFuture var4 = new CompletableFuture();
      this.scheduler.runTaskLaterAsynchronously(this.plugin, (var3) -> {
         var1.accept(this.wrapTask(var3));
         var4.complete((Object)null);
      }, var2);
      return var4;
   }

   public WrappedTask runLaterAsync(@NotNull Runnable var1, long var2, TimeUnit var4) {
      return this.runLaterAsync(var1, TimeConverter.toTicks(var2, var4));
   }

   public @NotNull CompletableFuture<Void> runLaterAsync(@NotNull Consumer<WrappedTask> var1, long var2, TimeUnit var4) {
      return this.runLaterAsync(var1, TimeConverter.toTicks(var2, var4));
   }

   public WrappedTask runTimer(@NotNull Runnable var1, long var2, long var4) {
      return this.wrapTask(this.scheduler.runTaskTimer(this.plugin, var1, var2, var4));
   }

   public void runTimer(@NotNull Consumer<WrappedTask> var1, long var2, long var4) {
      this.scheduler.runTaskTimer(this.plugin, (var2x) -> var1.accept(this.wrapTask(var2x)), var2, var4);
   }

   public WrappedTask runTimer(@NotNull Runnable var1, long var2, long var4, TimeUnit var6) {
      return this.runTimer(var1, TimeConverter.toTicks(var2, var6), TimeConverter.toTicks(var4, var6));
   }

   public void runTimer(@NotNull Consumer<WrappedTask> var1, long var2, long var4, TimeUnit var6) {
      this.runTimer(var1, TimeConverter.toTicks(var2, var6), TimeConverter.toTicks(var4, var6));
   }

   public WrappedTask runTimerAsync(@NotNull Runnable var1, long var2, long var4) {
      return this.wrapTask(this.scheduler.runTaskTimerAsynchronously(this.plugin, var1, var2, var4));
   }

   public void runTimerAsync(@NotNull Consumer<WrappedTask> var1, long var2, long var4) {
      this.scheduler.runTaskTimerAsynchronously(this.plugin, (var2x) -> var1.accept(this.wrapTask(var2x)), var2, var4);
   }

   public WrappedTask runTimerAsync(@NotNull Runnable var1, long var2, long var4, TimeUnit var6) {
      return this.runTimerAsync(var1, TimeConverter.toTicks(var2, var6), TimeConverter.toTicks(var4, var6));
   }

   public void runTimerAsync(@NotNull Consumer<WrappedTask> var1, long var2, long var4, TimeUnit var6) {
      this.runTimerAsync(var1, TimeConverter.toTicks(var2, var6), TimeConverter.toTicks(var4, var6));
   }

   public @NotNull CompletableFuture<Void> runAtLocation(Location var1, @NotNull Consumer<WrappedTask> var2) {
      return this.runNextTick(var2);
   }

   public WrappedTask runAtLocationLater(Location var1, @NotNull Runnable var2, long var3) {
      return this.runLater(var2, var3);
   }

   public @NotNull CompletableFuture<Void> runAtLocationLater(Location var1, @NotNull Consumer<WrappedTask> var2, long var3) {
      return this.runLater(var2, var3);
   }

   public WrappedTask runAtLocationLater(Location var1, @NotNull Runnable var2, long var3, TimeUnit var5) {
      return this.runAtLocationLater(var1, var2, TimeConverter.toTicks(var3, var5));
   }

   public @NotNull CompletableFuture<Void> runAtLocationLater(Location var1, @NotNull Consumer<WrappedTask> var2, long var3, TimeUnit var5) {
      return this.runAtLocationLater(var1, var2, TimeConverter.toTicks(var3, var5));
   }

   public WrappedTask runAtLocationTimer(Location var1, @NotNull Runnable var2, long var3, long var5) {
      return this.runTimer(var2, var3, var5);
   }

   public void runAtLocationTimer(Location var1, @NotNull Consumer<WrappedTask> var2, long var3, long var5) {
      this.runTimer(var2, var3, var5);
   }

   public WrappedTask runAtLocationTimer(Location var1, @NotNull Runnable var2, long var3, long var5, TimeUnit var7) {
      return this.runAtLocationTimer(var1, var2, TimeConverter.toTicks(var3, var7), TimeConverter.toTicks(var5, var7));
   }

   public void runAtLocationTimer(Location var1, @NotNull Consumer<WrappedTask> var2, long var3, long var5, TimeUnit var7) {
      this.runAtLocationTimer(var1, var2, TimeConverter.toTicks(var3, var7), TimeConverter.toTicks(var5, var7));
   }

   public @NotNull CompletableFuture<EntityTaskResult> runAtEntity(Entity var1, @NotNull Consumer<WrappedTask> var2) {
      CompletableFuture var3 = new CompletableFuture();
      this.runNextTick((var2x) -> {
         var2.accept(var2x);
         var3.complete(EntityTaskResult.SUCCESS);
      });
      return var3;
   }

   public @NotNull CompletableFuture<EntityTaskResult> runAtEntityWithFallback(Entity var1, @NotNull Consumer<WrappedTask> var2, Runnable var3) {
      CompletableFuture var4 = new CompletableFuture();
      this.runNextTick((var5) -> {
         if (this.isValid(var1)) {
            var2.accept(var5);
            var4.complete(EntityTaskResult.SUCCESS);
         } else {
            var3.run();
            var4.complete(EntityTaskResult.ENTITY_RETIRED);
         }

      });
      return var4;
   }

   public WrappedTask runAtEntityLater(Entity var1, @NotNull Runnable var2, long var3) {
      return this.runAtEntityLater(var1, var2, (Runnable)null, var3);
   }

   public WrappedTask runAtEntityLater(Entity var1, @NotNull Runnable var2, Runnable var3, long var4) {
      if (!this.isValid(var1)) {
         if (var3 != null) {
            var3.run();
         }

         return null;
      } else {
         return this.runLater(var2, var4);
      }
   }

   public @NotNull CompletableFuture<Void> runAtEntityLater(Entity var1, @NotNull Consumer<WrappedTask> var2, long var3) {
      return this.runAtEntityLater(var1, (Consumer)var2, (Runnable)null, var3);
   }

   public @NotNull CompletableFuture<Void> runAtEntityLater(Entity var1, @NotNull Consumer<WrappedTask> var2, @Nullable Runnable var3, long var4) {
      CompletableFuture var6 = new CompletableFuture();
      if (!this.isValid(var1)) {
         var6.complete((Object)null);
         if (var3 != null) {
            var3.run();
         }

         return var6;
      } else {
         this.runLater((Consumer)((var5) -> {
            if (!this.isValid(var1)) {
               if (var3 != null) {
                  var3.run();
               }

               var6.complete((Object)null);
            } else {
               var2.accept(var5);
               var6.complete((Object)null);
            }
         }), var4);
         return var6;
      }
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

   public WrappedTask runAtEntityTimer(Entity var1, @NotNull Runnable var2, @Nullable Runnable var3, long var4, long var6) {
      if (!this.isValid(var1)) {
         if (var3 != null) {
            var3.run();
         }

         return null;
      } else {
         Ref var8 = new Ref();
         WrappedTask var9 = this.runTimer((Runnable)(() -> {
            WrappedTask var5 = (WrappedTask)var8.get();
            if (var5 != null && !this.isValid(var1)) {
               var5.cancel();
               if (var3 != null) {
                  var3.run();
               }

            } else {
               var2.run();
            }
         }), var4, var6);
         var8.set(var9);
         return var9;
      }
   }

   public void runAtEntityTimer(Entity var1, @NotNull Consumer<WrappedTask> var2, long var3, long var5) {
      this.runAtEntityTimer(var1, (Consumer)var2, (Runnable)null, var3, var5);
   }

   public void runAtEntityTimer(Entity var1, @NotNull Consumer<WrappedTask> var2, Runnable var3, long var4, long var6) {
      if (!this.isValid(var1)) {
         if (var3 != null) {
            var3.run();
         }

      } else {
         this.runTimer((Consumer)((var4x) -> {
            if (!this.isValid(var1)) {
               if (var3 != null) {
                  var3.run();
               }

            } else {
               var2.accept(var4x);
            }
         }), var4, var6);
      }
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
      this.scheduler.cancelTasks(this.plugin);
   }

   public List<WrappedTask> getAllTasks() {
      return (List)this.scheduler.getPendingTasks().stream().filter((var1) -> var1.getOwner().equals(this.plugin)).map(this::wrapTask).collect(Collectors.toList());
   }

   public List<WrappedTask> getAllServerTasks() {
      return (List)this.scheduler.getPendingTasks().stream().map(this::wrapTask).collect(Collectors.toList());
   }

   public Player getPlayer(String var1) {
      return this.getPlayerFromMainThread(() -> this.plugin.getServer().getPlayer(var1));
   }

   public Player getPlayerExact(String var1) {
      return this.getPlayerFromMainThread(() -> this.plugin.getServer().getPlayerExact(var1));
   }

   public Player getPlayer(UUID var1) {
      return this.getPlayerFromMainThread(() -> this.plugin.getServer().getPlayer(var1));
   }

   private Player getPlayerFromMainThread(Supplier<Player> var1) {
      if (this.plugin.getServer().isPrimaryThread()) {
         return (Player)var1.get();
      } else {
         try {
            BukkitScheduler var10000 = this.scheduler;
            Plugin var10001 = this.plugin;
            Objects.requireNonNull(var1);
            return (Player)var10000.callSyncMethod(var10001, var1::get).get();
         } catch (ExecutionException | InterruptedException var3) {
            ((Exception)var3).printStackTrace();
            return null;
         }
      }
   }

   public CompletableFuture<Boolean> teleportAsync(Entity var1, Location var2) {
      return this.teleportAsync(var1, var2, TeleportCause.PLUGIN);
   }

   public CompletableFuture<Boolean> teleportAsync(Entity var1, Location var2, PlayerTeleportEvent.TeleportCause var3) {
      CompletableFuture var4 = new CompletableFuture();
      this.runAtEntity(var1, (var4x) -> {
         if (this.isValid(var1)) {
            boolean var5 = var1.teleport(var2);
            var4.complete(var5);
         } else {
            var4.complete(false);
         }

      });
      return var4;
   }

   public WrappedTask wrapTask(@NotNull Object var1) {
      Objects.requireNonNull(var1, "nativeTask cannot be null");
      if (!(var1 instanceof BukkitTask)) {
         throw new IllegalArgumentException("The nativeTask provided must be a BukkitTask. Got: " + var1.getClass().getName() + " instead.");
      } else {
         return new WrappedBukkitTask((BukkitTask)var1);
      }
   }

   private boolean isValid(Entity var1) {
      boolean var2 = !this.options.useIsValidOnNonFolia();
      if (var2) {
         return true;
      } else if (var1.isValid()) {
         return !(var1 instanceof Player) || ((Player)var1).isOnline();
      } else {
         return var1 instanceof Projectile && !var1.isDead();
      }
   }
}
