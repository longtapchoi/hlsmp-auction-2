package fr.maxlego08.zauctionhouse.loader.buttons;

import fr.maxlego08.menu.api.InventoryManager;
import fr.maxlego08.menu.api.MenuItemStack;
import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.button.DefaultButtonValue;
import fr.maxlego08.menu.api.loader.ButtonLoader;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.item.SortItem;
import fr.maxlego08.zauctionhouse.buttons.SortButton;
import java.util.ArrayList;
import org.bukkit.configuration.file.YamlConfiguration;

@SuppressWarnings({"unchecked", "rawtypes"})
public class SortLoader extends ButtonLoader {
   private final AuctionPlugin plugin;
   private final InventoryManager inventoryManager;

   public SortLoader(AuctionPlugin var1, InventoryManager var2) {
      super(var1, "ZAUCTIONHOUSE_CHANGE_SORT");
      this.plugin = var1;
      this.inventoryManager = var2;
   }

   public Button load(YamlConfiguration var1, String var2, DefaultButtonValue var3) {
      String var4 = var1.getString(var2 + "enable-text");
      String var5 = var1.getString(var2 + "disable-text");
      ArrayList var6 = new ArrayList();

      for(String var8 : var1.getStringList(var2 + "sorts")) {
         try {
            var6.add(SortItem.valueOf(var8));
         } catch (Exception var10) {
            this.plugin.getLogger().severe("Impossible to find the sort type : " + var8 + ", skip it...");
         }
      }

      MenuItemStack var11 = this.inventoryManager.loadItemStack(var1, var2 + "loading-item.", var3.getFile());
      return new SortButton(this.plugin, var4, var5, var11, var6);
   }
}
