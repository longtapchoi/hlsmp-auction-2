package fr.maxlego08.zauctionhouse.buttons;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCache;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCacheKey;
import fr.maxlego08.zauctionhouse.api.item.items.AuctionItem;
import fr.maxlego08.zauctionhouse.api.log.AdminLogItem;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class ItemContentButton extends Button {
   private final AuctionPlugin plugin;

   public ItemContentButton(Plugin var1) {
      this.plugin = (AuctionPlugin)var1;
   }

   public void onRender(Player var1, InventoryEngine var2) {
      List var3 = this.getItemStacks(var1);
      if (!var3.isEmpty()) {
         Objects.requireNonNull(var2);
         this.paginate(var3, var2, var2::addItem);
      }
   }

   public boolean hasSpecialRender() {
      return true;
   }

   private List<ItemStack> getItemStacks(Player var1) {
      PlayerCache var2 = this.plugin.getAuctionManager().getCache(var1);
      Object var3 = var2.get(PlayerCacheKey.ITEM_SHOW);
      if (var3 instanceof AuctionItem var4) {
         if (var4.getItemStacks() != null) {
            return var4.getItemStacks().stream().map(ItemStack::clone).toList();
         }
      }

      AdminLogItem var5 = (AdminLogItem)var2.get(PlayerCacheKey.ADMIN_LOG_SELECTED);
      return var5 != null && var5.itemStacks() != null ? var5.itemStacks().stream().map(ItemStack::clone).toList() : Collections.emptyList();
   }
}
