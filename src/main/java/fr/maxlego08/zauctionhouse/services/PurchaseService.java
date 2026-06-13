package fr.maxlego08.zauctionhouse.services;

import fr.maxlego08.menu.api.InventoryManager;
import fr.maxlego08.zauctionhouse.api.AuctionManager;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCacheKey;
import fr.maxlego08.zauctionhouse.api.cluster.AuctionClusterBridge;
import fr.maxlego08.zauctionhouse.api.cluster.LockToken;
import fr.maxlego08.zauctionhouse.api.configuration.records.ActionConfiguration;
import fr.maxlego08.zauctionhouse.api.configuration.records.PerformanceConfiguration;
import fr.maxlego08.zauctionhouse.api.economy.AuctionEconomy;
import fr.maxlego08.zauctionhouse.api.event.events.purchase.AuctionPrePurchaseItemEvent;
import fr.maxlego08.zauctionhouse.api.item.Item;
import fr.maxlego08.zauctionhouse.api.item.ItemStatus;
import fr.maxlego08.zauctionhouse.api.item.StorageType;
import fr.maxlego08.zauctionhouse.api.messages.Message;
import fr.maxlego08.zauctionhouse.api.services.AuctionPurchaseService;
import fr.maxlego08.zauctionhouse.api.services.result.PurchaseFailReason;
import fr.maxlego08.zauctionhouse.api.services.result.PurchaseResult;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;
import org.bukkit.entity.Player;

public class PurchaseService extends AuctionService implements AuctionPurchaseService {
   private final AuctionPlugin plugin;

   public PurchaseService(AuctionPlugin var1) {
      this.plugin = var1;
   }

   public CompletableFuture<PurchaseResult> purchaseItem(Player var1, Item var2) {
      AuctionPrePurchaseItemEvent var3 = new AuctionPrePurchaseItemEvent(var2, var1);
      if (!var3.callEvent()) {
         return CompletableFuture.completedFuture(PurchaseResult.failure("Event cancelled", PurchaseFailReason.EVENT_CANCELLED));
      } else {
         AuctionManager var4 = this.plugin.getAuctionManager();
         InventoryManager var5 = this.plugin.getInventoriesLoader().getInventoryManager();
         AuctionClusterBridge var6 = this.plugin.getAuctionClusterBridge();
         Logger var7 = this.plugin.getLogger();
         AuctionEconomy var8 = var2.getAuctionEconomy();
         ActionConfiguration.PurchasedConfiguration var9 = this.plugin.getConfiguration().getActions().purchased();
         if (var9.giveItem() && var9.freeSpace() && !var2.canReceiveItem(var1)) {
            this.message(this.plugin, var1, Message.NOT_ENOUGH_SPACE, new Object[0]);
            return CompletableFuture.completedFuture(PurchaseResult.failure("Not enough space", PurchaseFailReason.INSUFFICIENT_SPACE));
         } else if (var2.isExpired()) {
            var4.getCache(var1).remove(PlayerCacheKey.ITEMS_LISTED);
            var4.openMainAuction(var1);
            return CompletableFuture.completedFuture(PurchaseResult.failure("Item expired", PurchaseFailReason.ITEM_EXPIRED));
         } else if (var2.getStatus() != ItemStatus.IS_PURCHASE_CONFIRM) {
            var4.openMainAuction(var1);
            return CompletableFuture.completedFuture(PurchaseResult.failure("Item not in purchase state", PurchaseFailReason.ITEM_NOT_IN_PURCHASE_STATE));
         } else {
            AtomicReference var10 = new AtomicReference((Object)null);
            AtomicReference var11 = new AtomicReference((Object)null);
            AtomicReference var12 = new AtomicReference(var2.getStatus());
            PerformanceConfiguration var13 = this.plugin.getConfiguration().getPerformance();
            return var6.checkAvailability(var2).orTimeout(var13.checkAvailabilityTimeoutMs(), TimeUnit.MILLISECONDS).thenCompose((var7x) -> {
               if (!var7x) {
                  var5.updateInventory(var1);
                  var11.set(PurchaseResult.failure("Item not available", PurchaseFailReason.ITEM_NOT_AVAILABLE));
                  return this.failedFuture(new IllegalStateException("Item introuvable"));
               } else {
                  return var6.lockItem(var2, var1.getUniqueId(), StorageType.LISTED).orTimeout(var13.lockItemTimeoutMs(), TimeUnit.MILLISECONDS);
               }
            }).thenCompose((var10x) -> {
               var10.set(var10x);
               if (LockToken.noop().value().equals(var10x.value())) {
                  var5.updateInventory(var1);
                  var11.set(PurchaseResult.failure("Lock failed", PurchaseFailReason.LOCK_FAILED));
                  return this.failedFuture(new IllegalStateException("Item déjà en cours d'achat"));
               } else {
                  var2.setStatus(ItemStatus.IS_BEING_PURCHASED);
                  return var6.notifyItemStatusChange(var2, (ItemStatus)var12.get(), ItemStatus.IS_BEING_PURCHASED).orTimeout(var13.notifyStatusChangeTimeoutMs(), TimeUnit.MILLISECONDS).thenCompose((var3) -> var8.has(var1.getUniqueId(), var2.getPrice()));
               }
            }).thenCompose((var8x) -> {
               LockToken var9 = (LockToken)var10.get();
               if (var8x) {
                  return var4.purchaseItem(var1, var2).thenCompose((var4x) -> var6.notifyItemBought(var1, var2).orTimeout(var13.notifyItemActionTimeoutMs(), TimeUnit.MILLISECONDS)).thenCompose((var4x) -> var6.unlockItem(var2, var9, StorageType.LISTED).orTimeout(var13.unlockItemTimeoutMs(), TimeUnit.MILLISECONDS)).thenApply((var1x) -> {
                     var11.set(PurchaseResult.success("Purchase successful", true));
                     return (PurchaseResult)var11.get();
                  });
               } else {
                  this.message(this.plugin, var1, Message.NOT_ENOUGH_MONEY, new Object[0]);
                  var11.set(PurchaseResult.failure("Insufficient funds", PurchaseFailReason.INSUFFICIENT_FUNDS));
                  return var6.unlockItem(var2, var9, StorageType.LISTED).orTimeout(var13.unlockItemTimeoutMs(), TimeUnit.MILLISECONDS).thenApply((var1x) -> (PurchaseResult)var11.get());
               }
            }).exceptionally((var6x) -> {
               if (var6x.getCause() instanceof TimeoutException) {
                  var7.warning("Purchase operation timed out for item " + var2.getId());
               } else {
                  int var10001 = var2.getId();
                  var7.severe("Error during purchase for item " + var10001 + ": " + var6x.getMessage());
               }

               LockToken var7x = (LockToken)var10.get();
               if (var7x != null && !LockToken.noop().value().equals(var7x.value())) {
                  var6.unlockItem(var2, var7x, StorageType.LISTED).exceptionally((var1) -> {
                     var7.severe("Failed to unlock item after error: " + var1.getMessage());
                     return null;
                  });
               }

               if (var2.getStatus() == ItemStatus.IS_BEING_PURCHASED) {
                  ItemStatus var8 = (ItemStatus)var12.get();
                  var2.setStatus(var8);
                  var6.notifyItemStatusChange(var2, ItemStatus.IS_BEING_PURCHASED, var8).exceptionally((var2x) -> {
                     int var10001 = var2.getId();
                     var7.severe("Failed to restore item status for item " + var10001 + ": " + var2x.getMessage());
                     return null;
                  });
               }

               PurchaseResult var9 = (PurchaseResult)var11.get();
               return var9 != null ? var9 : PurchaseResult.failure("Internal error", PurchaseFailReason.INTERNAL_ERROR);
            });
         }
      }
   }
}
