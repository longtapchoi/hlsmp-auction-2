package fr.maxlego08.zauctionhouse.api;

import fr.maxlego08.zauctionhouse.api.cache.PlayerCache;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCacheKey;
import fr.maxlego08.zauctionhouse.api.item.Item;
import fr.maxlego08.zauctionhouse.api.item.StorageType;
import fr.maxlego08.zauctionhouse.api.messages.Message;
import fr.maxlego08.zauctionhouse.api.services.AuctionClaimService;
import fr.maxlego08.zauctionhouse.api.services.AuctionExpireService;
import fr.maxlego08.zauctionhouse.api.services.AuctionHistoryService;
import fr.maxlego08.zauctionhouse.api.services.AuctionOptionService;
import fr.maxlego08.zauctionhouse.api.services.AuctionPurchaseService;
import fr.maxlego08.zauctionhouse.api.services.AuctionRemoveService;
import fr.maxlego08.zauctionhouse.api.services.AuctionSellService;
import fr.maxlego08.zauctionhouse.api.utils.IntList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import org.bukkit.entity.Player;

public interface AuctionManager {
   void setupSortedItemsCache();

   void openMainAuction(Player var1);

   void openMainAuction(Player var1, int var2);

   void updateInventory(Player var1);

   AuctionPurchaseService getPurchaseService();

   AuctionSellService getSellService();

   AuctionRemoveService getRemoveService();

   AuctionExpireService getExpireService();

   AuctionClaimService getClaimService();

   AuctionHistoryService getHistoryService();

   AuctionOptionService getOptionService();

   List<Item> getItems(StorageType var1);

   List<Item> resolveItems(StorageType var1, IntList var2);

   List<Item> getItems(StorageType var1, Predicate<Item> var2);

   List<Item> getItems(StorageType var1, Predicate<Item> var2, Comparator<Item> var3);

   void addItem(StorageType var1, Item var2);

   void removeItem(StorageType var1, Item var2);

   void removeItem(StorageType var1, int var2);

   List<Item> getItemsListedForSale(Player var1);

   IntList getItemIdsListedForSale(Player var1);

   List<Item> resolveItemsForPage(StorageType var1, IntList var2, int var3, int var4);

   List<Item> getExpiredItems(Player var1);

   List<Item> getExpiredItems(UUID var1);

   List<Item> getPlayerSellingItems(Player var1);

   List<Item> getPurchasedItems(Player var1);

   List<Item> getPlayerSellingItems(UUID var1);

   List<Item> getPurchasedItems(UUID var1);

   PlayerCache getCache(Player var1);

   void clearPlayersCache(PlayerCacheKey... var1);

   void clearPlayerCache(Player var1, PlayerCacheKey... var2);

   void removeCache(Player var1);

   CompletableFuture<Void> removeListedItem(Player var1, Item var2);

   CompletableFuture<Void> removeSellingItem(Player var1, Item var2);

   CompletableFuture<Void> removeExpiredItem(Player var1, Item var2);

   CompletableFuture<Void> removePurchasedItem(Player var1, Item var2);

   void adminRemoveItem(Player var1, UUID var2, Item var3, StorageType var4);

   CompletableFuture<Void> purchaseItem(Player var1, Item var2);

   void message(Player var1, Message var2, Object... var3);

   void updateListedItems(Item var1, boolean var2, Player var3);

   void rebuildSortedItemsCache();

   void updateItemEconomies();

   void shutdown();

   void startSearch(Player var1, String var2);

   void clearSearch(Player var1);

   void removeAllExpiredItems(Player var1);

   void removeAllSellingItems(Player var1);

   void removeAllPurchasedItems(Player var1);
}
