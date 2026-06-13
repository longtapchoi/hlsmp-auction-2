package fr.maxlego08.zauctionhouse.storage;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.economy.AuctionEconomy;
import fr.maxlego08.zauctionhouse.api.item.Item;
import fr.maxlego08.zauctionhouse.api.item.ItemType;
import fr.maxlego08.zauctionhouse.api.item.StorageType;
import fr.maxlego08.zauctionhouse.api.item.items.AuctionItem;
import fr.maxlego08.zauctionhouse.api.log.LogType;
import fr.maxlego08.zauctionhouse.api.storage.Repository;
import fr.maxlego08.zauctionhouse.api.storage.StorageManager;
import fr.maxlego08.zauctionhouse.api.storage.dto.ItemDTO;
import fr.maxlego08.zauctionhouse.api.storage.dto.LogDTO;
import fr.maxlego08.zauctionhouse.api.storage.dto.PlayerDTO;
import fr.maxlego08.zauctionhouse.api.transaction.TransactionStatus;
import fr.maxlego08.zauctionhouse.libs.sarah.DatabaseConfiguration;
import fr.maxlego08.zauctionhouse.libs.sarah.DatabaseConnection;
import fr.maxlego08.zauctionhouse.libs.sarah.HikariDatabaseConnection;
import fr.maxlego08.zauctionhouse.libs.sarah.MigrationManager;
import fr.maxlego08.zauctionhouse.libs.sarah.SqliteConnection;
import fr.maxlego08.zauctionhouse.libs.sarah.database.DatabaseType;
import fr.maxlego08.zauctionhouse.libs.sarah.logger.JULogger;
import fr.maxlego08.zauctionhouse.libs.sarah.logger.Logger;
import fr.maxlego08.zauctionhouse.storage.migrations.CreateAuctionItemMigration;
import fr.maxlego08.zauctionhouse.storage.migrations.CreateItemMigration;
import fr.maxlego08.zauctionhouse.storage.migrations.CreateLogsMigration;
import fr.maxlego08.zauctionhouse.storage.migrations.CreateOptionsMigration;
import fr.maxlego08.zauctionhouse.storage.migrations.CreatePlayerMigration;
import fr.maxlego08.zauctionhouse.storage.migrations.CreateTransactionsMigration;
import fr.maxlego08.zauctionhouse.storage.repository.Repositories;
import fr.maxlego08.zauctionhouse.storage.repository.repositories.AuctionItemRepository;
import fr.maxlego08.zauctionhouse.storage.repository.repositories.ItemRepository;
import fr.maxlego08.zauctionhouse.storage.repository.repositories.LogRepository;
import fr.maxlego08.zauctionhouse.storage.repository.repositories.OptionRepository;
import fr.maxlego08.zauctionhouse.storage.repository.repositories.PlayerRepository;
import fr.maxlego08.zauctionhouse.storage.repository.repositories.TransactionRepository;
import fr.maxlego08.zauctionhouse.utils.ItemLoaderUtils;
import fr.maxlego08.zauctionhouse.utils.PerformanceDebug;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ZStorageManager extends ItemLoaderUtils implements StorageManager {
   private final AuctionPlugin plugin;
   private AuctionLoader auctionLoader;
   private Repositories repositories;
   private DatabaseConnection databaseConnection;

   public ZStorageManager(AuctionPlugin var1) {
      this.plugin = var1;
   }

   public boolean onEnable() {
      Logger var1 = JULogger.from(this.plugin.getLogger());
      DatabaseConfiguration var2 = this.getDatabaseConfiguration();
      boolean var3 = var2.getDatabaseType() == DatabaseType.SQLITE;
      this.databaseConnection = (DatabaseConnection)(var3 ? new SqliteConnection(var2, this.plugin.getDataFolder(), var1) : new HikariDatabaseConnection(var2, var1));
      if (!this.databaseConnection.isValid()) {
         this.plugin.getLogger().severe("Unable to connect to database !");
         Bukkit.getPluginManager().disablePlugin(this.plugin);
         return false;
      } else {
         this.plugin.getLogger().info("The database connection is valid !");
         MigrationManager.setMigrationTableName("zauctionhousev4_migrations");
         MigrationManager.setDatabaseConfiguration(var2);
         MigrationManager.registerMigration(new CreatePlayerMigration());
         MigrationManager.registerMigration(new CreateItemMigration());
         MigrationManager.registerMigration(new CreateAuctionItemMigration());
         MigrationManager.registerMigration(new CreateTransactionsMigration());
         MigrationManager.registerMigration(new CreateLogsMigration());
         MigrationManager.registerMigration(new CreateOptionsMigration());
         this.repositories = new Repositories(this.plugin, this.databaseConnection);
         this.repositories.register(PlayerRepository.class);
         this.repositories.register(ItemRepository.class);
         this.repositories.register(AuctionItemRepository.class);
         this.repositories.register(LogRepository.class);
         this.repositories.register(TransactionRepository.class);
         this.repositories.register(OptionRepository.class);
         MigrationManager.execute(this.databaseConnection, var1);
         return true;
      }
   }

   public void onDisable() {
      this.databaseConnection.disconnect();
   }

   public void loadItems() {
      this.auctionLoader = new AuctionLoader(this.plugin, this);
      this.auctionLoader.loadItems();
   }

   public <T extends Repository> T with(Class<T> var1) {
      return (T)this.repositories.getTable(var1);
   }

   public DatabaseConnection getDatabaseConnection() {
      return this.databaseConnection;
   }

   protected void async(Runnable var1) {
      this.plugin.getScheduler().runAsync((var1x) -> var1.run());
   }

   private @NotNull DatabaseConfiguration getDatabaseConfiguration() {
      FileConfiguration var1 = this.plugin.getConfig();
      DatabaseType var2 = DatabaseType.valueOf(var1.getString("storage-type", "SQLITE").toUpperCase());
      GlobalDatabaseConfiguration var3 = new GlobalDatabaseConfiguration(var1);
      String var4 = var3.getTablePrefix();
      String var5 = var3.getHost();
      int var6 = var3.getPort();
      String var7 = var3.getUser();
      String var8 = var3.getPassword();
      String var9 = var3.getDatabase();
      boolean var10 = var3.isDebug();
      return new DatabaseConfiguration(var4, var7, var8, var6, var5, var9, var10, var2);
   }

   public void upsertPlayer(Player var1) {
      this.async(() -> ((PlayerRepository)this.with(PlayerRepository.class)).upsertPlayer(var1));
   }

   public void upsertPlayer(UUID var1, String var2) {
      this.async(() -> ((PlayerRepository)this.with(PlayerRepository.class)).upsertPlayer(var1, var2));
   }

   public CompletableFuture<AuctionItem> createAuctionItem(Player var1, BigDecimal var2, long var3, List<ItemStack> var5, AuctionEconomy var6) {
      return CompletableFuture.supplyAsync(() -> {
         int var7 = ((ItemRepository)this.with(ItemRepository.class)).create(var1, ItemType.AUCTION, var2, var3, var6);
         return ((AuctionItemRepository)this.with(AuctionItemRepository.class)).create(var1, var7, var2, var3, var5, var6);
      }, this.plugin.getExecutorService());
   }

   public CompletableFuture<AuctionItem> createAuctionItem(UUID var1, String var2, BigDecimal var3, long var4, List<ItemStack> var6, AuctionEconomy var7) {
      return CompletableFuture.supplyAsync(() -> {
         int var8 = ((ItemRepository)this.with(ItemRepository.class)).create(var1, ItemType.AUCTION, var3, var4, var7);
         return ((AuctionItemRepository)this.with(AuctionItemRepository.class)).create(var1, var2, var8, var3, var4, var6, var7);
      }, this.plugin.getExecutorService());
   }

   public CompletableFuture<Void> updateItem(Item var1, StorageType var2) {
      return CompletableFuture.runAsync(() -> ((ItemRepository)this.with(ItemRepository.class)).updateItem(var1, var2), this.plugin.getExecutorService());
   }

   public CompletableFuture<Void> updateItems(Map<StorageType, List<Item>> var1) {
      return CompletableFuture.runAsync(() -> ((ItemRepository)this.with(ItemRepository.class)).updateItems(var1), this.plugin.getExecutorService());
   }

   public void log(LogType var1, int var2, Player var3, UUID var4, String var5, BigDecimal var6, String var7, String var8, Date var9) {
      this.async(() -> ((LogRepository)this.with(LogRepository.class)).createLog(var1, var2, var3.getUniqueId(), var4, var5, var6, var7, var8, var9));
   }

   public void createTransaction(Item var1, UUID var2, String var3, BigDecimal var4, BigDecimal var5, BigDecimal var6, TransactionStatus var7) {
      this.async(() -> ((TransactionRepository)this.with(TransactionRepository.class)).create(var1, var2, var3, var4, var5, var6, var7));
   }

   public CompletableFuture<Item> selectItem(int var1) {
      return CompletableFuture.supplyAsync(() -> {
         Optional var2 = ((ItemRepository)this.with(ItemRepository.class)).select(var1);
         if (var2.isEmpty()) {
            return null;
         } else {
            ItemDTO var3 = (ItemDTO)var2.get();
            String var4 = ((PlayerRepository)this.with(PlayerRepository.class)).select(var3.seller_unique_id());
            Optional var5 = this.plugin.getEconomyManager().getEconomy(var3.economy_name());
            if (var5.isEmpty()) {
               java.util.logging.Logger var10000 = this.plugin.getLogger();
               String var10001 = var3.economy_name();
               var10000.severe("Impossible to find the economy " + var10001 + " for auction item id " + var3.id() + ", skip it...");
               return null;
            } else {
               switch (var3.item_type()) {
                  case AUCTION:
                     List var6 = ((AuctionItemRepository)this.with(AuctionItemRepository.class)).select(List.of(String.valueOf(var3.id())));
                     AuctionItem var7 = this.createAuctionItem(this.plugin, var3, var4, var6, (AuctionEconomy)var5.get());
                     if (var3.buyer_unique_id() != null) {
                        var7.setBuyer(var3.buyer_unique_id(), ((PlayerRepository)this.with(PlayerRepository.class)).select(var3.buyer_unique_id()));
                     }

                     return var7;
                  case BID:
                  case RENT:
                  default:
                     return null;
               }
            }
         }
      });
   }

   public CompletableFuture<UUID> findUniqueId(String var1) {
      return CompletableFuture.supplyAsync(() -> ((PlayerRepository)this.with(PlayerRepository.class)).selectByName(var1));
   }

   public String getPlayerName(UUID var1) {
      return ((PlayerRepository)this.with(PlayerRepository.class)).select(var1);
   }

   public List<LogDTO> selectSalesHistory(UUID var1) {
      return ((LogRepository)this.plugin.getStorageManager().with(LogRepository.class)).selectSalesHistory(var1);
   }

   public List<Item> selectItems(List<Integer> var1) {
      if (var1.isEmpty()) {
         return new ArrayList();
      } else {
         List var2 = ((ItemRepository)this.with(ItemRepository.class)).select(var1.stream().map(String::valueOf).toList());
         if (var2.isEmpty()) {
            return new ArrayList();
         } else {
            List var3 = var2.stream().flatMap((var0) -> Stream.of(var0.seller_unique_id(), var0.buyer_unique_id())).filter(Objects::nonNull).map(UUID::toString).distinct().toList();
            Map var4 = this.selectPlayers(var3);
            ArrayList var5 = new ArrayList();
            PerformanceDebug var6 = new PerformanceDebug(this.plugin);
            this.createItems(this.plugin, var4, var2, var6, (var1x, var2x) -> var5.add(var2x));
            return var5;
         }
      }
   }

   public Map<UUID, String> selectPlayers(List<String> var1) {
      return (Map)((PlayerRepository)this.with(PlayerRepository.class)).select(var1).stream().collect(Collectors.toMap(PlayerDTO::unique_id, PlayerDTO::name));
   }

   public void markPurchaseLogAsRead(int var1, UUID var2) {
      this.async(() -> ((LogRepository)this.with(LogRepository.class)).markPurchaseLogsAsReadByItem(var1, var2));
   }
}
