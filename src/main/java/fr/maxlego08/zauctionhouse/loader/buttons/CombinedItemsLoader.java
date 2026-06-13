package fr.maxlego08.zauctionhouse.loader.buttons;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.button.DefaultButtonValue;
import fr.maxlego08.menu.api.loader.ButtonLoader;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.buttons.list.CombinedItemsButton;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CombinedItemsLoader extends ButtonLoader {
   private final AuctionPlugin plugin;

   public CombinedItemsLoader(AuctionPlugin var1) {
      super(var1, "ZAUCTIONHOUSE_COMBINED_ITEMS");
      this.plugin = var1;
   }

   public @Nullable Button load(@NotNull YamlConfiguration var1, @NotNull String var2, @NotNull DefaultButtonValue var3) {
      int var4 = var1.getInt(var2 + "empty-slot", -1);
      boolean var5 = var1.getBoolean(var2 + "include-selling", true);
      boolean var6 = var1.getBoolean(var2 + "include-expired", true);
      boolean var7 = var1.getBoolean(var2 + "include-purchased", true);
      return new CombinedItemsButton(this.plugin, var4, var5, var6, var7);
   }
}
