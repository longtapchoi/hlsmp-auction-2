package fr.maxlego08.zauctionhouse.search;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.messages.Message;
import io.papermc.paper.event.player.AsyncChatEvent;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class ChatSearchListener implements Listener {
   private final AuctionPlugin plugin;
   private final Set<UUID> waitingForInput = ConcurrentHashMap.newKeySet();

   public ChatSearchListener(AuctionPlugin var1) {
      this.plugin = var1;
   }

   public void startSearch(Player var1) {
      this.waitingForInput.add(var1.getUniqueId());
      var1.closeInventory();
      this.plugin.sendMessage(var1, Message.SEARCH_START);
   }

   @EventHandler(
      priority = EventPriority.LOWEST
   )
   public void onChat(AsyncChatEvent var1) {
      if (this.waitingForInput.remove(var1.getPlayer().getUniqueId())) {
         var1.setCancelled(true);
         String var2 = PlainTextComponentSerializer.plainText().serialize(var1.message());
         Player var3 = var1.getPlayer();
         this.plugin.getScheduler().runAtEntity(var3, (var3x) -> {
            if (var3.isOnline()) {
               this.plugin.getAuctionManager().startSearch(var3, var2);
            }

         });
      }
   }

   @EventHandler
   public void onQuit(PlayerQuitEvent var1) {
      this.waitingForInput.remove(var1.getPlayer().getUniqueId());
   }
}
