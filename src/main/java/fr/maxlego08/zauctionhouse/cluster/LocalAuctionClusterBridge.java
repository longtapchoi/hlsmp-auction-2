package fr.maxlego08.zauctionhouse.cluster;

import fr.maxlego08.zauctionhouse.api.cluster.AuctionClusterBridge;
import fr.maxlego08.zauctionhouse.api.cluster.LockToken;
import fr.maxlego08.zauctionhouse.api.item.Item;
import fr.maxlego08.zauctionhouse.api.item.ItemStatus;
import fr.maxlego08.zauctionhouse.api.item.StorageType;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.entity.Player;

public class LocalAuctionClusterBridge implements AuctionClusterBridge {
   private final ConcurrentHashMap<Integer, UUID> itemLocks = new ConcurrentHashMap();

   public CompletableFuture<Boolean> checkAvailability(Item var1) {
      return CompletableFuture.completedFuture(!this.itemLocks.containsKey(var1.getId()));
   }

   public CompletableFuture<LockToken> lockItem(Item var1, UUID var2, StorageType var3) {
      UUID var4 = (UUID)this.itemLocks.putIfAbsent(var1.getId(), var2);
      return var4 != null ? CompletableFuture.failedFuture(new IllegalStateException("Item already locked by another player")) : CompletableFuture.completedFuture(LockToken.of(var1));
   }

   public CompletableFuture<Void> unlockItem(Item var1, LockToken var2, StorageType var3) {
      this.itemLocks.remove(var1.getId());
      return CompletableFuture.<Void>completedFuture(null);
   }

   public CompletableFuture<Void> notifyItemBought(Player var1, Item var2) {
      return CompletableFuture.<Void>completedFuture(null);
   }

   public CompletableFuture<Void> notifyItemListed(Item var1) {
      return CompletableFuture.<Void>completedFuture(null);
   }

   public CompletableFuture<Void> notifyItemStatusChange(Item var1, ItemStatus var2, ItemStatus var3) {
      return CompletableFuture.<Void>completedFuture(null);
   }

   public CompletableFuture<Void> removeItem(Item var1, StorageType var2) {
      return CompletableFuture.<Void>completedFuture(null);
   }
}
