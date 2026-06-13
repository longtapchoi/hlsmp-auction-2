package fr.maxlego08.zauctionhouse.api.cluster;

import fr.maxlego08.zauctionhouse.api.item.Item;
import fr.maxlego08.zauctionhouse.api.item.ItemStatus;
import fr.maxlego08.zauctionhouse.api.item.StorageType;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.bukkit.entity.Player;

public interface AuctionClusterBridge {
   CompletableFuture<Boolean> checkAvailability(Item var1);

   CompletableFuture<LockToken> lockItem(Item var1, UUID var2, StorageType var3);

   CompletableFuture<Void> unlockItem(Item var1, LockToken var2, StorageType var3);

   CompletableFuture<Void> notifyItemBought(Player var1, Item var2);

   CompletableFuture<Void> notifyItemListed(Item var1);

   CompletableFuture<Void> notifyItemStatusChange(Item var1, ItemStatus var2, ItemStatus var3);

   CompletableFuture<Void> removeItem(Item var1, StorageType var2);

   default CompletableFuture<Void> removeItem(Item item, StorageType sourceStorageType, StorageType destinationStorageType) {
      return this.removeItem(item, sourceStorageType);
   }

   default boolean isDistributed() {
      return false;
   }
}
