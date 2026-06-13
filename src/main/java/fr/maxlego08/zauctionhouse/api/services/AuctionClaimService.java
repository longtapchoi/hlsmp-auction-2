package fr.maxlego08.zauctionhouse.api.services;

import fr.maxlego08.zauctionhouse.api.services.result.ClaimResult;
import fr.maxlego08.zauctionhouse.api.storage.dto.TransactionDTO;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.bukkit.entity.Player;

public interface AuctionClaimService {
   CompletableFuture<ClaimResult> claimMoney(Player var1);

   CompletableFuture<Map<String, BigDecimal>> getPendingMoneyByEconomy(UUID var1);

   CompletableFuture<BigDecimal> getTotalPendingMoney(UUID var1);

   CompletableFuture<List<TransactionDTO>> getPendingTransactions(UUID var1);

   void handlePlayerJoin(Player var1);

   CompletableFuture<Void> clearPendingTransactions(UUID var1, boolean var2);
}
