package fr.maxlego08.zauctionhouse.api.services.result;

public record RemoveResult(boolean success, String message, boolean itemGiven, RemoveFailReason failReason) {
   public static RemoveResult success(String var0) {
      return new RemoveResult(true, var0, true, RemoveFailReason.NONE);
   }

   public static RemoveResult success(String var0, boolean var1) {
      return new RemoveResult(true, var0, var1, RemoveFailReason.NONE);
   }

   public static RemoveResult failure(String var0, RemoveFailReason var1) {
      return new RemoveResult(false, var0, false, var1);
   }

   public boolean isSuccess() {
      return this.success;
   }

   public String getMessage() {
      return this.message;
   }

   public boolean isItemGiven() {
      return this.itemGiven;
   }

   public RemoveFailReason getFailReason() {
      return this.failReason;
   }
}
