package fr.maxlego08.zauctionhouse.loader.buttons;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.button.DefaultButtonValue;
import fr.maxlego08.menu.api.loader.ButtonLoader;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.buttons.categories.CategorySwitcherButton;
import java.util.List;
import org.bukkit.configuration.file.YamlConfiguration;

public class CategorySwitcherLoader extends ButtonLoader {
   private final AuctionPlugin plugin;

   public CategorySwitcherLoader(AuctionPlugin var1) {
      super(var1, "ZAUCTIONHOUSE_CATEGORY_SWITCHER");
      this.plugin = var1;
   }

   public Button load(YamlConfiguration var1, String var2, DefaultButtonValue var3) {
      String var4 = var1.getString(var2 + "enable-text", "&a%category%");
      String var5 = var1.getString(var2 + "disable-text", "&7%category%");
      List var6 = var1.getStringList(var2 + "categories");
      if (var6.isEmpty()) {
         this.plugin.getLogger().warning("Category switcher button at " + var2 + " has no categories defined");
      }

      return new CategorySwitcherButton(this.plugin, var4, var5, var6);
   }
}
