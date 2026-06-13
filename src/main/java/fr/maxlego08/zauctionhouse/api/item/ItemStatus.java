package fr.maxlego08.zauctionhouse.api.item;

public enum ItemStatus {
   AVAILABLE,
   IS_REMOVE_CONFIRM,
   IS_PURCHASE_CONFIRM,
   IS_BEING_REMOVED,
   IS_BEING_PURCHASED,
   REMOVED,
   PURCHASED,
   DELETED;

   // $FF: synthetic method
   private static ItemStatus[] $values() {
      return new ItemStatus[]{AVAILABLE, IS_REMOVE_CONFIRM, IS_PURCHASE_CONFIRM, IS_BEING_REMOVED, IS_BEING_PURCHASED, REMOVED, PURCHASED, DELETED};
   }
}
