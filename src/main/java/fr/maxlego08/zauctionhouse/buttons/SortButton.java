package fr.maxlego08.zauctionhouse.buttons;

import fr.maxlego08.menu.api.MenuItemStack;
import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCache;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCacheKey;
import fr.maxlego08.zauctionhouse.api.configuration.records.SortConfiguration;
import fr.maxlego08.zauctionhouse.api.item.SortItem;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

@SuppressWarnings({"unchecked", "rawtypes"})
public class SortButton extends Button {
   private final AuctionPlugin plugin;
   private final String enableText;
   private final String disableText;
   private final MenuItemStack loadingItemStack;
   private final List<SortItem> sortItems;

   public SortButton(AuctionPlugin var1, String var2, String var3, MenuItemStack var4, List<SortItem> var5) {
      this.plugin = var1;
      this.enableText = var2;
      this.disableText = var3;
      this.loadingItemStack = var4;
      this.sortItems = List.copyOf(var5);
   }

   public boolean isPermanent() {
      return true;
   }

   public ItemStack getCustomItemStack(@NotNull Player var1, boolean var2, @NotNull Placeholders var3) {
      SortConfiguration var4 = this.plugin.getConfiguration().getSort();
      PlayerCache var5 = this.plugin.getAuctionManager().getCache(var1);
      MenuItemStack var6 = this.getItemStack();
      this.sortItems.forEach((var4x) -> {
         SortItem var5x = (SortItem)var5.get(PlayerCacheKey.ITEM_SORT, var4.defaultSort());
         var3.register(var4x.name(), (var4x == var5x ? this.enableText : this.disableText).replace("%sorting%", (CharSequence)var4.sortItems().get(var4x)));
      });
      return var6.build(var1, false, var3);
   }

   public void onClick(@NonNull Player var1, @NonNull InventoryClickEvent var2, @NonNull InventoryEngine var3, int var4, @NonNull Placeholders var5) {
      super.onClick(var1, var2, var3, var4, var5);
      SortConfiguration var6 = this.plugin.getConfiguration().getSort();
      PlayerCache var7 = this.plugin.getAuctionManager().getCache(var1);
      if (!(Boolean)var7.get(PlayerCacheKey.ITEM_SORT_LOADING)) {
         if (!this.sortItems.isEmpty()) {
            SortItem var8 = (SortItem)var7.get(PlayerCacheKey.ITEM_SORT, var6.defaultSort());
            int var9 = this.sortItems.indexOf(var8);
            if (var9 != -1) {
               var7.set(PlayerCacheKey.ITEM_SORT_LOADING, true);
               int var10 = var2.isRightClick() ? -1 : 1;
               int var11 = this.sortItems.size();
               int var12 = (var9 + var10 + var11) % var11;
               SortItem var13 = (SortItem)this.sortItems.get(var12);
               ItemStack var14 = this.loadingItemStack.build(var1);

               for(Integer var16 : this.getSlots()) {
                  var3.getSpigotInventory().setItem(var16, var14);
               }

               this.plugin.getScheduler().runAsync((var4x) -> {
                  var7.set(PlayerCacheKey.ITEM_SORT, var13);
                  var7.remove(PlayerCacheKey.ITEMS_LISTED);
                  this.plugin.getAuctionManager().getItemsListedForSale(var1);
                  this.plugin.getScheduler().runAtEntity(var1, (var3) -> {
                     var7.set(PlayerCacheKey.ITEM_SORT_LOADING, false);
                     this.plugin.getInventoriesLoader().getInventoryManager().updateInventory(var1);
                  });
               });
            }
         }
      }
   }
}
