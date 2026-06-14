package fr.maxlego08.zauctionhouse.api.filter;

public enum SearchField {
   NAME("name"),
   MATERIAL("material"),
   LORE("lore"),
   SELLER("seller");

   private final String key;

   private SearchField(String var3) {
      this.key = var3;
   }

   public String getKey() {
      return this.key;
   }

   public static SearchField fromKey(String var0) {
      if (var0 == null) {
         return null;
      } else {
         for(SearchField var4 : values()) {
            if (var4.key.equalsIgnoreCase(var0)) {
               return var4;
            }
         }

         return null;
      }
   }

   // $FF: synthetic method
   private static SearchField[] $values() {
      return new SearchField[]{NAME, MATERIAL, LORE, SELLER};
   }
}
