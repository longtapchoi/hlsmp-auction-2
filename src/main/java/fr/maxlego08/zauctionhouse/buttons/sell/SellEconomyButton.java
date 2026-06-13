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
import fr.maxlego08.zauctionhouse.api.economy.EconomyManager;
import fr.maxlego08.zauctionhouse.api.item.ItemType;
import fr.maxlego08.zauctionhouse.api.messages.Message;
import java.math.BigDecimal;
import java.util.ArrayList;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class SellEconomyButton extends Button {
   private final AuctionPlugin plugin;

   public SellEconomyButton(Plugin var1) {
      this.plugin = (AuctionPlugin)var1;
   }

   public void onClick(Player var1, InventoryClickEvent var2, InventoryEngine var3, int var4, Placeholders var5) {
      super.onClick(var1, var2, var3, var4, var5);
      AuctionManager var6 = this.plugin.getAuctionManager();
      PlayerCache var7 = var6.getCache(var1);
      EconomyManager var8 = this.plugin.getEconomyManager();
      ArrayList var9 = new ArrayList(var8.getEconomies());
      if (!var9.isEmpty()) {
         AuctionEconomy var10 = var8.getDefaultEconomy(ItemType.AUCTION);
         if (var10 == null) {
            this.plugin.getAuctionManager().message(var1, Message.SELL_ERROR_DEFAULT_ECONOMY);
         } else {
            AuctionEconomy var11 = (AuctionEconomy)var7.get(PlayerCacheKey.SELL_ECONOMY, var10);
            int var12 = var9.indexOf(var11);
            if (var12 < 0) {
               var12 = 0;
            }

            int var13;
            if (var2.getClick() != ClickType.RIGHT && var2.getClick() != ClickType.SHIFT_RIGHT) {
               var13 = (var12 + 1) % var9.size();
            } else {
               var13 = (var12 - 1 + var9.size()) % var9.size();
            }

            AuctionEconomy var14 = (AuctionEconomy)var9.get(var13);
            var7.set(PlayerCacheKey.SELL_ECONOMY, var14);
            BigDecimal var15 = (BigDecimal)var7.get(PlayerCacheKey.SELL_PRICE, BigDecimal.ZERO);
            BigDecimal var16 = var14.getMinPrice(ItemType.AUCTION);
            BigDecimal var17 = var14.getMaxPrice(ItemType.AUCTION);
            if (var15.compareTo(var16) < 0) {
               var7.set(PlayerCacheKey.SELL_PRICE, var16);
            } else if (var15.compareTo(var17) > 0) {
               var7.set(PlayerCacheKey.SELL_PRICE, var17);
            }

            this.plugin.getInventoriesLoader().getInventoryManager().updateInventory(var1);
         }
      }
   }

   public ItemStack getCustomItemStack(@NotNull Player var1, boolean var2, @NotNull Placeholders var3) {
      MenuItemStack var4 = this.getItemStack();
      AuctionManager var5 = this.plugin.getAuctionManager();
      PlayerCache var6 = var5.getCache(var1);
      EconomyManager var7 = this.plugin.getEconomyManager();
      AuctionEconomy var8 = var7.getDefaultEconomy(ItemType.AUCTION);
      if (var8 == null) {
         return var4.build(var1, false, var3);
      } else {
         AuctionEconomy var9 = (AuctionEconomy)var6.get(PlayerCacheKey.SELL_ECONOMY, var8);
         BigDecimal var10 = (BigDecimal)var6.get(PlayerCacheKey.SELL_PRICE, BigDecimal.ZERO);
         var3.register("economy", var9.getDisplayName());
         var3.register("economy_name", var9.getName());
         var3.register("price", var7.format((AuctionEconomy)var9, var10));
         return var4.build(var1, false, var3);
      }
   }
}
