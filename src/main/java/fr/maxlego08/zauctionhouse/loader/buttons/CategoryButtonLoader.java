package fr.maxlego08.zauctionhouse.loader.buttons;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.button.DefaultButtonValue;
import fr.maxlego08.menu.api.loader.ButtonLoader;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.buttons.categories.CategoryButton;
import org.bukkit.configuration.file.YamlConfiguration;

public class CategoryButtonLoader extends ButtonLoader {
   private final AuctionPlugin plugin;

   public CategoryButtonLoader(AuctionPlugin var1) {
      super(var1, "ZAUCTIONHOUSE_CATEGORY");
      this.plugin = var1;
   }

   public Button load(YamlConfiguration var1, String var2, DefaultButtonValue var3) {
      String var4 = var1.getString(var2 + "category");
      if (var4 == null || var4.isEmpty()) {
         this.plugin.getLogger().warning("Category button at " + var2 + " is missing 'category' field");
         var4 = "misc";
      }

      return new CategoryButton(this.plugin, var4);
   }
}
