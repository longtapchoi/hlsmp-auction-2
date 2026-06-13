package fr.maxlego08.zauctionhouse.api.configuration.records;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import org.bukkit.configuration.file.FileConfiguration;

public record ItemDisplayConfiguration(String langDisplay, String itemNameDisplay, String and, String between, boolean mergeSimilar) {
   public static ItemDisplayConfiguration of(AuctionPlugin var0, FileConfiguration var1) {
      String var2 = var1.getString("item-display.lang", "");
      if (var2.isBlank()) {
         var0.getLogger().severe("The lang display is blank !, you need to fix that !");
         var2 = "#8ee6e3x%amount% &7<lang:%item-translation-key%>";
      }

      String var3 = var1.getString("item-display.item-name", "");
      if (var3.isBlank()) {
         var0.getLogger().severe("The item name display is blank !, you need to fix that !");
         var3 = "#8ee6e3x%amount% &7%item-name%";
      }

      String var4 = var1.getString("item-display.and", "and");
      String var5 = var1.getString("item-display.between", ",");
      boolean var6 = var1.getBoolean("item-display.merge-similar-items", true);
      return new ItemDisplayConfiguration(var2, var3, var4, var5, var6);
   }
}
