package fr.maxlego08.zauctionhouse.buttons.categories;

import fr.maxlego08.menu.api.MenuItemStack;
import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCache;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCacheKey;
import fr.maxlego08.zauctionhouse.api.category.Category;
import fr.maxlego08.zauctionhouse.api.category.CategoryManager;
import fr.maxlego08.zauctionhouse.api.inventories.Inventories;
import java.util.List;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

@SuppressWarnings({"unchecked", "rawtypes"})
public class CategorySwitcherButton extends Button {
   private final AuctionPlugin plugin;
   private final String enableText;
   private final String disableText;
   private final List<String> categories;

   public CategorySwitcherButton(AuctionPlugin var1, String var2, String var3, List<String> var4) {
      this.plugin = var1;
      this.enableText = var2;
      this.disableText = var3;
      this.categories = List.copyOf(var4);
   }

   public boolean isPermanent() {
      return true;
   }

   public ItemStack getCustomItemStack(@NotNull Player var1, boolean var2, @NotNull Placeholders var3) {
      MenuItemStack var4 = this.getItemStack();
      PlayerCache var5 = this.plugin.getAuctionManager().getCache(var1);
      Category var6 = (Category)var5.get(PlayerCacheKey.CURRENT_CATEGORY);
      String var7 = var6 != null ? var6.getId() : "main";
      var3.register("category", var6 != null ? var6.getDisplayName() : this.plugin.getCategoryManager().getAllCategoryName());
      CategoryManager var8 = this.plugin.getCategoryManager();
      this.categories.forEach((var4x) -> {
         String var5 = (String)var8.getCategory(var4x).map(Category::getDisplayName).orElse(this.plugin.getCategoryManager().getAllCategoryName());
         String var6 = (var4x.equalsIgnoreCase(var7) ? this.enableText : this.disableText).replace("%category%", var5);
         var3.register(var4x, var6);
      });
      return var4.build(var1, false, var3);
   }

   public void onClick(@NonNull Player var1, @NonNull InventoryClickEvent var2, @NonNull InventoryEngine var3, int var4, @NonNull Placeholders var5) {
      super.onClick(var1, var2, var3, var4, var5);
      if (!this.categories.isEmpty()) {
         PlayerCache var6 = this.plugin.getAuctionManager().getCache(var1);
         Category var7 = (Category)var6.get(PlayerCacheKey.CURRENT_CATEGORY);
         String var8 = var7 != null ? var7.getId() : "main";
         int var9 = -1;

         for(int var10 = 0; var10 < this.categories.size(); ++var10) {
            if (((String)this.categories.get(var10)).equalsIgnoreCase(var8)) {
               var9 = var10;
               break;
            }
         }

         if (var9 == -1) {
            var9 = 0;
         }

         int var15 = var2.isRightClick() ? -1 : 1;
         int var11 = (var9 + var15 + this.categories.size()) % this.categories.size();
         String var12 = (String)this.categories.get(var11);
         if (var12.equalsIgnoreCase("main")) {
            var6.remove(PlayerCacheKey.CURRENT_CATEGORY);
         } else {
            CategoryManager var13 = this.plugin.getCategoryManager();
            Optional var14 = var13.getCategory(var12);
            if (var14.isEmpty()) {
               this.plugin.getLogger().warning("Category not found: " + var12);
               return;
            }

            var6.set(PlayerCacheKey.CURRENT_CATEGORY, (Category)var14.get());
         }

         var6.remove(PlayerCacheKey.ITEMS_LISTED);
         this.plugin.getInventoriesLoader().openInventory(var1, Inventories.AUCTION);
      }
   }
}
