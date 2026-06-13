package fr.maxlego08.zauctionhouse.loader.buttons;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.button.DefaultButtonValue;
import fr.maxlego08.menu.api.loader.ButtonLoader;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.item.ItemType;
import fr.maxlego08.zauctionhouse.buttons.SellLimitButton;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SellLimitLoader extends ButtonLoader {
   private final AuctionPlugin plugin;

   public SellLimitLoader(AuctionPlugin var1) {
      super(var1, "ZAUCTIONHOUSE_SELL_LIMIT");
      this.plugin = var1;
   }

   public @Nullable Button load(@NotNull YamlConfiguration var1, @NotNull String var2, @NotNull DefaultButtonValue var3) {
      List var4 = var1.getStringList(var2 + "types");
      ArrayList var5 = new ArrayList();

      for(String var7 : var4) {
         try {
            var5.add(ItemType.valueOf(var7.toUpperCase()));
         } catch (IllegalArgumentException var9) {
            this.plugin.getLogger().warning("Invalid item type '" + var7 + "' in sell limit button configuration");
         }
      }

      if (var5.isEmpty()) {
         var5.add(ItemType.AUCTION);
      }

      return new SellLimitButton(this.plugin, var5);
   }
}
