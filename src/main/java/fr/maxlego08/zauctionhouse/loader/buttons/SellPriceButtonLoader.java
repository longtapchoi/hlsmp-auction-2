package fr.maxlego08.zauctionhouse.loader.buttons;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.button.DefaultButtonValue;
import fr.maxlego08.menu.api.loader.ButtonLoader;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.buttons.sell.SellPriceButton;
import java.math.BigDecimal;
import org.bukkit.configuration.file.YamlConfiguration;

public class SellPriceButtonLoader extends ButtonLoader {
   private final AuctionPlugin plugin;

   public SellPriceButtonLoader(AuctionPlugin var1) {
      super(var1, "ZAUCTIONHOUSE_SELL_PRICE");
      this.plugin = var1;
   }

   public Button load(YamlConfiguration var1, String var2, DefaultButtonValue var3) {
      String var4 = var2 + "amounts.";
      BigDecimal var5 = this.getBigDecimal(var1, var4 + "left-click", BigDecimal.valueOf(100L));
      BigDecimal var6 = this.getBigDecimal(var1, var4 + "right-click", BigDecimal.valueOf(-100L));
      BigDecimal var7 = this.getBigDecimal(var1, var4 + "shift-left-click", BigDecimal.valueOf(1000L));
      BigDecimal var8 = this.getBigDecimal(var1, var4 + "shift-right-click", BigDecimal.valueOf(-1000L));
      return new SellPriceButton(this.plugin, var5, var6, var7, var8);
   }

   private BigDecimal getBigDecimal(YamlConfiguration var1, String var2, BigDecimal var3) {
      if (!var1.contains(var2)) {
         return var3;
      } else {
         Object var4 = var1.get(var2);
         if (var4 instanceof Number) {
            Number var5 = (Number)var4;
            return BigDecimal.valueOf(var5.doubleValue());
         } else {
            try {
               return new BigDecimal(String.valueOf(var4));
            } catch (NumberFormatException var6) {
               this.plugin.getLogger().warning("Invalid number at " + var2 + ": " + String.valueOf(var4) + ", using default: " + String.valueOf(var3));
               return var3;
            }
         }
      }
   }
}
