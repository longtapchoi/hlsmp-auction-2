package fr.maxlego08.zauctionhouse.api.configuration;

import fr.maxlego08.zauctionhouse.api.configuration.commands.CommandConfiguration;
import fr.maxlego08.zauctionhouse.api.configuration.commands.InventoryCommandConfiguration;
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
import java.text.SimpleDateFormat;
import java.util.List;

public interface Configuration extends ConfigurationFile {
   boolean isEnableDebug();

   boolean isEnablePerformanceDebug();

   String getServerName();

   List<MessageColor> getMessageColors();

   NumberMultiplicationConfiguration getNumberMultiplicationConfiguration();

   ExpirationConfiguration getSellExpiration();

   ExpirationConfiguration getRentExpiration();

   ExpirationConfiguration getBidExpiration();

   ExpirationConfiguration getPurchaseExpiration();

   ExpirationConfiguration getExpireExpiration();

   ItemLoreConfiguration getItemLore();

   TimeConfiguration getTime();

   ActionConfiguration getActions();

   SimpleDateFormat getDateFormat();

   SortConfiguration getSort();

   PermissionConfiguration getPermission();

   WorldConfiguration getWorld();

   SpecialItemsConfiguration getSpecialItems();

   ItemDisplayConfiguration getItemDisplay();

   PerformanceDebugConfiguration getPerformanceDebug();

   AutoClaimConfiguration getAutoClaimConfiguration();

   SalesNotificationConfiguration getSalesNotificationConfiguration();

   PerformanceConfiguration getPerformance();

   SearchFilterConfiguration getSearchFilter();

   BroadcastConfiguration getBroadcast();

   boolean isSellInventoryEnabled();

   List<InventoryCommandConfiguration> getInventoryCommands();

   CooldownConfiguration getCooldown();

   <T extends Enum<T>> CommandConfiguration<T> loadCommandConfiguration(String var1, Class<T> var2);

   List<String> loadCommandAliases(String var1);

   SimpleCommandConfiguration loadSimpleCommandConfiguration(String var1);
}
