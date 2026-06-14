package fr.maxlego08.zauctionhouse.buttons.admin;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCache;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCacheKey;
import fr.maxlego08.zauctionhouse.api.filter.DateFilter;
import java.util.List;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public class LogDateFilterButton extends Button {
   private final AuctionPlugin plugin;
   private final String enableText;
   private final String disableText;
   private final List<DateFilter> dateFilters;
   private final Map<DateFilter, String> filterNames;

   public LogDateFilterButton(AuctionPlugin var1, String var2, String var3, List<DateFilter> var4, Map<DateFilter, String> var5) {
      this.plugin = var1;
      this.enableText = var2;
      this.disableText = var3;
      this.dateFilters = var4;
      this.filterNames = var5;
   }

   public boolean isPermanent() {
      return true;
   }

   public ItemStack getCustomItemStack(@NotNull Player var1, boolean var2, @NotNull Placeholders var3) {
      PlayerCache var4 = this.plugin.getAuctionManager().getCache(var1);
      DateFilter var5 = (DateFilter)var4.get(PlayerCacheKey.ADMIN_LOGS_DATE_FILTER, DateFilter.ALL);

      for(DateFilter var7 : this.dateFilters) {
         String var8 = (String)this.filterNames.getOrDefault(var7, var7.getDisplayName());
         String var9 = (var7 == var5 ? this.enableText : this.disableText).replace("%date%", var8);
         var3.register(var7.name(), var9);
      }

      return this.getItemStack().build(var1, false, var3);
   }

   public void onClick(@NonNull Player var1, @NonNull InventoryClickEvent var2, @NonNull InventoryEngine var3, int var4, @NonNull Placeholders var5) {
      super.onClick(var1, var2, var3, var4, var5);
      if (!this.dateFilters.isEmpty()) {
         PlayerCache var6 = this.plugin.getAuctionManager().getCache(var1);
         DateFilter var7 = (DateFilter)var6.get(PlayerCacheKey.ADMIN_LOGS_DATE_FILTER, DateFilter.ALL);
         int var8 = this.dateFilters.indexOf(var7);
         if (var8 == -1) {
            var8 = 0;
         }

         int var9 = var2.isRightClick() ? -1 : 1;
         int var10 = this.dateFilters.size();
         int var11 = (var8 + var9 + var10) % var10;
         DateFilter var12 = (DateFilter)this.dateFilters.get(var11);
         var6.set(PlayerCacheKey.ADMIN_LOGS_DATE_FILTER, var12);
         var6.set(PlayerCacheKey.CURRENT_PAGE, 1);
         this.plugin.getInventoriesLoader().getInventoryManager().updateInventory(var1);
      }
   }
}
