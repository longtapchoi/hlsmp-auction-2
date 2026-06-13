package fr.maxlego08.zauctionhouse.migration.v3;

public class V3MigrationResult {
   private final boolean success;
   private final int playersImported;
   private final int itemsImported;
   private final int transactionsImported;
   private final int errors;
   private final long durationMs;
   private final String errorMessage;

   private V3MigrationResult(boolean var1, int var2, int var3, int var4, int var5, long var6, String var8) {
      this.success = var1;
      this.playersImported = var2;
      this.itemsImported = var3;
      this.transactionsImported = var4;
      this.errors = var5;
      this.durationMs = var6;
      this.errorMessage = var8;
   }

   public static V3MigrationResult success(int var0, int var1, int var2, int var3, long var4) {
      return new V3MigrationResult(true, var0, var1, var2, var3, var4, (String)null);
   }

   public static V3MigrationResult failure(String var0) {
      return new V3MigrationResult(false, 0, 0, 0, 0, 0L, var0);
   }

   public boolean isSuccess() {
      return this.success;
   }

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
      return !this.success ? "Migration failed: " + this.errorMessage : String.format("Migration completed in %dms - Players: %d, Items: %d, Transactions: %d, Errors: %d", this.durationMs, this.playersImported, this.itemsImported, this.transactionsImported, this.errors);
   }
}
