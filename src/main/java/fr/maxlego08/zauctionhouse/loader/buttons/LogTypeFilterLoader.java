package fr.maxlego08.zauctionhouse.loader.buttons;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.button.DefaultButtonValue;
import fr.maxlego08.menu.api.loader.ButtonLoader;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.log.LogType;
import fr.maxlego08.zauctionhouse.buttons.admin.LogTypeFilterButton;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

@SuppressWarnings({"unchecked", "rawtypes"})
public class LogTypeFilterLoader extends ButtonLoader {
   private final AuctionPlugin plugin;

   public LogTypeFilterLoader(AuctionPlugin var1) {
      super(var1, "ZAUCTIONHOUSE_ADMIN_LOGS_FILTER_TYPE");
      this.plugin = var1;
   }

   public Button load(YamlConfiguration var1, String var2, DefaultButtonValue var3) {
      String var4 = var1.getString(var2 + "enable-text", "&a%type%");
      String var5 = var1.getString(var2 + "disable-text", "&7%type%");
      String var6 = var1.getString(var2 + "all-types-name", "All");
      ArrayList var7 = new ArrayList();

      for(String var9 : var1.getStringList(var2 + "types")) {
         try {
            var7.add(LogType.valueOf(var9.toUpperCase()));
         } catch (Exception var15) {
            this.plugin.getLogger().warning("Invalid log type: " + var9 + ", skipping...");
         }
      }

      if (var7.isEmpty()) {
         var7.addAll(List.of(LogType.values()));
      }

      EnumMap var16 = new EnumMap(LogType.class);
      ConfigurationSection var17 = var1.getConfigurationSection(var2 + "type-names");
      if (var17 != null) {
         for(String var11 : var17.getKeys(false)) {
            try {
               LogType var12 = LogType.valueOf(var11.toUpperCase());
               var16.put(var12, var17.getString(var11));
            } catch (Exception var14) {
            }
         }
      }

      for(LogType var13 : LogType.values()) {
         var16.putIfAbsent(var13, var13.getDefaultDisplayName());
      }

      return new LogTypeFilterButton(this.plugin, var4, var5, var7, var16, var6);
   }
}
