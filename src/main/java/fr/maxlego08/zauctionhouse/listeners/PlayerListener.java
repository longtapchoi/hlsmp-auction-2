package fr.maxlego08.zauctionhouse.listeners;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.utils.component.ComponentMessageHelper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
   private final AuctionPlugin plugin;

   public PlayerListener(AuctionPlugin var1) {
      this.plugin = var1;
   }

   @EventHandler
   public void onConnect(PlayerJoinEvent var1) {
      Player var2 = var1.getPlayer();
      this.plugin.getStorageManager().upsertPlayer(var2);
      this.plugin.getAuctionManager().getClaimService().handlePlayerJoin(var2);
      this.plugin.getAuctionManager().getHistoryService().handlePlayerJoin(var2);
      this.plugin.getAuctionManager().getOptionService().loadPlayerOptions(var2.getUniqueId());
      if (var2.getName().equals("Maxlego08")) {
         this.plugin.getScheduler().runLater((var2x) -> {
            if (var2.isOnline()) {
               ComponentMessageHelper.componentMessage.sendMessage(var2, "<#24d65d>zAuctionHouse <#656665>• <#e6fff3>Ce serveur utilise <#24d65d>zAuctionHouse <white>v" + this.plugin.getDescription().getVersion());
            }

         }, 40L);
      }

   }

   @EventHandler
   public void onQuit(PlayerQuitEvent var1) {
      this.plugin.getAuctionManager().removeCache(var1.getPlayer());
      this.plugin.getAuctionManager().getOptionService().clearPlayerOptions(var1.getPlayer().getUniqueId());
      this.plugin.getCommandManager().clearCooldowns(var1.getPlayer().getUniqueId());
   }
}
