package fr.maxlego08.zauctionhouse.buttons.shulker;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCache;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCacheKey;
import fr.maxlego08.zauctionhouse.api.hooks.itemcontent.ItemContentManager;
import fr.maxlego08.zauctionhouse.api.item.Item;
import fr.maxlego08.zauctionhouse.api.item.items.AuctionItem;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ShulkerInfoButton extends Button {
   private final AuctionPlugin plugin;

   public ShulkerInfoButton(Plugin var1) {
      this.plugin = (AuctionPlugin)var1;
   }

   public void onInventoryOpen(@NotNull Player var1, @NotNull InventoryEngine var2, @NotNull Placeholders var3) {
      this.registerPlaceholders(var1, var3);
      super.onInventoryOpen(var1, var2, var3);
   }

   public @Nullable ItemStack getCustomItemStack(@NotNull Player var1, boolean var2, @NotNull Placeholders var3) {
      ItemContentManager var4 = this.plugin.getItemContentManager();
      PlayerCache var5 = this.plugin.getAuctionManager().getCache(var1);
      Item var6 = (Item)var5.get(PlayerCacheKey.ITEM_SHOW);
      if (var6 instanceof AuctionItem var7) {
         List var8 = var4.getContainers(var7.getItemStacks());
         if (var8.isEmpty()) {
            return super.getCustomItemStack(var1, var2, var3);
         } else {
            int var9 = (Integer)var5.get(PlayerCacheKey.SHULKER_INDEX, 0);
            if (var9 < 0 || var9 >= var8.size()) {
               var9 = 0;
            }

            this.registerPlaceholders(var1, var3);
            return ((ItemStack)var8.get(var9)).clone();
         }
      } else {
         return super.getCustomItemStack(var1, var2, var3);
      }
   }

   private void registerPlaceholders(Player var1, Placeholders var2) {
      ItemContentManager var3 = this.plugin.getItemContentManager();
      PlayerCache var4 = this.plugin.getAuctionManager().getCache(var1);
      Item var5 = (Item)var4.get(PlayerCacheKey.ITEM_SHOW);
      if (var5 instanceof AuctionItem var6) {
         List var7 = var3.getContainers(var6.getItemStacks());
         int var8 = (Integer)var4.get(PlayerCacheKey.SHULKER_INDEX, 0);
         var2.register("shulker-current", String.valueOf(var8 + 1));
         var2.register("shulker-total", String.valueOf(var7.size()));
      } else {
         var2.register("shulker-current", "0");
         var2.register("shulker-total", "0");
      }
   }
}
