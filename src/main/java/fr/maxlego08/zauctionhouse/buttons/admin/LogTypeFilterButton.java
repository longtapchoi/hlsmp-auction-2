package fr.maxlego08.zauctionhouse.buttons.admin;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCache;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCacheKey;
import fr.maxlego08.zauctionhouse.api.log.LogType;
import java.util.List;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public class LogTypeFilterButton extends Button {
   private final AuctionPlugin plugin;
   private final String enableText;
   private final String disableText;
   private final List<LogType> logTypes;
   private final Map<LogType, String> typeNames;
   private final String allTypesName;

   public LogTypeFilterButton(AuctionPlugin var1, String var2, String var3, List<LogType> var4, Map<LogType, String> var5, String var6) {
      this.plugin = var1;
      this.enableText = var2;
      this.disableText = var3;
      this.logTypes = var4;
      this.typeNames = var5;
      this.allTypesName = var6;
   }

   public boolean isPermanent() {
      return true;
   }

   public ItemStack getCustomItemStack(@NotNull Player var1, boolean var2, @NotNull Placeholders var3) {
      PlayerCache var4 = this.plugin.getAuctionManager().getCache(var1);
      LogType var5 = (LogType)var4.get(PlayerCacheKey.ADMIN_LOGS_TYPE_FILTER);
      String var6 = (var5 == null ? this.enableText : this.disableText).replace("%type%", this.allTypesName);
      var3.register("ALL", var6);

      for(LogType var8 : this.logTypes) {
         String var9 = (String)this.typeNames.getOrDefault(var8, var8.getDefaultDisplayName());
         String var10 = (var8 == var5 ? this.enableText : this.disableText).replace("%type%", var9);
         var3.register(var8.name(), var10);
      }

      return this.getItemStack().build(var1, false, var3);
   }

   public void onClick(@NonNull Player var1, @NonNull InventoryClickEvent var2, @NonNull InventoryEngine var3, int var4, @NonNull Placeholders var5) {
      super.onClick(var1, var2, var3, var4, var5);
      PlayerCache var6 = this.plugin.getAuctionManager().getCache(var1);
      LogType var7 = (LogType)var6.get(PlayerCacheKey.ADMIN_LOGS_TYPE_FILTER);
      LogType var8 = this.getNextFilter(var7, var2.isRightClick());
      var6.set(PlayerCacheKey.ADMIN_LOGS_TYPE_FILTER, var8);
      var6.set(PlayerCacheKey.CURRENT_PAGE, 1);
      this.plugin.getInventoriesLoader().getInventoryManager().updateInventory(var1);
   }

   private LogType getNextFilter(LogType var1, boolean var2) {
      int var3 = this.logTypes.size() + 1;
      int var4;
      if (var1 == null) {
         var4 = 0;
      } else {
         int var5 = this.logTypes.indexOf(var1);
         var4 = var5 == -1 ? 0 : var5 + 1;
      }

      int var7 = var2 ? -1 : 1;
      int var6 = (var4 + var7 + var3) % var3;
      return var6 == 0 ? null : (LogType)this.logTypes.get(var6 - 1);
   }
}
