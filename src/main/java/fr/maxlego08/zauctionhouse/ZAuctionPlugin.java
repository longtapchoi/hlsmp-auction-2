package fr.maxlego08.zauctionhouse;

import fr.maxlego08.zauctionhouse.api.AuctionManager;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.InventoriesLoader;
import fr.maxlego08.zauctionhouse.api.category.CategoryManager;
import fr.maxlego08.zauctionhouse.api.cluster.AuctionClusterBridge;
import fr.maxlego08.zauctionhouse.api.command.CommandManager;
import fr.maxlego08.zauctionhouse.api.configuration.Configuration;
import fr.maxlego08.zauctionhouse.api.configuration.ConfigurationFile;
import fr.maxlego08.zauctionhouse.api.economy.EconomyManager;
import fr.maxlego08.zauctionhouse.api.hooks.itemcontent.ItemContentManager;
import fr.maxlego08.zauctionhouse.api.hooks.itemcontent.ItemContentProvider;
import fr.maxlego08.zauctionhouse.api.hooks.permission.OfflinePermission;
import fr.maxlego08.zauctionhouse.api.messages.Message;
import fr.maxlego08.zauctionhouse.api.migration.MigrationProvider;
import fr.maxlego08.zauctionhouse.api.migration.MigrationRegistry;
import fr.maxlego08.zauctionhouse.api.placeholders.Placeholder;
import fr.maxlego08.zauctionhouse.api.placeholders.PlaceholderRegister;
import fr.maxlego08.zauctionhouse.api.rules.ItemRuleManager;
import fr.maxlego08.zauctionhouse.api.rules.loader.RuleLoaderRegistry;
import fr.maxlego08.zauctionhouse.api.storage.StorageManager;
import fr.maxlego08.zauctionhouse.api.utils.Plugins;
import fr.maxlego08.zauctionhouse.category.ZCategoryManager;
import fr.maxlego08.zauctionhouse.cluster.LocalAuctionClusterBridge;
import fr.maxlego08.zauctionhouse.command.ZCommandManager;
import fr.maxlego08.zauctionhouse.command.commands.CommandAuction;
import fr.maxlego08.zauctionhouse.configuration.MainConfiguration;
import fr.maxlego08.zauctionhouse.discord.DiscordWebhookService;
import fr.maxlego08.zauctionhouse.economy.ZEconomyManager;
import fr.maxlego08.zauctionhouse.hooks.itemcontent.VanillaShulkerContentProvider;
import fr.maxlego08.zauctionhouse.hooks.itemcontent.ZItemContentManager;
import fr.maxlego08.zauctionhouse.hooks.permissions.EmptyOfflinePermission;
import fr.maxlego08.zauctionhouse.hooks.permissions.LuckPermsOfflinePermission;
import fr.maxlego08.zauctionhouse.libs.folialib.FoliaLib;
import fr.maxlego08.zauctionhouse.libs.folialib.impl.PlatformScheduler;
import fr.maxlego08.zauctionhouse.listeners.PlayerListener;
import fr.maxlego08.zauctionhouse.loader.MessageLoader;
import fr.maxlego08.zauctionhouse.loader.ZInventoriesLoader;
import fr.maxlego08.zauctionhouse.migration.ZMigrationRegistry;
import fr.maxlego08.zauctionhouse.migration.v3.V3MigrationProvider;
import fr.maxlego08.zauctionhouse.permissions.PermissionRegistrar;
import fr.maxlego08.zauctionhouse.placeholder.DistantPlaceholder;
import fr.maxlego08.zauctionhouse.placeholder.LocalPlaceholder;
import fr.maxlego08.zauctionhouse.placeholder.placeholders.GlobalPlaceholders;
import fr.maxlego08.zauctionhouse.placeholder.placeholders.OptionPlaceholders;
import fr.maxlego08.zauctionhouse.placeholder.placeholders.PlayerPlaceholders;
import fr.maxlego08.zauctionhouse.rule.ZItemRuleManager;
import fr.maxlego08.zauctionhouse.rule.ZRuleLoaderRegistry;
import fr.maxlego08.zauctionhouse.search.SignSearchListener;
import fr.maxlego08.zauctionhouse.toggle.AuctionToggleManager;
import fr.maxlego08.zauctionhouse.command.commands.CommandAuctionToggle;
import fr.maxlego08.zauctionhouse.services.BroadcastService;
import fr.maxlego08.zauctionhouse.storage.ZStorageManager;
import fr.maxlego08.zauctionhouse.utils.LocaleHelper;
import fr.maxlego08.zauctionhouse.utils.MessageUtils;
import fr.maxlego08.zauctionhouse.utils.Metrics;
import fr.maxlego08.zauctionhouse.utils.VersionChecker;
import fr.maxlego08.zauctionhouse.utils.documentation.DocumentationGenerator;
import fr.maxlego08.zauctionhouse.utils.yaml.YamlUpdater;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public class ZAuctionPlugin extends JavaPlugin implements AuctionPlugin {
   private final StorageManager storageManager = new ZStorageManager(this);
   private final Configuration configuration = new MainConfiguration(this);
   private final ConfigurationFile messageLoader = new MessageLoader(this);
   private final ZCommandManager commandManager = new ZCommandManager(this);
   private final AuctionManager auctionManager = new ZAuctionManager(this);
   private final EconomyManager economyManager = new ZEconomyManager(this);
   private final ExecutorService asyncExecutor = Executors.newFixedThreadPool(4);
   private final Placeholder placeholder = new LocalPlaceholder(this);
   private final ZRuleLoaderRegistry ruleLoaderRegistry = new ZRuleLoaderRegistry(this);
   private final ItemRuleManager itemRuleManager;
   private final CategoryManager categoryManager;
   private final YamlUpdater yamlUpdater;
   private final MigrationRegistry migrationRegistry;
   private final PermissionRegistrar permissionRegistrar;
   private final ItemContentManager itemContentManager;
   private final MessageHelper messageHelper;
   private LocaleHelper localeHelper;
   private InventoriesLoader inventoriesLoader;
   private SignSearchListener chatSearchListener;
   private AuctionToggleManager auctionToggleManager;
   private BroadcastService broadcastService;
   private DiscordWebhookService discordWebhookService;
   private VersionChecker versionChecker;
   private boolean isEnabled;
   private PlatformScheduler platformScheduler;
   private AuctionClusterBridge auctionClusterBridge;
   private OfflinePermission offlinePermission;

   public ZAuctionPlugin() {
      this.itemRuleManager = new ZItemRuleManager(this, this.ruleLoaderRegistry);
      this.categoryManager = new ZCategoryManager(this, this.ruleLoaderRegistry);
      this.yamlUpdater = new YamlUpdater(this);
      this.migrationRegistry = new ZMigrationRegistry(this);
      this.permissionRegistrar = new PermissionRegistrar(this);
      this.itemContentManager = new ZItemContentManager();
      this.messageHelper = new MessageHelper();
      this.isEnabled = false;
      this.auctionClusterBridge = new LocalAuctionClusterBridge();
      this.offlinePermission = new EmptyOfflinePermission();
   }

   public void onEnable() {
      loadConfig0();
      File var1 = this.getDataFolder();
      if (!var1.exists()) {
         var1.mkdirs();
      }

      this.saveLanguageFile();
      String var2 = this.loadLanguageConfiguration();
      this.localeHelper = new LocaleHelper(this.getLogger(), var2);
      this.saveFile("config.yml", true);
      FoliaLib var3 = new FoliaLib(this);
      this.platformScheduler = var3.getScheduler();
      this.inventoriesLoader = new ZInventoriesLoader(this);
      this.ruleLoaderRegistry.registerDefaultLoaders();
      this.registerCustomItemLoaders();
      this.loadFiles();
      this.permissionRegistrar.register();
      this.auctionManager.setupSortedItemsCache();
      if (this.storageManager.onEnable()) {
         this.registerDefaultMigrationProviders();
         this.broadcastService = new BroadcastService(this);
         this.discordWebhookService = new DiscordWebhookService(this);
         this.chatSearchListener = new SignSearchListener(this);
         this.addListener(new PlayerListener(this));
         this.addListener(this.chatSearchListener);
         this.auctionToggleManager = new AuctionToggleManager(this);
         if (this.getCommand("auctiontoggle") != null) {
            this.getCommand("auctiontoggle").setExecutor(new CommandAuctionToggle(this));
         }
         ArrayList var4 = new ArrayList(this.getConfig().getStringList("commands.main-command.aliases"));
         String var5;
         if (!var4.isEmpty()) {
            var5 = (String)var4.removeFirst();
            var4.add("zauctionhouse");
         } else {
            var5 = "zauctionhouse";
         }

         this.commandManager.registerCommand(this, var5, new CommandAuction(this), var4);
         this.inventoriesLoader.load();
         this.storageManager.loadItems();
         this.registerPlaceholders();
         this.registerHooks();
         new Metrics(this, 5326);
         if (this.getConfig().getBoolean("enable-version-checker", true)) {
            this.versionChecker = new VersionChecker(this, 1);
            this.versionChecker.useLastVersion();
         }

         DocumentationGenerator var6 = new DocumentationGenerator(this);
         var6.generate(this.commandManager.getCommands(), ((LocalPlaceholder)this.placeholder).getAutoPlaceholders());
         this.getServer().getServicesManager().register(AuctionPlugin.class, this, this, ServicePriority.Highest);
         this.isEnabled = true;
         this.getLogger().info("zAuctionHouse has just been loaded successfully!");
      }
   }

   public void onDisable() {
      if (this.isEnabled) {
         if (this.versionChecker != null) {
            this.versionChecker.unregister();
         }

         this.auctionManager.shutdown();
         this.asyncExecutor.shutdown();

         try {
            if (!this.asyncExecutor.awaitTermination(5L, TimeUnit.SECONDS)) {
               this.asyncExecutor.shutdownNow();
               if (!this.asyncExecutor.awaitTermination(2L, TimeUnit.SECONDS)) {
                  this.getLogger().warning("ExecutorService did not terminate properly");
               }
            }
         } catch (InterruptedException var2) {
            this.asyncExecutor.shutdownNow();
            Thread.currentThread().interrupt();
         }

         this.storageManager.onDisable();
      }
   }

   public void reload() {
      if (!(new File(this.getDataFolder(), "config.yml")).exists()) {
         this.saveFile("config.yml", true);
      }

      this.reloadConfig();
      String var1 = this.loadLanguageConfiguration();
      this.localeHelper = new LocaleHelper(this.getLogger(), var1);
      this.loadFiles();
      this.permissionRegistrar.register();
      this.inventoriesLoader.reload();
      this.auctionManager.updateItemEconomies();
   }

   private void loadFiles() {
      this.configuration.load();
      this.messageLoader.load();
      this.economyManager.loadEconomies();
      this.itemRuleManager.loadRules();
      this.categoryManager.loadCategories();
      if (this.discordWebhookService != null) {
         this.discordWebhookService.loadConfiguration();
      }

   }

   private void registerPlaceholders() {
      DistantPlaceholder var1 = new DistantPlaceholder(this, this.placeholder);
      var1.register();
      this.registerPlaceholder(PlayerPlaceholders.class);
      this.registerPlaceholder(GlobalPlaceholders.class);
      this.registerPlaceholder(OptionPlaceholders.class);
   }

   private void registerHooks() {
      if (this.isEnable(Plugins.LUCKPERMS)) {
         this.offlinePermission = new LuckPermsOfflinePermission();
         this.getLogger().info("LuckPerms has been enabled successfully!");
      }

      this.registerItemContentProviders();
   }

   private void registerItemContentProviders() {
      this.itemContentManager.registerProvider(new VanillaShulkerContentProvider());
      if (this.isEnable(Plugins.AXSHULKERS)) {
         this.registerOptionalItemContentProvider("fr.maxlego08.zauctionhouse.hooks.axshulkers.AxShulkersContentProvider", "AxShulkers");
      }

   }

   private void registerOptionalItemContentProvider(String var1, String var2) {
      try {
         Class var3 = Class.forName(var1);
         ItemContentProvider var4 = (ItemContentProvider)var3.getDeclaredConstructor().newInstance();
         this.itemContentManager.registerProvider(var4);
         this.getLogger().info(var2 + " item content provider registered.");
      } catch (Exception var5) {
         this.getLogger().log(Level.WARNING, "Failed to register " + var2 + " item content provider.", var5);
      }

   }

   private void registerCustomItemLoaders() {
      if (this.isEnable(Plugins.ORAXEN)) {
         this.ruleLoaderRegistry.registerOraxenLoader();
         this.getLogger().info("Oraxen rule loader registered.");
      }

      if (this.isEnable(Plugins.NEXO)) {
         this.ruleLoaderRegistry.registerNexoLoader();
         this.getLogger().info("Nexo rule loader registered.");
      }

      if (this.isEnable(Plugins.MMOITEMS)) {
         this.ruleLoaderRegistry.registerMMOItemsLoader();
         this.getLogger().info("MMOItems rule loader registered.");
      }

      if (this.isEnable(Plugins.EXECUTABLE_ITEMS)) {
         this.ruleLoaderRegistry.registerExecutableItemsLoader();
         this.getLogger().info("ExecutableItems rule loader registered.");
      }

      if (this.isEnable(Plugins.SLIMEFUN)) {
         this.ruleLoaderRegistry.registerSlimefunLoader();
         this.getLogger().info("Slimefun rule loader registered.");
      }

      if (this.isEnable(Plugins.HEADDATABASE)) {
         this.ruleLoaderRegistry.registerHeadDatabaseLoader();
         this.getLogger().info("HeadDatabase rule loader registered.");
      }

      if (this.isEnable(Plugins.NOVA)) {
         this.ruleLoaderRegistry.registerNovaLoader();
         this.getLogger().info("Nova rule loader registered.");
      }

      if (this.isEnable(Plugins.DENIZEN)) {
         this.ruleLoaderRegistry.registerDenizenLoader();
         this.getLogger().info("Denizen rule loader registered.");
      }

      if (this.isEnable(Plugins.CRAFTENGINE)) {
         this.ruleLoaderRegistry.registerCraftEngineLoader();
         this.getLogger().info("CraftEngine rule loader registered.");
      }

      if (this.isEnable(Plugins.EXECUTABLE_BLOCKS)) {
         this.ruleLoaderRegistry.registerExecutableBlocksLoader();
         this.getLogger().info("ExecutableBlocks rule loader registered.");
      }

   }

   private void registerDefaultMigrationProviders() {
      this.migrationRegistry.register(new V3MigrationProvider());
      this.registerOptionalMigrationProvider("fr.maxlego08.zauctionhouse.hooks.zelauction.ZelAuctionMigrationProvider", "ZelAuction");
   }

   private void registerOptionalMigrationProvider(String var1, String var2) {
      try {
         Class var3 = Class.forName(var1);
         MigrationProvider var4 = (MigrationProvider)var3.getDeclaredConstructor().newInstance();
         this.migrationRegistry.register(var4);
         this.getLogger().info(var2 + " migration provider registered.");
      } catch (ClassNotFoundException var5) {
      } catch (Exception var6) {
         this.getLogger().warning("Failed to register " + var2 + " migration provider: " + var6.getMessage());
      }

   }

   public PlatformScheduler getScheduler() {
      return this.platformScheduler;
   }

   public StorageManager getStorageManager() {
      return this.storageManager;
   }

   public Configuration getConfiguration() {
      return this.configuration;
   }

   public AuctionManager getAuctionManager() {
      return this.auctionManager;
   }

   public CommandManager getCommandManager() {
      return this.commandManager;
   }

   public void sendMessage(CommandSender var1, Message var2, Object... var3) {
      this.messageHelper.send(this, var1, var2, var3);
   }

   public InventoriesLoader getInventoriesLoader() {
      return this.inventoriesLoader;
   }

   public EconomyManager getEconomyManager() {
      return this.economyManager;
   }

   public ExecutorService getExecutorService() {
      return this.asyncExecutor;
   }

   public AuctionClusterBridge getAuctionClusterBridge() {
      return this.auctionClusterBridge;
   }

   public void setAuctionClusterBridge(AuctionClusterBridge var1) {
      this.auctionClusterBridge = var1;
   }

   public ItemRuleManager getItemRuleManager() {
      return this.itemRuleManager;
   }

   public CategoryManager getCategoryManager() {
      return this.categoryManager;
   }

   public RuleLoaderRegistry getRuleLoaderRegistry() {
      return this.ruleLoaderRegistry;
   }

   public MigrationRegistry getMigrationRegistry() {
      return this.migrationRegistry;
   }

   public ItemContentManager getItemContentManager() {
      return this.itemContentManager;
   }

   public OfflinePermission getOfflinePermission() {
      return this.offlinePermission;
   }

   public void setOfflinePermission(OfflinePermission var1) {
      this.offlinePermission = var1;
   }

   public Placeholder getPlaceholder() {
      return this.placeholder;
   }

   public SignSearchListener getChatSearchListener() {
      return this.chatSearchListener;
   }

   public AuctionToggleManager getAuctionToggleManager() {
      return this.auctionToggleManager;
   }

   public BroadcastService getBroadcastService() {
      return this.broadcastService;
   }

   public DiscordWebhookService getDiscordWebhookService() {
      return this.discordWebhookService;
   }

   public YamlUpdater getYamlUpdater() {
      return this.yamlUpdater;
   }

   private void addListener(Listener var1) {
      this.getServer().getPluginManager().registerEvents(var1, this);
   }

   public boolean resourceExist(String var1) {
      if (var1 != null && !var1.isEmpty()) {
         var1 = var1.replace('\\', '/');
         InputStream var2 = this.getResource(var1);
         return var2 != null;
      } else {
         return false;
      }
   }

   public void saveResource(String var1, String var2, boolean var3) {
      if (var1 != null && !var1.isEmpty()) {
         var1 = var1.replace('\\', '/');
         InputStream var4 = this.getResource(var1);
         if (var4 == null) {
            throw new IllegalArgumentException("The embedded resource '" + var1 + "' cannot be found in " + String.valueOf(this.getFile()));
         } else {
            File var5 = new File(this.getDataFolder(), var2);
            int var6 = var2.lastIndexOf(47);
            File var7 = new File(this.getDataFolder(), var2.substring(0, Math.max(var6, 0)));
            if (!var7.exists()) {
               var7.mkdirs();
            }

            if (var5.exists() && !var3) {
               Logger var10000 = this.getLogger();
               Level var10001 = Level.WARNING;
               String var10002 = var5.getName();
               var10000.log(var10001, "Could not save " + var10002 + " to " + String.valueOf(var5) + " because " + var5.getName() + " already exists.");
            } else {
               try {
                  OutputStream var8 = Files.newOutputStream(var5.toPath());

                  try {
                     InputStream var9 = var4;

                     try {
                        byte[] var10 = new byte[1024];

                        int var11;
                        while((var11 = var4.read(var10)) > 0) {
                           var8.write(var10, 0, var11);
                        }
                     } catch (Throwable var14) {
                        if (var4 != null) {
                           try {
                              var9.close();
                           } catch (Throwable var13) {
                              var14.addSuppressed(var13);
                           }
                        }

                        throw var14;
                     }

                     if (var4 != null) {
                        var4.close();
                     }
                  } catch (Throwable var15) {
                     if (var8 != null) {
                        try {
                           var8.close();
                        } catch (Throwable var12) {
                           var15.addSuppressed(var12);
                        }
                     }

                     throw var15;
                  }

                  if (var8 != null) {
                     var8.close();
                  }
               } catch (IOException var16) {
                  this.getLogger().log(Level.SEVERE, "Could not save " + var5.getName() + " to " + String.valueOf(var5), var16);
               }
            }

         }
      } else {
         throw new IllegalArgumentException("ResourcePath cannot be null or empty");
      }
   }

   public void saveOrUpdateConfiguration(String var1, String var2, boolean var3) {
      File var4 = new File(this.getDataFolder(), var2);
      if (!var4.exists()) {
         this.saveResource(var1, var2, false);
      } else {
         this.yamlUpdater.update(var1, var2);
      }
   }

   private void saveLanguageFile() {
      File var1 = new File(this.getDataFolder(), "language.yml");
      if (!var1.exists()) {
         this.saveResource("language.yml", "language.yml", false);
      } else {
         this.yamlUpdater.update("language.yml", "language.yml");
      }

   }

   private String loadLanguageConfiguration() {
      File var1 = new File(this.getDataFolder(), "language.yml");
      if (!var1.exists()) {
         return null;
      } else {
         YamlConfiguration var2 = YamlConfiguration.loadConfiguration(var1);
         String var3 = var2.getString("language", "auto");
         return var3 != null && !var3.equalsIgnoreCase("auto") ? var3.toLowerCase() : null;
      }
   }

   public void saveFile(String var1, boolean var2) {
      this.saveFile(var1, var1, var2);
   }

   public void saveFile(String var1, String var2, boolean var3) {
      String var10000 = this.localeHelper.getLanguage();
      String var4 = var10000 + "/" + var1;
      String var5 = var1;
      if (this.resourceExist(var4)) {
         var5 = var4;
      }

      if (var3) {
         this.saveOrUpdateConfiguration(var5, var2, false);
      } else {
         this.saveResource(var5, var2, false);
      }

   }

   private <T extends PlaceholderRegister> T registerPlaceholder(Class<T> var1) {
      try {
         PlaceholderRegister var2 = (PlaceholderRegister)var1.getConstructor().newInstance();
         var2.register(this.placeholder, this);
         return (T)var2;
      } catch (Exception var3) {
         var3.printStackTrace();
         return null;
      }
   }

   public boolean isEnable(Plugins var1) {
      Plugin var2 = this.getPlugin(var1);
      return var2 != null && var2.isEnabled();
   }

   public boolean isActive(Plugins var1) {
      Plugin var2 = this.getPlugin(var1);
      return var2 != null;
   }

   protected Plugin getPlugin(Plugins var1) {
      return Bukkit.getPluginManager().getPlugin(var1.getName());
   }

   private static class MessageHelper extends MessageUtils {
      void send(AuctionPlugin var1, CommandSender var2, Message var3, Object... var4) {
         this.message(var1, var2, var3, var4);
      }
   }
}
