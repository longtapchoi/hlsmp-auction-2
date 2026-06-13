package fr.maxlego08.zauctionhouse.migration.v3;

import fr.maxlego08.zauctionhouse.api.item.StorageType;

public enum V3StorageType {
   STORAGE("auction"),
   BUY("auction_expire"),
   EXPIRE("auction_buy");

   private final String tableSuffix;

   private V3StorageType(String var3) {
      this.tableSuffix = var3;
   }

   public String getTableSuffix() {
      return this.tableSuffix;
   }

   public StorageType toV4StorageType() {
      StorageType var10000;
      switch (this.ordinal()) {
         case 0 -> var10000 = StorageType.LISTED;
         case 1 -> var10000 = StorageType.PURCHASED;
         case 2 -> var10000 = StorageType.EXPIRED;
         default -> throw new MatchException((String)null, (Throwable)null);
      }

      return var10000;
   }

   public static V3StorageType fromString(String var0) {
      if (var0 == null) {
         return STORAGE;
      } else {
         V3StorageType var10000;
         switch (var0.toUpperCase()) {
            case "BUY" -> var10000 = BUY;
            case "EXPIRE" -> var10000 = EXPIRE;
            default -> var10000 = STORAGE;
         }

         return var10000;
      }
   }

   // $FF: synthetic method
   private static V3StorageType[] $values() {
      return new V3StorageType[]{STORAGE, BUY, EXPIRE};
   }
}
