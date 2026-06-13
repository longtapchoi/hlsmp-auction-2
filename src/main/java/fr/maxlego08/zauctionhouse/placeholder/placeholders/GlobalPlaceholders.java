package fr.maxlego08.zauctionhouse.placeholder.placeholders;

import fr.maxlego08.zauctionhouse.api.AuctionManager;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.category.CategoryManager;
import fr.maxlego08.zauctionhouse.api.item.StorageType;
import fr.maxlego08.zauctionhouse.api.placeholders.Placeholder;
import fr.maxlego08.zauctionhouse.api.placeholders.PlaceholderRegister;

public class GlobalPlaceholders implements PlaceholderRegister {
   public void register(Placeholder var1, AuctionPlugin var2) {
      AuctionManager var3 = var2.getAuctionManager();
      CategoryManager var4 = var2.getCategoryManager();
      var1.register("listed_items", (var1x) -> String.valueOf(var3.getItems(StorageType.LISTED).size()), "Returns the number of listed items");
      var1.register("category_count_", (var1x, var2x) -> {
         if (var2x != null && !var2x.isEmpty()) {
            long var3 = var4.getItemCountForCategory(var2x);
            return String.valueOf(var3);
         } else {
            return "0";
         }
      }, "Returns the number of items in a category", "<category_id>");
   }
}
