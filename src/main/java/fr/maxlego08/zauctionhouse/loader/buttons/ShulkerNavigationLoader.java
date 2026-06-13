package fr.maxlego08.zauctionhouse.loader.buttons;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.button.DefaultButtonValue;
import fr.maxlego08.menu.api.loader.ButtonLoader;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.buttons.shulker.ShulkerNavigationButton;
import org.bukkit.configuration.file.YamlConfiguration;

public class ShulkerNavigationLoader extends ButtonLoader {
   private final AuctionPlugin plugin;

   public ShulkerNavigationLoader(AuctionPlugin var1) {
      super(var1, "ZAUCTIONHOUSE_SHULKER_NAVIGATION");
      this.plugin = var1;
   }

   public Button load(YamlConfiguration var1, String var2, DefaultButtonValue var3) {
      String var4 = var1.getString(var2 + "direction", "next");
      boolean var5 = var4.equalsIgnoreCase("next");
      return new ShulkerNavigationButton(this.plugin, var5);
   }
}
