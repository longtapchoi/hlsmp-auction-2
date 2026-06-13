package fr.maxlego08.zauctionhouse.api;

import fr.maxlego08.zauctionhouse.api.category.CategoryManager;
import fr.maxlego08.zauctionhouse.api.cluster.AuctionClusterBridge;
import fr.maxlego08.zauctionhouse.api.command.CommandManager;
import fr.maxlego08.zauctionhouse.api.configuration.Configuration;
import fr.maxlego08.zauctionhouse.api.economy.EconomyManager;
import fr.maxlego08.zauctionhouse.api.hooks.itemcontent.ItemContentManager;
import fr.maxlego08.zauctionhouse.api.hooks.permission.OfflinePermission;
import fr.maxlego08.zauctionhouse.api.messages.Message;
import fr.maxlego08.zauctionhouse.api.migration.MigrationRegistry;
import fr.maxlego08.zauctionhouse.api.placeholders.Placeholder;
import fr.maxlego08.zauctionhouse.api.rules.ItemRuleManager;
import fr.maxlego08.zauctionhouse.api.rules.loader.RuleLoaderRegistry;
import fr.maxlego08.zauctionhouse.api.storage.StorageManager;
import fr.maxlego08.zauctionhouse.libs.folialib.impl.PlatformScheduler;
import java.util.concurrent.ExecutorService;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public interface AuctionPlugin extends Plugin {
   void reload();

   PlatformScheduler getScheduler();

   StorageManager getStorageManager();

   Configuration getConfiguration();

   AuctionManager getAuctionManager();

   CommandManager getCommandManager();

   void sendMessage(CommandSender var1, Message var2, Object... var3);

   InventoriesLoader getInventoriesLoader();

   EconomyManager getEconomyManager();

   ExecutorService getExecutorService();

   Placeholder getPlaceholder();

   AuctionClusterBridge getAuctionClusterBridge();

   ItemRuleManager getItemRuleManager();

   CategoryManager getCategoryManager();

   RuleLoaderRegistry getRuleLoaderRegistry();

   MigrationRegistry getMigrationRegistry();

   void setAuctionClusterBridge(AuctionClusterBridge var1);

   ItemContentManager getItemContentManager();

   OfflinePermission getOfflinePermission();

   void setOfflinePermission(OfflinePermission var1);

   boolean resourceExist(String var1);

   void saveResource(String var1, String var2, boolean var3);

   void saveOrUpdateConfiguration(String var1, String var2, boolean var3);

   void saveFile(String var1, boolean var2);

   void saveFile(String var1, String var2, boolean var3);
}
