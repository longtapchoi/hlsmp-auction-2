package fr.maxlego08.zauctionhouse.api.services.result;

public record PurchaseResult(boolean success, String message, boolean itemGiven, PurchaseFailReason failReason) {
   public static PurchaseResult success(String var0) {
      return new PurchaseResult(true, var0, true, PurchaseFailReason.NONE);
   }

   public static PurchaseResult success(String var0, boolean var1) {
      return new PurchaseResult(true, var0, var1, PurchaseFailReason.NONE);
   }

   public static PurchaseResult failure(String var0, PurchaseFailReason var1) {
      return new PurchaseResult(false, var0, false, var1);
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

   public PurchaseFailReason getFailReason() {
      return this.failReason;
   }
}
