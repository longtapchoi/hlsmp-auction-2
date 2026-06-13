package fr.maxlego08.zauctionhouse.api.services;

import fr.maxlego08.zauctionhouse.api.item.Item;
import fr.maxlego08.zauctionhouse.api.services.result.PurchaseResult;
import java.util.concurrent.CompletableFuture;
import org.bukkit.entity.Player;

public interface AuctionPurchaseService {
   CompletableFuture<PurchaseResult> purchaseItem(Player var1, Item var2);
}
