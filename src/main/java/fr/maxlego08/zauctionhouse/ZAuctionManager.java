package fr.maxlego08.zauctionhouse;

import fr.maxlego08.menu.api.InventoryManager;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.utils.CompatibilityUtil;
import fr.maxlego08.zauctionhouse.api.AuctionManager;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.InventoriesLoader;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCache;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCacheKey;
import fr.maxlego08.zauctionhouse.api.category.Category;
import fr.maxlego08.zauctionhouse.api.cluster.AuctionClusterBridge;
import fr.maxlego08.zauctionhouse.api.configuration.Configuration;
import fr.maxlego08.zauctionhouse.api.configuration.records.ActionConfiguration;
import fr.maxlego08.zauctionhouse.api.economy.AuctionEconomy;
import fr.maxlego08.zauctionhouse.api.economy.EconomyManager;
import fr.maxlego08.zauctionhouse.api.event.AuctionEvent;
import fr.maxlego08.zauctionhouse.api.event.events.remove.AuctionRemoveExpiredItemEvent;
import fr.maxlego08.zauctionhouse.api.event.events.remove.AuctionRemoveListedItemEvent;
import fr.maxlego08.zauctionhouse.api.event.events.remove.AuctionRemovePurchasedItemEvent;
import fr.maxlego08.zauctionhouse.api.inventories.Inventories;
import fr.maxlego08.zauctionhouse.api.item.Item;
import fr.maxlego08.zauctionhouse.api.item.ItemStatus;
import fr.maxlego08.zauctionhouse.api.item.SortItem;
import fr.maxlego08.zauctionhouse.api.item.StorageType;
import fr.maxlego08.zauctionhouse.api.item.items.AuctionItem;
import fr.maxlego08.zauctionhouse.api.log.LogType;
import fr.maxlego08.zauctionhouse.api.messages.Message;
import fr.maxlego08.zauctionhouse.api.services.AuctionClaimService;
import fr.maxlego08.zauctionhouse.api.services.AuctionExpireService;
import fr.maxlego08.zauctionhouse.api.services.AuctionHistoryService;
import fr.maxlego08.zauctionhouse.api.services.AuctionOptionService;
import fr.maxlego08.zauctionhouse.api.services.AuctionPurchaseService;
import fr.maxlego08.zauctionhouse.api.services.AuctionRemoveService;
import fr.maxlego08.zauctionhouse.api.services.AuctionSellService;
import fr.maxlego08.zauctionhouse.api.storage.StorageManager;
import fr.maxlego08.zauctionhouse.api.tax.TaxConfiguration;
import fr.maxlego08.zauctionhouse.api.tax.TaxResult;
import fr.maxlego08.zauctionhouse.api.tax.TaxType;
import fr.maxlego08.zauctionhouse.api.transaction.TransactionStatus;
import fr.maxlego08.zauctionhouse.api.utils.Base64ItemStack;
import fr.maxlego08.zauctionhouse.api.utils.IntArrayList;
import fr.maxlego08.zauctionhouse.api.utils.IntList;
import fr.maxlego08.zauctionhouse.buttons.list.ListedItemsButton;
import fr.maxlego08.zauctionhouse.discord.DiscordWebhookService;
import fr.maxlego08.zauctionhouse.services.ClaimService;
import fr.maxlego08.zauctionhouse.services.ExpireService;
import fr.maxlego08.zauctionhouse.services.HistoryService;
import fr.maxlego08.zauctionhouse.services.OptionService;
import fr.maxlego08.zauctionhouse.services.PurchaseService;
import fr.maxlego08.zauctionhouse.services.RemoveService;
import fr.maxlego08.zauctionhouse.services.SearchService;
import fr.maxlego08.zauctionhouse.services.SellService;
import fr.maxlego08.zauctionhouse.utils.PerformanceDebug;
import fr.maxlego08.zauctionhouse.utils.ZUtils;
import fr.maxlego08.zauctionhouse.utils.cache.SortedItemsCache;
import fr.maxlego08.zauctionhouse.utils.cache.ZPlayerCache;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class ZAuctionManager extends ZUtils implements AuctionManager {
   private final AuctionPlugin plugin;
   private final AuctionPurchaseService auctionPurchaseService;
   private final AuctionSellService auctionSellService;
   private final AuctionRemoveService auctionRemoveService;
   private final AuctionExpireService auctionExpireService;
   private final AuctionClaimService auctionClaimService;
   private final AuctionHistoryService auctionHistoryService;
   private final AuctionOptionService auctionOptionService;
   private final PerformanceDebug performanceDebug;
   private final SearchService searchService;
   private final Map<Player, PlayerCache> caches = new ConcurrentHashMap();
   private final Map<StorageType, Map<Integer, Item>> storageItemsById = new EnumMap(StorageType.class);
   private final Map<UUID, IntList> idsListedByOwner = new ConcurrentHashMap();
   private final Map<UUID, IntList> idsExpiredByOwner = new ConcurrentHashMap();
   private final Map<UUID, IntList> idsPurchasedByBuyer = new ConcurrentHashMap();
   private SortedItemsCache sortedItemsCache;

   public ZAuctionManager(AuctionPlugin var1) {
      this.plugin = var1;
      this.auctionPurchaseService = new PurchaseService(var1);
      this.auctionSellService = new SellService(var1, this);
      this.auctionRemoveService = new RemoveService(var1);
      this.auctionExpireService = new ExpireService(var1, this);
      this.auctionClaimService = new ClaimService(var1);
      this.auctionHistoryService = new HistoryService(var1);
      this.auctionOptionService = new OptionService(var1);
      this.performanceDebug = new PerformanceDebug(var1);
      this.searchService = new SearchService(var1);

      for(StorageType var5 : StorageType.values()) {
         this.storageItemsById.put(var5, new ConcurrentHashMap());
      }

   }

   public void setupSortedItemsCache() {
      this.sortedItemsCache = new SortedItemsCache(this.plugin, () -> ((Map)this.storageItemsById.get(StorageType.LISTED)).values());
   }

   public void openMainAuction(Player var1) {
      this.openMainAuction(var1, 1);
   }

   public void openMainAuction(Player var1, int var2) {
      PlayerCache var3 = this.getCache(var1);
      if (this.plugin.getConfiguration().getActions().resetCategoryOnOpen() && var3.has(PlayerCacheKey.CURRENT_CATEGORY)) {
         var3.remove(PlayerCacheKey.CURRENT_CATEGORY);
         var3.remove(PlayerCacheKey.ITEMS_LISTED);
      }

      if (this.plugin.getConfiguration().getActions().resetSearchOnOpen() && var3.has(PlayerCacheKey.SEARCH_QUERY)) {
         var3.remove(PlayerCacheKey.SEARCH_QUERY);
         var3.remove(PlayerCacheKey.ITEMS_SEARCH);
         var3.remove(PlayerCacheKey.ITEMS_LISTED);
      }

      this.openAuctionInventory(var1, var2);
   }

   private void openAuctionInventory(Player var1, int var2) {
      InventoriesLoader var3 = this.plugin.getInventoriesLoader();
      PlayerCache var4 = this.getCache(var1);
      boolean var5 = var4.has(PlayerCacheKey.ITEMS_LISTED);
      boolean var6 = !this.sortedItemsCache.isDirty();
      if (var5 && var6) {
         var3.openInventory(var1, Inventories.AUCTION, var2);
      } else {
         this.prepareCacheAsync(var1).thenRun(() -> this.plugin.getScheduler().runAtEntity(var1, (var3x) -> {
               if (var1.isOnline()) {
                  var3.openInventory(var1, Inventories.AUCTION, var2);
               }

            }));
      }

   }

   public CompletableFuture<Void> prepareCacheAsync(Player var1) {
      return this.sortedItemsCache.ensureCacheValidAsync().thenRun(() -> {
         PlayerCache var2 = this.getCache(var1);
         SortItem var3 = (SortItem)var2.get(PlayerCacheKey.ITEM_SORT, this.plugin.getConfiguration().getSort().defaultSort());
         Category var4 = (Category)var2.get(PlayerCacheKey.CURRENT_CATEGORY, (Category)null);
         var2.getOrCompute(PlayerCacheKey.ITEMS_LISTED, () -> this.sortedItemsCache.getSortedIds(var4, var3));
      });
   }

   public void updateInventory(Player var1) {
      this.plugin.getScheduler().runAtEntity(var1, (var2) -> this.plugin.getInventoriesLoader().getInventoryManager().updateInventory(var1));
   }

   public AuctionPlugin getPlugin() {
      return this.plugin;
   }

   public SortedItemsCache getSortedItemsCache() {
      return this.sortedItemsCache;
   }

   public void rebuildSortedItemsCache() {
      this.sortedItemsCache.rebuildAsync();
   }

   public AuctionPurchaseService getPurchaseService() {
      return this.auctionPurchaseService;
   }

   public AuctionSellService getSellService() {
      return this.auctionSellService;
   }

   public AuctionRemoveService getRemoveService() {
      return this.auctionRemoveService;
   }

   public AuctionExpireService getExpireService() {
      return this.auctionExpireService;
   }

   public AuctionClaimService getClaimService() {
      return this.auctionClaimService;
   }

   public AuctionHistoryService getHistoryService() {
      return this.auctionHistoryService;
   }

   public AuctionOptionService getOptionService() {
      return this.auctionOptionService;
   }

   public List<Item> getItems(StorageType var1) {
      return new ArrayList(((Map)this.storageItemsById.getOrDefault(var1, Map.of())).values());
   }

   public List<Item> getItems(StorageType var1, Predicate<Item> var2) {
      return this.resolveItems(var1, this.getItemIds(var1, var2, (Comparator)null));
   }

   public List<Item> getItems(StorageType var1, Predicate<Item> var2, Comparator<Item> var3) {
      return this.resolveItems(var1, this.getItemIds(var1, var2, var3));
   }

   public void addItem(StorageType var1, Item var2) {
      Map var3 = (Map)this.storageItemsById.get(var1);
      var3.put(var2.getId(), var2);
      this.indexItem(var1, var2);
      if (var1 == StorageType.LISTED) {
         this.plugin.getCategoryManager().invalidateCategoryCountCache();
         this.sortedItemsCache.invalidate();
      }

   }

   public void removeItem(StorageType var1, Item var2) {
      this.removeItem(var1, var2.getId());
   }

   public void removeItem(StorageType var1, int var2) {
      Map var3 = (Map)this.storageItemsById.get(var1);
      if (var3 != null) {
         Item var4 = (Item)var3.remove(var2);
         if (var4 != null) {
            this.deindexItem(var1, var4);
            if (var1 == StorageType.LISTED) {
               this.plugin.getCategoryManager().invalidateCategoryCountCache();
               this.sortedItemsCache.invalidate();
            }
         }

      }
   }

   public List<Item> getItemsListedForSale(Player var1) {
      long var2 = this.performanceDebug.start();
      IntList var4 = this.getItemIdsListedForSale(var1);
      this.performanceDebug.end("getItemsListedForSale", var2, "items=" + var4.size());
      return this.resolveItems(StorageType.LISTED, var4);
   }

   public IntList getItemIdsListedForSale(Player var1) {
      long var2 = this.performanceDebug.start();
      PlayerCache var4 = this.getCache(var1);
      SortItem var5 = (SortItem)var4.get(PlayerCacheKey.ITEM_SORT, this.plugin.getConfiguration().getSort().defaultSort());
      Category var6 = (Category)var4.get(PlayerCacheKey.CURRENT_CATEGORY, (Category)null);
      String var7 = (String)var4.get(PlayerCacheKey.SEARCH_QUERY);
      if (var7 != null && !var7.isBlank()) {
         IntList var9 = (IntList)var4.getOrCompute(PlayerCacheKey.ITEMS_SEARCH, () -> this.searchService.search(this.sortedItemsCache, var7, var5, var6));
         this.performanceDebug.end("getItemIdsListedForSale[search]", var2, "query=" + var7 + ", sort=" + String.valueOf(var5) + ", ids=" + var9.size());
         return var9;
      } else {
         IntList var8 = (IntList)var4.getOrCompute(PlayerCacheKey.ITEMS_LISTED, () -> this.sortedItemsCache.getSortedIds(var6, var5));
         PerformanceDebug var10000 = this.performanceDebug;
         String var10003 = String.valueOf(var5);
         var10000.end("getItemIdsListedForSale", var2, "sort=" + var10003 + ", category=" + (var6 != null ? var6.getId() : "all") + ", ids=" + var8.size());
         return var8;
      }
   }

   public List<Item> getExpiredItems(Player var1) {
      IntList var2 = (IntList)this.getCache(var1).getOrCompute(PlayerCacheKey.ITEMS_EXPIRED, () -> this.getItemIds(StorageType.EXPIRED, (var1x) -> var1x.getSellerUniqueId().equals(var1.getUniqueId()), Comparator.comparing(Item::getExpiredAt)));
      return this.resolveItems(StorageType.EXPIRED, var2);
   }

   public List<Item> getExpiredItems(UUID var1) {
      return this.resolveItems(StorageType.EXPIRED, this.getItemIds(StorageType.EXPIRED, (var1x) -> var1x.getSellerUniqueId().equals(var1), Comparator.comparing(Item::getExpiredAt)));
   }

   public List<Item> getPlayerSellingItems(Player var1) {
      IntList var2 = (IntList)this.getCache(var1).getOrCompute(PlayerCacheKey.ITEMS_SELLING, () -> this.getItemIds(StorageType.LISTED, (var1x) -> var1x.getSellerUniqueId().equals(var1.getUniqueId()), Comparator.comparing(Item::getExpiredAt)));
      return this.resolveItems(StorageType.LISTED, var2);
   }

   public List<Item> getPlayerSellingItems(UUID var1) {
      return this.resolveItems(StorageType.LISTED, this.getItemIds(StorageType.LISTED, (var1x) -> var1x.getSellerUniqueId().equals(var1), Comparator.comparing(Item::getExpiredAt)));
   }

   public List<Item> getPurchasedItems(Player var1) {
      IntList var2 = (IntList)this.getCache(var1).getOrCompute(PlayerCacheKey.ITEMS_PURCHASED, () -> this.getItemIds(StorageType.PURCHASED, (var1x) -> var1x.getBuyerUniqueId() != null && var1x.getBuyerUniqueId().equals(var1.getUniqueId()), Comparator.comparing(Item::getExpiredAt)));
      return this.resolveItems(StorageType.PURCHASED, var2);
   }

   public List<Item> getPurchasedItems(UUID var1) {
      return this.resolveItems(StorageType.PURCHASED, this.getItemIds(StorageType.PURCHASED, (var1x) -> var1x.getBuyerUniqueId() != null && var1x.getBuyerUniqueId().equals(var1), Comparator.comparing(Item::getExpiredAt)));
   }

   public List<Item> resolveItems(StorageType var1, IntList var2) {
      long var3 = this.performanceDebug.start();
      if (var2 != null && !var2.isEmpty()) {
         Map var5 = (Map)this.storageItemsById.get(var1);
         if (var5 != null && !var5.isEmpty()) {
            ArrayList var6 = new ArrayList(var2.size());

            for(int var8 : var2) {
               Item var9 = (Item)var5.get(var8);
               if (var9 != null) {
                  var6.add(var9);
               }
            }

            PerformanceDebug var10000 = this.performanceDebug;
            String var10001 = "resolveItems[" + String.valueOf(var1) + "]";
            int var10003 = var2.size();
            var10000.end(var10001, var3, "requested=" + var10003 + ", resolved=" + var6.size());
            return var6;
         } else {
            this.performanceDebug.end("resolveItems[" + String.valueOf(var1) + "]", var3, "empty storage");
            return List.of();
         }
      } else {
         this.performanceDebug.end("resolveItems[" + String.valueOf(var1) + "]", var3, "empty ids");
         return List.of();
      }
   }

   public List<Item> resolveItemsForPage(StorageType var1, IntList var2, int var3, int var4) {
      long var5 = this.performanceDebug.start();
      if (var2 != null && !var2.isEmpty()) {
         int var7 = var3 * var4;
         if (var7 >= var2.size()) {
            this.performanceDebug.end("resolveItemsForPage[" + String.valueOf(var1) + "]", var5, "page out of range");
            return List.of();
         } else {
            int var8 = Math.min(var7 + var4, var2.size());
            IntArrayList var9 = new IntArrayList(var8 - var7);

            for(int var10 = var7; var10 < var8; ++var10) {
               var9.add(var2.getInt(var10));
            }

            List var11 = this.resolveItems(var1, var9);
            this.performanceDebug.end("resolveItemsForPage[" + String.valueOf(var1) + "]", var5, "page=" + var3 + ", pageSize=" + var4 + ", resolved=" + var11.size() + "/" + var2.size());
            return var11;
         }
      } else {
         this.performanceDebug.end("resolveItemsForPage[" + String.valueOf(var1) + "]", var5, "empty ids");
         return List.of();
      }
   }

   public List<Item> onPlayerOpenMenu(Player var1) {
      IntList var2 = (IntList)this.getCache(var1).get(PlayerCacheKey.ITEMS_LISTED, new IntArrayList());
      return this.resolveItems(StorageType.LISTED, var2);
   }

   private IntList getItemIds(StorageType var1, Predicate<Item> var2, Comparator<Item> var3) {
      long var4 = this.performanceDebug.start();
      Map<Integer, ? extends Item> var6 = this.storageItemsById.get(var1);
      if (var6 != null && !var6.isEmpty()) {
         ArrayList<Item> var7 = new ArrayList<>();
         ArrayList<Item> var8 = new ArrayList<>();

         for(Item var10 : var6.values()) {
            if (var10.isExpired()) {
               var8.add(var10);
            } else if (var2.test(var10)) {
               var7.add(var10);
            }
         }

         if (!var8.isEmpty()) {
            this.auctionExpireService.processExpiredItems(var8, var1);
         }

         if (var3 != null && var7.size() > 1) {
            var7.sort(var3);
         }

         IntArrayList var12 = new IntArrayList(var7.size());

         for(Item var11 : var7) {
            var12.add(var11.getId());
         }

         PerformanceDebug var10000 = this.performanceDebug;
         String var10001 = String.valueOf(var1);
         var10001 = "getItemIds[" + var10001 + "]";
         int var10003 = var6.size();
         var10000.end(var10001, var4, "total=" + var10003 + ", filtered=" + var12.size() + ", expired=" + var8.size() + ", sorted=" + (var3 != null));
         return var12;
      } else {
         this.performanceDebug.end("getItemIds[" + String.valueOf(var1) + "]", var4, "empty");
         return new IntArrayList();
      }
   }

   private void indexItem(StorageType var1, Item var2) {
      Map var3 = this.getIndexFor(var1);
      if (var3 != null) {
         UUID var4 = this.getOwner(var1, var2);
         this.addToIndex(var3, var4, var2.getId());
      }
   }

   private void deindexItem(StorageType var1, Item var2) {
      Map var3 = this.getIndexFor(var1);
      if (var3 != null) {
         UUID var4 = this.getOwner(var1, var2);
         this.removeFromIndex(var3, var4, var2.getId());
      }
   }

   private Map<UUID, IntList> getIndexFor(StorageType var1) {
      Map var10000;
      switch (var1) {
         case LISTED -> var10000 = this.idsListedByOwner;
         case EXPIRED -> var10000 = this.idsExpiredByOwner;
         case PURCHASED -> var10000 = this.idsPurchasedByBuyer;
         default -> var10000 = null;
      }

      return var10000;
   }

   private UUID getOwner(StorageType var1, Item var2) {
      UUID var10000;
      switch (var1) {
         case LISTED:
         case EXPIRED:
            var10000 = var2.getSellerUniqueId();
            break;
         case PURCHASED:
            var10000 = var2.getBuyerUniqueId();
            break;
         default:
            var10000 = null;
      }

      return var10000;
   }

   private void addToIndex(Map<UUID, IntList> var1, UUID var2, int var3) {
      if (var2 != null) {
         ((IntList)var1.computeIfAbsent(var2, (var0) -> new IntArrayList())).add(var3);
      }
   }

   private void removeFromIndex(Map<UUID, IntList> var1, UUID var2, int var3) {
      if (var2 != null) {
         IntList var4 = (IntList)var1.get(var2);
         if (var4 != null) {
            var4.rem(var3);
            if (var4.isEmpty()) {
               var1.remove(var2);
            }

         }
      }
   }

   public PlayerCache getCache(Player var1) {
      return (PlayerCache)this.caches.computeIfAbsent(var1, (var0) -> new ZPlayerCache());
   }

   public void clearPlayersCache(PlayerCacheKey... var1) {
      this.caches.forEach((var1x, var2) -> var2.remove(var1));
   }

   public void clearPlayerCache(Player var1, PlayerCacheKey... var2) {
      this.getCache(var1).remove(var2);
   }

   public void removeCache(Player var1) {
      this.caches.remove(var1);
   }

   public CompletableFuture<Void> removeListedItem(Player var1, Item var2) {
      Configuration var3 = this.plugin.getConfiguration();
      StorageManager var4 = this.plugin.getStorageManager();
      var2.setStatus(ItemStatus.REMOVED);
      this.removeItem(StorageType.LISTED, var2);
      this.updateListedItems(var2, false, var1);
      this.clearPlayerCache(var1, PlayerCacheKey.ITEMS_SELLING, PlayerCacheKey.ITEMS_EXPIRED);
      CompletableFuture var5;
      if (var3.getActions().listed().giveItem() && var2.canReceiveItem(var1)) {
         var5 = var4.updateItem(var2, StorageType.DELETED);
         this.giveItem(var1, var2);
      } else {
         long var6 = var3.getExpireExpiration().getExpiration(var1);
         long var8 = var6 > 0L ? System.currentTimeMillis() + var6 * 1000L : 0L;
         var2.setExpiredAt(new Date(var8));
         this.addItem(StorageType.EXPIRED, var2);
         var5 = var4.updateItem(var2, StorageType.EXPIRED);
      }

      this.message(this.plugin, var1, Message.ITEM_REMOVE_LISTED, new Object[]{"%items%", var2.getItemDisplay()});
      if (var3.getActions().listed().openInventory()) {
         this.openMainAuction(var1, (Integer)this.getCache(var1).get(PlayerCacheKey.CURRENT_PAGE, 1));
      } else {
         this.plugin.getScheduler().runAtEntity(var1, (var1e) -> {
            if (var1.isOnline()) {
               var1.closeInventory();
            }

         });
      }

      this.callEvent(new AuctionRemoveListedItemEvent(var2, var1));
      this.logItemAction(LogType.REMOVE_LISTED, var2, var1, (UUID)null, "removed_from_listed");
      return var5;
   }

   public CompletableFuture<Void> removeSellingItem(Player var1, Item var2) {
      Configuration var3 = this.plugin.getConfiguration();
      StorageManager var4 = this.plugin.getStorageManager();
      var2.setStatus(ItemStatus.DELETED);
      this.removeItem(StorageType.LISTED, var2);
      this.updateListedItems(var2, false, var1);
      this.clearPlayerCache(var1, PlayerCacheKey.ITEMS_SELLING, PlayerCacheKey.ITEMS_EXPIRED);
      CompletableFuture var5 = var4.updateItem(var2, StorageType.DELETED);
      this.giveItem(var1, var2);
      this.message(this.plugin, var1, Message.ITEM_REMOVE_SELLING, new Object[]{"%items%", var2.getItemDisplay()});
      if (var3.getActions().listed().openInventory()) {
         this.updateInventory(var1);
      } else {
         this.plugin.getScheduler().runAtEntity(var1, (var1e) -> {
            if (var1.isOnline()) {
               var1.closeInventory();
            }

         });
      }

      this.callEvent(new AuctionRemoveListedItemEvent(var2, var1));
      this.logItemAction(LogType.REMOVE_SELLING, var2, var1, (UUID)null, "removed_selling_item");
      return var5;
   }

   public CompletableFuture<Void> removeExpiredItem(Player var1, Item var2) {
      Configuration var3 = this.plugin.getConfiguration();
      StorageManager var4 = this.plugin.getStorageManager();
      this.removeItem(StorageType.EXPIRED, var2);
      this.clearPlayerCache(var1, PlayerCacheKey.ITEMS_EXPIRED);
      CompletableFuture var5 = var4.updateItem(var2, StorageType.DELETED);
      this.giveItem(var1, var2);
      this.message(this.plugin, var1, Message.ITEM_REMOVE_EXPIRED, new Object[]{"%items%", var2.getItemDisplay()});
      if (var3.getActions().expired().openInventory()) {
         this.updateInventory(var1);
      } else {
         this.plugin.getScheduler().runAtEntity(var1, (var1e) -> {
            if (var1.isOnline()) {
               var1.closeInventory();
            }

         });
      }

      this.callEvent(new AuctionRemoveExpiredItemEvent(var2, var1));
      this.logItemAction(LogType.REMOVE_EXPIRED, var2, var1, (UUID)null, "removed_expired_item");
      return var5;
   }

   public CompletableFuture<Void> removePurchasedItem(Player var1, Item var2) {
      Configuration var3 = this.plugin.getConfiguration();
      StorageManager var4 = this.plugin.getStorageManager();
      this.removeItem(StorageType.PURCHASED, var2);
      this.clearPlayerCache(var1, PlayerCacheKey.ITEMS_PURCHASED);
      CompletableFuture var5 = var4.updateItem(var2, StorageType.DELETED);
      this.giveItem(var1, var2);
      this.message(this.plugin, var1, Message.ITEM_REMOVE_PURCHASED, new Object[]{"%items%", var2.getItemDisplay()});
      if (var3.getActions().purchased().openInventory()) {
         this.updateInventory(var1);
      } else {
         this.plugin.getScheduler().runAtEntity(var1, (var1e) -> {
            if (var1.isOnline()) {
               var1.closeInventory();
            }

         });
      }

      this.callEvent(new AuctionRemovePurchasedItemEvent(var2, var1));
      this.logItemAction(LogType.REMOVE_PURCHASED, var2, var1, var2.getSellerUniqueId(), "removed_purchased_item");
      return var5;
   }

   public void adminRemoveItem(Player var1, UUID var2, Item var3, StorageType var4) {
      AuctionClusterBridge var5 = this.plugin.getAuctionClusterBridge();
      if (var5 == null) {
         this.plugin.getLogger().severe("Cluster bridge is not initialized");
      } else {
         InventoriesLoader var6 = this.plugin.getInventoriesLoader();
         if (var6 == null) {
            this.plugin.getLogger().severe("Inventories loader is not initialized");
         } else {
            InventoryManager var7 = var6.getInventoryManager();
            if (var7 == null) {
               this.plugin.getLogger().severe("Inventory manager is not initialized");
            } else {
               var5.checkAvailability(var3).thenCompose((var6x) -> {
                  if (!var6x) {
                     this.plugin.getLogger().info("Item is not available");
                     var7.updateInventory(var1);
                     return this.failedFuture(new IllegalStateException("Item introuvable"));
                  } else {
                     return var5.lockItem(var3, var1.getUniqueId(), var4);
                  }
               }).thenCompose((var3x) -> var5.removeItem(var3, var4).thenApply((var1) -> var3x)).thenAccept((var7x) -> {
                  this.removeItem(var4, var3);
                  this.plugin.getStorageManager().updateItem(var3, StorageType.DELETED);
                  this.clearPlayersCache(PlayerCacheKey.ITEMS_LISTED, PlayerCacheKey.ITEMS_EXPIRED, PlayerCacheKey.ITEMS_PURCHASED, PlayerCacheKey.ITEMS_SELLING, PlayerCacheKey.ITEMS_SEARCH);
                  this.giveItem(var1, var3);
                  String var8 = var3.getSellerUniqueId().equals(var2) ? var3.getSellerName() : var3.getBuyerName();
                  this.message(this.plugin, var1, Message.ADMIN_ITEM_REMOVED, new Object[]{"%items%", var3.getItemDisplay(), "%target%", var8 == null ? "unknown" : var8});
                  var7.updateInventory(var1);
                  var5.unlockItem(var3, var7x, var4);
               }).exceptionally((var3x) -> {
                  this.plugin.getLogger().severe("Failed to remove item for admin: " + var3x.getMessage());
                  var7.updateInventory(var1);
                  return null;
               });
            }
         }
      }
   }

   public CompletableFuture<Void> purchaseItem(Player var1, Item var2) {
      if (var2 instanceof AuctionItem var3) {
         return this.purchaseAuctionItem(var1, var3);
      } else {
         return CompletableFuture.<Void>completedFuture(null);
      }
   }

   private CompletableFuture<Void> purchaseAuctionItem(Player var1, AuctionItem var2) {
      AuctionEconomy var3 = var2.getAuctionEconomy();
      BigDecimal var4 = var2.getPrice();
      OfflinePlayer var5 = var2.getSeller();
      StorageManager var6 = this.plugin.getStorageManager();
      Configuration var7 = this.plugin.getConfiguration();
      PlayerCache var8 = this.getCache(var1);
      String var9 = var3.getName();
      EconomyManager var10 = this.plugin.getEconomyManager();
      String var11 = var2.getItemsAsString();
      String var12 = var2.getItemDisplay();
      TaxConfiguration var13 = var3.getTaxConfiguration();
      TaxType var14 = var13.getTaxType();
      TaxResult var15 = TaxResult.disabled(var4);
      List var16 = var2.getItemStacks();
      ItemStack var17 = var16 != null && !var16.isEmpty() ? (ItemStack) var16.getFirst() : null;
      if (var13.isEnabled() && (var14 == TaxType.PURCHASE || var14 == TaxType.BOTH || var14 == TaxType.CAPITALISM)) {
         var15 = var3.calculatePurchaseTax(var1, var4, var17);
         if (var15.isBypassed()) {
            this.message(this.plugin, var1, Message.TAX_EXEMPT, new Object[0]);
         }
      }

      BigDecimal var18;
      BigDecimal var19;
      if (var15.hasTax()) {
         if (var14 == TaxType.CAPITALISM) {
            var18 = var15.finalPrice();
            var19 = var4;
            if (var15.isReduced()) {
               this.message(this.plugin, var1, Message.TAX_REDUCED, new Object[]{"%percentage%", String.format("%.1f", (double)100.0F - var15.reductionPercentage())});
            }

            this.message(var1, Message.TAX_CAPITALISM_INFO, "%tax%", var10.format((AuctionEconomy)var3, var15.taxAmount()), "%percentage%", String.format("%.1f", var15.taxPercentage()));
         } else {
            var18 = var4;
            var19 = var15.finalPrice();
            if (var15.isReduced()) {
               this.message(this.plugin, var1, Message.TAX_REDUCED, new Object[]{"%percentage%", String.format("%.1f", (double)100.0F - var15.reductionPercentage())});
            }

            this.message(var1, Message.TAX_PURCHASE_APPLIED, "%tax%", var10.format((AuctionEconomy)var3, var15.taxAmount()), "%percentage%", String.format("%.1f", var15.taxPercentage()));
         }
      } else {
         var18 = var4;
         var19 = var4;
      }

      var3.withdraw(var1.getUniqueId(), var18, this.args(var3.getWithdrawReason(), new Object[]{"%seller%", var2.getSellerName(), "%items%", var11}));
      AuctionClusterBridge var21 = this.plugin.getAuctionClusterBridge();
      boolean var22 = var5.isOnline();
      boolean var23 = !var3.isAutoClaim() || !var22 && var3.mustBeOnline() || !var22 && var21.isDistributed();
      TransactionStatus var20;
      if (var23) {
         var20 = TransactionStatus.PENDING;
      } else {
         var20 = TransactionStatus.RETRIEVED;
         var3.deposit(var5.getUniqueId(), var19, this.args(var3.getDepositReason(), new Object[]{"%buyer%", var1.getName(), "%items%", var11}));
      }

      var3.get(var1.getUniqueId()).thenAccept((var5x) -> var6.createTransaction(var2, var1.getUniqueId(), var9, var5x.add(var18), var5x, var18.negate(), TransactionStatus.RETRIEVED)).exceptionally((var2x) -> {
         Logger var10000 = this.plugin.getLogger();
         int var10001 = var2.getId();
         var10000.severe("Failed to create buyer transaction for item " + var10001 + ": " + var2x.getMessage());
         return null;
      });
      var3.get(var5.getUniqueId()).thenAccept((var6x) -> var6.createTransaction(var2, var5.getUniqueId(), var9, var6x.subtract(var19), var6x, var19, var20)).exceptionally((var2x) -> {
         Logger var10000 = this.plugin.getLogger();
         int var10001 = var2.getId();
         var10000.severe("Failed to create seller transaction for item " + var10001 + ": " + var2x.getMessage());
         return null;
      });
      if (var5.isOnline()) {
         Player var26 = var5.getPlayer();
         if (var26 != null) {
            this.message(this.plugin, var26, Message.ITEM_BOUGHT_SELLER, new Object[]{"%items%", var12, "%price%", var10.format((AuctionEconomy)var3, var19), "%seller%", var2.getSellerName(), "%buyer%", var1.getName()});
         }
      }

      this.message(var1, Message.ITEM_BOUGHT_BUYER, "%items%", var12, "%price%", var10.format((AuctionEconomy)var3, var18), "%seller%", var2.getSellerName(), "%buyer%", var1.getName());
      var2.setBuyer(var1);
      var2.setStatus(ItemStatus.PURCHASED);
      this.updateListedItems(var2, false, var1);
      this.clearPlayerCache(var1, PlayerCacheKey.ITEMS_PURCHASED);
      if (var5.isOnline()) {
         Player var32 = var5.getPlayer();
         if (var32 != null) {
            this.clearPlayerCache(var32, PlayerCacheKey.ITEMS_SELLING, PlayerCacheKey.HISTORY_DATA, PlayerCacheKey.PENDING_MONEY_DATA);
         }
      }

      this.removeItem(StorageType.LISTED, var2);
      ActionConfiguration.PurchasedConfiguration var33 = var7.getActions().purchased();
      CompletableFuture var27;
      if (var33.giveItem()) {
         var27 = var6.updateItem(var2, StorageType.DELETED);
         this.giveItem(var1, var2);
      } else {
         long var28 = var7.getPurchaseExpiration().getExpiration(var1);
         long var30 = var28 > 0L ? System.currentTimeMillis() + var28 * 1000L : 0L;
         var2.setExpiredAt(new Date(var30));
         this.addItem(StorageType.PURCHASED, var2);
         var27 = var6.updateItem(var2, StorageType.PURCHASED);
      }

      var8.remove(PlayerCacheKey.ITEM_SHOW);
      if (var33.openInventory()) {
         this.openMainAuction(var1, (Integer)var8.get(PlayerCacheKey.CURRENT_PAGE, 1));
      } else {
         this.plugin.getScheduler().runAtEntity(var1, (var1e) -> {
            if (var1.isOnline()) {
               var1.closeInventory();
            }

         });
      }

      this.logItemAction(LogType.PURCHASE, var2, var1, var2.getSellerUniqueId(), "purchase_item", var5.isOnline() ? new Date() : null);
      AuctionPlugin var29 = this.plugin;
      if (var29 instanceof ZAuctionPlugin var34) {
         DiscordWebhookService var35 = var34.getDiscordWebhookService();
         if (var35 != null && var35.isEnabled()) {
            var35.notifyItemPurchased(var1, var2);
         }

         var34.getBroadcastService().broadcastPurchase(var1, var2);
      }

      return var27;
   }

   public void message(Player var1, Message var2, Object... var3) {
      this.message(this.plugin, var1, var2, var3);
   }

   public void giveItem(Player var1, Item var2) {
      if (var2 instanceof AuctionItem var3) {
         for(ItemStack var6 : var3.getItemStacks()) {
            var1.getInventory().addItem(new ItemStack[]{var6}).forEach((var1x, var2x) -> var1.getWorld().dropItem(var1.getLocation(), var2x));
         }
      } else {
         this.plugin.getLogger().severe("give item not implemented");
      }

   }

   public void updateListedItems(Item var1, boolean var2, Player var3) {
      if (this.plugin.getConfiguration().getActions().updateInventoryOnAction()) {
         if (!var2 && var3 != null) {
            this.removeFromCache(var3, var1);
         }

         this.sortedItemsCache.ensureCacheValidAsync().thenRun(() -> {
            for(Player var5 : this.plugin.getServer().getOnlinePlayers()) {
               if (var5 == var3) {
                  return;
               }

               this.plugin.getScheduler().runAtEntity(var5, (var4) -> {
                  Inventory var5x = CompatibilityUtil.getTopInventory(var5);
                  if (var5x != null) {
                     InventoryHolder var6 = var5x.getHolder();
                     if (var6 instanceof InventoryEngine) {
                        InventoryEngine var7 = (InventoryEngine)var6;
                        List var8 = var7.getMenuInventory().getButtons(ListedItemsButton.class);
                        if (var8.isEmpty()) {
                           return;
                        }

                        ListedItemsButton var9 = (ListedItemsButton)var8.getFirst();
                        var9.updateInventory(var5, var7, var1, var2, this);
                     }

                     if (!var2) {
                        this.removeFromCache(var5, var1);
                     }

                  }
               });
            }

         });
      } else {
         if (!var2) {
            for(Player var5 : this.plugin.getServer().getOnlinePlayers()) {
               this.removeFromCache(var5, var1);
            }
         }

      }
   }

   private void removeFromCache(Player var1, Item var2) {
      if (this.caches.containsKey(var1)) {
         PlayerCache var3 = (PlayerCache)this.caches.get(var1);
         IntList var4 = (IntList)var3.get(PlayerCacheKey.ITEMS_LISTED);
         if (var4 != null && !var4.isEmpty()) {
            var4.rem(var2.getId());
         }
      }

   }

   private <T> CompletableFuture<T> failedFuture(Throwable var1) {
      CompletableFuture var2 = new CompletableFuture();
      var2.completeExceptionally(var1);
      return var2;
   }

   private void logItemAction(LogType var1, Item var2, Player var3, UUID var4, String var5, Date var6) {
      StorageManager var7 = this.plugin.getStorageManager();
      AuctionEconomy var8 = var2.getAuctionEconomy();
      String var9 = var8 == null ? null : var8.getName();
      String var10 = null;
      if (var2 instanceof AuctionItem var11) {
         List var12 = var11.getItemStacks();
         if (var12 != null && !var12.isEmpty()) {
            var10 = (String)var12.stream().map(Base64ItemStack::encode).collect(Collectors.joining(";"));
         }
      }

      var7.log(var1, var2.getId(), var3, var4, var10, var2.getPrice(), var9, var5, var6);
   }

   private void logItemAction(LogType var1, Item var2, Player var3, UUID var4, String var5) {
      this.logItemAction(var1, var2, var3, var4, var5, (Date)null);
   }

   private void callEvent(AuctionEvent var1) {
      if (this.plugin.getServer().isPrimaryThread()) {
         var1.callEvent();
      } else {
         this.plugin.getScheduler().runNextTick((var1x) -> var1.callEvent());
      }

   }

   public void updateItemEconomies() {
      EconomyManager var1 = this.plugin.getEconomyManager();
      int var2 = 0;
      int var3 = 0;

      for(StorageType var7 : StorageType.values()) {
         Map<Integer, ? extends fr.maxlego08.zauctionhouse.api.item.Item> var8 = this.storageItemsById.get(var7);
         if (var8 != null && !var8.isEmpty()) {
            for(fr.maxlego08.zauctionhouse.api.item.Item var10 : var8.values()) {
               String var11 = var10.getEconomyName();
               Optional<fr.maxlego08.zauctionhouse.api.economy.AuctionEconomy> var12 = var1.getEconomy(var11);
               if (var12.isPresent()) {
                  var10.setAuctionEconomy(var12.get());
                  ++var2;
               } else {
                  this.plugin.getLogger().warning("Economy '" + var11 + "' not found for item ID " + var10.getId() + " in " + var7.name() + ". The item will keep its old economy reference.");
                  ++var3;
               }
            }
         }
      }

      if (var2 > 0 || var3 > 0) {
         this.plugin.getLogger().info("Economy update completed: " + var2 + " items updated, " + var3 + " items with missing economy.");
      }

   }

   public void shutdown() {
      if (this.sortedItemsCache != null) {
         this.sortedItemsCache.shutdown();
      }

   }

   public void startSearch(Player var1, String var2) {
      String var3 = var2 == null ? null : var2.trim();
      if (var3 != null && !var3.isBlank()) {
         PlayerCache var4 = this.getCache(var1);
         var4.set(PlayerCacheKey.SEARCH_QUERY, var3);
         var4.remove(PlayerCacheKey.ITEMS_SEARCH);
         var4.remove(PlayerCacheKey.ITEMS_LISTED);
         this.message(var1, Message.SEARCH_SEARCHING, "%query%", var3);
         this.openAuctionInventory(var1, 1);
      } else {
         this.clearSearch(var1);
         this.openAuctionInventory(var1, 1);
      }
   }

   public void clearSearch(Player var1) {
      PlayerCache var2 = this.getCache(var1);
      var2.remove(PlayerCacheKey.SEARCH_QUERY);
      var2.remove(PlayerCacheKey.ITEMS_SEARCH);
      var2.remove(PlayerCacheKey.ITEMS_LISTED);
      this.message(var1, Message.SEARCH_CLEARED);
   }

   public void removeAllExpiredItems(Player var1) {
      Configuration var2 = this.plugin.getConfiguration();
      StorageManager var3 = this.plugin.getStorageManager();
      boolean var4 = var2.getActions().expired().freeSpace();
      ArrayList<fr.maxlego08.zauctionhouse.api.item.Item> var5 = new ArrayList<>(this.getExpiredItems(var1));
      if (!var5.isEmpty()) {
         int var6 = 0;

         for(Item var8 : var5) {
            if (var4 && !var8.canReceiveItem(var1)) {
               break;
            }

            this.removeItem(StorageType.EXPIRED, var8);
            var3.updateItem(var8, StorageType.DELETED);
            this.giveItem(var1, var8);
            this.callEvent(new AuctionRemoveExpiredItemEvent(var8, var1));
            this.logItemAction(LogType.REMOVE_EXPIRED, var8, var1, (UUID)null, "removed_expired_item_bulk");
            ++var6;
         }

         if (var6 > 0) {
            this.clearPlayerCache(var1, PlayerCacheKey.ITEMS_EXPIRED);
            this.message(this.plugin, var1, Message.REMOVE_ALL_ITEMS, new Object[]{"%amount%", String.valueOf(var6)});
         }

         if (var2.getActions().expired().openInventory()) {
            this.updateInventory(var1);
         } else {
            this.plugin.getScheduler().runAtEntity(var1, (var1e) -> {
               if (var1.isOnline()) {
                  var1.closeInventory();
               }

            });
         }

      }
   }

   public void removeAllSellingItems(Player var1) {
      Configuration var2 = this.plugin.getConfiguration();
      StorageManager var3 = this.plugin.getStorageManager();
      boolean var4 = var2.getActions().selling().freeSpace();
      AuctionClusterBridge var5 = this.plugin.getAuctionClusterBridge();
      ArrayList<fr.maxlego08.zauctionhouse.api.item.Item> var6 = new ArrayList<>(this.getPlayerSellingItems(var1));
      if (!var6.isEmpty()) {
         int var7 = 0;

         for(Item var9 : var6) {
            if (var9.getStatus() == ItemStatus.AVAILABLE) {
               if (var4 && !var9.canReceiveItem(var1)) {
                  break;
               }

               var9.setStatus(ItemStatus.DELETED);
               this.removeItem(StorageType.LISTED, var9);
               this.updateListedItems(var9, false, var1);
               var3.updateItem(var9, StorageType.DELETED);
               this.giveItem(var1, var9);
               var5.removeItem(var9, StorageType.LISTED);
               this.callEvent(new AuctionRemoveListedItemEvent(var9, var1));
               this.logItemAction(LogType.REMOVE_SELLING, var9, var1, (UUID)null, "removed_selling_item_bulk");
               ++var7;
            }
         }

         if (var7 > 0) {
            this.clearPlayerCache(var1, PlayerCacheKey.ITEMS_SELLING, PlayerCacheKey.ITEMS_EXPIRED);
            this.message(this.plugin, var1, Message.REMOVE_ALL_ITEMS, new Object[]{"%amount%", String.valueOf(var7)});
         }

         this.updateInventory(var1);
      }
   }

   public void removeAllPurchasedItems(Player var1) {
      Configuration var2 = this.plugin.getConfiguration();
      StorageManager var3 = this.plugin.getStorageManager();
      boolean var4 = var2.getActions().purchased().freeSpace();
      ArrayList<fr.maxlego08.zauctionhouse.api.item.Item> var5 = new ArrayList<>(this.getPurchasedItems(var1));
      if (!var5.isEmpty()) {
         int var6 = 0;

         for(Item var8 : var5) {
            if (var4 && !var8.canReceiveItem(var1)) {
               break;
            }

            this.removeItem(StorageType.PURCHASED, var8);
            var3.updateItem(var8, StorageType.DELETED);
            this.giveItem(var1, var8);
            this.callEvent(new AuctionRemovePurchasedItemEvent(var8, var1));
            this.logItemAction(LogType.REMOVE_PURCHASED, var8, var1, var8.getSellerUniqueId(), "removed_purchased_item_bulk");
            ++var6;
         }

         if (var6 > 0) {
            this.clearPlayerCache(var1, PlayerCacheKey.ITEMS_PURCHASED);
            this.message(this.plugin, var1, Message.REMOVE_ALL_ITEMS, new Object[]{"%amount%", String.valueOf(var6)});
         }

         if (var2.getActions().purchased().openInventory()) {
            this.updateInventory(var1);
         } else {
            this.plugin.getScheduler().runAtEntity(var1, (var1e) -> {
               if (var1.isOnline()) {
                  var1.closeInventory();
               }

            });
         }

      }
   }
}
