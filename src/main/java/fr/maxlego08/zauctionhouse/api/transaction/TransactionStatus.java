package fr.maxlego08.zauctionhouse.api.transaction;

public enum TransactionStatus {
   PENDING("Pending"),
   RETRIEVED("Retrieved");

   private final String defaultDisplayName;

   private TransactionStatus(String var3) {
      this.defaultDisplayName = var3;
   }

   public String getDefaultDisplayName() {
      return this.defaultDisplayName;
   }

   // $FF: synthetic method
   private static TransactionStatus[] $values() {
      return new TransactionStatus[]{PENDING, RETRIEVED};
   }
}
