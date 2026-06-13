package fr.maxlego08.zauctionhouse.api.configuration.records;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.item.SortItem;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.configuration.file.FileConfiguration;

@SuppressWarnings({"unchecked", "rawtypes"})
public record SortConfiguration(SortItem defaultSort, Map<SortItem, String> sortItems) {
   public static SortConfiguration of(AuctionPlugin var0, FileConfiguration var1) {
      HashMap var2 = new HashMap();
      SortItem var3 = get(var1.getString("sort-items.default-sort"));
      if (var3 == null) {
         var0.getLogger().severe("The default sort is null !, you need to fix that !");
         var3 = SortItem.DECREASING_DATE;
      }

      for(SortItem var7 : SortItem.values()) {
         String var8 = var1.getString("sort-items.translations." + var7.name());
         if (var8 == null) {
            var0.getLogger().severe("The sort item translation for '" + var7.name() + "' is null !, you need to fix that !");
            var8 = var7.name();
         }

         var2.put(var7, var8);
      }

      return new SortConfiguration(var3, var2);
   }

   private static SortItem get(String var0) {
      try {
         return SortItem.valueOf(var0.toUpperCase());
      } catch (Exception var2) {
         return null;
      }
   }
}
