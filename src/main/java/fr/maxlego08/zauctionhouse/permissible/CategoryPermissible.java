package fr.maxlego08.zauctionhouse.permissible;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.requirement.Action;
import fr.maxlego08.menu.api.requirement.Permissible;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCache;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCacheKey;
import fr.maxlego08.zauctionhouse.api.category.Category;
import java.util.List;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CategoryPermissible extends Permissible {
   private final AuctionPlugin plugin;
   private final String categoryId;

   public CategoryPermissible(@NotNull AuctionPlugin var1, @NotNull String var2, @NotNull List<Action> var3, @NotNull List<Action> var4) {
      super(var3, var4);
      this.plugin = var1;
      this.categoryId = var2;
   }

   public boolean hasPermission(@NotNull Player var1, @Nullable Button var2, @NotNull InventoryEngine var3, @NotNull Placeholders var4) {
      PlayerCache var5 = this.plugin.getAuctionManager().getCache(var1);
      Category var6 = (Category)var5.get(PlayerCacheKey.CURRENT_CATEGORY);
      String var7 = var6 != null ? var6.getId() : "main";
      return var7.equalsIgnoreCase(this.categoryId);
   }

   public boolean isValid() {
      return this.categoryId != null && !this.categoryId.isEmpty();
   }

   public String getCategoryId() {
      return this.categoryId;
   }
}
