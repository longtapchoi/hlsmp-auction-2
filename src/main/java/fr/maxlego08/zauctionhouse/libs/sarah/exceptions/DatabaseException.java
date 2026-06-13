package fr.maxlego08.zauctionhouse.libs.sarah.exceptions;

public class DatabaseException extends SarahException {
   private final String tableName;
   private final String operation;

   public DatabaseException(String var1, String var2, Throwable var3) {
      super(String.format("Database operation '%s' failed on table '%s'", var1, var2), var3);
      this.operation = var1;
      this.tableName = var2;
   }

   public DatabaseException(String var1, Throwable var2) {
      super(String.format("Database operation '%s' failed", var1), var2);
      this.operation = var1;
      this.tableName = null;
   }

   public String getTableName() {
      return this.tableName;
   }

   public String getOperation() {
      return this.operation;
   }
}
