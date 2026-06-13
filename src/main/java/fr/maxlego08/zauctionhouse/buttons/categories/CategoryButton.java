package fr.maxlego08.zauctionhouse.buttons.categories;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCache;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCacheKey;
import fr.maxlego08.zauctionhouse.api.category.Category;
import fr.maxlego08.zauctionhouse.api.category.CategoryManager;
import fr.maxlego08.zauctionhouse.api.inventories.Inventories;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jspecify.annotations.NonNull;

public class CategoryButton extends Button {
   private final AuctionPlugin plugin;
   private final String categoryId;

   public CategoryButton(AuctionPlugin var1, String var2) {
      this.plugin = var1;
      this.categoryId = var2;
   }

   public void onClick(@NonNull Player var1, @NonNull InventoryClickEvent var2, @NonNull InventoryEngine var3, int var4, @NonNull Placeholders var5) {
      super.onClick(var1, var2, var3, var4, var5);
      PlayerCache var6 = this.plugin.getAuctionManager().getCache(var1);
      if (this.categoryId.equalsIgnoreCase("all")) {
         var6.remove(PlayerCacheKey.CURRENT_CATEGORY);
         var6.remove(PlayerCacheKey.ITEMS_LISTED);
         this.plugin.getInventoriesLoader().openInventory(var1, Inventories.AUCTION);
      } else {
         CategoryManager var7 = this.plugin.getCategoryManager();
         Optional var8 = var7.getCategory(this.categoryId);
         if (var8.isEmpty()) {
            this.plugin.getLogger().warning("Category not found: " + this.categoryId);
         } else {
            Category var9 = (Category)var8.get();
            var6.set(PlayerCacheKey.CURRENT_CATEGORY, var9);
            var6.remove(PlayerCacheKey.ITEMS_LISTED);
            this.plugin.getInventoriesLoader().openInventory(var1, Inventories.AUCTION);
         }
      }
   }

   public String getCategoryId() {
      return this.categoryId;
   }
}
