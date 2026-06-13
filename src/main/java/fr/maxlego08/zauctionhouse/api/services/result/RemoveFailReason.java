package fr.maxlego08.zauctionhouse.api.services.result;

public enum RemoveFailReason {
   NONE,
   ITEM_EXPIRED,
   ITEM_NOT_FOUND,
   ITEM_NOT_AVAILABLE,
   INVALID_ITEM_STATUS,
   LOCK_FAILED,
   NO_PERMISSION,
   INSUFFICIENT_SPACE,
   WORLD_RESTRICTED,
   EVENT_CANCELLED,
   INTERNAL_ERROR;

   // $FF: synthetic method
   private static RemoveFailReason[] $values() {
      return new RemoveFailReason[]{NONE, ITEM_EXPIRED, ITEM_NOT_FOUND, ITEM_NOT_AVAILABLE, INVALID_ITEM_STATUS, LOCK_FAILED, NO_PERMISSION, INSUFFICIENT_SPACE, WORLD_RESTRICTED, EVENT_CANCELLED, INTERNAL_ERROR};
   }
}
