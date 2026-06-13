package fr.maxlego08.zauctionhouse.services;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.configuration.records.AutoClaimConfiguration;
import fr.maxlego08.zauctionhouse.api.economy.AuctionEconomy;
import fr.maxlego08.zauctionhouse.api.economy.EconomyManager;
import fr.maxlego08.zauctionhouse.api.messages.Message;
import fr.maxlego08.zauctionhouse.api.services.AuctionClaimService;
import fr.maxlego08.zauctionhouse.api.services.result.ClaimResult;
import fr.maxlego08.zauctionhouse.api.storage.dto.TransactionDTO;
import fr.maxlego08.zauctionhouse.api.transaction.TransactionStatus;
import fr.maxlego08.zauctionhouse.storage.repository.repositories.TransactionRepository;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.bukkit.entity.Player;

public class ClaimService extends AuctionService implements AuctionClaimService {
   private final AuctionPlugin plugin;

   public ClaimService(AuctionPlugin var1) {
      this.plugin = var1;
   }

   public CompletableFuture<ClaimResult> claimMoney(Player var1) {
      return this.getPendingTransactions(var1.getUniqueId()).thenApply((var2) -> {
         if (var2.isEmpty()) {
            this.message(this.plugin, var1, Message.CLAIM_NO_PENDING, new Object[0]);
            return ClaimResult.nothingToClaim("No pending transactions");
         } else {
            Map<String, List<TransactionDTO>> var3 = var2.stream().collect(Collectors.groupingBy(TransactionDTO::economy_name));
            if (var3.isEmpty()) {
               this.message(this.plugin, var1, Message.CLAIM_NO_PENDING, new Object[0]);
               return ClaimResult.nothingToClaim("No pending transactions");
            } else {
               EconomyManager var4 = this.plugin.getEconomyManager();
               List<Integer> var5 = var2.stream().map(TransactionDTO::id).toList();
               String var6 = this.plugin.getConfiguration().getAutoClaimConfiguration().depositReason();
               BigDecimal var7 = BigDecimal.ZERO;
               AuctionEconomy var8 = null;

               for(Map.Entry<String, List<TransactionDTO>> var10 : var3.entrySet()) {
                  String var11 = var10.getKey();
                  List<TransactionDTO> var12 = var10.getValue();
                  Optional<AuctionEconomy> var13 = var4.getEconomy(var11);
                  if (var13.isEmpty()) {
                     this.plugin.getLogger().warning("Economy not found: " + var11);
                  } else {
                     AuctionEconomy var14 = var13.get();
                     var8 = var14;
                     BigDecimal var15 = var12.stream().map(TransactionDTO::value).filter((var0) -> var0.compareTo(BigDecimal.ZERO) > 0).reduce(BigDecimal.ZERO, BigDecimal::add);
                     if (var15.compareTo(BigDecimal.ZERO) > 0) {
                        if (var1.isOnline()) {
                           this.plugin.getScheduler().runAsync((var4x) -> var14.deposit(var1.getUniqueId(), var15, var6));
                           this.message(this.plugin, var1, Message.CLAIM_ECONOMY_SUCCESS, new Object[]{"%amount%", var4.format(var14, var15), "%economy%", var14.getDisplayName()});
                        }

                        var7 = var7.add(var15);
                     }
                  }
               }

               TransactionRepository var16 = (TransactionRepository)this.plugin.getStorageManager().with(TransactionRepository.class);
               var16.updateStatus(var5, TransactionStatus.RETRIEVED);
               if (var7.compareTo(BigDecimal.ZERO) > 0) {
                  this.message(this.plugin, var1, Message.CLAIM_SUCCESS, new Object[]{"%amount%", var7.toString()});
                  return ClaimResult.success("Money claimed successfully", var7.doubleValue(), var8);
               } else {
                  return ClaimResult.nothingToClaim("No positive amount to claim");
               }
            }
         }
      });
   }

   public CompletableFuture<Map<String, BigDecimal>> getPendingMoneyByEconomy(UUID var1) {
      return this.getPendingTransactions(var1).thenApply((var0) -> {
         HashMap<String, BigDecimal> var1m = new HashMap<>();

         for(TransactionDTO var3 : var0) {
            if (var3.value().compareTo(BigDecimal.ZERO) > 0) {
               var1m.merge(var3.economy_name(), var3.value(), BigDecimal::add);
            }
         }

         return var1m;
      });
   }

   public CompletableFuture<BigDecimal> getTotalPendingMoney(UUID var1) {
      return this.getPendingMoneyByEconomy(var1).thenApply((var0) -> var0.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add));
   }

   public CompletableFuture<List<TransactionDTO>> getPendingTransactions(UUID var1) {
      return CompletableFuture.supplyAsync(() -> {
         TransactionRepository var2 = (TransactionRepository)this.plugin.getStorageManager().with(TransactionRepository.class);
         return var2.selectByPlayerAndStatus(var1, TransactionStatus.PENDING);
      }, this.plugin.getExecutorService());
   }

   public void handlePlayerJoin(Player var1) {
      AutoClaimConfiguration var2 = this.plugin.getConfiguration().getAutoClaimConfiguration();
      this.getPendingMoneyByEconomy(var1.getUniqueId()).thenAccept((var3) -> {
         if (var1.isOnline()) {
            if (!var3.isEmpty()) {
               BigDecimal var4 = (BigDecimal)var3.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
               if (var4.compareTo(BigDecimal.ZERO) > 0) {
                  if (var2.enabled()) {
                     long var5 = var2.delayTicks();
                     if (var5 <= 0L) {
                        this.claimMoney(var1);
                     } else {
                        this.plugin.getScheduler().runLater((var2x) -> {
                           if (var1.isOnline()) {
                              this.claimMoney(var1);
                           }

                        }, var5);
                     }
                  } else if (var2.notifyPending()) {
                     long var8 = var2.notifyDelayTicks();
                     Runnable var7 = () -> {
                        if (var1.isOnline()) {
                           String var3x = this.formatPendingMoney(var3);
                           this.message(this.plugin, var1, Message.CLAIM_PENDING_NOTIFY, new Object[]{"%amount%", var3x, "%count%", String.valueOf(var3.size())});
                        }

                     };
                     if (var8 <= 0L) {
                        var7.run();
                     } else {
                        this.plugin.getScheduler().runLater((var1x) -> var7.run(), var8);
                     }
                  }

               }
            }
         }
      });
   }

   public CompletableFuture<Void> clearPendingTransactions(UUID var1, boolean var2) {
      return this.getPendingTransactions(var1).thenAccept((var3) -> {
         if (!var3.isEmpty()) {
            if (var2) {
               EconomyManager var4 = this.plugin.getEconomyManager();
               String var5 = this.plugin.getConfiguration().getAutoClaimConfiguration().depositReason();
               Map<String, List<fr.maxlego08.zauctionhouse.api.storage.dto.TransactionDTO>> var6 = var3.stream().collect(Collectors.groupingBy(fr.maxlego08.zauctionhouse.api.storage.dto.TransactionDTO::economy_name));

               for(Map.Entry<String, List<fr.maxlego08.zauctionhouse.api.storage.dto.TransactionDTO>> var8 : var6.entrySet()) {
                  java.util.Optional<fr.maxlego08.zauctionhouse.api.economy.AuctionEconomy> var9 = var4.getEconomy(var8.getKey());
                  if (var9.isEmpty()) {
                     this.plugin.getLogger().warning("Economy not found: " + var8.getKey());
                  } else {
                     AuctionEconomy var10 = var9.get();
                     BigDecimal var11 = var8.getValue().stream().map(fr.maxlego08.zauctionhouse.api.storage.dto.TransactionDTO::value).filter((var0) -> var0.compareTo(BigDecimal.ZERO) > 0).reduce(BigDecimal.ZERO, BigDecimal::add);
                     if (var11.compareTo(BigDecimal.ZERO) > 0) {
                        var10.deposit(var1, var11, var5);
                     }
                  }
               }
            }

            List var12 = var3.stream().map(TransactionDTO::id).toList();
            TransactionRepository var13 = (TransactionRepository)this.plugin.getStorageManager().with(TransactionRepository.class);
            var13.updateStatus(var12, TransactionStatus.RETRIEVED);
         }
      });
   }

   private String formatPendingMoney(Map<String, BigDecimal> var1) {
      EconomyManager var2 = this.plugin.getEconomyManager();
      StringBuilder var3 = new StringBuilder();
      boolean var4 = true;

      for(Map.Entry var6 : var1.entrySet()) {
         if (!var4) {
            var3.append(", ");
         }

         var4 = false;
         Optional var7 = var2.getEconomy((String)var6.getKey());
         if (var7.isPresent()) {
            var3.append(var2.format((AuctionEconomy)var7.get(), (Number)var6.getValue()));
         } else {
            var3.append(var6.getValue()).append(" ").append((String)var6.getKey());
         }
      }

      return var3.toString();
   }
}
