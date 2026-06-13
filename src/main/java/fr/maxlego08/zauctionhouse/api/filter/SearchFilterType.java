package fr.maxlego08.zauctionhouse.api.filter;

public enum SearchFilterType {
   CONTAINS("~"),
   EQUALS("="),
   CONTAINS_IGNORE_CASE("~="),
   EQUALS_IGNORE_CASE("==");

   private final String defaultOperator;

   private SearchFilterType(String var3) {
      this.defaultOperator = var3;
   }

   public String getDefaultOperator() {
      return this.defaultOperator;
   }

   // $FF: synthetic method
   private static SearchFilterType[] $values() {
      return new SearchFilterType[]{CONTAINS, EQUALS, CONTAINS_IGNORE_CASE, EQUALS_IGNORE_CASE};
   }
}
