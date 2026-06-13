package fr.maxlego08.zauctionhouse.loader.buttons;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.button.DefaultButtonValue;
import fr.maxlego08.menu.api.loader.ButtonLoader;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.transaction.TransactionStatus;
import fr.maxlego08.zauctionhouse.buttons.admin.TransactionStatusFilterButton;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class TransactionStatusFilterLoader extends ButtonLoader {
   private final AuctionPlugin plugin;

   public TransactionStatusFilterLoader(AuctionPlugin var1) {
      super(var1, "ZAUCTIONHOUSE_ADMIN_TRANSACTIONS_FILTER_STATUS");
      this.plugin = var1;
   }

   public Button load(YamlConfiguration var1, String var2, DefaultButtonValue var3) {
      String var4 = var1.getString(var2 + "enable-text", "&a%status%");
      String var5 = var1.getString(var2 + "disable-text", "&7%status%");
      String var6 = var1.getString(var2 + "all-statuses-name", "All");
      ArrayList var7 = new ArrayList();

      for(String var9 : var1.getStringList(var2 + "statuses")) {
         try {
            var7.add(TransactionStatus.valueOf(var9.toUpperCase()));
         } catch (Exception var15) {
            this.plugin.getLogger().warning("Invalid transaction status: " + var9 + ", skipping...");
         }
      }

      if (var7.isEmpty()) {
         var7.addAll(List.of(TransactionStatus.values()));
      }

      EnumMap var16 = new EnumMap(TransactionStatus.class);
      ConfigurationSection var17 = var1.getConfigurationSection(var2 + "status-names");
      if (var17 != null) {
         for(String var11 : var17.getKeys(false)) {
            try {
               TransactionStatus var12 = TransactionStatus.valueOf(var11.toUpperCase());
               var16.put(var12, var17.getString(var11));
            } catch (Exception var14) {
            }
         }
      }

      for(TransactionStatus var13 : TransactionStatus.values()) {
         var16.putIfAbsent(var13, var13.getDefaultDisplayName());
      }

      return new TransactionStatusFilterButton(this.plugin, var4, var5, var7, var16, var6);
   }
}
