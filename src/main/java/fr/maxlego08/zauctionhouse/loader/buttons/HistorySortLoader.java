package fr.maxlego08.zauctionhouse.loader.buttons;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.button.DefaultButtonValue;
import fr.maxlego08.menu.api.loader.ButtonLoader;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.history.HistorySortType;
import fr.maxlego08.zauctionhouse.buttons.history.HistorySortButton;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

@SuppressWarnings({"unchecked", "rawtypes"})
public class HistorySortLoader extends ButtonLoader {
   private final AuctionPlugin plugin;

   public HistorySortLoader(AuctionPlugin var1) {
      super(var1, "ZAUCTIONHOUSE_HISTORY_SORT");
      this.plugin = var1;
   }

   public Button load(YamlConfiguration var1, String var2, DefaultButtonValue var3) {
      String var4 = var1.getString(var2 + "enable-text", "&a%sorting%");
      String var5 = var1.getString(var2 + "disable-text", "&7%sorting%");
      ArrayList var6 = new ArrayList();

      for(String var8 : var1.getStringList(var2 + "sorts")) {
         try {
            var6.add(HistorySortType.valueOf(var8.toUpperCase()));
         } catch (Exception var14) {
            this.plugin.getLogger().warning("Invalid history sort type: " + var8 + ", skipping...");
         }
      }

      if (var6.isEmpty()) {
         var6.addAll(List.of(HistorySortType.values()));
      }

      EnumMap var15 = new EnumMap(HistorySortType.class);
      ConfigurationSection var16 = var1.getConfigurationSection(var2 + "sort-names");
      if (var16 != null) {
         for(String var10 : var16.getKeys(false)) {
            try {
               HistorySortType var11 = HistorySortType.valueOf(var10.toUpperCase());
               var15.put(var11, var16.getString(var10));
            } catch (Exception var13) {
            }
         }
      }

      for(HistorySortType var12 : HistorySortType.values()) {
         var15.putIfAbsent(var12, var12.getDefaultDisplayName());
      }

      return new HistorySortButton(this.plugin, var4, var5, var6, var15);
   }
}
