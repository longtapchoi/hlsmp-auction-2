package fr.maxlego08.zauctionhouse.services;

import fr.maxlego08.zauctionhouse.api.AuctionManager;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCacheKey;
import fr.maxlego08.zauctionhouse.api.cluster.AuctionClusterBridge;
import fr.maxlego08.zauctionhouse.api.cluster.LockToken;
import fr.maxlego08.zauctionhouse.api.configuration.records.ActionConfiguration;
import fr.maxlego08.zauctionhouse.api.configuration.records.PerformanceConfiguration;
import fr.maxlego08.zauctionhouse.api.event.events.remove.AuctionPreRemoveExpiredItemEvent;
import fr.maxlego08.zauctionhouse.api.event.events.remove.AuctionPreRemoveListedItemEvent;
import fr.maxlego08.zauctionhouse.api.event.events.remove.AuctionPreRemovePurchasedItemEvent;
import fr.maxlego08.zauctionhouse.api.item.Item;
import fr.maxlego08.zauctionhouse.api.item.ItemStatus;
import fr.maxlego08.zauctionhouse.api.item.StorageType;
import fr.maxlego08.zauctionhouse.api.messages.Message;
import fr.maxlego08.zauctionhouse.api.services.AuctionRemoveService;
import fr.maxlego08.zauctionhouse.api.services.result.RemoveFailReason;
import fr.maxlego08.zauctionhouse.api.services.result.RemoveResult;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;
import java.util.logging.Logger;
import org.bukkit.entity.Player;

public class RemoveService extends AuctionService implements AuctionRemoveService {
   private final AuctionPlugin plugin;

   public RemoveService(AuctionPlugin var1) {
      this.plugin = var1;
   }

   public CompletableFuture<RemoveResult> removeListedItem(Player var1, Item var2) {
      AuctionPreRemoveListedItemEvent var3 = new AuctionPreRemoveListedItemEvent(var2, var1);
      if (!var3.callEvent()) {
         return CompletableFuture.completedFuture(RemoveResult.failure("Event cancelled", RemoveFailReason.EVENT_CANCELLED));
      } else {
         AuctionManager var4 = this.plugin.getAuctionManager();
         Logger var5 = this.plugin.getLogger();
         if (var2.isExpired()) {
            var5.info("Item expired (Remove Listed)");
            var4.clearPlayerCache(var1, PlayerCacheKey.ITEMS_SELLING, PlayerCacheKey.ITEMS_LISTED);
            var4.openMainAuction(var1);
            return CompletableFuture.completedFuture(RemoveResult.failure("Item expired", RemoveFailReason.ITEM_EXPIRED));
         } else if (var2.getStatus() != ItemStatus.AVAILABLE && var2.getStatus() != ItemStatus.IS_REMOVE_CONFIRM) {
            var5.info("Item not available (Remove Listed)");
            var4.clearPlayerCache(var1, PlayerCacheKey.ITEMS_SELLING, PlayerCacheKey.ITEMS_LISTED);
            var4.openMainAuction(var1);
            return CompletableFuture.completedFuture(RemoveResult.failure("Item not available", RemoveFailReason.INVALID_ITEM_STATUS));
         } else {
            ActionConfiguration.ListedConfiguration var6 = this.plugin.getConfiguration().getActions().listed();
            StorageType var7 = var6.giveItem() && var2.canReceiveItem(var1) ? StorageType.DELETED : StorageType.EXPIRED;
            return this.executeRemoval(ItemStatus.IS_BEING_REMOVED, var1, var2, () -> var4.updateInventory(var1), () -> var4.removeListedItem(var1, var2), StorageType.LISTED, var7);
         }
      }
   }

   public CompletableFuture<RemoveResult> removeSellingItem(Player var1, Item var2) {
      AuctionPreRemoveListedItemEvent var3 = new AuctionPreRemoveListedItemEvent(var2, var1);
      if (!var3.callEvent()) {
         return CompletableFuture.completedFuture(RemoveResult.failure("Event cancelled", RemoveFailReason.EVENT_CANCELLED));
      } else {
         AuctionManager var4 = this.plugin.getAuctionManager();
         Logger var5 = this.plugin.getLogger();
         ActionConfiguration.SellingConfiguration var6 = this.plugin.getConfiguration().getActions().selling();
         if (var6.freeSpace() && !var2.canReceiveItem(var1)) {
            this.message(this.plugin, var1, Message.NOT_ENOUGH_SPACE, new Object[0]);
            return CompletableFuture.completedFuture(RemoveResult.failure("Not enough space", RemoveFailReason.INSUFFICIENT_SPACE));
         } else if (var2.isExpired()) {
            var5.info("Item expired (Remove Selling)");
            var4.clearPlayerCache(var1, PlayerCacheKey.ITEMS_SELLING, PlayerCacheKey.ITEMS_LISTED);
            var4.updateInventory(var1);
            return CompletableFuture.completedFuture(RemoveResult.failure("Item expired", RemoveFailReason.ITEM_EXPIRED));
         } else if (var2.getStatus() != ItemStatus.AVAILABLE) {
            var5.info("Item not available (Remove Selling)");
            var4.clearPlayerCache(var1, PlayerCacheKey.ITEMS_SELLING, PlayerCacheKey.ITEMS_LISTED);
            var4.updateInventory(var1);
            return CompletableFuture.completedFuture(RemoveResult.failure("Item not available", RemoveFailReason.INVALID_ITEM_STATUS));
         } else {
            return this.executeRemoval(ItemStatus.IS_BEING_REMOVED, var1, var2, () -> var4.updateInventory(var1), () -> var4.removeSellingItem(var1, var2), StorageType.LISTED, StorageType.DELETED);
         }
      }
   }

   public CompletableFuture<RemoveResult> removeExpiredItem(Player var1, Item var2) {
      AuctionPreRemoveExpiredItemEvent var3 = new AuctionPreRemoveExpiredItemEvent(var2, var1);
      if (!var3.callEvent()) {
         return CompletableFuture.completedFuture(RemoveResult.failure("Event cancelled", RemoveFailReason.EVENT_CANCELLED));
      } else {
         AuctionManager var4 = this.plugin.getAuctionManager();
         Logger var5 = this.plugin.getLogger();
         ActionConfiguration.ExpiredConfiguration var6 = this.plugin.getConfiguration().getActions().expired();
         if (var6.freeSpace() && !var2.canReceiveItem(var1)) {
            this.message(this.plugin, var1, Message.NOT_ENOUGH_SPACE, new Object[0]);
            return CompletableFuture.completedFuture(RemoveResult.failure("Not enough space", RemoveFailReason.INSUFFICIENT_SPACE));
         } else if (var2.isExpired()) {
            var5.info("Item expired (Remove Expired)");
            var4.clearPlayerCache(var1, PlayerCacheKey.ITEMS_EXPIRED);
            var4.updateInventory(var1);
            return CompletableFuture.completedFuture(RemoveResult.failure("Item expired", RemoveFailReason.ITEM_EXPIRED));
         } else if (var2.getStatus() != ItemStatus.REMOVED) {
            var5.info("Item not available (Remove Expired), Current status: " + String.valueOf(var2.getStatus()));
            var4.clearPlayerCache(var1, PlayerCacheKey.ITEMS_EXPIRED);
            var4.updateInventory(var1);
            return CompletableFuture.completedFuture(RemoveResult.failure("Item not in removed status", RemoveFailReason.INVALID_ITEM_STATUS));
         } else {
            return this.executeRemoval(ItemStatus.DELETED, var1, var2, () -> var4.updateInventory(var1), () -> this.plugin.getAuctionManager().removeExpiredItem(var1, var2), StorageType.EXPIRED, StorageType.DELETED);
         }
      }
   }

   public CompletableFuture<RemoveResult> removePurchasedItem(Player var1, Item var2) {
      AuctionPreRemovePurchasedItemEvent var3 = new AuctionPreRemovePurchasedItemEvent(var2, var1);
      if (!var3.callEvent()) {
         return CompletableFuture.completedFuture(RemoveResult.failure("Event cancelled", RemoveFailReason.EVENT_CANCELLED));
      } else {
         AuctionManager var4 = this.plugin.getAuctionManager();
         Logger var5 = this.plugin.getLogger();
         ActionConfiguration.PurchasedConfiguration var6 = this.plugin.getConfiguration().getActions().purchased();
         if (var6.freeSpace() && !var2.canReceiveItem(var1)) {
            this.message(this.plugin, var1, Message.NOT_ENOUGH_SPACE, new Object[0]);
            return CompletableFuture.completedFuture(RemoveResult.failure("Not enough space", RemoveFailReason.INSUFFICIENT_SPACE));
         } else if (var2.isExpired()) {
            var5.info("Item expired (Remove Purchased)");
            var4.clearPlayerCache(var1, PlayerCacheKey.ITEMS_EXPIRED);
            var4.updateInventory(var1);
            return CompletableFuture.completedFuture(RemoveResult.failure("Item expired", RemoveFailReason.ITEM_EXPIRED));
         } else if (var2.getStatus() != ItemStatus.PURCHASED) {
            var5.info("Item not available (Remove Purchased)");
            var4.clearPlayerCache(var1, PlayerCacheKey.ITEMS_EXPIRED);
            var4.updateInventory(var1);
            return CompletableFuture.completedFuture(RemoveResult.failure("Item not in purchased status", RemoveFailReason.INVALID_ITEM_STATUS));
         } else {
            return this.executeRemoval(ItemStatus.DELETED, var1, var2, () -> var4.updateInventory(var1), () -> var4.removePurchasedItem(var1, var2), StorageType.PURCHASED, StorageType.DELETED);
         }
      }
   }

   private CompletableFuture<RemoveResult> executeRemoval(ItemStatus var1, Player var2, Item var3, Runnable var4, Supplier<CompletableFuture<Void>> var5, StorageType var6, StorageType var7) {
      RemovalContext var8 = new RemovalContext(var3, var1, var6, var7, var4, var5);
      PerformanceConfiguration var9 = this.plugin.getConfiguration().getPerformance();
      AuctionClusterBridge var10 = this.plugin.getAuctionClusterBridge();
      Logger var11 = this.plugin.getLogger();
      return this.checkAvailabilityStep(var8, var10, var9).thenCompose((var5x) -> this.acquireLockStep(var8, var5x, var2, var10, var9)).thenCompose((var4x) -> this.changeStatusAndNotifyStep(var8, var4x, var10, var9)).thenCompose((var4x) -> this.executeLocalRemovalStep(var8, var10, var9)).thenCompose((var4x) -> this.unlockAndCompleteStep(var8, var10, var9)).exceptionally((var4x) -> this.handleRemovalException(var8, var4x, var10, var11));
   }

   private CompletableFuture<Boolean> checkAvailabilityStep(RemovalContext var1, AuctionClusterBridge var2, PerformanceConfiguration var3) {
      return var2.checkAvailability(var1.item).orTimeout(var3.checkAvailabilityTimeoutMs(), TimeUnit.MILLISECONDS);
   }

   private CompletableFuture<LockToken> acquireLockStep(RemovalContext var1, boolean var2, Player var3, AuctionClusterBridge var4, PerformanceConfiguration var5) {
      if (!var2) {
         var1.onUnavailable.run();
         var1.result = RemoveResult.failure("Item not available", RemoveFailReason.ITEM_NOT_AVAILABLE);
         return this.failedFuture(new IllegalStateException("Item introuvable"));
      } else {
         return var4.lockItem(var1.item, var3.getUniqueId(), var1.storageType).orTimeout(var5.lockItemTimeoutMs(), TimeUnit.MILLISECONDS);
      }
   }

   private CompletableFuture<Void> changeStatusAndNotifyStep(RemovalContext var1, LockToken var2, AuctionClusterBridge var3, PerformanceConfiguration var4) {
      var1.token = var2;
      if (LockToken.noop().value().equals(var2.value())) {
         var1.onUnavailable.run();
         var1.result = RemoveResult.failure("Lock failed", RemoveFailReason.LOCK_FAILED);
         return this.failedFuture(new IllegalStateException("Item déjà en cours de traitement"));
      } else {
         var1.item.setStatus(var1.targetStatus);
         var1.statusChanged = true;
         return var3.notifyItemStatusChange(var1.item, var1.oldStatus, var1.targetStatus).orTimeout(var4.notifyStatusChangeTimeoutMs(), TimeUnit.MILLISECONDS);
      }
   }

   private CompletableFuture<Void> executeLocalRemovalStep(RemovalContext var1, AuctionClusterBridge var2, PerformanceConfiguration var3) {
      return ((CompletableFuture)var1.onLocalRemoval.get()).thenCompose((var3x) -> var2.removeItem(var1.item, var1.storageType, var1.destinationStorageType).orTimeout(var3.notifyItemActionTimeoutMs(), TimeUnit.MILLISECONDS));
   }

   private CompletableFuture<RemoveResult> unlockAndCompleteStep(RemovalContext var1, AuctionClusterBridge var2, PerformanceConfiguration var3) {
      return var2.unlockItem(var1.item, var1.token, var1.storageType).orTimeout(var3.unlockItemTimeoutMs(), TimeUnit.MILLISECONDS).thenApply((var1x) -> {
         var1.result = RemoveResult.success("Item removed successfully", true);
         return var1.result;
      });
   }

   private RemoveResult handleRemovalException(RemovalContext var1, Throwable var2, AuctionClusterBridge var3, Logger var4) {
      if (var2.getCause() instanceof TimeoutException) {
         var4.warning("Removal operation timed out for item " + var1.item.getId());
      } else {
         int var10001 = var1.item.getId();
         var4.severe("Error during removal for item " + var10001 + ": " + var2.getMessage());
      }

      this.releaseLockOnError(var1, var3, var4);
      this.restoreStatusOnError(var1, var3);
      return var1.result != null ? var1.result : RemoveResult.failure("Internal error", RemoveFailReason.INTERNAL_ERROR);
   }

   private void releaseLockOnError(RemovalContext var1, AuctionClusterBridge var2, Logger var3) {
      if (var1.token != null && !LockToken.noop().value().equals(var1.token.value())) {
         var2.unlockItem(var1.item, var1.token, var1.storageType).exceptionally((var1x) -> {
            var3.severe("Failed to unlock item after error: " + var1x.getMessage());
            return null;
         });
      }

   }

   private void restoreStatusOnError(RemovalContext var1, AuctionClusterBridge var2) {
      if (var1.statusChanged) {
         var1.item.setStatus(var1.oldStatus);
         var2.notifyItemStatusChange(var1.item, var1.targetStatus, var1.oldStatus);
      }

   }

   private static class RemovalContext {
      final Item item;
      final ItemStatus oldStatus;
      final ItemStatus targetStatus;
      final StorageType storageType;
      final StorageType destinationStorageType;
      final Runnable onUnavailable;
      final Supplier<CompletableFuture<Void>> onLocalRemoval;
      LockToken token;
      boolean statusChanged;
      RemoveResult result;

      RemovalContext(Item var1, ItemStatus var2, StorageType var3, StorageType var4, Runnable var5, Supplier<CompletableFuture<Void>> var6) {
         this.item = var1;
         this.oldStatus = var1.getStatus();
         this.targetStatus = var2;
         this.storageType = var3;
         this.destinationStorageType = var4;
         this.onUnavailable = var5;
         this.onLocalRemoval = var6;
         this.statusChanged = false;
      }
   }
}
