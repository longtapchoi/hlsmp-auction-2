package fr.maxlego08.zauctionhouse.loader.permissibles;

import fr.maxlego08.menu.api.ButtonManager;
import fr.maxlego08.menu.api.loader.PermissibleLoader;
import fr.maxlego08.menu.api.requirement.Permissible;
import fr.maxlego08.menu.api.utils.TypedMapAccessor;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.permissible.CategoryPermissible;
import java.io.File;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CategoryPermissibleLoader extends PermissibleLoader {
   private final AuctionPlugin plugin;

   public CategoryPermissibleLoader(@NotNull AuctionPlugin var1) {
      super("zauctionhouse_category");
      this.plugin = var1;
   }

   public @Nullable Permissible load(@NotNull String var1, @NotNull TypedMapAccessor var2, @NotNull File var3) {
      ButtonManager var4 = this.plugin.getInventoriesLoader().getButtonManager();
      String var5 = var2.getString("category", "main");
      List var6 = this.loadAction(var4, var2, "deny", var1, var3);
      List var7 = this.loadAction(var4, var2, "success", var1, var3);
      return new CategoryPermissible(this.plugin, var5, var6, var7);
   }
}
