package fr.maxlego08.zauctionhouse.buttons.history;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCache;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCacheKey;
import fr.maxlego08.zauctionhouse.api.history.HistorySortType;
import java.util.List;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public class HistorySortButton extends Button {
   private final AuctionPlugin plugin;
   private final String enableText;
   private final String disableText;
   private final List<HistorySortType> sortTypes;
   private final Map<HistorySortType, String> sortNames;

   public HistorySortButton(AuctionPlugin var1, String var2, String var3, List<HistorySortType> var4, Map<HistorySortType, String> var5) {
      this.plugin = var1;
      this.enableText = var2;
      this.disableText = var3;
      this.sortTypes = var4;
      this.sortNames = var5;
   }

   public boolean isPermanent() {
      return true;
   }

   public ItemStack getCustomItemStack(@NotNull Player var1, boolean var2, @NotNull Placeholders var3) {
      PlayerCache var4 = this.plugin.getAuctionManager().getCache(var1);
      HistorySortType var5 = (HistorySortType)var4.get(PlayerCacheKey.HISTORY_SORT, HistorySortType.DATE_DESC);

      for(HistorySortType var7 : this.sortTypes) {
         String var8 = (String)this.sortNames.getOrDefault(var7, var7.getDefaultDisplayName());
         String var9 = (var7 == var5 ? this.enableText : this.disableText).replace("%sorting%", var8);
         var3.register(var7.name(), var9);
      }

      return this.getItemStack().build(var1, false, var3);
   }

   public void onClick(@NonNull Player var1, @NonNull InventoryClickEvent var2, @NonNull InventoryEngine var3, int var4, @NonNull Placeholders var5) {
      super.onClick(var1, var2, var3, var4, var5);
      if (!this.sortTypes.isEmpty()) {
         PlayerCache var6 = this.plugin.getAuctionManager().getCache(var1);
         HistorySortType var7 = (HistorySortType)var6.get(PlayerCacheKey.HISTORY_SORT, HistorySortType.DATE_DESC);
         int var8 = this.sortTypes.indexOf(var7);
         if (var8 == -1) {
            var8 = 0;
         }

         int var9 = var2.isRightClick() ? -1 : 1;
         int var10 = this.sortTypes.size();
         int var11 = (var8 + var9 + var10) % var10;
         HistorySortType var12 = (HistorySortType)this.sortTypes.get(var11);
         var6.set(PlayerCacheKey.HISTORY_SORT, var12);
         this.plugin.getInventoriesLoader().getInventoryManager().updateInventory(var1);
      }
   }
}
