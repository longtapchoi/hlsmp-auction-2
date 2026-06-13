package fr.maxlego08.zauctionhouse.api.event.events.remove;

import fr.maxlego08.zauctionhouse.api.item.Item;
import org.bukkit.entity.Player;

public class AuctionPreRemoveListedItemEvent extends CancelledRemoveEvent {
   public AuctionPreRemoveListedItemEvent(Item var1, Player var2) {
      super(var1, var2);
   }
}
