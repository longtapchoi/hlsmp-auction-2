package fr.maxlego08.zauctionhouse.api;

import fr.maxlego08.menu.api.ButtonManager;
import fr.maxlego08.menu.api.InventoryManager;
import fr.maxlego08.zauctionhouse.api.inventories.Inventories;
import java.io.File;
import org.bukkit.entity.Player;

public interface InventoriesLoader {
   void loadInventories();

   void loadInventory(File var1);

   void loadPatterns();

   void loadPattern(File var1);

   void loadButtons();

   void load();

   void reload();

   InventoryManager getInventoryManager();

   ButtonManager getButtonManager();

   void openInventory(Player var1, Inventories var2);

   void openInventory(Player var1, Inventories var2, int var3);
}
