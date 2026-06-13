package fr.maxlego08.zauctionhouse.loader.buttons;

import fr.maxlego08.menu.api.InventoryManager;
import fr.maxlego08.menu.api.MenuItemStack;
import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.button.DefaultButtonValue;
import fr.maxlego08.menu.api.loader.ButtonLoader;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.buttons.ClaimButton;
import org.bukkit.configuration.file.YamlConfiguration;

public class ClaimButtonLoader extends ButtonLoader {
   private final AuctionPlugin plugin;
   private final InventoryManager inventoryManager;

   public ClaimButtonLoader(AuctionPlugin var1, InventoryManager var2) {
      super(var1, "ZAUCTIONHOUSE_CLAIM");
      this.plugin = var1;
      this.inventoryManager = var2;
   }

   public Button load(YamlConfiguration var1, String var2, DefaultButtonValue var3) {
      MenuItemStack var4 = this.inventoryManager.loadItemStack(var1, var2 + "loading-item.", var3.getFile());
      return new ClaimButton(this.plugin, var4);
   }
}
