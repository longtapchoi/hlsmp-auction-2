package fr.maxlego08.zauctionhouse.placeholder.placeholders;

import fr.maxlego08.zauctionhouse.api.AuctionManager;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCache;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCacheKey;
import fr.maxlego08.zauctionhouse.api.category.Category;
import fr.maxlego08.zauctionhouse.api.configuration.Configuration;
import fr.maxlego08.zauctionhouse.api.configuration.records.SortConfiguration;
import fr.maxlego08.zauctionhouse.api.economy.AuctionEconomy;
import fr.maxlego08.zauctionhouse.api.item.ItemType;
import fr.maxlego08.zauctionhouse.api.item.SortItem;
import fr.maxlego08.zauctionhouse.api.placeholders.Placeholder;
import fr.maxlego08.zauctionhouse.api.placeholders.PlaceholderRegister;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

public class PlayerPlaceholders implements PlaceholderRegister {
   public void register(Placeholder var1, AuctionPlugin var2) {
      AuctionManager var3 = var2.getAuctionManager();
      Configuration var4 = var2.getConfiguration();
      var1.register("expired_items", (var1x) -> String.valueOf(var3.getExpiredItems(var1x).size()), "Returns the number of expired items for a player");
      var1.register("selling_items", (var1x) -> String.valueOf(var3.getPlayerSellingItems(var1x).size()), "Returns the number of items being sold by a player");
      var1.register("purchased_items", (var1x) -> String.valueOf(var3.getPurchasedItems(var1x).size()), "Returns the number of purchased items for a player");
      var1.register("sorting_value", (var2x) -> ((SortItem)var3.getCache(var2x).get(PlayerCacheKey.ITEM_SORT, var2.getConfiguration().getSort().defaultSort())).name(), "Returns the value for sorting the items for the player");
      var1.register("sorting_name", (var2x) -> {
         SortConfiguration var3x = var4.getSort();
         SortItem sortI = var3.getCache(var2x).get(PlayerCacheKey.ITEM_SORT, var3x.defaultSort());
         return var3x.sortItems().get(sortI);
      }, "Returns the name of the value used to sort the items for the player");
      var1.register("category_name", (var2x) -> {
         PlayerCache var3x = var3.getCache(var2x);
         if (var3x.has(PlayerCacheKey.CURRENT_CATEGORY)) {
            Category var4c = var3x.get(PlayerCacheKey.CURRENT_CATEGORY);
            return var4c.getDisplayName();
         } else {
            return var2.getCategoryManager().getAllCategoryName();
         }
      }, "Returns the name of the current category for the player");
      var1.register("category_id", (var1x) -> {
         PlayerCache var2c = var3.getCache(var1x);
         if (var2c.has(PlayerCacheKey.CURRENT_CATEGORY)) {
            Category var3x = var2c.get(PlayerCacheKey.CURRENT_CATEGORY);
            return var3x.getId();
         } else {
            return "main";
         }
      }, "Returns the id of the current category for the player (main for auction house)");
      var1.register("pending_money", (var2x) -> {
         PlayerCache var3x = var3.getCache(var2x);
         if (var3x.has(PlayerCacheKey.PENDING_MONEY_DATA)) {
            Map<String, BigDecimal> var4m = var3x.get(PlayerCacheKey.PENDING_MONEY_DATA);
            if (var4m != null && !var4m.isEmpty()) {
               BigDecimal var5 = var4m.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
               AuctionEconomy var6 = var2.getEconomyManager().getDefaultEconomy(ItemType.AUCTION);
               if (var6 != null) {
                  return var2.getEconomyManager().format(var6, var5);
               }

               return var5.toString();
            }
         }

         return "0";
      }, "Returns the total pending money for a player");
      var1.register("pending_money_raw", (var1x) -> {
         PlayerCache var2r = var3.getCache(var1x);
         if (var2r.has(PlayerCacheKey.PENDING_MONEY_DATA)) {
            Map<String, BigDecimal> var3x = var2r.get(PlayerCacheKey.PENDING_MONEY_DATA);
            if (var3x != null && !var3x.isEmpty()) {
               BigDecimal var4 = var3x.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
               return var4.toString();
            }
         }

         return "0";
      }, "Returns the raw pending money value for a player");
      var1.register("pending_money_", (var2x, var3x) -> {
         if (var3x != null && !var3x.isEmpty()) {
            PlayerCache var4pc = var3.getCache(var2x);
            if (var4pc.has(PlayerCacheKey.PENDING_MONEY_DATA)) {
               Map<String, BigDecimal> var5 = var4pc.get(PlayerCacheKey.PENDING_MONEY_DATA);
               if (var5 != null && var5.containsKey(var3x)) {
                  Optional<AuctionEconomy> var6 = var2.getEconomyManager().getEconomy(var3x);
                  if (var6.isPresent()) {
                     return var2.getEconomyManager().format(var6.get(), var5.get(var3x));
                  }

                  return var5.get(var3x).toString();
               }
            }

            return "0";
         } else {
            return "0";
         }
      }, "Returns the pending money for a specific economy", "<economy>");
      var1.register("has_pending_money", (var1x) -> {
         PlayerCache var2h = var3.getCache(var1x);
         if (var2h.has(PlayerCacheKey.PENDING_MONEY_DATA)) {
            Map<String, BigDecimal> var3x = var2h.get(PlayerCacheKey.PENDING_MONEY_DATA);
            if (var3x != null && !var3x.isEmpty()) {
               BigDecimal var4hpm = var3x.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
               return String.valueOf(var4hpm.compareTo(BigDecimal.ZERO) > 0);
            }
         }

         return "false";
      }, "Returns true if the player has pending money to claim");
      var1.register("max_items_", (var1x, var2x) -> {
         if (var2x != null && !var2x.isEmpty()) {
            try {
               ItemType var3i = ItemType.valueOf(var2x.toUpperCase());
               return String.valueOf(var4.getPermission().getLimit(var3i, var1x));
            } catch (IllegalArgumentException var4x) {
               return "0";
            }
         } else {
            return "0";
         }
      }, "Returns the maximum number of items a player can list for a specific type", "<type>");
   }
}
