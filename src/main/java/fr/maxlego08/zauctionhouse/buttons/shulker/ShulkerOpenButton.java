package fr.maxlego08.zauctionhouse.buttons.shulker;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCache;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCacheKey;
import fr.maxlego08.zauctionhouse.api.hooks.itemcontent.ItemContentManager;
import fr.maxlego08.zauctionhouse.api.inventories.Inventories;
import fr.maxlego08.zauctionhouse.api.item.Item;
import fr.maxlego08.zauctionhouse.api.item.items.AuctionItem;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public class ShulkerOpenButton extends Button {
   private final AuctionPlugin plugin;

   public ShulkerOpenButton(Plugin var1) {
      this.plugin = (AuctionPlugin)var1;
   }

   public boolean hasPermission() {
      return true;
   }

   public boolean checkPermission(@NonNull Player var1, @NonNull InventoryEngine var2, @NonNull Placeholders var3) {
      PlayerCache var4 = this.plugin.getAuctionManager().getCache(var1);
      Item var5 = (Item)var4.get(PlayerCacheKey.ITEM_SHOW);
      if (var5 instanceof AuctionItem var6) {
         return this.plugin.getItemContentManager().containsContainer(var6.getItemStacks());
      } else {
         return false;
      }
   }

   public void onClick(@NotNull Player var1, @NotNull InventoryClickEvent var2, @NotNull InventoryEngine var3, int var4, @NotNull Placeholders var5) {
      super.onClick(var1, var2, var3, var4, var5);
      ItemContentManager var6 = this.plugin.getItemContentManager();
      PlayerCache var7 = this.plugin.getAuctionManager().getCache(var1);
      Item var8 = (Item)var7.get(PlayerCacheKey.ITEM_SHOW);
      if (var8 instanceof AuctionItem var9) {
         List var10 = var6.getContainers(var9.getItemStacks());
         if (!var10.isEmpty()) {
            var7.set(PlayerCacheKey.SHULKER_INDEX, 0);
            List var11 = var6.getContents((ItemStack)var10.getFirst());
            var7.set(PlayerCacheKey.SHULKER_ITEMS, var11);
            this.plugin.getInventoriesLoader().openInventory(var1, Inventories.SHULKER_CONTENT);
         }
      }
   }
}
