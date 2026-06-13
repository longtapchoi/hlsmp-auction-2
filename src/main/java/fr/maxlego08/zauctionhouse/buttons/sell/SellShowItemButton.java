package fr.maxlego08.zauctionhouse.buttons.sell;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.engine.ItemButton;
import fr.maxlego08.menu.api.utils.LoreType;
import fr.maxlego08.menu.api.utils.MetaUpdater;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.zauctionhouse.api.AuctionManager;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCache;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCacheKey;
import fr.maxlego08.zauctionhouse.api.messages.Message;
import fr.maxlego08.zauctionhouse.api.rules.ItemRuleManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"unchecked", "rawtypes"})
public class SellShowItemButton extends Button {
   private final AuctionPlugin plugin;
   private final int emptySlot;

   public SellShowItemButton(AuctionPlugin var1, int var2) {
      this.plugin = var1;
      this.emptySlot = var2;
   }

   public void onRender(Player var1, InventoryEngine var2) {
      AuctionManager var3 = this.plugin.getAuctionManager();
      MetaUpdater var4 = this.plugin.getInventoriesLoader().getInventoryManager().getMeta();
      PlayerCache var5 = var3.getCache(var1);
      List var6 = this.plugin.getConfiguration().getItemLore().sellInventoryRemoveItem();
      Map var7 = (Map)var5.get(PlayerCacheKey.SELL_ITEMS);
      this.slots.forEach((var1x) -> {
         var2.getSpigotInventory().setItem(var1x, (ItemStack)null);
         var2.removeItem(var1x);
      });
      if (var7 != null && !var7.isEmpty()) {
         ArrayList var8 = new ArrayList(var7.entrySet());
         int var9 = Math.min(this.slots.size(), var8.size());

         for(int var10 = 0; var10 != var9; ++var10) {
            Map.Entry var11 = (Map.Entry)var8.get(var10);
            Integer var12 = (Integer)var11.getKey();
            ItemStack var13 = (ItemStack)var11.getValue();
            ItemStack var14 = var13.clone();
            ItemMeta var15 = var14.getItemMeta();
            var4.updateLore(var15, var6, LoreType.APPEND);
            var14.setItemMeta(var15);
            ItemButton var16 = var2.addItem((Integer)this.slots.get(var10), var14);
            if (var16 != null) {
               var16.setClick((var3x) -> {
                  if (this.removeFromSellList(var1, var12)) {
                     this.plugin.getInventoriesLoader().getInventoryManager().updateInventory(var1);
                  }

               });
            }
         }
      } else {
         var2.addItem(this.emptySlot, this.getItemStack().build(var1, false, new Placeholders()));
      }

   }

   public boolean hasSpecialRender() {
      return true;
   }

   public void onInventoryClick(@NotNull InventoryClickEvent var1, @NotNull Player var2, @NotNull InventoryEngine var3) {
      super.onInventoryClick(var1, var2, var3);
      if (var1.getClickedInventory() != null) {
         if (var1.getClickedInventory().getType() == InventoryType.PLAYER) {
            ItemStack var4 = var1.getCurrentItem();
            if (var4 != null && !var4.getType().isAir()) {
               var1.setCancelled(true);
               AuctionManager var5 = this.plugin.getAuctionManager();
               PlayerCache var6 = var5.getCache(var2);
               ItemRuleManager var7 = this.plugin.getItemRuleManager();
               if (var7.isBlacklistEnabled() && var7.isBlacklisted(var4)) {
                  var5.message(var2, Message.ITEM_BLACKLISTED);
               } else if (var7.isWhitelistEnabled() && !var7.isWhitelisted(var4)) {
                  var5.message(var2, Message.ITEM_WHITELISTED);
               } else {
                  int var8 = var1.getSlot();
                  Object var9 = (Map)var6.get(PlayerCacheKey.SELL_ITEMS);
                  if (var9 == null) {
                     var9 = new HashMap();
                  }

                  if (((Map)var9).containsKey(var8)) {
                     ((Map)var9).remove(var8);
                     var5.message(var2, Message.SELL_ITEM_REMOVED);
                  } else {
                     ((Map)var9).put(var8, var4.clone());
                     var5.message(var2, Message.SELL_ITEM_ADDED);
                  }

                  var6.set(PlayerCacheKey.SELL_ITEMS, var9);
                  this.plugin.getInventoriesLoader().getInventoryManager().updateInventory(var2);
               }
            }
         }
      }
   }

   public boolean removeFromSellList(Player var1, int var2) {
      AuctionManager var3 = this.plugin.getAuctionManager();
      PlayerCache var4 = var3.getCache(var1);
      Map var5 = (Map)var4.get(PlayerCacheKey.SELL_ITEMS);
      if (var5 != null && !var5.isEmpty()) {
         boolean var6 = var5.remove(var2) != null;
         var4.set(PlayerCacheKey.SELL_ITEMS, var5);
         return var6;
      } else {
         return false;
      }
   }
}
