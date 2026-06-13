package fr.maxlego08.zauctionhouse.buttons.sell;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.zauctionhouse.api.AuctionManager;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCache;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCacheKey;
import fr.maxlego08.zauctionhouse.api.economy.AuctionEconomy;
import fr.maxlego08.zauctionhouse.api.economy.EconomyManager;
import fr.maxlego08.zauctionhouse.api.item.ItemType;
import fr.maxlego08.zauctionhouse.api.messages.Message;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public class SellConfirmButton extends Button {
   private final AuctionPlugin plugin;

   public SellConfirmButton(Plugin var1) {
      this.plugin = (AuctionPlugin)var1;
   }

   public void onInventoryOpen(@NotNull Player var1, @NotNull InventoryEngine var2, @NotNull Placeholders var3) {
      this.createPlaceholders(var3, var1);
      super.onInventoryOpen(var1, var2, var3);
   }

   public void onClick(@NonNull Player var1, @NonNull InventoryClickEvent var2, @NonNull InventoryEngine var3, int var4, @NonNull Placeholders var5) {
      super.onClick(var1, var2, var3, var4, var5);
      AuctionManager var6 = this.plugin.getAuctionManager();
      PlayerCache var7 = var6.getCache(var1);
      Map<Integer, ItemStack> var8 = var7.get(PlayerCacheKey.SELL_ITEMS);
      if (var8 == null || var8.isEmpty()) {
         ItemStack var9 = var1.getInventory().getItemInMainHand();
         if (var9.getType().isAir()) {
            this.plugin.getAuctionManager().message(var1, Message.SELL_INVENTORY_EMPTY);
            return;
         }

         var8 = new HashMap<>();
         var8.put(-1, var9.clone());
      }

      BigDecimal var14 = var7.get(PlayerCacheKey.SELL_PRICE, BigDecimal.ZERO);
      AuctionEconomy var10 = this.plugin.getEconomyManager().getDefaultEconomy(ItemType.AUCTION);
      if (var10 == null) {
         this.plugin.getAuctionManager().message(var1, Message.SELL_ERROR_DEFAULT_ECONOMY);
      } else {
         AuctionEconomy var11 = var7.get(PlayerCacheKey.SELL_ECONOMY, var10);
         long var12 = var7.<Long>get(PlayerCacheKey.SELL_EXPIRED_AT, 0L);
         var7.set(PlayerCacheKey.CURRENT_PAGE, 1);
         var7.remove(PlayerCacheKey.SELL_ITEMS);
         var1.closeInventory();
         var6.getSellService().sellAuctionItems(var1, var14, var12, var8, var11);
      }
   }

   public ItemStack getCustomItemStack(@NotNull Player var1, boolean var2, @NotNull Placeholders var3) {
      this.createPlaceholders(var3, var1);
      return this.getItemStack().build(var1, false, var3);
   }

   private void createPlaceholders(Placeholders var1, Player var2) {
      AuctionManager var3 = this.plugin.getAuctionManager();
      PlayerCache var4 = var3.getCache(var2);
      EconomyManager var5 = this.plugin.getEconomyManager();
      BigDecimal var6 = var4.get(PlayerCacheKey.SELL_PRICE, BigDecimal.ZERO);
      AuctionEconomy var7 = var5.getDefaultEconomy(ItemType.AUCTION);
      AuctionEconomy var8 = var7 != null ? var4.get(PlayerCacheKey.SELL_ECONOMY, var7) : null;
      Map<Integer, ItemStack> var9 = var4.get(PlayerCacheKey.SELL_ITEMS);
      int var10 = 0;
      int var11 = 0;
      if (var9 != null && !var9.isEmpty()) {
         var10 = var9.size();
         var11 = var9.values().stream().filter((var0) -> var0 != null && !var0.getType().isAir()).mapToInt(ItemStack::getAmount).sum();
      }

      if (var8 != null) {
         var1.register("price", var5.format(var8, var6));
         var1.register("economy", var8.getDisplayName());
         var1.register("economy_name", var8.getName());
      } else {
         var1.register("price", var6.toPlainString());
         var1.register("economy", "N/A");
         var1.register("economy_name", "N/A");
      }

      var1.register("item_count", String.valueOf(var10));
      var1.register("total_amount", String.valueOf(var11));
   }
}
