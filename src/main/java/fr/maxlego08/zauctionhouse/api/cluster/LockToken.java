package fr.maxlego08.zauctionhouse.api.cluster;

import fr.maxlego08.zauctionhouse.api.item.Item;

public record LockToken(String value) {
   public static LockToken noop() {
      return new LockToken("NOOP");
   }

   public static LockToken of(Item var0) {
      return new LockToken("item:" + var0.getId());
   }
}
