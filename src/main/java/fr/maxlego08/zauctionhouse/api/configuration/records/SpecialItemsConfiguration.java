package fr.maxlego08.zauctionhouse.api.configuration.records;

import fr.maxlego08.menu.api.MenuItemStack;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public record SpecialItemsConfiguration(MenuItemStack auctionItem) {
   public static SpecialItemsConfiguration of(AuctionPlugin var0, FileConfiguration var1) {
      MenuItemStack var2 = var0.getInventoriesLoader().getInventoryManager().loadItemStack((YamlConfiguration)var1, "special-items.auction-item.", new File(var0.getDataFolder(), "config.yml"));
      return new SpecialItemsConfiguration(var2);
   }
}
