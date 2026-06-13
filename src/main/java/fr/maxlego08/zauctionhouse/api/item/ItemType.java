package fr.maxlego08.zauctionhouse.api.item;

public enum ItemType {
   AUCTION,
   BID,
   RENT;

   // $FF: synthetic method
   private static ItemType[] $values() {
      return new ItemType[]{AUCTION, BID, RENT};
   }
}
