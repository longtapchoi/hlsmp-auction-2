package fr.maxlego08.zauctionhouse.api.configuration.commands;

import fr.maxlego08.menu.api.utils.TypedMapAccessor;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.inventories.Inventories;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bukkit.configuration.file.FileConfiguration;

public record InventoryCommandConfiguration(String permission, String description, List<String> aliases, Inventories inventories, boolean enablePage, String pageName) {
   public static List<InventoryCommandConfiguration> of(AuctionPlugin var0, FileConfiguration var1) {
      List var2 = var1.getMapList("commands.inventories");
      ArrayList var3 = new ArrayList();

      for(Map var5 : var2) {
         TypedMapAccessor var6 = new TypedMapAccessor(var5);
         if (var6.getBoolean("enable", false)) {
            String var7 = var6.getString("permission", "");
            String var8 = var6.getString("description", "");
            List var9 = var6.getStringList("aliases");
            if (var9.isEmpty()) {
               var0.getLogger().warning("Aliases is empty for inventories command");
            } else {
               Inventories var10 = null;
               String var11 = var6.getString("inventory", "").toUpperCase();

               try {
                  var10 = Inventories.valueOf(var11);
               } catch (Exception var15) {
                  var0.getLogger().warning("Impossible to find the inventory '" + var11 + "' for inventories command");
                  continue;
               }

               boolean var12 = var6.getBoolean("enablePage", true);
               String var13 = var6.getString("pageName", "page");
               InventoryCommandConfiguration var14 = new InventoryCommandConfiguration(var7, var8, var9, var10, var12, var13);
               var3.add(var14);
            }
         }
      }

      return var3;
   }
}
