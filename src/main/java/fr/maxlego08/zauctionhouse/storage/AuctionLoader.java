package fr.maxlego08.zauctionhouse.storage;

import fr.maxlego08.zauctionhouse.api.AuctionManager;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.category.CategoryManager;
import fr.maxlego08.zauctionhouse.api.economy.EconomyManager;
import fr.maxlego08.zauctionhouse.api.storage.StorageManager;
import fr.maxlego08.zauctionhouse.api.storage.dto.PlayerDTO;
import fr.maxlego08.zauctionhouse.storage.repository.repositories.ItemRepository;
import fr.maxlego08.zauctionhouse.storage.repository.repositories.PlayerRepository;
import fr.maxlego08.zauctionhouse.utils.ItemLoaderUtils;
import fr.maxlego08.zauctionhouse.utils.PerformanceDebug;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@SuppressWarnings({"unchecked", "rawtypes"})
public class AuctionLoader extends ItemLoaderUtils {
   private final AuctionPlugin plugin;
   private final Logger logger;
   private final AuctionManager auctionManager;
   private final StorageManager storageManager;
   private final EconomyManager economyManager;
   private final PerformanceDebug performanceDebug;

   public AuctionLoader(AuctionPlugin var1, StorageManager var2) {
      this.plugin = var1;
      this.logger = var1.getLogger();
      this.storageManager = var2;
      this.auctionManager = var1.getAuctionManager();
      this.economyManager = var1.getEconomyManager();
      this.performanceDebug = new PerformanceDebug(var1);
   }

   public void loadItems() {
      long var1 = this.performanceDebug.start();
      long var3 = this.performanceDebug.start();
      Map var5 = (Map)((PlayerRepository)this.storageManager.with(PlayerRepository.class)).select().stream().collect(Collectors.toMap(PlayerDTO::unique_id, PlayerDTO::name));
      this.performanceDebug.end("loadItems.loadPlayers", var3, "count=" + var5.size());
      this.plugin.getLogger().info("Loaded " + var5.size() + " players successfully");
      CategoryManager var6 = this.plugin.getCategoryManager();
      long var7 = this.performanceDebug.start();
      List var9 = ((ItemRepository)this.storageManager.with(ItemRepository.class)).select();
      this.performanceDebug.end("loadItems.loadItemsFromDB", var7, "count=" + var9.size());
      AuctionPlugin var10001 = this.plugin;
      PerformanceDebug var10004 = this.performanceDebug;
      AuctionManager var10005 = this.auctionManager;
      Objects.requireNonNull(var10005);
      ItemLoaderUtils.Result var10 = this.createItems(var10001, var5, var9, var10004, var10005::addItem);
      PerformanceDebug var10000 = this.performanceDebug;
      int var10003 = var5.size();
      var10000.end("loadItems.total", var1, "players=" + var10003 + ", items=" + var10.amount() + ", auctionItems=" + var10.auctionItems());
      Logger var13 = this.logger;
      int var14 = var10.amount();
      var13.info("Loaded " + var14 + " items successfully (" + var10.auctionItems() + " total)");
      long var11 = this.performanceDebug.start();
      this.auctionManager.rebuildSortedItemsCache();
      this.performanceDebug.end("loadItems.rebuildSortedItemsCache", var11, "scheduled async rebuild");
   }
}
