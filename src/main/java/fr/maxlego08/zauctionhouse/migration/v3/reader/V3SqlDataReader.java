package fr.maxlego08.zauctionhouse.migration.v3.reader;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.migration.v3.V3StorageType;
import fr.maxlego08.zauctionhouse.migration.v3.items.V3AuctionItem;
import fr.maxlego08.zauctionhouse.migration.v3.items.V3Transaction;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class V3SqlDataReader implements V3DataReader {
   private final AuctionPlugin plugin;
   private final String jdbcUrl;
   private final String username;
   private final String password;
   private final String tablePrefix;
   private Connection connection;

   public V3SqlDataReader(AuctionPlugin var1, String var2, int var3, String var4, String var5, String var6, String var7) {
      this.plugin = var1;
      this.jdbcUrl = "jdbc:mysql://" + var2 + ":" + var3 + "/" + var4 + "?useSSL=false&autoReconnect=true&useUnicode=true&characterEncoding=UTF-8";
      this.username = var5;
      this.password = var6;
      this.tablePrefix = var7 != null ? var7 : "zauctionhouse_";
   }

   public V3SqlDataReader(AuctionPlugin var1, String var2, String var3) {
      this.plugin = var1;
      this.jdbcUrl = "jdbc:sqlite:" + var2;
      this.username = null;
      this.password = null;
      this.tablePrefix = var3 != null ? var3 : "zauctionhouse_";
   }

   private Connection getConnection() throws java.sql.SQLException {
      if (this.connection == null || this.connection.isClosed()) {
         if (this.username != null) {
            try { this.connection = DriverManager.getConnection(this.jdbcUrl, this.username, this.password); } catch (java.sql.SQLException e) { throw new RuntimeException(e); }
         } else {
            try { this.connection = DriverManager.getConnection(this.jdbcUrl); } catch (java.sql.SQLException e) { throw new RuntimeException(e); }
         }
      }

      return this.connection;
   }

   public CompletableFuture<Boolean> testConnection() throws java.sql.SQLException {
      return CompletableFuture.supplyAsync(() -> {
         try {
            Connection var1 = this.getConnection();
            return var1 != null && !var1.isClosed();
         } catch (SQLException var2) {
            this.plugin.getLogger().severe("Failed to connect to V3 database: " + var2.getMessage());
            return false;
         }
      });
   }

   public CompletableFuture<List<V3AuctionItem>> readItems() throws java.sql.SQLException {
      return CompletableFuture.supplyAsync(() -> {
         ArrayList var1 = new ArrayList();
         String var2 = this.tablePrefix + "items";
         String var3 = "SELECT id, itemstack, price, seller, buyer, economy, auction_type, expire_at, storage_type, sellerName, server_name, priority FROM " + var2;

         try {
            PreparedStatement var4 = this.getConnection().prepareStatement(var3);

            try {
               ResultSet var5 = var4.executeQuery();

               try {
                  while(var5.next()) {
                     try {
                        V3AuctionItem var6 = this.parseItem(var5);
                        if (var6 != null) {
                           var1.add(var6);
                        }
                     } catch (Exception var10) {
                        this.plugin.getLogger().warning("Failed to parse V3 item: " + var10.getMessage());
                     }
                  }
               } catch (Throwable var11) {
                  if (var5 != null) {
                     try {
                        var5.close();
                     } catch (Throwable var9) {
                        var11.addSuppressed(var9);
                     }
                  }

                  throw var11;
               }

               if (var5 != null) {
                  var5.close();
               }
            } catch (Throwable var12) {
               if (var4 != null) {
                  try {
                     var4.close();
                  } catch (Throwable var8) {
                     var12.addSuppressed(var8);
                  }
               }

               throw var12;
            }

            if (var4 != null) {
               var4.close();
            }
         } catch (SQLException var13) {
            this.plugin.getLogger().severe("Failed to read V3 items: " + var13.getMessage());
         }

         return var1;
      });
   }

   private V3AuctionItem parseItem(ResultSet var1) throws java.sql.SQLException {
      String var2 = var1.getString("id");
      UUID var3 = this.parseUUID(var2);
      if (var3 == null) {
         this.plugin.getLogger().warning("Invalid UUID for item: " + var2);
         return null;
      } else {
         String var4 = var1.getString("itemstack");
         long var5 = var1.getLong("price");
         UUID var7 = this.parseUUID(var1.getString("seller"));
         UUID var8 = this.parseUUID(var1.getString("buyer"));
         String var9 = var1.getString("economy");
         String var10 = var1.getString("auction_type");
         long var11 = var1.getLong("expire_at");
         V3StorageType var13 = V3StorageType.fromString(var1.getString("storage_type"));
         String var14 = var1.getString("sellerName");
         String var15 = var1.getString("server_name");
         int var16 = 0;

         try {
            var16 = var1.getInt("priority");
         } catch (SQLException var18) {
         }

         return new V3AuctionItem(var3, var4, var5, var7, var8, var9, var10, var11, var13, var14, var15, var16);
      }
   }

   public CompletableFuture<List<V3Transaction>> readTransactions() throws java.sql.SQLException {
      return CompletableFuture.supplyAsync(() -> {
         ArrayList var1 = new ArrayList();
         String var2 = this.tablePrefix + "transactions";
         String var3 = "SELECT id, seller, buyer, itemstack, transaction_date, price, economy, is_read, need_money FROM " + var2;

         try {
            PreparedStatement var4 = this.getConnection().prepareStatement(var3);

            try {
               ResultSet var5 = var4.executeQuery();

               try {
                  while(var5.next()) {
                     try {
                        V3Transaction var6 = this.parseTransaction(var5);
                        if (var6 != null) {
                           var1.add(var6);
                        }
                     } catch (Exception var10) {
                        this.plugin.getLogger().warning("Failed to parse V3 transaction: " + var10.getMessage());
                     }
                  }
               } catch (Throwable var11) {
                  if (var5 != null) {
                     try {
                        var5.close();
                     } catch (Throwable var9) {
                        var11.addSuppressed(var9);
                     }
                  }

                  throw var11;
               }

               if (var5 != null) {
                  var5.close();
               }
            } catch (Throwable var12) {
               if (var4 != null) {
                  try {
                     var4.close();
                  } catch (Throwable var8) {
                     var12.addSuppressed(var8);
                  }
               }

               throw var12;
            }

            if (var4 != null) {
               var4.close();
            }
         } catch (SQLException var13) {
            this.plugin.getLogger().severe("Failed to read V3 transactions: " + var13.getMessage());
         }

         return var1;
      });
   }

   private V3Transaction parseTransaction(ResultSet var1) throws java.sql.SQLException {
      int var2 = var1.getInt("id");
      UUID var3 = this.parseUUID(var1.getString("seller"));
      UUID var4 = this.parseUUID(var1.getString("buyer"));
      String var5 = var1.getString("itemstack");
      long var6 = var1.getLong("transaction_date");
      long var8 = var1.getLong("price");
      String var10 = var1.getString("economy");
      boolean var11 = var1.getBoolean("is_read");
      boolean var12 = var1.getBoolean("need_money");
      return new V3Transaction(var2, var3, var4, var5, var6, var8, var10, var11, var12);
   }

   public CompletableFuture<Integer> getItemCount() throws java.sql.SQLException {
      return CompletableFuture.supplyAsync(() -> {
         String var1 = this.tablePrefix + "items";
         String var2 = "SELECT COUNT(*) FROM " + var1;

         try {
            PreparedStatement var3 = this.getConnection().prepareStatement(var2);

            Integer var5;
            label84: {
               try {
                  ResultSet var4 = var3.executeQuery();

                  label78: {
                     try {
                        if (!var4.next()) {
                           break label78;
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
                     break label84;
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
            this.plugin.getLogger().severe("Failed to count V3 items: " + var11.getMessage());
            return 0;
         }
      });
   }

   public CompletableFuture<Integer> getTransactionCount() throws java.sql.SQLException {
      return CompletableFuture.supplyAsync(() -> {
         String var1 = this.tablePrefix + "transactions";
         String var2 = "SELECT COUNT(*) FROM " + var1;

         try {
            PreparedStatement var3 = this.getConnection().prepareStatement(var2);

            Integer var5;
            label84: {
               try {
                  ResultSet var4 = var3.executeQuery();

                  label78: {
                     try {
                        if (!var4.next()) {
                           break label78;
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
                     break label84;
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
            this.plugin.getLogger().severe("Failed to count V3 transactions: " + var11.getMessage());
            return 0;
         }
      });
   }

   public void close() throws java.sql.SQLException {
      if (this.connection != null) {
         try {
            this.connection.close();
         } catch (SQLException var2) {
            this.plugin.getLogger().warning("Failed to close V3 database connection: " + var2.getMessage());
         }
      }

   }

   private UUID parseUUID(String var1) {
      if (var1 != null && !var1.isEmpty() && !var1.equals("null")) {
         try {
            return UUID.fromString(var1);
         } catch (IllegalArgumentException var3) {
            return null;
         }
      } else {
         return null;
      }
   }
}
