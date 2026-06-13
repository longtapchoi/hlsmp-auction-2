package fr.maxlego08.zauctionhouse.api.configuration.records;

import fr.maxlego08.menu.api.utils.TypedMapAccessor;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.configuration.file.FileConfiguration;

@SuppressWarnings({"unchecked", "rawtypes"})
public record NumberMultiplicationConfiguration(boolean enable, Map<String, BigDecimal> multiplications) {
   private static final Pattern PATTERN = Pattern.compile("([0-9]+(?:\\.[0-9]+)?)\\s*([a-z]?)");

   public static NumberMultiplicationConfiguration of(AuctionPlugin var0, FileConfiguration var1) {
      boolean var2 = var1.getBoolean("number-sell-multiplication.enable");
      HashMap var3 = new HashMap();
      if (var2) {
         for(Map var6 : var1.getMapList("number-sell-multiplication.formats")) {
            TypedMapAccessor var7 = new TypedMapAccessor(var6);
            String var8 = var7.getString("format");
            String var9 = var7.getString("multiplication");
            if (var8 == null) {
               var0.getLogger().info("The format key is null !, you need to fix that !");
            } else if (var9 == null) {
               var0.getLogger().info("The multiplication key is null !, you need to fix that !");
            } else {
               var3.put(var8, new BigDecimal(var9));
            }
         }
      }

      return new NumberMultiplicationConfiguration(var2, var3);
   }

   public BigDecimal parseNumber(String var1) {
      if (var1 != null && !var1.isBlank()) {
         var1 = var1.trim().toLowerCase().replace(",", ".");
         Matcher var2 = PATTERN.matcher(var1);
         if (!var2.matches()) {
            return null;
         } else {
            BigDecimal var3 = new BigDecimal(var2.group(1));
            String var4 = var2.group(2).toUpperCase();
            if (this.multiplications.containsKey(var4)) {
               BigDecimal var5 = (BigDecimal)this.multiplications.get(var4);
               return var3.multiply(var5);
            } else {
               return var3;
            }
         }
      } else {
         return BigDecimal.ZERO;
      }
   }
}
