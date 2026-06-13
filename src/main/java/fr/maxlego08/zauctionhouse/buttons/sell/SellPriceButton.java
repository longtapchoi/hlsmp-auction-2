package fr.maxlego08.zauctionhouse.buttons.sell;

import fr.maxlego08.menu.api.MenuItemStack;
import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.zauctionhouse.api.AuctionManager;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCache;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCacheKey;
import fr.maxlego08.zauctionhouse.api.economy.AuctionEconomy;
import fr.maxlego08.zauctionhouse.api.item.ItemType;
import fr.maxlego08.zauctionhouse.api.messages.Message;
import java.math.BigDecimal;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public class SellPriceButton extends Button {
   private final AuctionPlugin plugin;
   private final BigDecimal leftClickAmount;
   private final BigDecimal rightClickAmount;
   private final BigDecimal shiftLeftClickAmount;
   private final BigDecimal shiftRightClickAmount;

   public SellPriceButton(Plugin var1, BigDecimal var2, BigDecimal var3, BigDecimal var4, BigDecimal var5) {
      this.plugin = (AuctionPlugin)var1;
      this.leftClickAmount = var2;
      this.rightClickAmount = var3;
      this.shiftLeftClickAmount = var4;
      this.shiftRightClickAmount = var5;
   }

   public void onClick(@NonNull Player var1, @NonNull InventoryClickEvent var2, @NonNull InventoryEngine var3, int var4, @NonNull Placeholders var5) {
      super.onClick(var1, var2, var3, var4, var5);
      AuctionManager var6 = this.plugin.getAuctionManager();
      PlayerCache var7 = var6.getCache(var1);
      BigDecimal var8 = (BigDecimal)var7.get(PlayerCacheKey.SELL_PRICE, BigDecimal.ZERO);
      AuctionEconomy var9 = this.plugin.getEconomyManager().getDefaultEconomy(ItemType.AUCTION);
      if (var9 == null) {
         this.plugin.getAuctionManager().message(var1, Message.SELL_ERROR_DEFAULT_ECONOMY);
      } else {
         AuctionEconomy var10 = (AuctionEconomy)var7.get(PlayerCacheKey.SELL_ECONOMY, var9);
         BigDecimal var11 = this.getAmountForClick(var2.getClick());
         BigDecimal var12 = var8.add(var11);
         BigDecimal var13 = var10.getMinPrice(ItemType.AUCTION);
         if (var12.compareTo(var13) < 0) {
            var12 = var13;
         }

         BigDecimal var14 = var10.getMaxPrice(ItemType.AUCTION);
         if (var12.compareTo(var14) > 0) {
            var12 = var14;
         }

         var7.set(PlayerCacheKey.SELL_PRICE, var12);
         this.plugin.getInventoriesLoader().getInventoryManager().updateInventory(var1);
      }
   }

   private BigDecimal getAmountForClick(ClickType var1) {
      BigDecimal var10000;
      switch (var1) {
         case RIGHT -> var10000 = this.rightClickAmount;
         case SHIFT_LEFT -> var10000 = this.shiftLeftClickAmount;
         case SHIFT_RIGHT -> var10000 = this.shiftRightClickAmount;
         default -> var10000 = this.leftClickAmount;
      }

      return var10000;
   }

   public ItemStack getCustomItemStack(@NotNull Player var1, boolean var2, @NotNull Placeholders var3) {
      MenuItemStack var4 = this.getItemStack();
      AuctionManager var5 = this.plugin.getAuctionManager();
      PlayerCache var6 = var5.getCache(var1);
      BigDecimal var7 = (BigDecimal)var6.get(PlayerCacheKey.SELL_PRICE, BigDecimal.ZERO);
      AuctionEconomy var8 = this.plugin.getEconomyManager().getDefaultEconomy(ItemType.AUCTION);
      if (var8 == null) {
         return this.getItemStack().build(var1, false, var3);
      } else {
         AuctionEconomy var9 = (AuctionEconomy)var6.get(PlayerCacheKey.SELL_ECONOMY, var8);
         var3.register("price", this.plugin.getEconomyManager().format((AuctionEconomy)var9, var7));
         var3.register("economy", var9.getDisplayName());
         var3.register("left_click_amount", this.formatAmount(this.leftClickAmount));
         var3.register("right_click_amount", this.formatAmount(this.rightClickAmount));
         var3.register("shift_left_click_amount", this.formatAmount(this.shiftLeftClickAmount));
         var3.register("shift_right_click_amount", this.formatAmount(this.shiftRightClickAmount));
         return var4.build(var1, false, var3);
      }
   }

   private String formatAmount(BigDecimal var1) {
      String var10000 = var1.compareTo(BigDecimal.ZERO) >= 0 ? "+" : "";
      return var10000 + var1.stripTrailingZeros().toPlainString();
   }
}
