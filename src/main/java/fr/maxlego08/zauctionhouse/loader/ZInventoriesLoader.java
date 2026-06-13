package fr.maxlego08.zauctionhouse.loader;

import fr.maxlego08.menu.api.ButtonManager;
import fr.maxlego08.menu.api.Inventory;
import fr.maxlego08.menu.api.InventoryManager;
import fr.maxlego08.menu.api.exceptions.InventoryException;
import fr.maxlego08.menu.api.loader.NoneLoader;
import fr.maxlego08.menu.api.pattern.PatternManager;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.InventoriesLoader;
import fr.maxlego08.zauctionhouse.api.inventories.Inventories;
import fr.maxlego08.zauctionhouse.api.messages.Message;
import fr.maxlego08.zauctionhouse.buttons.ClearSearchButton;
import fr.maxlego08.zauctionhouse.buttons.ItemContentButton;
import fr.maxlego08.zauctionhouse.buttons.RemoveAllExpiredButton;
import fr.maxlego08.zauctionhouse.buttons.RemoveAllPurchasedButton;
import fr.maxlego08.zauctionhouse.buttons.RemoveAllSellingButton;
import fr.maxlego08.zauctionhouse.buttons.SearchButton;
import fr.maxlego08.zauctionhouse.buttons.ShowButton;
import fr.maxlego08.zauctionhouse.buttons.admin.AdminExpiredItemsButton;
import fr.maxlego08.zauctionhouse.buttons.admin.AdminLogsButton;
import fr.maxlego08.zauctionhouse.buttons.admin.AdminPurchasedItemsButton;
import fr.maxlego08.zauctionhouse.buttons.admin.AdminSellingItemsButton;
import fr.maxlego08.zauctionhouse.buttons.admin.AdminTransactionsButton;
import fr.maxlego08.zauctionhouse.buttons.admin.history.AdminHistoryMainButton;
import fr.maxlego08.zauctionhouse.buttons.admin.history.AdminHistoryMainExpiredButton;
import fr.maxlego08.zauctionhouse.buttons.admin.history.AdminHistoryMainLogsButton;
import fr.maxlego08.zauctionhouse.buttons.admin.history.AdminHistoryMainPurchasedButton;
import fr.maxlego08.zauctionhouse.buttons.admin.history.AdminHistoryMainSellingButton;
import fr.maxlego08.zauctionhouse.buttons.admin.history.AdminHistoryMainTransactionsButton;
import fr.maxlego08.zauctionhouse.buttons.confirm.ConfirmPurchaseButton;
import fr.maxlego08.zauctionhouse.buttons.confirm.ConfirmRemoveListedButton;
import fr.maxlego08.zauctionhouse.buttons.history.HistoryItemsButton;
import fr.maxlego08.zauctionhouse.buttons.inventory.ExpiredInventoryButton;
import fr.maxlego08.zauctionhouse.buttons.inventory.HistoryInventoryButton;
import fr.maxlego08.zauctionhouse.buttons.inventory.PurchasedInventoryButton;
import fr.maxlego08.zauctionhouse.buttons.inventory.SellingInventoryButton;
import fr.maxlego08.zauctionhouse.buttons.list.ExpiredItemsButton;
import fr.maxlego08.zauctionhouse.buttons.list.ListedItemsButton;
import fr.maxlego08.zauctionhouse.buttons.list.PurchasedItemsButton;
import fr.maxlego08.zauctionhouse.buttons.list.SellingItemsButton;
import fr.maxlego08.zauctionhouse.buttons.sell.SellCancelButton;
import fr.maxlego08.zauctionhouse.buttons.sell.SellConfirmButton;
import fr.maxlego08.zauctionhouse.buttons.sell.SellEconomyButton;
import fr.maxlego08.zauctionhouse.buttons.shulker.ShulkerInfoButton;
import fr.maxlego08.zauctionhouse.buttons.shulker.ShulkerOpenButton;
import fr.maxlego08.zauctionhouse.loader.buttons.CategoryButtonLoader;
import fr.maxlego08.zauctionhouse.loader.buttons.CategorySwitcherLoader;
import fr.maxlego08.zauctionhouse.loader.buttons.ClaimButtonLoader;
import fr.maxlego08.zauctionhouse.loader.buttons.CombinedItemsLoader;
import fr.maxlego08.zauctionhouse.loader.buttons.EmptySlotLoader;
import fr.maxlego08.zauctionhouse.loader.buttons.HistorySortLoader;
import fr.maxlego08.zauctionhouse.loader.buttons.LoadingSlotLoader;
import fr.maxlego08.zauctionhouse.loader.buttons.LogDateFilterLoader;
import fr.maxlego08.zauctionhouse.loader.buttons.LogTypeFilterLoader;
import fr.maxlego08.zauctionhouse.loader.buttons.OptionToggleLoader;
import fr.maxlego08.zauctionhouse.loader.buttons.RefreshLoader;
import fr.maxlego08.zauctionhouse.loader.buttons.SellLimitLoader;
import fr.maxlego08.zauctionhouse.loader.buttons.SellPriceButtonLoader;
import fr.maxlego08.zauctionhouse.loader.buttons.SellShowItemLoader;
import fr.maxlego08.zauctionhouse.loader.buttons.ShulkerContentLoader;
import fr.maxlego08.zauctionhouse.loader.buttons.ShulkerNavigationLoader;
import fr.maxlego08.zauctionhouse.loader.buttons.SortLoader;
import fr.maxlego08.zauctionhouse.loader.buttons.TransactionDateFilterLoader;
import fr.maxlego08.zauctionhouse.loader.buttons.TransactionStatusFilterLoader;
import fr.maxlego08.zauctionhouse.loader.permissibles.CategoryPermissibleLoader;
import fr.maxlego08.zauctionhouse.utils.PerformanceDebug;
import fr.maxlego08.zauctionhouse.utils.ZUtils;
import java.io.File;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class ZInventoriesLoader extends ZUtils implements InventoriesLoader {
   private final AuctionPlugin plugin;
   private final PatternManager patternManager;
   private final ButtonManager buttonManager;
   private final InventoryManager inventoryManager;
   private final PerformanceDebug performanceDebug;

   public ZInventoriesLoader(AuctionPlugin var1) {
      this.plugin = var1;
      this.buttonManager = (ButtonManager)this.getProvider(ButtonManager.class);
      this.inventoryManager = (InventoryManager)this.getProvider(InventoryManager.class);
      this.patternManager = (PatternManager)this.getProvider(PatternManager.class);
      this.performanceDebug = new PerformanceDebug(var1);
   }

   private <T> T getProvider(Class<T> var1) {
      RegisteredServiceProvider var2 = this.plugin.getServer().getServicesManager().getRegistration(var1);
      if (var2 == null) {
         this.plugin.getLogger().severe("Unable to retrieve the provider " + String.valueOf(var1));
         return null;
      } else {
         return (T)var2.getProvider();
      }
   }

   public void loadInventories() {
      File var1 = new File(this.plugin.getDataFolder(), "inventories");
      if (!var1.exists()) {
         var1.mkdir();
         this.createInventoriesFile();
      }

      this.files(var1, this::loadInventory);
   }

   public void loadInventory(File var1) {
      try {
         this.inventoryManager.loadInventory(this.plugin, var1);
      } catch (InventoryException var3) {
         throw new RuntimeException(var3);
      }
   }

   public void loadPatterns() {
      File var1 = new File(this.plugin.getDataFolder(), "patterns");
      if (!var1.exists()) {
         var1.mkdir();
         this.createPatternFiles();
      }

      this.files(var1, this::loadPattern);
   }

   public void loadPattern(File var1) {
      try {
         this.patternManager.loadPattern(var1);
      } catch (InventoryException var3) {
         throw new RuntimeException(var3);
      }
   }

   public void loadButtons() {
      this.buttonManager.unregisters(this.plugin);
      this.buttonManager.registerPermissible(new CategoryPermissibleLoader(this.plugin));
      this.buttonManager.register(new EmptySlotLoader(this.plugin, ListedItemsButton.class, "ZAUCTIONHOUSE_LISTED_ITEMS"));
      this.buttonManager.register(new EmptySlotLoader(this.plugin, ExpiredItemsButton.class, "ZAUCTIONHOUSE_EXPIRED_ITEMS"));
      this.buttonManager.register(new EmptySlotLoader(this.plugin, SellingItemsButton.class, "ZAUCTIONHOUSE_SELLING_ITEMS"));
      this.buttonManager.register(new EmptySlotLoader(this.plugin, PurchasedItemsButton.class, "ZAUCTIONHOUSE_PURCHASED_ITEMS"));
      this.buttonManager.register(new NoneLoader(this.plugin, ItemContentButton.class, "ZAUCTIONHOUSE_ITEM_CONTENT"));
      this.buttonManager.register(new CombinedItemsLoader(this.plugin));
      this.buttonManager.register(new NoneLoader(this.plugin, RemoveAllExpiredButton.class, "ZAUCTIONHOUSE_REMOVE_ALL_EXPIRED"));
      this.buttonManager.register(new NoneLoader(this.plugin, RemoveAllSellingButton.class, "ZAUCTIONHOUSE_REMOVE_ALL_SELLING"));
      this.buttonManager.register(new NoneLoader(this.plugin, RemoveAllPurchasedButton.class, "ZAUCTIONHOUSE_REMOVE_ALL_PURCHASED"));
      this.buttonManager.register(new NoneLoader(this.plugin, ExpiredInventoryButton.class, "ZAUCTIONHOUSE_EXPIRED_INVENTORY"));
      this.buttonManager.register(new NoneLoader(this.plugin, SellingInventoryButton.class, "ZAUCTIONHOUSE_SELLING_INVENTORY"));
      this.buttonManager.register(new NoneLoader(this.plugin, PurchasedInventoryButton.class, "ZAUCTIONHOUSE_PURCHASED_INVENTORY"));
      this.buttonManager.register(new NoneLoader(this.plugin, HistoryInventoryButton.class, "ZAUCTIONHOUSE_HISTORY_INVENTORY"));
      this.buttonManager.register(new NoneLoader(this.plugin, ShowButton.class, "ZAUCTIONHOUSE_SHOW"));
      this.buttonManager.register(new NoneLoader(this.plugin, ConfirmRemoveListedButton.class, "ZAUCTIONHOUSE_CONFIRM_REMOVE_LISTED"));
      this.buttonManager.register(new NoneLoader(this.plugin, ConfirmPurchaseButton.class, "ZAUCTIONHOUSE_CONFIRM_PURCHASE"));
      this.buttonManager.register(new NoneLoader(this.plugin, SellCancelButton.class, "ZAUCTIONHOUSE_SELL_CANCEL"));
      this.buttonManager.register(new NoneLoader(this.plugin, SellConfirmButton.class, "ZAUCTIONHOUSE_SELL_CONFIRM"));
      this.buttonManager.register(new NoneLoader(this.plugin, SellEconomyButton.class, "ZAUCTIONHOUSE_SELL_ECONOMY"));
      this.buttonManager.register(new LoadingSlotLoader(this.plugin, HistoryItemsButton.class, "ZAUCTIONHOUSE_HISTORY_ITEMS"));
      this.buttonManager.register(new SellShowItemLoader(this.plugin));
      this.buttonManager.register(new SellPriceButtonLoader(this.plugin));
      this.buttonManager.register(new HistorySortLoader(this.plugin));
      this.buttonManager.register(new NoneLoader(this.plugin, AdminSellingItemsButton.class, "ZAUCTIONHOUSE_ADMIN_SELLING_ITEMS"));
      this.buttonManager.register(new NoneLoader(this.plugin, AdminExpiredItemsButton.class, "ZAUCTIONHOUSE_ADMIN_EXPIRED_ITEMS"));
      this.buttonManager.register(new NoneLoader(this.plugin, AdminPurchasedItemsButton.class, "ZAUCTIONHOUSE_ADMIN_PURCHASED_ITEMS"));
      this.buttonManager.register(new LoadingSlotLoader(this.plugin, AdminLogsButton.class, "ZAUCTIONHOUSE_ADMIN_LOGS"));
      this.buttonManager.register(new LoadingSlotLoader(this.plugin, AdminTransactionsButton.class, "ZAUCTIONHOUSE_ADMIN_TRANSACTIONS"));
      this.buttonManager.register(new LogTypeFilterLoader(this.plugin));
      this.buttonManager.register(new LogDateFilterLoader(this.plugin));
      this.buttonManager.register(new TransactionStatusFilterLoader(this.plugin));
      this.buttonManager.register(new TransactionDateFilterLoader(this.plugin));
      this.buttonManager.register(new NoneLoader(this.plugin, AdminHistoryMainButton.class, "ZAUCTIONHOUSE_ADMIN_HISTORY_MAIN"));
      this.buttonManager.register(new NoneLoader(this.plugin, AdminHistoryMainLogsButton.class, "ZAUCTIONHOUSE_ADMIN_HISTORY_LOGS"));
      this.buttonManager.register(new NoneLoader(this.plugin, AdminHistoryMainSellingButton.class, "ZAUCTIONHOUSE_ADMIN_HISTORY_SELLING"));
      this.buttonManager.register(new NoneLoader(this.plugin, AdminHistoryMainPurchasedButton.class, "ZAUCTIONHOUSE_ADMIN_HISTORY_PURCHASED"));
      this.buttonManager.register(new NoneLoader(this.plugin, AdminHistoryMainTransactionsButton.class, "ZAUCTIONHOUSE_ADMIN_HISTORY_TRANSACTIONS"));
      this.buttonManager.register(new NoneLoader(this.plugin, AdminHistoryMainExpiredButton.class, "ZAUCTIONHOUSE_ADMIN_HISTORY_EXPIRED"));
      this.buttonManager.register(new SortLoader(this.plugin, this.inventoryManager));
      this.buttonManager.register(new RefreshLoader(this.plugin, this.inventoryManager));
      this.buttonManager.register(new CategoryButtonLoader(this.plugin));
      this.buttonManager.register(new CategorySwitcherLoader(this.plugin));
      this.buttonManager.register(new SellLimitLoader(this.plugin));
      this.buttonManager.register(new ClaimButtonLoader(this.plugin, this.inventoryManager));
      this.buttonManager.register(new NoneLoader(this.plugin, SearchButton.class, "ZAUCTIONHOUSE_SEARCH"));
      this.buttonManager.register(new NoneLoader(this.plugin, ClearSearchButton.class, "ZAUCTIONHOUSE_CLEAR_SEARCH"));
      this.buttonManager.register(new OptionToggleLoader(this.plugin));
      this.buttonManager.register(new NoneLoader(this.plugin, ShulkerOpenButton.class, "ZAUCTIONHOUSE_SHULKER_OPEN"));
      this.buttonManager.register(new ShulkerContentLoader(this.plugin));
      this.buttonManager.register(new NoneLoader(this.plugin, ShulkerInfoButton.class, "ZAUCTIONHOUSE_SHULKER_INFO"));
      this.buttonManager.register(new ShulkerNavigationLoader(this.plugin));
   }

   public void load() {
      this.loadButtons();
      this.loadPatterns();
      this.loadInventories();
   }

   public void reload() {
      this.inventoryManager.deleteInventories(this.plugin);
      this.loadPatterns();
      this.loadInventories();
   }

   private void createPatternFiles() {
      this.copyFiles("patterns", "decoration", "pagination", "back");
   }

   private void createInventoriesFile() {
      this.copyFiles("inventories", "auction", "expired-items", "selling-items", "sell-inventory", "categories", "purchased-items", "history", "admin/admin-selling-items", "admin/admin-expired-items", "admin/admin-purchased-items", "admin/admin-logs", "admin/admin-transactions", "admin/admin-history-main", "combined-items", "confirms/remove-confirm", "confirms/purchase-confirm", "confirms/purchase-inventory-confirm", "confirms/remove-inventory-confirm", "shulker-content", "options");
   }

   private void copyFiles(String var1, String... var2) {
      for(String var6 : var2) {
         this.plugin.saveFile(var1 + "/" + var6 + ".yml", false);
      }

   }

   public InventoryManager getInventoryManager() {
      return this.inventoryManager;
   }

   public ButtonManager getButtonManager() {
      return this.buttonManager;
   }

   public void openInventory(Player var1, Inventories var2) {
      this.openInventory(var1, var2, 1);
   }

   public void openInventory(Player var1, Inventories var2, int var3) {
      long var4 = this.performanceDebug.start();
      Optional var6 = this.inventoryManager.getInventory(this.plugin, var2.getFileName());
      if (var6.isEmpty()) {
         this.plugin.getLogger().severe("Unable to open inventory " + var2.getFileName() + ", inventory not found");
         this.message(this.plugin, var1, Message.INVENTORY_NOT_FOUND, new Object[]{"%inventory-name%", var2.getFileName()});
      } else {
         Inventory var7 = (Inventory)var6.get();
         this.plugin.getScheduler().runAtEntity(var1, (var4x) -> this.inventoryManager.openInventoryWithOldInventories(var1, var7, var3));
         PerformanceDebug var10000 = this.performanceDebug;
         String var10001 = "openInventory." + var2.getFileName();
         String var10003 = var1.getName();
         var10000.end(var10001, var4, "for=" + var10003 + ", page=" + var3);
      }
   }
}
