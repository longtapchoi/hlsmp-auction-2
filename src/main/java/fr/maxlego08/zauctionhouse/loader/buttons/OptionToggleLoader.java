package fr.maxlego08.zauctionhouse.loader.buttons;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.button.DefaultButtonValue;
import fr.maxlego08.menu.api.loader.ButtonLoader;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.option.PlayerOption;
import fr.maxlego08.zauctionhouse.buttons.option.OptionToggleButton;
import org.bukkit.configuration.file.YamlConfiguration;

public class OptionToggleLoader extends ButtonLoader {
   private final AuctionPlugin plugin;

   public OptionToggleLoader(AuctionPlugin var1) {
      super(var1, "ZAUCTIONHOUSE_OPTION_TOGGLE");
      this.plugin = var1;
   }

   public Button load(YamlConfiguration var1, String var2, DefaultButtonValue var3) {
      String var4 = var1.getString(var2 + "option-name", "broadcast_sell");
      String var5 = var1.getString(var2 + "enable-text", "<green>Enabled");
      String var6 = var1.getString(var2 + "disable-text", "<red>Disabled");
      PlayerOption var7 = PlayerOption.fromKey(var4);
      if (var7 == null) {
         this.plugin.getLogger().warning("Unknown player option: " + var4 + ", defaulting to BROADCAST_SELL");
         var7 = PlayerOption.BROADCAST_SELL;
      }

      return new OptionToggleButton(this.plugin, var7, var5, var6);
   }
}
