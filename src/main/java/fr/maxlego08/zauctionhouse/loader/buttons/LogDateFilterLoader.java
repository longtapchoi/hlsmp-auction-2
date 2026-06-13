package fr.maxlego08.zauctionhouse.loader.buttons;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.button.DefaultButtonValue;
import fr.maxlego08.menu.api.loader.ButtonLoader;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.filter.DateFilter;
import fr.maxlego08.zauctionhouse.buttons.admin.LogDateFilterButton;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class LogDateFilterLoader extends ButtonLoader {
   private final AuctionPlugin plugin;

   public LogDateFilterLoader(AuctionPlugin var1) {
      super(var1, "ZAUCTIONHOUSE_ADMIN_LOGS_FILTER_DATE");
      this.plugin = var1;
   }

   public Button load(YamlConfiguration var1, String var2, DefaultButtonValue var3) {
      String var4 = var1.getString(var2 + "enable-text", "&a%date%");
      String var5 = var1.getString(var2 + "disable-text", "&7%date%");
      ArrayList var6 = new ArrayList();

      for(String var8 : var1.getStringList(var2 + "filters")) {
         try {
            var6.add(DateFilter.valueOf(var8.toUpperCase()));
         } catch (Exception var14) {
            this.plugin.getLogger().warning("Invalid date filter: " + var8 + ", skipping...");
         }
      }

      if (var6.isEmpty()) {
         var6.addAll(List.of(DateFilter.values()));
      }

      EnumMap var15 = new EnumMap(DateFilter.class);
      ConfigurationSection var16 = var1.getConfigurationSection(var2 + "filter-names");
      if (var16 != null) {
         for(String var10 : var16.getKeys(false)) {
            try {
               DateFilter var11 = DateFilter.valueOf(var10.toUpperCase());
               var15.put(var11, var16.getString(var10));
            } catch (Exception var13) {
            }
         }
      }

      for(DateFilter var12 : DateFilter.values()) {
         var15.putIfAbsent(var12, var12.getDisplayName());
      }

      return new LogDateFilterButton(this.plugin, var4, var5, var6, var15);
   }
}
