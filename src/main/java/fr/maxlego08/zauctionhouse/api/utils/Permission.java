package fr.maxlego08.zauctionhouse.api.utils;

public enum Permission {
   ZAUCTIONHOUSE_USE,
   ZAUCTIONHOUSE_RELOAD,
   ZAUCTIONHOUSE_SELL,
   ZAUCTIONHOUSE_BID,
   ZAUCTIONHOUSE_RENT,
   ZAUCTIONHOUSE_ADMIN_REMOVE_INVENTORY("Allows access to the inventory to remove an item from the sale", new String[0]),
   ZAUCTIONHOUSE_ADMIN,
   ZAUCTIONHOUSE_ADMIN_ITEMS,
   ZAUCTIONHOUSE_OPTION;

   private final String description;
   private final String[] args;

   private Permission() {
      this.description = "";
      this.args = new String[0];
   }

   private Permission(String var3, String... var4) {
      this.description = var3;
      this.args = var4;
   }

   public String asPermission() {
      return this.name().toLowerCase().replace("_", ".");
   }

   public String toPermission() {
      StringBuilder var1 = new StringBuilder(this.asPermission());

      for(int var2 = 0; var2 < this.args.length; ++var2) {
         var1.append("<");
         var1.append(this.args[var2]);
         var1.append(">");
         if (var2 < this.args.length - 1) {
            var1.append(".");
         }
      }

      return var1.toString();
   }

   public String asPermission(String var1) {
      String var10000 = this.asPermission();
      return var10000 + var1;
   }

   public String getDescription() {
      return this.description;
   }

   public String[] getArgs() {
      return this.args;
   }

   // $FF: synthetic method
   private static Permission[] $values() {
      return new Permission[]{ZAUCTIONHOUSE_USE, ZAUCTIONHOUSE_RELOAD, ZAUCTIONHOUSE_SELL, ZAUCTIONHOUSE_BID, ZAUCTIONHOUSE_RENT, ZAUCTIONHOUSE_ADMIN_REMOVE_INVENTORY, ZAUCTIONHOUSE_ADMIN, ZAUCTIONHOUSE_ADMIN_ITEMS, ZAUCTIONHOUSE_OPTION};
   }
}
