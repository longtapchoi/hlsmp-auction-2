package fr.maxlego08.zauctionhouse.migration.v3;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.item.ItemType;
import fr.maxlego08.zauctionhouse.api.log.LogType;
import fr.maxlego08.zauctionhouse.api.transaction.TransactionStatus;
import fr.maxlego08.sarah.SchemaBuilder;
import fr.maxlego08.sarah.database.Schema;
import fr.maxlego08.sarah.logger.JULogger;
import fr.maxlego08.sarah.logger.Logger;
import fr.maxlego08.zauctionhouse.migration.v3.items.V3AuctionItem;
import fr.maxlego08.zauctionhouse.migration.v3.items.V3Transaction;
import fr.maxlego08.zauctionhouse.migration.v3.reader.V3DataReader;
import fr.maxlego08.zauctionhouse.migration.v3.reader.V3JsonDataReader;
import fr.maxlego08.zauctionhouse.migration.v3.reader.V3SqlDataReader;
import fr.maxlego08.zauctionhouse.storage.repository.repositories.PlayerRepository;
import java.io.File;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class V3MigrationService {
   private final AuctionPlugin plugin;
   private final Logger logger;
   private Consumer<String> progressCallback;

   public V3MigrationService(AuctionPlugin var1) {
      this.plugin = var1;
      this.logger = JULogger.from(var1.getLogger());
   }

   public V3MigrationService onProgress(Consumer<String> var1) {
      this.progressCallback = var1;
      return this;
   }

   private void progress(String var1) {
      this.plugin.getLogger().info("[Migration] " + var1);
      if (this.progressCallback != null) {
         this.progressCallback.accept(var1);
      }

   }

   public CompletableFuture<V3MigrationResult> migrateFromSql(String var1, int var2, String var3, String var4, String var5, String var6) {
      V3SqlDataReader var7 = new V3SqlDataReader(this.plugin, var1, var2, var3, var4, var5, var6);
      return this.migrate(var7);
   }

   public CompletableFuture<V3MigrationResult> migrateFromSqlite(String var1, String var2) {
      V3SqlDataReader var3 = new V3SqlDataReader(this.plugin, var1, var2);
      return this.migrate(var3);
   }

   public CompletableFuture<V3MigrationResult> migrateFromJson(File var1) {
      V3JsonDataReader var2 = new V3JsonDataReader(this.plugin, var1);
      return this.migrate(var2);
   }

   public CompletableFuture<V3MigrationResult> migrate(V3DataReader var1) {
      return CompletableFuture.supplyAsync(() -> {
         long var2 = System.currentTimeMillis();
         AtomicInteger var4 = new AtomicInteger(0);

         V3MigrationResult var22;
         try {
            this.progress("Testing connection to V3 data source...");
            Boolean var5 = (Boolean)var1.testConnection().join();
            if (var5) {
               this.progress("Connection successful!");
               int var23 = (Integer)var1.getItemCount().join();
               int var7 = (Integer)var1.getTransactionCount().join();
               this.progress("Found " + var23 + " items and " + var7 + " transactions to migrate");
               if (var23 == 0 && var7 == 0) {
                  V3MigrationResult var24 = V3MigrationResult.failure("No data found to migrate");
                  return var24;
               }

               this.progress("Reading V3 items...");
               List var8 = (List)var1.readItems().join();
               this.progress("Read " + var8.size() + " items");
               this.progress("Reading V3 transactions...");
               List var9 = (List)var1.readTransactions().join();
               this.progress("Read " + var9.size() + " transactions");
               this.progress("Collecting player data...");
               Map var10 = this.collectPlayers(var8, var9);
               this.progress("Found " + var10.size() + " unique players");
               this.progress("Migrating players...");
               int var11 = this.migratePlayers(var10, var4);
               this.progress("Migrated " + var11 + " players");
               this.progress("Migrating items...");
               int var12 = this.migrateItems(var8, var4);
               this.progress("Migrated " + var12 + " items");
               this.progress("Migrating transactions to logs...");
               int var13 = this.migrateTransactions(var9, var4);
               this.progress("Migrated " + var13 + " transactions");
               long var14 = System.currentTimeMillis() - var2;
               this.progress("Migration completed in " + var14 + "ms");
               V3MigrationResult var16 = V3MigrationResult.success(var11, var12, var13, var4.get(), var14);
               return var16;
            }

            var22 = V3MigrationResult.failure("Failed to connect to V3 data source");
         } catch (Exception var20) {
            this.plugin.getLogger().severe("Migration failed: " + var20.getMessage());
            var22 = V3MigrationResult.failure("Migration failed: " + var20.getMessage());
            return var22;
         } finally {
            try { var1.close(); } catch (java.sql.SQLException e) { this.plugin.getLogger().warning("Error closing reader: " + e.getMessage()); }
         }

         return var22;
      }, this.plugin.getExecutorService());
   }

   private Map<UUID, String> collectPlayers(List<V3AuctionItem> var1, List<V3Transaction> var2) {
      HashMap var3 = new HashMap();

      for(V3AuctionItem var5 : var1) {
         if (var5.getSeller() != null && var5.getSellerName() != null) {
            var3.putIfAbsent(var5.getSeller(), var5.getSellerName());
         }

         if (var5.getBuyer() != null) {
            var3.putIfAbsent(var5.getBuyer(), "Unknown");
         }
      }

      for(V3Transaction var7 : var2) {
         if (var7.getSeller() != null) {
            var3.putIfAbsent(var7.getSeller(), "Unknown");
         }

         if (var7.getBuyer() != null) {
            var3.putIfAbsent(var7.getBuyer(), "Unknown");
         }
      }

      return var3;
   }

   private int migratePlayers(Map<UUID, String> var1, AtomicInteger var2) {
      int var3 = 0;
      PlayerRepository var4 = (PlayerRepository)this.plugin.getStorageManager().with(PlayerRepository.class);

      for(Map.Entry var6 : var1.entrySet()) {
         try {
            var4.upsertPlayer((UUID)var6.getKey(), (String)var6.getValue());
            ++var3;
         } catch (Exception var8) {
            java.util.logging.Logger var10000 = this.plugin.getLogger();
            String var10001 = String.valueOf(var6.getKey());
            var10000.warning("Failed to migrate player " + var10001 + ": " + var8.getMessage());
            var2.incrementAndGet();
         }
      }

      return var3;
   }

   private int migrateItems(List<V3AuctionItem> var1, AtomicInteger var2) {
      int var3 = 0;

      for(V3AuctionItem var5 : var1) {
         try {
            int var6 = this.createItem(var5);
            if (var6 == -1) {
               var2.incrementAndGet();
            } else {
               this.createAuctionItems(var6, var5);
               ++var3;
               if (var3 % 100 == 0) {
                  this.progress("Migrated " + var3 + "/" + var1.size() + " items...");
               }
            }
         } catch (Exception var7) {
            java.util.logging.Logger var10000 = this.plugin.getLogger();
            String var10001 = String.valueOf(var5.getId());
            var10000.warning("Failed to migrate item " + var10001 + ": " + var7.getMessage());
            var2.incrementAndGet();
         }
      }

      return var3;
   }

   private int createItem(V3AuctionItem var1) {
      try {
         Schema var2 = SchemaBuilder.insert("%prefix%items", (var2x) -> {
            var2x.string("item_type", ItemType.AUCTION.name());
            var2x.uuid("seller_unique_id", var1.getSeller());
            if (var1.getBuyer() != null) {
               var2x.uuid("buyer_unique_id", var1.getBuyer());
            }

            var2x.decimal("price", BigDecimal.valueOf(var1.getPrice()));
            var2x.string("economy_name", var1.getEconomy() != null ? var1.getEconomy() : "vault");
            var2x.string("storage_type", var1.getStorageType().toV4StorageType().name());
            var2x.string("server_name", var1.getServerName() != null ? var1.getServerName() : this.plugin.getConfiguration().getServerName());
            var2x.object("expired_at", new Date(var1.getExpireAt()));
         });
         return var2.execute(((PlayerRepository)this.plugin.getStorageManager().with(PlayerRepository.class)).getConnection(), this.logger);
      } catch (SQLException var3) {
         this.plugin.getLogger().warning("Failed to create item: " + var3.getMessage());
         return -1;
      }
   }

   private void createAuctionItems(int var1, V3AuctionItem var2) {
      String var3 = var2.getItemstack();
      if (var2.isInventoryType() && var3.contains(";")) {
         String[] var4 = var3.split(";");

         for(String var8 : var4) {
            if (!var8.trim().isEmpty()) {
               this.insertAuctionItem(var1, var8.trim());
            }
         }
      } else {
         this.insertAuctionItem(var1, var3);
      }

   }

   private void insertAuctionItem(int var1, String var2) {
      try {
         Schema var3 = SchemaBuilder.insert("%prefix%auction_items", (var2x) -> {
            var2x.object("item_id", var1);
            var2x.string("itemstack", var2);
         });
         var3.execute(((PlayerRepository)this.plugin.getStorageManager().with(PlayerRepository.class)).getConnection(), this.logger);
      } catch (SQLException var4) {
         this.plugin.getLogger().warning("Failed to create auction item for item_id " + var1 + ": " + var4.getMessage());
      }

   }

   private int migrateTransactions(List<V3Transaction> var1, AtomicInteger var2) {
      int var3 = 0;

      for(V3Transaction var5 : var1) {
         try {
            this.createLogEntry(var5);
            if (var5.isNeedMoney()) {
               this.createPendingTransaction(var5);
            }

            ++var3;
            if (var3 % 100 == 0) {
               this.progress("Migrated " + var3 + "/" + var1.size() + " transactions...");
            }
         } catch (Exception var7) {
            java.util.logging.Logger var10000 = this.plugin.getLogger();
            int var10001 = var5.getId();
            var10000.warning("Failed to migrate transaction " + var10001 + ": " + var7.getMessage());
            var2.incrementAndGet();
         }
      }

      return var3;
   }

   private void createLogEntry(V3Transaction var1) {
      try {
         Schema var2 = SchemaBuilder.insert("%prefix%logs", (var1x) -> {
            var1x.string("log_type", LogType.PURCHASE.name());
            var1x.object("item_id", 0);
            var1x.uuid("player_unique_id", var1.getBuyer());
            var1x.uuid("target_unique_id", var1.getSeller());
            var1x.string("itemstack", var1.getItemstack());
            var1x.decimal("price", BigDecimal.valueOf(var1.getPrice()));
            var1x.string("economy_name", var1.getEconomy() != null ? var1.getEconomy() : "vault");
            var1x.string("additional_data", "migrated_from_v3");
            if (var1.isRead()) {
               var1x.object("readed_at", new Date(var1.getTransactionDate()));
            }

            var1x.object("created_at", new Date(var1.getTransactionDate()));
         });
         var2.execute(((PlayerRepository)this.plugin.getStorageManager().with(PlayerRepository.class)).getConnection(), this.logger);
      } catch (SQLException var3) {
         this.plugin.getLogger().warning("Failed to create log entry: " + var3.getMessage());
      }

   }

   private void createPendingTransaction(V3Transaction var1) {
      try {
         Schema var2 = SchemaBuilder.insert("%prefix%transactions", (var1x) -> {
            var1x.object("item_id", 0);
            var1x.uuid("player_unique_id", var1.getSeller());
            var1x.string("economy_name", var1.getEconomy() != null ? var1.getEconomy() : "vault");
            var1x.decimal("before", BigDecimal.ZERO);
            var1x.decimal("after", BigDecimal.ZERO);
            var1x.decimal("value", BigDecimal.valueOf(var1.getPrice()));
            var1x.string("status", TransactionStatus.PENDING.name());
            var1x.object("created_at", new Date(var1.getTransactionDate()));
         });
         var2.execute(((PlayerRepository)this.plugin.getStorageManager().with(PlayerRepository.class)).getConnection(), this.logger);
      } catch (SQLException var3) {
         this.plugin.getLogger().warning("Failed to create pending transaction: " + var3.getMessage());
      }

   }
}
