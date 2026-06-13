package fr.maxlego08.zauctionhouse.buttons;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCacheKey;
import fr.maxlego08.zauctionhouse.api.item.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ShowButton extends Button {
   private final AuctionPlugin plugin;

   public ShowButton(Plugin var1) {
      this.plugin = (AuctionPlugin)var1;
   }

   public @Nullable ItemStack getCustomItemStack(@NotNull Player var1, boolean var2, @NotNull Placeholders var3) {
      Item var4 = (Item)this.plugin.getAuctionManager().getCache(var1).get(PlayerCacheKey.ITEM_SHOW);
      return var4 == null ? super.getCustomItemStack(var1, var2, var3) : var4.buildItemStack(var1);
   }
}
