package fr.maxlego08.zauctionhouse.permissions;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.configuration.Configuration;
import fr.maxlego08.zauctionhouse.api.configuration.commands.InventoryCommandConfiguration;
import fr.maxlego08.zauctionhouse.api.configuration.records.CooldownConfiguration;
import fr.maxlego08.zauctionhouse.api.configuration.records.ExpirationConfiguration;
import fr.maxlego08.zauctionhouse.api.configuration.records.PermissionConfiguration;
import fr.maxlego08.zauctionhouse.api.economy.AuctionEconomy;
import fr.maxlego08.zauctionhouse.api.item.ItemType;
import fr.maxlego08.zauctionhouse.api.tax.TaxConfiguration;
import fr.maxlego08.zauctionhouse.api.tax.TaxReduction;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;

@SuppressWarnings({"unchecked", "rawtypes"})
public class PermissionRegistrar {
   private final AuctionPlugin plugin;
   private final Set<String> registeredPermissions = new HashSet();

   public PermissionRegistrar(AuctionPlugin var1) {
      this.plugin = var1;
   }

   public void register() {
      this.unregister();
      this.registerStaticPermissions();
      this.registerCooldownPermissions();
      this.registerListingLimitPermissions();
      this.registerExpirationPermissions();
      this.registerInventoryCommandPermissions();
      this.registerEconomyPermissions();
      this.registerWildcardPermission();
      this.plugin.getLogger().info("Registered " + this.registeredPermissions.size() + " permissions in Bukkit.");
   }

   public void unregister() {
      PluginManager var1 = Bukkit.getPluginManager();

      for(String var3 : this.registeredPermissions) {
         Permission var4 = var1.getPermission(var3);
         if (var4 != null) {
            var1.removePermission(var4);
         }
      }

      this.registeredPermissions.clear();
   }

   private void registerStaticPermissions() {
      for(fr.maxlego08.zauctionhouse.api.utils.Permission var4 : fr.maxlego08.zauctionhouse.api.utils.Permission.values()) {
         boolean var5 = var4.name().startsWith("ZAUCTIONHOUSE_ADMIN") || var4 == fr.maxlego08.zauctionhouse.api.utils.Permission.ZAUCTIONHOUSE_RELOAD;
         this.addPermission(var4.asPermission(), var4.getDescription(), var5 ? PermissionDefault.OP : PermissionDefault.TRUE);
      }

   }

   private void registerCooldownPermissions() {
      CooldownConfiguration var1 = this.plugin.getConfiguration().getCooldown();
      if (var1 != null && var1.bypassPermission() != null && !var1.bypassPermission().isEmpty()) {
         this.addPermission(var1.bypassPermission(), "Bypasses command cooldown restrictions", PermissionDefault.OP);
      }

   }

   private void registerListingLimitPermissions() {
      PermissionConfiguration var1 = this.plugin.getConfiguration().getPermission();
      if (var1 != null) {
         for(Map.Entry var3 : var1.permissions().entrySet()) {
            for(Map.Entry var5 : ((Map)var3.getValue()).entrySet()) {
               this.addPermission((String)var5.getKey(), "Listing limit of " + String.valueOf(var5.getValue()) + " for " + ((ItemType)var3.getKey()).name().toLowerCase(), PermissionDefault.FALSE);
            }
         }

      }
   }

   private void registerExpirationPermissions() {
      Configuration var1 = this.plugin.getConfiguration();
      this.registerExpirationPermissions(var1.getSellExpiration(), "auction");
      this.registerExpirationPermissions(var1.getRentExpiration(), "rent");
      this.registerExpirationPermissions(var1.getBidExpiration(), "bid");
      this.registerExpirationPermissions(var1.getPurchaseExpiration(), "purchase");
      this.registerExpirationPermissions(var1.getExpireExpiration(), "expire");
   }

   private void registerExpirationPermissions(ExpirationConfiguration var1, String var2) {
      if (var1 != null && var1.enablePermission()) {
         for(String var4 : var1.expirations().keySet()) {
            this.addPermission(var4, "Extended " + var2 + " expiration time", PermissionDefault.FALSE);
         }
      }

   }

   private void registerInventoryCommandPermissions() {
      for(InventoryCommandConfiguration var2 : this.plugin.getConfiguration().getInventoryCommands()) {
         if (var2.permission() != null && !var2.permission().isEmpty()) {
            this.addPermission(var2.permission(), "Access to inventory command", PermissionDefault.TRUE);
         }
      }

   }

   private void registerEconomyPermissions() {
      for(AuctionEconomy var2 : this.plugin.getEconomyManager().getEconomies()) {
         if (var2.getPermission() != null && !var2.getPermission().isEmpty()) {
            this.addPermission(var2.getPermission(), "Access to " + var2.getDisplayName() + " economy", PermissionDefault.TRUE);
         }

         TaxConfiguration var3 = var2.getTaxConfiguration();
         if (var3 != null && var3.isEnabled()) {
            String var4 = var3.getBypassPermission();
            if (var4 != null && !var4.isEmpty()) {
               this.addPermission(var4, "Bypasses tax for " + var2.getDisplayName() + " economy", PermissionDefault.FALSE);
            }

            for(TaxReduction var6 : var3.getReductions()) {
               if (var6.permission() != null && !var6.permission().isEmpty()) {
                  this.addPermission(var6.permission(), var6.percentage() + "% tax reduction for " + var2.getDisplayName() + " economy", PermissionDefault.FALSE);
               }
            }
         }
      }

   }

   private void registerWildcardPermission() {
      HashMap var1 = new HashMap();

      for(String var3 : this.registeredPermissions) {
         var1.put(var3, true);
      }

      this.addPermission("zauctionhouse.*", "Gives access to all zAuctionHouse permissions", PermissionDefault.OP, var1);
   }

   private void addPermission(String var1, String var2, PermissionDefault var3) {
      this.addPermission(var1, var2, var3, (Map)null);
   }

   private void addPermission(String var1, String var2, PermissionDefault var3, Map<String, Boolean> var4) {
      if (var1 != null && !var1.isEmpty()) {
         PluginManager var5 = Bukkit.getPluginManager();
         if (var5.getPermission(var1) == null) {
            try {
               Permission var6 = var4 != null && !var4.isEmpty() ? new Permission(var1, var2, var3, var4) : new Permission(var1, var2, var3);
               var5.addPermission(var6);
               this.registeredPermissions.add(var1);
            } catch (Exception var7) {
               this.plugin.getLogger().warning("Failed to register permission '" + var1 + "': " + var7.getMessage());
            }

         }
      }
   }
}
