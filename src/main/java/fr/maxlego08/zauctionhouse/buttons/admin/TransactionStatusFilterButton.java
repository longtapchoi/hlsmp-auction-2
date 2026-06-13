package fr.maxlego08.zauctionhouse.buttons.admin;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCache;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCacheKey;
import fr.maxlego08.zauctionhouse.api.transaction.TransactionStatus;
import java.util.List;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public class TransactionStatusFilterButton extends Button {
   private final AuctionPlugin plugin;
   private final String enableText;
   private final String disableText;
   private final List<TransactionStatus> statuses;
   private final Map<TransactionStatus, String> statusNames;
   private final String allStatusesName;

   public TransactionStatusFilterButton(AuctionPlugin var1, String var2, String var3, List<TransactionStatus> var4, Map<TransactionStatus, String> var5, String var6) {
      this.plugin = var1;
      this.enableText = var2;
      this.disableText = var3;
      this.statuses = var4;
      this.statusNames = var5;
      this.allStatusesName = var6;
   }

   public boolean isPermanent() {
      return true;
   }

   public ItemStack getCustomItemStack(@NotNull Player var1, boolean var2, @NotNull Placeholders var3) {
      PlayerCache var4 = this.plugin.getAuctionManager().getCache(var1);
      TransactionStatus var5 = (TransactionStatus)var4.get(PlayerCacheKey.ADMIN_TRANSACTIONS_STATUS_FILTER);
      String var6 = (var5 == null ? this.enableText : this.disableText).replace("%status%", this.allStatusesName);
      var3.register("ALL", var6);

      for(TransactionStatus var8 : this.statuses) {
         String var9 = (String)this.statusNames.getOrDefault(var8, var8.getDefaultDisplayName());
         String var10 = (var8 == var5 ? this.enableText : this.disableText).replace("%status%", var9);
         var3.register(var8.name(), var10);
      }

      return this.getItemStack().build(var1, false, var3);
   }

   public void onClick(@NonNull Player var1, @NonNull InventoryClickEvent var2, @NonNull InventoryEngine var3, int var4, @NonNull Placeholders var5) {
      super.onClick(var1, var2, var3, var4, var5);
      PlayerCache var6 = this.plugin.getAuctionManager().getCache(var1);
      TransactionStatus var7 = (TransactionStatus)var6.get(PlayerCacheKey.ADMIN_TRANSACTIONS_STATUS_FILTER);
      TransactionStatus var8 = this.getNextFilter(var7, var2.isRightClick());
      var6.set(PlayerCacheKey.ADMIN_TRANSACTIONS_STATUS_FILTER, var8);
      var6.set(PlayerCacheKey.CURRENT_PAGE, 1);
      this.plugin.getInventoriesLoader().getInventoryManager().updateInventory(var1);
   }

   private TransactionStatus getNextFilter(TransactionStatus var1, boolean var2) {
      int var3 = this.statuses.size() + 1;
      int var4;
      if (var1 == null) {
         var4 = 0;
      } else {
         int var5 = this.statuses.indexOf(var1);
         var4 = var5 == -1 ? 0 : var5 + 1;
      }

      int var7 = var2 ? -1 : 1;
      int var6 = (var4 + var7 + var3) % var3;
      return var6 == 0 ? null : (TransactionStatus)this.statuses.get(var6 - 1);
   }
}
