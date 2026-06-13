package fr.maxlego08.zauctionhouse.api.event.events;

import fr.maxlego08.zauctionhouse.api.event.AuctionEvent;
import fr.maxlego08.zauctionhouse.api.item.Item;
import fr.maxlego08.zauctionhouse.api.item.StorageType;
import java.util.List;

public class AuctionExpireEvent extends AuctionEvent {
   private final List<Item> items;
   private final StorageType type;

   public AuctionExpireEvent(List<Item> var1, StorageType var2) {
      this.items = var1;
      this.type = var2;
   }

   public List<Item> getItems() {
      return this.items;
   }

   public StorageType getType() {
      return this.type;
   }
}
