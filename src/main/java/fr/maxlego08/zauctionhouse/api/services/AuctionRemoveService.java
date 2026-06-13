package fr.maxlego08.zauctionhouse.api.services;

import fr.maxlego08.zauctionhouse.api.item.Item;
import fr.maxlego08.zauctionhouse.api.services.result.RemoveResult;
import java.util.concurrent.CompletableFuture;
import org.bukkit.entity.Player;

public interface AuctionRemoveService {
   CompletableFuture<RemoveResult> removeListedItem(Player var1, Item var2);

   CompletableFuture<RemoveResult> removeSellingItem(Player var1, Item var2);

   CompletableFuture<RemoveResult> removeExpiredItem(Player var1, Item var2);

   CompletableFuture<RemoveResult> removePurchasedItem(Player var1, Item var2);
}
