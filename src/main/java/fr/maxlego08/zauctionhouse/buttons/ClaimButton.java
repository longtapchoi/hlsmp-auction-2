package fr.maxlego08.zauctionhouse.buttons;

import fr.maxlego08.menu.api.MenuItemStack;
import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCache;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCacheKey;
import fr.maxlego08.zauctionhouse.api.economy.AuctionEconomy;
import fr.maxlego08.zauctionhouse.api.economy.EconomyManager;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.logging.Logger;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public class ClaimButton extends Button {
   private final AuctionPlugin plugin;
   private final MenuItemStack loadingItemStack;

   public ClaimButton(AuctionPlugin var1, MenuItemStack var2) {
      this.plugin = var1;
      this.loadingItemStack = var2;
   }

   public void onInventoryOpen(@NotNull Player var1, @NotNull InventoryEngine var2, @NotNull Placeholders var3) {
      super.onInventoryOpen(var1, var2, var3);
      PlayerCache var4 = this.plugin.getAuctionManager().getCache(var1);
      if (!var4.has(PlayerCacheKey.PENDING_MONEY_DATA)) {
         Boolean var5 = (Boolean)var4.get(PlayerCacheKey.PENDING_MONEY_LOADING, false);
         if (!var5) {
            var4.set(PlayerCacheKey.PENDING_MONEY_LOADING, true);
            this.plugin.getAuctionManager().getClaimService().getPendingMoneyByEconomy(var1.getUniqueId()).thenAccept((var4x) -> {
               var4.set(PlayerCacheKey.PENDING_MONEY_DATA, var4x);
               var4.set(PlayerCacheKey.PENDING_MONEY_LOADING, false);
               this.plugin.getScheduler().runAtEntity(var1, (var3) -> {
                  if (var1.isOnline()) {
                     var2.getSpigotInventory().setItem(this.getSlot(), this.getCustomItemStack(var1, false, new Placeholders()));
                  }

               });
            });
         }
      }
   }

   public ItemStack getCustomItemStack(@NotNull Player var1, boolean var2, @NotNull Placeholders var3) {
      PlayerCache var4 = this.plugin.getAuctionManager().getCache(var1);
      EconomyManager var5 = this.plugin.getEconomyManager();
      if ((Boolean)var4.get(PlayerCacheKey.PENDING_MONEY_LOADING, false)) {
         return this.loadingItemStack.build(var1, false, var3);
      } else {
         Map var6 = (Map)var4.get(PlayerCacheKey.PENDING_MONEY_DATA);
         BigDecimal var7 = BigDecimal.ZERO;
         if (var6 != null && !var6.isEmpty()) {
            var7 = (BigDecimal)var6.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
         }

         for(AuctionEconomy var9 : var5.getEconomies()) {
            BigDecimal var10 = var6 != null ? (BigDecimal)var6.getOrDefault(var9.getName(), BigDecimal.ZERO) : BigDecimal.ZERO;
            var3.register("pending_" + var9.getName(), var5.format((AuctionEconomy)var9, var10));
         }

         Collection var11 = var5.getEconomies();
         if (!var11.isEmpty()) {
            AuctionEconomy var12 = (AuctionEconomy)var11.iterator().next();
            var3.register("pending_total", var5.format((AuctionEconomy)var12, var7));
         } else {
            var3.register("pending_total", var7.toPlainString());
         }

         var3.register("has_pending", String.valueOf(var7.compareTo(BigDecimal.ZERO) > 0));
         return this.getItemStack().build(var1, false, var3);
      }
   }

   public void onClick(@NonNull Player var1, @NonNull InventoryClickEvent var2, @NonNull InventoryEngine var3, int var4, @NonNull Placeholders var5) {
      PlayerCache var6 = this.plugin.getAuctionManager().getCache(var1);
      Map var7 = (Map)var6.get(PlayerCacheKey.PENDING_MONEY_DATA);
      if (var7 != null && !var7.isEmpty()) {
         BigDecimal var8 = (BigDecimal)var7.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
         if (var8.compareTo(BigDecimal.ZERO) > 0) {
            var6.remove(PlayerCacheKey.PENDING_MONEY_DATA, PlayerCacheKey.PENDING_MONEY_LOADING);
            this.plugin.getAuctionManager().getClaimService().claimMoney(var1).thenRun(() -> this.plugin.getScheduler().runAtEntity(var1, (var2) -> {
                  if (var1.isOnline()) {
                     this.plugin.getAuctionManager().updateInventory(var1);
                  }

               })).exceptionally((var2x) -> {
               Logger var10000 = this.plugin.getLogger();
               String var10001 = var1.getName();
               var10000.warning("Failed to claim money for " + var10001 + ": " + var2x.getMessage());
               return null;
            });
         }
      }
   }
}
