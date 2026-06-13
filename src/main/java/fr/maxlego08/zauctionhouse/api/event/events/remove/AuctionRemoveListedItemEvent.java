package fr.maxlego08.zauctionhouse.api.event.events.remove;

import fr.maxlego08.zauctionhouse.api.item.Item;
import org.bukkit.entity.Player;

public class AuctionRemoveListedItemEvent extends RemoveEvent {
   public AuctionRemoveListedItemEvent(Item var1, Player var2) {
      super(var1, var2);
   }
}
