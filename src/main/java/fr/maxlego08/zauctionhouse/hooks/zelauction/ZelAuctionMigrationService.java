package fr.maxlego08.zauctionhouse.hooks.zelauction;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.item.ItemType;
import fr.maxlego08.zauctionhouse.api.item.StorageType;
import fr.maxlego08.zauctionhouse.api.log.LogType;
import fr.maxlego08.zauctionhouse.api.utils.Base64ItemStack;
import fr.maxlego08.zauctionhouse.libs.sarah.DatabaseConnection;
import fr.maxlego08.zauctionhouse.libs.sarah.SchemaBuilder;
import fr.maxlego08.zauctionhouse.libs.sarah.database.Schema;
import fr.maxlego08.zauctionhouse.libs.sarah.logger.JULogger;
import fr.maxlego08.zauctionhouse.libs.sarah.logger.Logger;
import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings({"unchecked", "rawtypes"})
public class ZelAuctionMigrationService {
   private final AuctionPlugin plugin;
   private final Logger logger;
   private Consumer<String> progressCallback;

   public ZelAuctionMigrationService(AuctionPlugin var1) {
      this.plugin = var1;
      this.logger = JULogger.from(var1.getLogger());
   }

   public void onProgress(Consumer<String> var1) {
      this.progressCallback = var1;
   }

   public CompletableFuture<ZelMigrationResult> migrate() {
      return CompletableFuture.supplyAsync(() -> {
         long var1 = System.currentTimeMillis();
         AtomicInteger var3 = new AtomicInteger(0);
         Connection var4 = null;

         ZelMigrationResult var7;
         try {
            File var5 = new File("plugins/ZelAuction");
            File var34 = new File(var5, "database.yml");
            if (var34.exists()) {
               YamlConfiguration var35 = YamlConfiguration.loadConfiguration(var34);
               String var8 = var35.getString("Database-Type", "SQLite");
               this.progress("Connecting to ZelAuction " + var8 + " database...");
               var4 = this.createConnection(var8, var35, var5);
               if (var4 != null && !var4.isClosed()) {
                  this.progress("Connection successful!");
                  int var36 = this.countRows(var4, "products");
                  int var10 = this.countRows(var4, "mailbox");
                  int var11 = this.countRows(var4, "transactions");
                  this.progress("Found " + var36 + " products, " + var10 + " mailbox items, " + var11 + " transactions");
                  if (var36 == 0 && var10 == 0 && var11 == 0) {
                     ZelMigrationResult var37 = ZelMigrationResult.failure("No data found to migrate");
                     return var37;
                  }

                  DatabaseConnection var12 = this.plugin.getStorageManager().getDatabaseConnection();
                  HashMap var13 = new HashMap();
                  this.progress("Migrating products (listed items)...");
                  int var14 = this.migrateProducts(var4, var12, var13, var3);
                  this.progress("Migrated " + var14 + " products");
                  this.progress("Migrating mailbox (purchased items)...");
                  int var15 = this.migrateMailbox(var4, var12, var13, var3);
                  this.progress("Migrated " + var15 + " mailbox items");
                  this.progress("Migrating transactions...");
                  int var16 = this.migrateTransactions(var4, var12, var13, var3);
                  this.progress("Migrated " + var16 + " transactions");
                  long var17 = System.currentTimeMillis() - var1;
                  this.progress("Migration completed in " + var17 + "ms");
                  ZelMigrationResult var19 = ZelMigrationResult.success(var13.size(), var14 + var15, var16, var3.get(), var17);
                  return var19;
               }

               ZelMigrationResult var9 = ZelMigrationResult.failure("Failed to connect to ZelAuction database");
               return var9;
            }

            var7 = ZelMigrationResult.failure("ZelAuction database.yml not found at: " + var34.getAbsolutePath());
         } catch (Exception var32) {
            this.plugin.getLogger().severe("Migration failed: " + var32.getMessage());
            var32.printStackTrace();
            ZelMigrationResult var6 = ZelMigrationResult.failure("Migration failed: " + var32.getMessage());
            return var6;
         } finally {
            if (var4 != null) {
               try {
                  var4.close();
               } catch (SQLException var31) {
               }
            }

         }

         return var7;
      }, this.plugin.getExecutorService());
   }

   private Connection createConnection(String var1, YamlConfiguration var2, File var3) {
      if (var1.equalsIgnoreCase("SQLite")) {
         File var11 = this.findSqliteFile(var3);
         if (var11 == null) {
            throw new SQLException("No SQLite database file found in " + var3.getAbsolutePath());
         } else {
            this.progress("Using SQLite database: " + var11.getName());
            return DriverManager.getConnection("jdbc:sqlite:" + var11.getAbsolutePath());
         }
      } else {
         String var4 = var2.getString("MySQL.host", "localhost");
         int var5 = var2.getInt("MySQL.port", 3306);
         String var6 = var2.getString("MySQL.database", "zelauction");
         String var7 = var2.getString("MySQL.user", "root");
         String var8 = var2.getString("MySQL.password", "");
         boolean var9 = var2.getBoolean("MySQL.use-ssl", false);
         String var10 = "jdbc:mysql://" + var4 + ":" + var5 + "/" + var6 + "?useSSL=" + var9 + "&autoReconnect=true&useUnicode=true&characterEncoding=UTF-8";
         return DriverManager.getConnection(var10, var7, var8);
      }
   }

   private File findSqliteFile(File var1) {
      File[] var2 = var1.listFiles((var0, var1x) -> var1x.endsWith(".db"));
      return var2 != null && var2.length > 0 ? var2[0] : null;
   }

   private int countRows(Connection var1, String var2) {
      try {
         PreparedStatement var3 = var1.prepareStatement("SELECT COUNT(*) FROM " + var2);

         int var5;
         label80: {
            try {
               ResultSet var4 = var3.executeQuery();

               label74: {
                  try {
                     if (!var4.next()) {
                        break label74;
                     }

                     var5 = var4.getInt(1);
                  } catch (Throwable var9) {
                     if (var4 != null) {
                        try {
                           var4.close();
                        } catch (Throwable var8) {
                           var9.addSuppressed(var8);
                        }
                     }

                     throw var9;
                  }

                  if (var4 != null) {
                     var4.close();
                  }
                  break label80;
               }

               if (var4 != null) {
                  var4.close();
               }
            } catch (Throwable var10) {
               if (var3 != null) {
                  try {
                     var3.close();
                  } catch (Throwable var7) {
                     var10.addSuppressed(var7);
                  }
               }

               throw var10;
            }

            if (var3 != null) {
               var3.close();
            }

            return 0;
         }

         if (var3 != null) {
            var3.close();
         }

         return var5;
      } catch (SQLException var11) {
         this.plugin.getLogger().warning("Could not count rows in " + var2 + ": " + var11.getMessage());
         return 0;
      }
   }

   private int migrateProducts(Connection var1, DatabaseConnection var2, Map<UUID, String> var3, AtomicInteger var4) {
      int var5 = 0;
      String var6 = "SELECT uuid, item, seller, sellerName, price, date FROM products";

      try {
         PreparedStatement var7 = var1.prepareStatement(var6);

         try {
            ResultSet var8 = var7.executeQuery();

            try {
               while(var8.next()) {
                  try {
                     String var9 = var8.getString("item");
                     UUID var10 = UUID.fromString(var8.getString("seller"));
                     String var11 = var8.getString("sellerName");
                     double var12 = var8.getDouble("price");
                     long var14 = var8.getLong("date");
                     this.trackPlayer(var2, var3, var10, var11);
                     String var16 = this.convertItemStack(var9);
                     if (var16 == null) {
                        this.plugin.getLogger().warning("Failed to convert itemstack for product, skipping");
                        var4.incrementAndGet();
                     } else {
                        long var17 = var14 > 0L ? var14 + 172800000L : System.currentTimeMillis() + 172800000L;
                        int var19 = this.createItem(var2, var10, (UUID)null, var12, StorageType.LISTED, var17);
                        if (var19 == -1) {
                           var4.incrementAndGet();
                        } else {
                           this.insertAuctionItem(var2, var19, var16);
                           ++var5;
                           if (var5 % 100 == 0) {
                              this.progress("Migrated " + var5 + " products...");
                           }
                        }
                     }
                  } catch (Exception var22) {
                     this.plugin.getLogger().warning("Failed to migrate product: " + var22.getMessage());
                     var4.incrementAndGet();
                  }
               }
            } catch (Throwable var23) {
               if (var8 != null) {
                  try {
                     var8.close();
                  } catch (Throwable var21) {
                     var23.addSuppressed(var21);
                  }
               }

               throw var23;
            }

            if (var8 != null) {
               var8.close();
            }
         } catch (Throwable var24) {
            if (var7 != null) {
               try {
                  var7.close();
               } catch (Throwable var20) {
                  var24.addSuppressed(var20);
               }
            }

            throw var24;
         }

         if (var7 != null) {
            var7.close();
         }
      } catch (SQLException var25) {
         this.plugin.getLogger().severe("Failed to read products table: " + var25.getMessage());
      }

      return var5;
   }

   private int migrateMailbox(Connection var1, DatabaseConnection var2, Map<UUID, String> var3, AtomicInteger var4) {
      int var5 = 0;
      String var6 = "SELECT uuid, item, seller, price FROM mailbox";

      try {
         PreparedStatement var7 = var1.prepareStatement(var6);

         try {
            ResultSet var8 = var7.executeQuery();

            try {
               while(var8.next()) {
                  try {
                     String var9 = var8.getString("item");
                     UUID var10 = UUID.fromString(var8.getString("seller"));
                     double var11 = var8.getDouble("price");
                     this.trackPlayer(var2, var3, var10, (String)null);
                     String var13 = this.convertItemStack(var9);
                     if (var13 == null) {
                        this.plugin.getLogger().warning("Failed to convert itemstack for mailbox item, skipping");
                        var4.incrementAndGet();
                     } else {
                        int var14 = this.createItem(var2, var10, (UUID)null, var11, StorageType.EXPIRED, System.currentTimeMillis());
                        if (var14 == -1) {
                           var4.incrementAndGet();
                        } else {
                           this.insertAuctionItem(var2, var14, var13);
                           ++var5;
                           if (var5 % 100 == 0) {
                              this.progress("Migrated " + var5 + " mailbox items...");
                           }
                        }
                     }
                  } catch (Exception var17) {
                     this.plugin.getLogger().warning("Failed to migrate mailbox item: " + var17.getMessage());
                     var4.incrementAndGet();
                  }
               }
            } catch (Throwable var18) {
               if (var8 != null) {
                  try {
                     var8.close();
                  } catch (Throwable var16) {
                     var18.addSuppressed(var16);
                  }
               }

               throw var18;
            }

            if (var8 != null) {
               var8.close();
            }
         } catch (Throwable var19) {
            if (var7 != null) {
               try {
                  var7.close();
               } catch (Throwable var15) {
                  var19.addSuppressed(var15);
               }
            }

            throw var19;
         }

         if (var7 != null) {
            var7.close();
         }
      } catch (SQLException var20) {
         this.plugin.getLogger().severe("Failed to read mailbox table: " + var20.getMessage());
      }

      return var5;
   }

   private int migrateTransactions(Connection var1, DatabaseConnection var2, Map<UUID, String> var3, AtomicInteger var4) {
      int var5 = 0;
      String var6 = "SELECT uuid, item, transactionType, fromUUID, fromUsername, toUsername, price, date FROM transactions";

      try {
         PreparedStatement var7 = var1.prepareStatement(var6);

         try {
            ResultSet var8 = var7.executeQuery();

            try {
               while(var8.next()) {
                  try {
                     String var9 = var8.getString("item");
                     String var10 = var8.getString("transactionType");
                     UUID var11 = UUID.fromString(var8.getString("fromUUID"));
                     String var12 = var8.getString("fromUsername");
                     String var13 = var8.getString("toUsername");
                     double var14 = var8.getDouble("price");
                     long var16 = var8.getLong("date");
                     this.trackPlayer(var2, var3, var11, var12);
                     OfflinePlayer var18 = Bukkit.getOfflinePlayer(var13);
                     this.trackPlayer(var2, var3, var18.getUniqueId(), var13);
                     String var19 = this.convertItemStack(var9);
                     if (var19 == null) {
                        this.plugin.getLogger().warning("Failed to convert itemstack for transaction, skipping");
                        var4.incrementAndGet();
                     } else {
                        LogType var20 = "BUY".equalsIgnoreCase(var10) ? LogType.PURCHASE : LogType.SALE;
                        this.createLogEntry(var2, var11, var19, var14, var20, var16);
                        ++var5;
                        if (var5 % 100 == 0) {
                           this.progress("Migrated " + var5 + " transactions...");
                        }
                     }
                  } catch (Exception var23) {
                     this.plugin.getLogger().warning("Failed to migrate transaction: " + var23.getMessage());
                     var4.incrementAndGet();
                  }
               }
            } catch (Throwable var24) {
               if (var8 != null) {
                  try {
                     var8.close();
                  } catch (Throwable var22) {
                     var24.addSuppressed(var22);
                  }
               }

               throw var24;
            }

            if (var8 != null) {
               var8.close();
            }
         } catch (Throwable var25) {
            if (var7 != null) {
               try {
                  var7.close();
               } catch (Throwable var21) {
                  var25.addSuppressed(var21);
               }
            }

            throw var25;
         }

         if (var7 != null) {
            var7.close();
         }
      } catch (SQLException var26) {
         this.plugin.getLogger().severe("Failed to read transactions table: " + var26.getMessage());
      }

      return var5;
   }

   private String convertItemStack(String var1) {
      ItemStack var2 = Serialize.deserializeLegacy(var1, this.plugin);
      return var2 == null ? null : Base64ItemStack.encode(var2);
   }

   private int createItem(DatabaseConnection var1, UUID var2, UUID var3, double var4, StorageType var6, long var7) {
      try {
         Schema var9 = SchemaBuilder.insert("%prefix%items", (var8) -> {
            var8.string("item_type", ItemType.AUCTION.name());
            var8.uuid("seller_unique_id", var2);
            if (var3 != null) {
               var8.uuid("buyer_unique_id", var3);
            }

            var8.decimal("price", BigDecimal.valueOf(var4));
            var8.string("economy_name", "vault");
            var8.string("storage_type", var6.name());
            var8.string("server_name", this.plugin.getConfiguration().getServerName());
            var8.object("expired_at", new Date(var7));
         });
         return var9.execute(var1, this.logger);
      } catch (SQLException var10) {
         this.plugin.getLogger().warning("Failed to create item: " + var10.getMessage());
         return -1;
      }
   }

   private void insertAuctionItem(DatabaseConnection var1, int var2, String var3) {
      try {
         Schema var4 = SchemaBuilder.insert("%prefix%auction_items", (var2x) -> {
            var2x.object("item_id", var2);
            var2x.string("itemstack", var3);
         });
         var4.execute(var1, this.logger);
      } catch (SQLException var5) {
         this.plugin.getLogger().warning("Failed to create auction item for item_id " + var2 + ": " + var5.getMessage());
      }

   }

   private void createLogEntry(DatabaseConnection var1, UUID var2, String var3, double var4, LogType var6, long var7) {
      try {
         Schema var9 = SchemaBuilder.insert("%prefix%logs", (var7x) -> {
            var7x.string("log_type", var6.name());
            var7x.object("item_id", 0);
            var7x.uuid("player_unique_id", var2);
            var7x.string("itemstack", var3);
            var7x.decimal("price", BigDecimal.valueOf(var4));
            var7x.string("economy_name", "vault");
            var7x.string("additional_data", "migrated_from_zelauction");
            var7x.object("created_at", new Date(var7));
         });
         var9.execute(var1, this.logger);
      } catch (SQLException var10) {
         this.plugin.getLogger().warning("Failed to create log entry: " + var10.getMessage());
      }

   }

   private void trackPlayer(DatabaseConnection var1, Map<UUID, String> var2, UUID var3, String var4) {
      String var5 = var4 != null && !var4.isBlank() ? var4 : "Unknown";
      String var6 = (String)var2.get(var3);
      if (var6 == null || "Unknown".equals(var6) && !"Unknown".equals(var5)) {
         var2.put(var3, var5);

         try {
            SchemaBuilder.upsert("%prefix%players", (var2x) -> {
               var2x.uuid("unique_id", var3).primary();
               var2x.string("name", var5);
            }).execute(var1, this.logger);
         } catch (Exception var8) {
            java.util.logging.Logger var10000 = this.plugin.getLogger();
            String var10001 = String.valueOf(var3);
            var10000.warning("Failed to upsert player " + var10001 + ": " + var8.getMessage());
         }
      }

   }

   private void progress(String var1) {
      this.plugin.getLogger().info("[Migration] " + var1);
      if (this.progressCallback != null) {
         this.progressCallback.accept(var1);
      }

   }
}
