package fr.maxlego08.zauctionhouse.libs.sarah.requests;

import fr.maxlego08.zauctionhouse.libs.sarah.DatabaseConfiguration;
import fr.maxlego08.zauctionhouse.libs.sarah.DatabaseConnection;
import fr.maxlego08.zauctionhouse.libs.sarah.SchemaBuilder;
import fr.maxlego08.zauctionhouse.libs.sarah.database.Executor;
import fr.maxlego08.zauctionhouse.libs.sarah.database.Schema;
import fr.maxlego08.zauctionhouse.libs.sarah.database.SchemaType;
import fr.maxlego08.zauctionhouse.libs.sarah.exceptions.DatabaseException;
import fr.maxlego08.zauctionhouse.libs.sarah.logger.Logger;
import java.sql.SQLException;

public class ModifyRequest implements Executor {
   private final Schema schema;

   public ModifyRequest(Schema var1) {
      this.schema = var1;
   }

   public int execute(DatabaseConnection var1, DatabaseConfiguration var2, Logger var3) {
      String var4 = this.schema.getTableName() + "_tmp";
      Schema var5 = SchemaBuilder.copy(var4, SchemaType.CREATE, this.schema);

      try {
         var5.execute(var1, var3);
      } catch (SQLException var7) {
         var3.info("Modify table operation failed on table: " + this.schema.getTableName() + " - Failed to create temporary table - " + var7.getMessage());
         throw new DatabaseException("modify", this.schema.getTableName(), var7);
      }

      Executor var6 = new InsertAllRequest(this.schema, var4);
      var6.execute(var1, var2, var3);
      var6 = new DropTableRequest(this.schema);
      var6.execute(var1, var2, var3);
      var6 = new RenameExecutor(SchemaBuilder.rename(var4, this.schema.getTableName()));
      var6.execute(var1, var2, var3);
      return 0;
   }
}
