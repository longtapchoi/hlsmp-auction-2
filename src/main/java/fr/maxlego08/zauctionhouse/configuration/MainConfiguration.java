package fr.maxlego08.zauctionhouse.configuration;

import fr.maxlego08.menu.api.utils.TypedMapAccessor;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.configuration.Configuration;
import fr.maxlego08.zauctionhouse.api.configuration.commands.CommandArgumentConfiguration;
import fr.maxlego08.zauctionhouse.api.configuration.commands.CommandConfiguration;
import fr.maxlego08.zauctionhouse.api.configuration.commands.InventoryCommandConfiguration;
import fr.maxlego08.zauctionhouse.api.configuration.commands.SimpleArgumentConfiguration;
import fr.maxlego08.zauctionhouse.api.configuration.commands.SimpleCommandConfiguration;
import fr.maxlego08.zauctionhouse.api.configuration.records.ActionConfiguration;
import fr.maxlego08.zauctionhouse.api.configuration.records.AutoClaimConfiguration;
import fr.maxlego08.zauctionhouse.api.configuration.records.BroadcastConfiguration;
import fr.maxlego08.zauctionhouse.api.configuration.records.CooldownConfiguration;
import fr.maxlego08.zauctionhouse.api.configuration.records.ExpirationConfiguration;
import fr.maxlego08.zauctionhouse.api.configuration.records.ItemDisplayConfiguration;
import fr.maxlego08.zauctionhouse.api.configuration.records.ItemLoreConfiguration;
import fr.maxlego08.zauctionhouse.api.configuration.records.NumberMultiplicationConfiguration;
import fr.maxlego08.zauctionhouse.api.configuration.records.PerformanceConfiguration;
import fr.maxlego08.zauctionhouse.api.configuration.records.PerformanceDebugConfiguration;
import fr.maxlego08.zauctionhouse.api.configuration.records.PermissionConfiguration;
import fr.maxlego08.zauctionhouse.api.configuration.records.SalesNotificationConfiguration;
import fr.maxlego08.zauctionhouse.api.configuration.records.SearchFilterConfiguration;
import fr.maxlego08.zauctionhouse.api.configuration.records.SortConfiguration;
import fr.maxlego08.zauctionhouse.api.configuration.records.SpecialItemsConfiguration;
import fr.maxlego08.zauctionhouse.api.configuration.records.TimeConfiguration;
import fr.maxlego08.zauctionhouse.api.configuration.records.WorldConfiguration;
import fr.maxlego08.zauctionhouse.api.messages.MessageColor;
import fr.maxlego08.zauctionhouse.utils.YamlLoader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import org.bukkit.configuration.file.FileConfiguration;

public class MainConfiguration extends YamlLoader implements Configuration {
   private final AuctionPlugin plugin;
   private final List<MessageColor> messageColors = new ArrayList();
   private boolean enableDebug;
   private boolean enablePerformanceDebug;
   private String serverName;
   private SimpleDateFormat dateFormat;
   private NumberMultiplicationConfiguration numberMultiplicationConfiguration;
   private ExpirationConfiguration sellExpiration;
   private ExpirationConfiguration rentExpiration;
   private ExpirationConfiguration bidExpiration;
   private ExpirationConfiguration purchaseExpiration;
   private ExpirationConfiguration expireExpiration;
   private ItemLoreConfiguration itemLoreConfiguration;
   private TimeConfiguration timeConfiguration;
   private ActionConfiguration actionConfiguration;
   private SortConfiguration sortConfiguration;
   private PermissionConfiguration permissionConfiguration;
   private WorldConfiguration worldConfiguration;
   private SpecialItemsConfiguration specialItemsConfiguration;
   private ItemDisplayConfiguration itemDisplayConfiguration;
   private PerformanceDebugConfiguration performanceDebugConfiguration;
   private AutoClaimConfiguration autoClaimConfiguration;
   private SalesNotificationConfiguration salesNotificationConfiguration;
   private BroadcastConfiguration broadcastConfiguration;
   private PerformanceConfiguration performanceConfiguration;
   private SearchFilterConfiguration searchFilterConfiguration;
   private CooldownConfiguration cooldownConfiguration;
   private List<InventoryCommandConfiguration> inventoryCommandConfigurations;
   private boolean sellInventoryEnabled;

   public MainConfiguration(AuctionPlugin var1) {
      this.plugin = var1;
   }

   public void load() {
      FileConfiguration var1 = this.plugin.getConfig();
      super.loadYamlConfirmation(this.plugin, var1);
      this.numberMultiplicationConfiguration = NumberMultiplicationConfiguration.of(this.plugin, var1);
      this.sellExpiration = ExpirationConfiguration.of(this.plugin, var1, "expiration.auction.");
      this.rentExpiration = ExpirationConfiguration.of(this.plugin, var1, "expiration.rent.");
      this.bidExpiration = ExpirationConfiguration.of(this.plugin, var1, "expiration.bid.");
      this.purchaseExpiration = ExpirationConfiguration.of(this.plugin, var1, "expiration.purchase.");
      this.expireExpiration = ExpirationConfiguration.of(this.plugin, var1, "expiration.expire.");
      this.itemLoreConfiguration = ItemLoreConfiguration.of(this.plugin, var1);
      this.timeConfiguration = TimeConfiguration.of(this.plugin, var1);
      this.actionConfiguration = ActionConfiguration.of(this.plugin, var1);
      this.sortConfiguration = SortConfiguration.of(this.plugin, var1);
      this.permissionConfiguration = PermissionConfiguration.of(this.plugin, var1);
      this.worldConfiguration = WorldConfiguration.of(this.plugin, var1);
      this.specialItemsConfiguration = SpecialItemsConfiguration.of(this.plugin, var1);
      this.itemDisplayConfiguration = ItemDisplayConfiguration.of(this.plugin, var1);
      this.performanceDebugConfiguration = PerformanceDebugConfiguration.of(this.plugin, var1);
      this.autoClaimConfiguration = AutoClaimConfiguration.of(this.plugin, var1);
      this.salesNotificationConfiguration = SalesNotificationConfiguration.of(this.plugin, var1);
      this.broadcastConfiguration = BroadcastConfiguration.of(this.plugin, var1);
      this.performanceConfiguration = PerformanceConfiguration.of(this.plugin, var1);
      this.searchFilterConfiguration = SearchFilterConfiguration.of(this.plugin, var1);
      this.cooldownConfiguration = CooldownConfiguration.of(this.plugin, var1);
      this.inventoryCommandConfigurations = InventoryCommandConfiguration.of(this.plugin, var1);
      this.dateFormat = new SimpleDateFormat(var1.getString("date-format", "dd/MM/yyyy HH:mm:ss"));
      String var2 = var1.getString("timezone", "auto");
      if (var2 != null && !var2.equalsIgnoreCase("auto")) {
         this.dateFormat.setTimeZone(TimeZone.getTimeZone(var2));
      }

      this.sellInventoryEnabled = var1.getBoolean("commands.sell.enable-sell-inventory", false);
      this.validateConfigurations();
   }

   private void validateConfigurations() {
      if (this.sellExpiration == null) {
         this.plugin.getLogger().severe("Failed to load sell expiration configuration, plugin may not work correctly");
      }

      if (this.expireExpiration == null) {
         this.plugin.getLogger().severe("Failed to load expire expiration configuration, plugin may not work correctly");
      }

      if (this.actionConfiguration == null) {
         this.plugin.getLogger().severe("Failed to load action configuration, plugin may not work correctly");
      }

      if (this.sortConfiguration == null) {
         this.plugin.getLogger().severe("Failed to load sort configuration, plugin may not work correctly");
      }

      if (this.performanceConfiguration == null) {
         this.plugin.getLogger().severe("Failed to load performance configuration, plugin may not work correctly");
      }

   }

   public boolean isEnableDebug() {
      return this.enableDebug;
   }

   public boolean isEnablePerformanceDebug() {
      return this.enablePerformanceDebug;
   }

   public String getServerName() {
      return this.serverName;
   }

   public List<MessageColor> getMessageColors() {
      return this.messageColors;
   }

   public NumberMultiplicationConfiguration getNumberMultiplicationConfiguration() {
      return this.numberMultiplicationConfiguration;
   }

   public ExpirationConfiguration getSellExpiration() {
      return this.sellExpiration;
   }

   public ExpirationConfiguration getRentExpiration() {
      return this.rentExpiration;
   }

   public ExpirationConfiguration getBidExpiration() {
      return this.bidExpiration;
   }

   public ExpirationConfiguration getPurchaseExpiration() {
      return this.purchaseExpiration;
   }

   public ExpirationConfiguration getExpireExpiration() {
      return this.expireExpiration;
   }

   public ItemLoreConfiguration getItemLore() {
      return this.itemLoreConfiguration;
   }

   public TimeConfiguration getTime() {
      return this.timeConfiguration;
   }

   public ActionConfiguration getActions() {
      return this.actionConfiguration;
   }

   public SimpleDateFormat getDateFormat() {
      return this.dateFormat;
   }

   public SortConfiguration getSort() {
      return this.sortConfiguration;
   }

   public PermissionConfiguration getPermission() {
      return this.permissionConfiguration;
   }

   public WorldConfiguration getWorld() {
      return this.worldConfiguration;
   }

   public SpecialItemsConfiguration getSpecialItems() {
      return this.specialItemsConfiguration;
   }

   public ItemDisplayConfiguration getItemDisplay() {
      return this.itemDisplayConfiguration;
   }

   public PerformanceDebugConfiguration getPerformanceDebug() {
      return this.performanceDebugConfiguration;
   }

   public AutoClaimConfiguration getAutoClaimConfiguration() {
      return this.autoClaimConfiguration;
   }

   public SalesNotificationConfiguration getSalesNotificationConfiguration() {
      return this.salesNotificationConfiguration;
   }

   public BroadcastConfiguration getBroadcast() {
      return this.broadcastConfiguration;
   }

   public PerformanceConfiguration getPerformance() {
      return this.performanceConfiguration;
   }

   public SearchFilterConfiguration getSearchFilter() {
      return this.searchFilterConfiguration;
   }

   public CooldownConfiguration getCooldown() {
      return this.cooldownConfiguration;
   }

   public List<InventoryCommandConfiguration> getInventoryCommands() {
      return this.inventoryCommandConfigurations;
   }

   public boolean isSellInventoryEnabled() {
      return this.sellInventoryEnabled;
   }

   public <T extends Enum<T>> CommandConfiguration<T> loadCommandConfiguration(String var1, Class<T> var2) {
      FileConfiguration var3 = this.plugin.getConfig();
      List var4 = var3.getStringList(var1 + "aliases");
      ArrayList var5 = new ArrayList();

      for(Map var7 : var3.getMapList(var1 + "arguments")) {
         TypedMapAccessor var8 = new TypedMapAccessor(var7);
         String var9 = var8.getString("name");
         if (var9 == null) {
            this.plugin.getLogger().severe("Missing name for " + var1);
         } else {
            Enum var10;
            try {
               var10 = Enum.valueOf(var2, var9.toUpperCase());
            } catch (IllegalArgumentException var15) {
               List var12 = Arrays.stream((Enum[])var2.getEnumConstants()).map(Enum::name).toList();
               this.plugin.getLogger().severe("Invalid enum value '" + var9 + "' for enum " + var2.getSimpleName() + ". Possible values: " + String.join(", ", var12));
               continue;
            }

            String var11 = var8.getString("display-name", var9);
            if (var11 == null) {
               this.plugin.getLogger().severe("Impossible to find an aliases display-name for " + var1);
            } else {
               boolean var16 = var8.getBoolean("required", false);
               List var13 = var8.getList("auto-completion").stream().map(String::valueOf).toList();
               String var14 = var8.getString("default-value", (String)null);
               var5.add(new CommandArgumentConfiguration(var10, var11, var16, var13, var14));
            }
         }
      }

      return new CommandConfiguration<T>(var4, var5);
   }

   public List<String> loadCommandAliases(String var1) {
      return this.plugin.getConfig().getStringList(var1 + "aliases");
   }

   public SimpleCommandConfiguration loadSimpleCommandConfiguration(String var1) {
      FileConfiguration var2 = this.plugin.getConfig();
      List var3 = var2.getStringList(var1 + "aliases");
      ArrayList var4 = new ArrayList();

      for(Map var6 : var2.getMapList(var1 + "arguments")) {
         TypedMapAccessor var7 = new TypedMapAccessor(var6);
         String var8 = var7.getString("name");
         if (var8 == null) {
            this.plugin.getLogger().severe("Missing name for " + var1);
         } else {
            String var9 = var7.getString("display-name", var8);
            boolean var10 = var7.getBoolean("required", false);
            List var11 = var7.getList("auto-completion").stream().map(String::valueOf).toList();
            var4.add(new SimpleArgumentConfiguration(var8, var9, var10, var11));
         }
      }

      return new SimpleCommandConfiguration(var3, var4);
   }
}
