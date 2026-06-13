package fr.maxlego08.zauctionhouse.buttons.admin.history;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCacheKey;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.entity.Player;

public abstract class TargetHelper extends Button {
   protected final AuctionPlugin plugin;

   public TargetHelper(AuctionPlugin var1) {
      this.plugin = var1;
   }

   protected Optional<UUID> getTargetUniqueId(Player var1) {
      return Optional.ofNullable((UUID)this.plugin.getAuctionManager().getCache(var1).get(PlayerCacheKey.ADMIN_TARGET_UUID));
   }

   protected Optional<String> getTargetName(Player var1) {
      return Optional.ofNullable((String)this.plugin.getAuctionManager().getCache(var1).get(PlayerCacheKey.ADMIN_TARGET_NAME));
   }
}
