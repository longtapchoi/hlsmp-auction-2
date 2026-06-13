package fr.maxlego08.zauctionhouse.api.inventories;

public enum Inventories {
   AUCTION("auction"),
   REMOVE_CONFIRM("remove-confirm"),
   PURCHASE_CONFIRM("purchase-confirm"),
   PURCHASE_INVENTORY_CONFIRM("purchase-inventory-confirm"),
   REMOVE_INVENTORY_CONFIRM("remove-inventory-confirm"),
   SELL_INVENTORY("sell-inventory"),
   EXPIRED_ITEMS("expired-items"),
   PURCHASED_ITEMS("purchased-items"),
   SELLING_ITEMS("selling-items"),
   ADMIN_EXPIRED_ITEMS("admin-expired-items"),
   ADMIN_PURCHASED_ITEMS("admin-purchased-items"),
   ADMIN_SELLING_ITEMS("admin-selling-items"),
   ADMIN_HISTORY_MAIN("admin-history-main"),
   ADMIN_LOGS("admin-logs"),
   ADMIN_TRANSACTIONS("admin-transactions"),
   HISTORY("history"),
   SHULKER_CONTENT("shulker-content"),
   COMBINED_ITEMS("combined-items"),
   OPTIONS("options");

   private final String fileName;

   private Inventories(String var3) {
      this.fileName = var3;
   }

   public String getFileName() {
      return this.fileName;
   }

   // $FF: synthetic method
   private static Inventories[] $values() {
      return new Inventories[]{AUCTION, REMOVE_CONFIRM, PURCHASE_CONFIRM, PURCHASE_INVENTORY_CONFIRM, REMOVE_INVENTORY_CONFIRM, SELL_INVENTORY, EXPIRED_ITEMS, PURCHASED_ITEMS, SELLING_ITEMS, ADMIN_EXPIRED_ITEMS, ADMIN_PURCHASED_ITEMS, ADMIN_SELLING_ITEMS, ADMIN_HISTORY_MAIN, ADMIN_LOGS, ADMIN_TRANSACTIONS, HISTORY, SHULKER_CONTENT, COMBINED_ITEMS, OPTIONS};
   }
}
