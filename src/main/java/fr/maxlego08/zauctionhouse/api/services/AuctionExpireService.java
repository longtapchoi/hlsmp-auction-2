package fr.maxlego08.zauctionhouse.api.services;

import fr.maxlego08.zauctionhouse.api.item.Item;
import fr.maxlego08.zauctionhouse.api.item.StorageType;
import java.util.List;

public interface AuctionExpireService {
   void processExpiredItem(Item var1, StorageType var2);

   void processExpiredItems(List<Item> var1, StorageType var2);
}
