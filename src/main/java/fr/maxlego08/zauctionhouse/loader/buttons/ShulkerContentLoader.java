package fr.maxlego08.zauctionhouse.loader.buttons;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.button.DefaultButtonValue;
import fr.maxlego08.menu.api.loader.ButtonLoader;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.buttons.shulker.ShulkerContentButton;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ShulkerContentLoader extends ButtonLoader {
   private final AuctionPlugin plugin;

   public ShulkerContentLoader(AuctionPlugin var1) {
      super(var1, "ZAUCTIONHOUSE_SHULKER_CONTENT");
      this.plugin = var1;
   }

   public @Nullable Button load(@NotNull YamlConfiguration var1, @NotNull String var2, @NotNull DefaultButtonValue var3) {
      int var4 = var1.getInt(var2 + ".empty-slot", 0);
      return new ShulkerContentButton(this.plugin, var4);
   }
}
