package fr.maxlego08.zauctionhouse.buttons.admin;

import fr.maxlego08.menu.api.button.PaginateButton;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.zauctionhouse.api.AuctionManager;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCacheKey;
import fr.maxlego08.zauctionhouse.api.item.StorageType;
import fr.maxlego08.zauctionhouse.api.messages.Message;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class AdminExpiredItemsButton extends PaginateButton {
   private final AuctionPlugin plugin;

   public AdminExpiredItemsButton(Plugin var1) {
      this.plugin = (AuctionPlugin)var1;
   }

   public void onRender(Player var1, InventoryEngine var2) {
      Optional var3 = this.getTarget(var1);
      if (var3.isEmpty()) {
         this.plugin.getAuctionManager().message(var1, Message.ADMIN_TARGET_REQUIRED);
      } else {
         AuctionManager var4 = this.plugin.getAuctionManager();
         List var5 = var4.getExpiredItems((UUID)var3.get());
         this.paginate(var5, var2, (var4x, var5x) -> var2.addItem(var4x, var5x.buildItemStack(var1)).setClick((var4xx) -> var4.adminRemoveItem(var1, (UUID)var3.get(), var5x, StorageType.EXPIRED)));
      }
   }

   public int getPaginationSize(Player var1) {
      return (Integer)this.getTarget(var1).map((var1x) -> this.plugin.getAuctionManager().getExpiredItems(var1x).size()).orElse(0);
   }

   private Optional<UUID> getTarget(Player var1) {
      return Optional.ofNullable((UUID)this.plugin.getAuctionManager().getCache(var1).get(PlayerCacheKey.ADMIN_TARGET_UUID));
   }
}
