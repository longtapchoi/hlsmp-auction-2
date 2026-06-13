package fr.maxlego08.zauctionhouse.api.event.events.purchase;

import fr.maxlego08.zauctionhouse.api.event.events.remove.CancelledRemoveEvent;
import fr.maxlego08.zauctionhouse.api.item.Item;
import org.bukkit.entity.Player;

public class AuctionPrePurchaseItemEvent extends CancelledRemoveEvent {
   public AuctionPrePurchaseItemEvent(Item var1, Player var2) {
      super(var1, var2);
   }
}
