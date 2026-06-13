package fr.maxlego08.zauctionhouse.api.migration;

@FunctionalInterface
public interface MigrationCallback {
   void onProgress(String var1);

   static MigrationCallback empty() {
      return (message) -> {
      };
   }
}
