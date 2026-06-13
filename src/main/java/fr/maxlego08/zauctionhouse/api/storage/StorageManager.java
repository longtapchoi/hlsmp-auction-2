package fr.maxlego08.zauctionhouse.api.storage;

import fr.maxlego08.zauctionhouse.api.economy.AuctionEconomy;
import fr.maxlego08.zauctionhouse.api.item.Item;
import fr.maxlego08.zauctionhouse.api.item.StorageType;
import fr.maxlego08.zauctionhouse.api.item.items.AuctionItem;
import fr.maxlego08.zauctionhouse.api.log.LogType;
import fr.maxlego08.zauctionhouse.api.storage.dto.LogDTO;
import fr.maxlego08.zauctionhouse.api.transaction.TransactionStatus;
import fr.maxlego08.sarah.DatabaseConnection;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface StorageManager {
   boolean onEnable();

   void onDisable();

   void loadItems();

   void upsertPlayer(Player var1);

   CompletableFuture<AuctionItem> createAuctionItem(Player var1, BigDecimal var2, long var3, List<ItemStack> var5, AuctionEconomy var6);

   CompletableFuture<AuctionItem> createAuctionItem(UUID var1, String var2, BigDecimal var3, long var4, List<ItemStack> var6, AuctionEconomy var7);

   void upsertPlayer(UUID var1, String var2);

   <T extends Repository> T with(Class<T> var1);

   DatabaseConnection getDatabaseConnection();

   CompletableFuture<Void> updateItem(Item var1, StorageType var2);

   CompletableFuture<Void> updateItems(Map<StorageType, List<Item>> var1);

   void log(LogType var1, int var2, Player var3, UUID var4, String var5, BigDecimal var6, String var7, String var8, Date var9);

   void createTransaction(Item var1, UUID var2, String var3, BigDecimal var4, BigDecimal var5, BigDecimal var6, TransactionStatus var7);

   CompletableFuture<Item> selectItem(int var1);

   CompletableFuture<UUID> findUniqueId(String var1);

   String getPlayerName(UUID var1);

   List<LogDTO> selectSalesHistory(UUID var1);

   List<Item> selectItems(List<Integer> var1);

   Map<UUID, String> selectPlayers(List<String> var1);

   void markPurchaseLogAsRead(int var1, UUID var2);
}
