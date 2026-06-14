package fr.maxlego08.zauctionhouse.api.event.events.remove;

import fr.maxlego08.zauctionhouse.api.event.CancelledAuctionEvent;
import fr.maxlego08.zauctionhouse.api.item.Item;
import org.bukkit.entity.Player;

public abstract class CancelledRemoveEvent extends CancelledAuctionEvent {
   private final Item item;
   private final Player player;

   public CancelledRemoveEvent(Item var1, Player var2) {
      this.item = var1;
      this.player = var2;
   }

   public Item getItem() {
      return this.item;
   }

   public Player getPlayer() {
      return this.player;
   }
}
