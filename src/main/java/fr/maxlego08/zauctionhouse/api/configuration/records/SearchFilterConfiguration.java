package fr.maxlego08.zauctionhouse.api.configuration.records;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.filter.SearchFilterType;
import java.util.Map;
import java.util.TreeMap;
import org.bukkit.configuration.file.FileConfiguration;

public record SearchFilterConfiguration(Map<SearchFilterType, String> operators) {
   public static SearchFilterConfiguration of(AuctionPlugin var0, FileConfiguration var1) {
      TreeMap<SearchFilterType, String> var2 = new TreeMap((var0x, var1x) -> Integer.compare(var1x.name().length(), var0x.name().length()));

      for(SearchFilterType var6 : SearchFilterType.values()) {
         String var7 = var1.getString("search-filters." + var6.name(), var6.getDefaultOperator());
         var2.put(var6, var7);
      }

      return new SearchFilterConfiguration(var2);
   }

   public String getOperator(SearchFilterType var1) {
      return (String)this.operators.getOrDefault(var1, var1.getDefaultOperator());
   }

   public Map.Entry<SearchFilterType, String>[] orderedByLength() {
      return (Map.Entry[])this.operators.entrySet().stream().sorted((var0, var1) -> Integer.compare(var1.getValue().length(), var0.getValue().length())).toArray((var0) -> new Map.Entry[var0]);
   }
}
