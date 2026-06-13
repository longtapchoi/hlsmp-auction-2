package fr.maxlego08.zauctionhouse.api.category;

import fr.maxlego08.zauctionhouse.api.item.Item;
import java.util.List;
import java.util.Optional;
import org.bukkit.inventory.ItemStack;

public interface CategoryManager {
   void loadCategories();

   List<Category> getCategories();

   Optional<Category> getCategory(String var1);

   Category getCategoryFor(ItemStack var1);

   List<Category> getCategoriesFor(ItemStack var1);

   boolean matches(ItemStack var1, Category var2);

   Category getMiscCategory();

   boolean isEnabled();

   int getCategoryCount();

   void applyCategories(Item var1);

   long getItemCountForCategory(String var1);

   void invalidateCategoryCountCache();

   String getAllCategoryName();
}
