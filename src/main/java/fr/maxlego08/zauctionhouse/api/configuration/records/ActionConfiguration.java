package fr.maxlego08.zauctionhouse.api.configuration.records;

import fr.maxlego08.menu.api.MenuItemStack;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public record ActionConfiguration(boolean updateInventoryOnAction, boolean resetCategoryOnOpen, boolean resetSearchOnOpen, ListedConfiguration listed, PurchasedConfiguration purchased, SellingConfiguration selling, ExpiredConfiguration expired) {
   public static ActionConfiguration of(AuctionPlugin var0, FileConfiguration var1) {
      return new ActionConfiguration(var1.getBoolean("action.update-inventory-on-action"), var1.getBoolean("action.reset-category-on-open", true), var1.getBoolean("action.reset-search-on-open", true), ActionConfiguration.ListedConfiguration.of(var0, var1), ActionConfiguration.PurchasedConfiguration.of(var0, var1), ActionConfiguration.SellingConfiguration.of(var0, var1), ActionConfiguration.ExpiredConfiguration.of(var0, var1));
   }

   public static record ListedConfiguration(boolean giveItem, boolean openInventory, boolean openConfirmInventory) {
      public static ListedConfiguration of(AuctionPlugin var0, FileConfiguration var1) {
         return new ListedConfiguration(var1.getBoolean("action.remove-listed-item.give-item"), var1.getBoolean("action.remove-listed-item.open-inventory"), var1.getBoolean("action.remove-listed-item.open-confirm-inventory"));
      }
   }

   public static record PurchasedConfiguration(boolean giveItem, boolean openInventory, PurchaseNoMoneyConfiguration noMoney, boolean sendNoMoneyMessage, SoundConfiguration noMoneySound, boolean freeSpace) {
      public static PurchasedConfiguration of(AuctionPlugin var0, FileConfiguration var1) {
         return new PurchasedConfiguration(var1.getBoolean("action.purchased-item.give-item"), var1.getBoolean("action.purchased-item.open-inventory"), ActionConfiguration.PurchaseNoMoneyConfiguration.of(var0, var1), var1.getBoolean("action.purchased-item.money-message"), SoundConfiguration.of(var0, var1, "action.purchased-item.money-sound."), var1.getBoolean("action.purchased-item.player-inventory-must-have-free-space"));
      }
   }

   public static record PurchaseNoMoneyConfiguration(boolean enable, int duration, MenuItemStack menuItemStack) {
      public static PurchaseNoMoneyConfiguration of(AuctionPlugin var0, FileConfiguration var1) {
         boolean var2 = var1.getBoolean("action.purchased-item.money-item.enable");
         MenuItemStack var3 = null;
         int var4 = 0;
         if (var2) {
            var3 = var0.getInventoriesLoader().getInventoryManager().loadItemStack((YamlConfiguration)var1, "action.purchased-item.money-item.item.", new File(var0.getDataFolder(), "config.yml"));
            var4 = var1.getInt("action.purchased-item.money-item.duration", 1);
            if (var4 <= 0) {
               var0.getLogger().warning("The duration of the purchase no money is less than or equal to 0 !");
               var4 = 1;
            }
         }

         return new PurchaseNoMoneyConfiguration(var2, var4, var3);
      }
   }

   public static record SellingConfiguration(boolean openInventory, boolean freeSpace) {
      public static SellingConfiguration of(AuctionPlugin var0, FileConfiguration var1) {
         return new SellingConfiguration(var1.getBoolean("action.selling-item.open-inventory"), var1.getBoolean("action.selling-item.player-inventory-must-have-free-space", true));
      }
   }

   public static record ExpiredConfiguration(boolean openInventory, boolean freeSpace) {
      public static ExpiredConfiguration of(AuctionPlugin var0, FileConfiguration var1) {
         return new ExpiredConfiguration(var1.getBoolean("action.remove-expired-item.open-inventory"), var1.getBoolean("action.remove-expired-item.player-inventory-must-have-free-space", true));
      }
   }
}
