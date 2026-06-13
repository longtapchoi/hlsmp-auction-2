package fr.maxlego08.zauctionhouse.api.services;

import fr.maxlego08.zauctionhouse.api.history.ItemLog;
import fr.maxlego08.zauctionhouse.api.storage.dto.LogDTO;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.bukkit.entity.Player;

public interface AuctionHistoryService {
   CompletableFuture<List<ItemLog>> getSalesHistory(UUID var1);

   CompletableFuture<List<LogDTO>> getUnreadSales(UUID var1);

   CompletableFuture<Void> markSalesAsRead(List<Integer> var1);

   void handlePlayerJoin(Player var1);

   void openHistoryInventory(Player var1);

   void openHistoryInventory(Player var1, int var2);
}
