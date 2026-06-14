package fr.maxlego08.zauctionhouse.api.log;

public enum LogType {
   SALE("Sale"),
   PURCHASE("Purchase"),
   REMOVE_LISTED("Remove Listed"),
   REMOVE_SELLING("Remove Selling"),
   REMOVE_EXPIRED("Remove Expired"),
   REMOVE_PURCHASED("Remove Purchased");

   private final String defaultDisplayName;

   private LogType(String var3) {
      this.defaultDisplayName = var3;
   }

   public String getDefaultDisplayName() {
      return this.defaultDisplayName;
   }

   // $FF: synthetic method
   private static LogType[] $values() {
      return new LogType[]{SALE, PURCHASE, REMOVE_LISTED, REMOVE_SELLING, REMOVE_EXPIRED, REMOVE_PURCHASED};
   }
}
