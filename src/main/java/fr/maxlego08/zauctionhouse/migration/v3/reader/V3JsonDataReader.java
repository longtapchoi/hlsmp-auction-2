package fr.maxlego08.zauctionhouse.migration.v3.reader;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.migration.v3.V3StorageType;
import fr.maxlego08.zauctionhouse.migration.v3.items.V3AuctionItem;
import fr.maxlego08.zauctionhouse.migration.v3.items.V3Transaction;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings({"unchecked", "rawtypes"})
public class V3JsonDataReader implements V3DataReader {
   private final AuctionPlugin plugin;
   private final File dataFolder;
   private final Gson gson;

   public V3JsonDataReader(AuctionPlugin var1, File var2) {
      this.plugin = var1;
      this.dataFolder = var2;
      this.gson = new Gson();
   }

   public CompletableFuture<Boolean> testConnection() {
      return CompletableFuture.supplyAsync(() -> {
         File var1 = new File(this.dataFolder, "items.json");
         return var1.exists() && var1.canRead();
      });
   }

   public CompletableFuture<List<V3AuctionItem>> readItems() {
      return CompletableFuture.supplyAsync(() -> {
         ArrayList var1 = new ArrayList();
         var1.addAll(this.readItemsFromFile("items.json", V3StorageType.STORAGE));
         var1.addAll(this.readItemsFromFile("buying_items.json", V3StorageType.BUY));
         var1.addAll(this.readItemsFromFile("expired_items.json", V3StorageType.EXPIRE));
         if (var1.isEmpty()) {
            var1.addAll(this.readAllItemsFromSingleFile());
         }

         return var1;
      });
   }

   private List<V3AuctionItem> readItemsFromFile(String var1, V3StorageType var2) {
      ArrayList var3 = new ArrayList();
      File var4 = new File(this.dataFolder, var1);
      if (!var4.exists()) {
         return var3;
      } else {
         try {
            FileReader var5 = new FileReader(var4);

            try {
               JsonElement var6 = JsonParser.parseReader(var5);
               if (var6.isJsonArray()) {
                  for(JsonElement var9 : var6.getAsJsonArray()) {
                     V3AuctionItem var10 = this.parseItemFromJson(var9.getAsJsonObject(), var2);
                     if (var10 != null) {
                        var3.add(var10);
                     }
                  }
               }
            } catch (Throwable var12) {
               try {
                  var5.close();
               } catch (Throwable var11) {
                  var12.addSuppressed(var11);
               }

               throw var12;
            }

            var5.close();
         } catch (IOException var13) {
            this.plugin.getLogger().warning("Failed to read V3 JSON file " + var1 + ": " + var13.getMessage());
         }

         return var3;
      }
   }

   private List<V3AuctionItem> readAllItemsFromSingleFile() {
      ArrayList var1 = new ArrayList();
      File var2 = new File(this.dataFolder, "data.json");
      if (!var2.exists()) {
         return var1;
      } else {
         try {
            FileReader var3 = new FileReader(var2);

            try {
               JsonElement var4 = JsonParser.parseReader(var3);
               if (var4.isJsonObject()) {
                  JsonObject var5 = var4.getAsJsonObject();
                  if (var5.has("items")) {
                     var1.addAll(this.parseItemArray(var5.getAsJsonArray("items"), V3StorageType.STORAGE));
                  }

                  if (var5.has("buyingItems")) {
                     var1.addAll(this.parseItemArray(var5.getAsJsonArray("buyingItems"), V3StorageType.BUY));
                  }

                  if (var5.has("expiredItems")) {
                     var1.addAll(this.parseItemArray(var5.getAsJsonArray("expiredItems"), V3StorageType.EXPIRE));
                  }
               }
            } catch (Throwable var7) {
               try {
                  var3.close();
               } catch (Throwable var6) {
                  var7.addSuppressed(var6);
               }

               throw var7;
            }

            var3.close();
         } catch (IOException var8) {
            this.plugin.getLogger().warning("Failed to read V3 data.json: " + var8.getMessage());
         }

         return var1;
      }
   }

   private List<V3AuctionItem> parseItemArray(JsonArray var1, V3StorageType var2) {
      ArrayList var3 = new ArrayList();
      if (var1 == null) {
         return var3;
      } else {
         for(JsonElement var5 : var1) {
            V3AuctionItem var6 = this.parseItemFromJson(var5.getAsJsonObject(), var2);
            if (var6 != null) {
               var3.add(var6);
            }
         }

         return var3;
      }
   }

   private V3AuctionItem parseItemFromJson(JsonObject var1, V3StorageType var2) {
      try {
         UUID var3 = this.getUUID(var1, "id");
         if (var3 == null) {
            var3 = this.getUUID(var1, "uniqueId");
         }

         if (var3 == null) {
            return null;
         } else {
            String var4 = this.getString(var1, "itemstack");
            if (var4 == null) {
               var4 = this.getString(var1, "itemStack");
            }

            long var5 = this.getLong(var1, "price");
            UUID var7 = this.getUUID(var1, "seller");
            if (var7 == null) {
               var7 = this.getUUID(var1, "sellerUniqueId");
            }

            UUID var8 = this.getUUID(var1, "buyer");
            if (var8 == null) {
               var8 = this.getUUID(var1, "buyerUniqueId");
            }

            String var9 = this.getString(var1, "economy");
            if (var9 == null) {
               var9 = "VAULT";
            }

            String var10 = this.getString(var1, "auctionType");
            if (var10 == null) {
               var10 = this.getString(var1, "auction_type");
            }

            if (var10 == null) {
               var10 = "DEFAULT";
            }

            long var11 = this.getLong(var1, "expireAt");
            if (var11 == 0L) {
               var11 = this.getLong(var1, "expire_at");
            }

            V3StorageType var13 = var2;
            String var14 = this.getString(var1, "storageType");
            if (var14 == null) {
               var14 = this.getString(var1, "storage_type");
            }

            if (var14 != null) {
               var13 = V3StorageType.fromString(var14);
            }

            String var15 = this.getString(var1, "sellerName");
            String var16 = this.getString(var1, "serverName");
            if (var16 == null) {
               var16 = this.getString(var1, "server_name");
            }

            int var17 = this.getInt(var1, "priority");
            return new V3AuctionItem(var3, var4, var5, var7, var8, var9, var10, var11, var13, var15, var16, var17);
         }
      } catch (Exception var18) {
         this.plugin.getLogger().warning("Failed to parse V3 item from JSON: " + var18.getMessage());
         return null;
      }
   }

   public CompletableFuture<List<V3Transaction>> readTransactions() {
      return CompletableFuture.supplyAsync(() -> {
         ArrayList var1 = new ArrayList();
         File var2 = new File(this.dataFolder, "transactions.json");
         if (!var2.exists()) {
            return var1;
         } else {
            try {
               FileReader var3 = new FileReader(var2);

               try {
                  JsonElement var4 = JsonParser.parseReader(var3);
                  if (var4.isJsonArray()) {
                     for(JsonElement var7 : var4.getAsJsonArray()) {
                        V3Transaction var8 = this.parseTransactionFromJson(var7.getAsJsonObject());
                        if (var8 != null) {
                           var1.add(var8);
                        }
                     }
                  }
               } catch (Throwable var10) {
                  try {
                     var3.close();
                  } catch (Throwable var9) {
                     var10.addSuppressed(var9);
                  }

                  throw var10;
               }

               var3.close();
            } catch (IOException var11) {
               this.plugin.getLogger().warning("Failed to read V3 transactions.json: " + var11.getMessage());
            }

            return var1;
         }
      });
   }

   private V3Transaction parseTransactionFromJson(JsonObject var1) {
      try {
         int var2 = this.getInt(var1, "id");
         UUID var3 = this.getUUID(var1, "seller");
         if (var3 == null) {
            var3 = this.getUUID(var1, "sellerUniqueId");
         }

         UUID var4 = this.getUUID(var1, "buyer");
         if (var4 == null) {
            var4 = this.getUUID(var1, "buyerUniqueId");
         }

         String var5 = this.getString(var1, "itemstack");
         if (var5 == null) {
            var5 = this.getString(var1, "itemStack");
         }

         long var6 = this.getLong(var1, "transactionDate");
         if (var6 == 0L) {
            var6 = this.getLong(var1, "transaction_date");
         }

         if (var6 == 0L) {
            var6 = this.getLong(var1, "date");
         }

         long var8 = this.getLong(var1, "price");
         String var10 = this.getString(var1, "economy");
         if (var10 == null) {
            var10 = "VAULT";
         }

         boolean var11 = this.getBoolean(var1, "isRead");
         if (!var11) {
            var11 = this.getBoolean(var1, "is_read");
         }

         boolean var12 = this.getBoolean(var1, "needMoney");
         if (!var12) {
            var12 = this.getBoolean(var1, "need_money");
         }

         return new V3Transaction(var2, var3, var4, var5, var6, var8, var10, var11, var12);
      } catch (Exception var13) {
         this.plugin.getLogger().warning("Failed to parse V3 transaction from JSON: " + var13.getMessage());
         return null;
      }
   }

   public CompletableFuture<Integer> getItemCount() {
      return this.readItems().thenApply(List::size);
   }

   public CompletableFuture<Integer> getTransactionCount() {
      return this.readTransactions().thenApply(List::size);
   }

   public void close() {
   }

   private String getString(JsonObject var1, String var2) {
      return var1.has(var2) && !var1.get(var2).isJsonNull() ? var1.get(var2).getAsString() : null;
   }

   private long getLong(JsonObject var1, String var2) {
      return var1.has(var2) && !var1.get(var2).isJsonNull() ? var1.get(var2).getAsLong() : 0L;
   }

   private int getInt(JsonObject var1, String var2) {
      return var1.has(var2) && !var1.get(var2).isJsonNull() ? var1.get(var2).getAsInt() : 0;
   }

   private boolean getBoolean(JsonObject var1, String var2) {
      return var1.has(var2) && !var1.get(var2).isJsonNull() ? var1.get(var2).getAsBoolean() : false;
   }

   private UUID getUUID(JsonObject var1, String var2) {
      String var3 = this.getString(var1, var2);
      if (var3 != null && !var3.isEmpty() && !var3.equals("null")) {
         try {
            return UUID.fromString(var3);
         } catch (IllegalArgumentException var5) {
            return null;
         }
      } else {
         return null;
      }
   }
}
