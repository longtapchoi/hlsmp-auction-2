package fr.maxlego08.zauctionhouse.api.configuration.records;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

@SuppressWarnings({"unchecked", "rawtypes"})
public record BroadcastConfiguration(boolean sellEnabled, boolean purchaseEnabled, boolean excludeSeller, boolean excludeBuyer, Map<String, String> categoryMessagesSell, Map<String, String> categoryMessagesPurchase) {
   public static BroadcastConfiguration of(AuctionPlugin var0, FileConfiguration var1) {
      boolean var2 = var1.getBoolean("broadcast.sell.enable", false);
      boolean var3 = var1.getBoolean("broadcast.purchase.enable", false);
      boolean var4 = var1.getBoolean("broadcast.sell.exclude-seller", true);
      boolean var5 = var1.getBoolean("broadcast.purchase.exclude-buyer", true);
      HashMap var6 = new HashMap();
      ConfigurationSection var7 = var1.getConfigurationSection("broadcast.category-messages.sell");
      if (var7 != null) {
         for(String var9 : var7.getKeys(false)) {
            var6.put(var9, var7.getString(var9));
         }
      }

      HashMap var12 = new HashMap();
      ConfigurationSection var13 = var1.getConfigurationSection("broadcast.category-messages.purchase");
      if (var13 != null) {
         for(String var11 : var13.getKeys(false)) {
            var12.put(var11, var13.getString(var11));
         }
      }

      return new BroadcastConfiguration(var2, var3, var4, var5, var6, var12);
   }
}
