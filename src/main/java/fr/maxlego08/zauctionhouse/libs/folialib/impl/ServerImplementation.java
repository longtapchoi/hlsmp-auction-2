package fr.maxlego08.zauctionhouse.libs.folialib.impl;

import fr.maxlego08.zauctionhouse.libs.folialib.enums.EntityTaskResult;
import fr.maxlego08.zauctionhouse.libs.folialib.wrapper.task.WrappedTask;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** @deprecated */
@Deprecated
public interface ServerImplementation {
   boolean isOwnedByCurrentRegion(@NotNull Location var1);

   boolean isOwnedByCurrentRegion(@NotNull Location var1, int var2);

   boolean isOwnedByCurrentRegion(@NotNull Block var1);

   boolean isOwnedByCurrentRegion(@NotNull World var1, int var2, int var3);

   boolean isOwnedByCurrentRegion(@NotNull World var1, int var2, int var3, int var4);

   boolean isOwnedByCurrentRegion(@NotNull Entity var1);

   boolean isGlobalTickThread();

   @NotNull CompletableFuture<Void> runNextTick(@NotNull Consumer<WrappedTask> var1);

   @NotNull CompletableFuture<Void> runAsync(@NotNull Consumer<WrappedTask> var1);

   WrappedTask runLater(@NotNull Runnable var1, long var2);

   @NotNull CompletableFuture<Void> runLater(@NotNull Consumer<WrappedTask> var1, long var2);

   WrappedTask runLater(@NotNull Runnable var1, long var2, TimeUnit var4);

   @NotNull CompletableFuture<Void> runLater(@NotNull Consumer<WrappedTask> var1, long var2, TimeUnit var4);

   WrappedTask runLaterAsync(@NotNull Runnable var1, long var2);

   @NotNull CompletableFuture<Void> runLaterAsync(@NotNull Consumer<WrappedTask> var1, long var2);

   WrappedTask runLaterAsync(@NotNull Runnable var1, long var2, TimeUnit var4);

   @NotNull CompletableFuture<Void> runLaterAsync(@NotNull Consumer<WrappedTask> var1, long var2, TimeUnit var4);

   WrappedTask runTimer(@NotNull Runnable var1, long var2, long var4);

   void runTimer(@NotNull Consumer<WrappedTask> var1, long var2, long var4);

   WrappedTask runTimer(@NotNull Runnable var1, long var2, long var4, TimeUnit var6);

   void runTimer(@NotNull Consumer<WrappedTask> var1, long var2, long var4, TimeUnit var6);

   WrappedTask runTimerAsync(@NotNull Runnable var1, long var2, long var4);

   void runTimerAsync(@NotNull Consumer<WrappedTask> var1, long var2, long var4);

   WrappedTask runTimerAsync(@NotNull Runnable var1, long var2, long var4, TimeUnit var6);

   void runTimerAsync(@NotNull Consumer<WrappedTask> var1, long var2, long var4, TimeUnit var6);

   @NotNull CompletableFuture<Void> runAtLocation(Location var1, @NotNull Consumer<WrappedTask> var2);

   WrappedTask runAtLocationLater(Location var1, @NotNull Runnable var2, long var3);

   @NotNull CompletableFuture<Void> runAtLocationLater(Location var1, @NotNull Consumer<WrappedTask> var2, long var3);

   WrappedTask runAtLocationLater(Location var1, @NotNull Runnable var2, long var3, TimeUnit var5);

   @NotNull CompletableFuture<Void> runAtLocationLater(Location var1, @NotNull Consumer<WrappedTask> var2, long var3, TimeUnit var5);

   WrappedTask runAtLocationTimer(Location var1, @NotNull Runnable var2, long var3, long var5);

   void runAtLocationTimer(Location var1, @NotNull Consumer<WrappedTask> var2, long var3, long var5);

   WrappedTask runAtLocationTimer(Location var1, @NotNull Runnable var2, long var3, long var5, TimeUnit var7);

   void runAtLocationTimer(Location var1, @NotNull Consumer<WrappedTask> var2, long var3, long var5, TimeUnit var7);

   @NotNull CompletableFuture<EntityTaskResult> runAtEntity(Entity var1, @NotNull Consumer<WrappedTask> var2);

   @NotNull CompletableFuture<EntityTaskResult> runAtEntityWithFallback(Entity var1, @NotNull Consumer<WrappedTask> var2, @Nullable Runnable var3);

   WrappedTask runAtEntityLater(Entity var1, @NotNull Runnable var2, long var3);

   WrappedTask runAtEntityLater(Entity var1, @NotNull Runnable var2, @Nullable Runnable var3, long var4);

   @NotNull CompletableFuture<Void> runAtEntityLater(Entity var1, @NotNull Consumer<WrappedTask> var2, long var3);

   @NotNull CompletableFuture<Void> runAtEntityLater(Entity var1, @NotNull Consumer<WrappedTask> var2, Runnable var3, long var4);

   WrappedTask runAtEntityLater(Entity var1, @NotNull Runnable var2, long var3, TimeUnit var5);

   @NotNull CompletableFuture<Void> runAtEntityLater(Entity var1, @NotNull Consumer<WrappedTask> var2, long var3, TimeUnit var5);

   WrappedTask runAtEntityTimer(Entity var1, @NotNull Runnable var2, long var3, long var5);

   WrappedTask runAtEntityTimer(Entity var1, @NotNull Runnable var2, Runnable var3, long var4, long var6);

   void runAtEntityTimer(Entity var1, @NotNull Consumer<WrappedTask> var2, long var3, long var5);

   void runAtEntityTimer(Entity var1, @NotNull Consumer<WrappedTask> var2, Runnable var3, long var4, long var6);

   WrappedTask runAtEntityTimer(Entity var1, @NotNull Runnable var2, long var3, long var5, TimeUnit var7);

   void runAtEntityTimer(Entity var1, @NotNull Consumer<WrappedTask> var2, long var3, long var5, TimeUnit var7);

   void cancelTask(WrappedTask var1);

   void cancelAllTasks();

   List<WrappedTask> getAllTasks();

   List<WrappedTask> getAllServerTasks();

   Player getPlayer(String var1);

   Player getPlayerExact(String var1);

   Player getPlayer(UUID var1);

   CompletableFuture<Boolean> teleportAsync(Entity var1, Location var2);

   CompletableFuture<Boolean> teleportAsync(Entity var1, Location var2, PlayerTeleportEvent.TeleportCause var3);

   WrappedTask wrapTask(@NotNull Object var1);
}
