package fr.maxlego08.zauctionhouse.buttons.shulker;

import fr.maxlego08.menu.api.button.PaginateButton;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCache;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCacheKey;
import java.util.List;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public class ShulkerContentButton extends PaginateButton {
   private final AuctionPlugin plugin;
   private final int emptySlot;

   public ShulkerContentButton(AuctionPlugin var1, int var2) {
      this.plugin = var1;
      this.emptySlot = var2;
   }

   public void onRender(Player var1, InventoryEngine var2) {
      PlayerCache var3 = this.plugin.getAuctionManager().getCache(var1);
      List<org.bukkit.inventory.ItemStack> var4 = var3.get(PlayerCacheKey.SHULKER_ITEMS);
      if (var4 != null && !var4.isEmpty()) {
         this.paginate(var4, var2, (var1x, var2x) -> {
            if (var2x != null && !var2x.getType().isAir()) {
               var2.addItem(var1x, var2x.clone());
            }

         });
      } else {
         var2.addItem(this.emptySlot, super.getCustomItemStack(var1, false, new Placeholders()));
      }
   }

   public int getPaginationSize(@NonNull Player var1) {
      PlayerCache var2 = this.plugin.getAuctionManager().getCache(var1);
      List<org.bukkit.inventory.ItemStack> var3 = var2.get(PlayerCacheKey.SHULKER_ITEMS);
      return var3 != null ? (int)var3.stream().filter((var0) -> var0 != null && !var0.getType().isAir()).count() : 0;
   }
}
