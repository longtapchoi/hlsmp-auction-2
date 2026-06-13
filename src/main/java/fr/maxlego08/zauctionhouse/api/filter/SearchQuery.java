package fr.maxlego08.zauctionhouse.api.filter;

import fr.maxlego08.zauctionhouse.api.configuration.records.SearchFilterConfiguration;
import java.util.Map;

public record SearchQuery(SearchField field, SearchFilterType type, String value) {
   public static SearchQuery parse(String var0, SearchFilterConfiguration var1) {
      if (var0 != null && !var0.isEmpty()) {
         for(Map.Entry var5 : var1.orderedByLength()) {
            String var6 = (String)var5.getValue();
            int var7 = var0.indexOf(var6);
            if (var7 > 0) {
               String var8 = var0.substring(0, var7).trim();
               SearchField var9 = SearchField.fromKey(var8);
               if (var9 != null) {
                  String var10 = var0.substring(var7 + var6.length()).trim();
                  return new SearchQuery(var9, (SearchFilterType)var5.getKey(), var10);
               }
            }
         }

         return new SearchQuery((SearchField)null, (SearchFilterType)null, var0);
      } else {
         return new SearchQuery((SearchField)null, (SearchFilterType)null, "");
      }
   }

   public boolean isDefault() {
      return this.field == null;
   }
}
