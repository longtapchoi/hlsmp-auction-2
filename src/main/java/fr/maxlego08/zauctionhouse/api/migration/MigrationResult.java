package fr.maxlego08.zauctionhouse.api.migration;

public interface MigrationResult {
   boolean isSuccess();

   int getPlayersImported();

   int getItemsImported();

   int getTransactionsImported();

   int getErrors();

   long getDurationMs();

   String getErrorMessage();

   static MigrationResult success(int playersImported, int itemsImported, int transactionsImported, int errors, long durationMs) {
      return new SimpleMigrationResult(true, playersImported, itemsImported, transactionsImported, errors, durationMs, (String)null);
   }

   static MigrationResult failure(String errorMessage) {
      return new SimpleMigrationResult(false, 0, 0, 0, 0, 0L, errorMessage);
   }

   public static record SimpleMigrationResult(boolean isSuccess, int playersImported, int itemsImported, int transactionsImported, int errors, long durationMs, String errorMessage) implements MigrationResult {
      public int getPlayersImported() {
         return this.playersImported;
      }

      public int getItemsImported() {
         return this.itemsImported;
      }

      public int getTransactionsImported() {
         return this.transactionsImported;
      }

      public int getErrors() {
         return this.errors;
      }

      public long getDurationMs() {
         return this.durationMs;
      }

      public String getErrorMessage() {
         return this.errorMessage;
      }

      public String toString() {
         return !this.isSuccess ? "Migration failed: " + this.errorMessage : String.format("Migration completed in %dms - Players: %d, Items: %d, Transactions: %d, Errors: %d", this.durationMs, this.playersImported, this.itemsImported, this.transactionsImported, this.errors);
      }
   }
}
