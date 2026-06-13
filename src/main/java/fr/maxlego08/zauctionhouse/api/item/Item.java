package fr.maxlego08.zauctionhouse.api.item;

import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.zauctionhouse.api.category.Category;
import fr.maxlego08.zauctionhouse.api.economy.AuctionEconomy;
import fr.maxlego08.zauctionhouse.api.economy.PriceFormat;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface Item {
   int getId();

   String getServerName();

   UUID getSellerUniqueId();

   String getSellerName();

   BigDecimal getPrice();

   AuctionEconomy getAuctionEconomy();

   void setAuctionEconomy(AuctionEconomy var1);

   String getEconomyName();

   OfflinePlayer getSeller();

   Date getExpiredAt();

   void setExpiredAt(Date var1);

   Date getCreatedAt();

   ItemStack buildItemStack(Player var1);

   ItemStack buildItemStack(Player var1, List<String> var2);

   default ItemStack buildItemStack(Player player, List<String> lore, Set<ItemPlaceholder> needed) {
      return this.buildItemStack(player, lore);
   }

   Placeholders createPlaceholders(Player var1);

   default Placeholders createPlaceholders(Player player, Set<ItemPlaceholder> needed) {
      return this.createPlaceholders(player);
   }

   String createStatus(Player var1);

   String getFormattedPrice();

   String getFormattedPrice(PriceFormat var1);

   String getFormattedExpireDate();

   String getRemainingTime();

   boolean isExpired();

   ItemStatus getStatus();

   void setStatus(ItemStatus var1);

   boolean canReceiveItem(Player var1);

   int getAmount();

   String getTranslationKey();

   UUID getBuyerUniqueId();

   String getBuyerName();

   void setBuyer(Player var1);

   void setBuyer(UUID var1, String var2);

   String getItemDisplay();

   Set<Category> getCategories();

   void setCategories(Set<Category> var1);

   default boolean hasCategory(Category category) {
      Set<Category> categories = this.getCategories();
      return categories != null && categories.contains(category);
   }

   default boolean hasCategory(String categoryId) {
      Set<Category> categories = this.getCategories();
      if (categories != null && categoryId != null) {
         for(Category category : categories) {
            if (categoryId.equals(category.getId())) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }
}
