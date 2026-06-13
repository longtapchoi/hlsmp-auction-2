package fr.maxlego08.zauctionhouse.libs.sarah.requests;

import fr.maxlego08.zauctionhouse.libs.sarah.DatabaseConfiguration;
import fr.maxlego08.zauctionhouse.libs.sarah.DatabaseConnection;
import fr.maxlego08.zauctionhouse.libs.sarah.conditions.ColumnDefinition;
import fr.maxlego08.zauctionhouse.libs.sarah.database.DatabaseType;
import fr.maxlego08.zauctionhouse.libs.sarah.database.Executor;
import fr.maxlego08.zauctionhouse.libs.sarah.database.Schema;
import fr.maxlego08.zauctionhouse.libs.sarah.exceptions.DatabaseException;
import fr.maxlego08.zauctionhouse.libs.sarah.logger.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

@SuppressWarnings({"unchecked", "rawtypes"})
public class CreateRequest implements Executor {
   private final Schema schema;

   public CreateRequest(Schema var1) {
      this.schema = var1;
   }

   public int execute(DatabaseConnection var1, DatabaseConfiguration var2, Logger var3) {
      StringBuilder var4 = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
      var4.append(this.schema.getTableName()).append(" (");
      ArrayList var5 = new ArrayList();
      boolean var6 = false;

      for(ColumnDefinition var8 : this.schema.getColumns()) {
         var5.add(var8.build(var2));
         if (var8.isAutoIncrement() && var8.isPrimaryKey() && var2.getDatabaseType() == DatabaseType.SQLITE) {
            var6 = true;
         }
      }

      var4.append(String.join(", ", var5));
      if (!this.schema.getPrimaryKeys().isEmpty() && !var6) {
         var4.append(", PRIMARY KEY (").append(String.join(", ", this.schema.getPrimaryKeys())).append(")");
      }

      for(String var19 : this.schema.getForeignKeys()) {
         var4.append(", ").append(var19);
      }

      var4.append(")");
      if (var2.getDatabaseType() != DatabaseType.SQLITE) {
         var4.append(" ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");
      }

      String var18 = var2.replacePrefix(var4.toString());
      if (var2.isDebug()) {
         var3.info("Executing SQL: " + var18);
      }

      try {
         Connection var20 = var1.getConnection();

         int var10;
         try {
            PreparedStatement var9 = var20.prepareStatement(var18);

            try {
               var9.execute();
               var10 = var9.getUpdateCount();
            } catch (Throwable var14) {
               if (var9 != null) {
                  try {
                     var9.close();
                  } catch (Throwable var13) {
                     var14.addSuppressed(var13);
                  }
               }

               throw var14;
            }

            if (var9 != null) {
               var9.close();
            }
         } catch (Throwable var15) {
            if (var20 != null) {
               try {
                  var20.close();
               } catch (Throwable var12) {
                  var15.addSuppressed(var12);
               }
            }

            throw var15;
         }

         if (var20 != null) {
            var20.close();
         }

         return var10;
      } catch (SQLException var16) {
         var3.info("Create table operation failed on table: " + this.schema.getTableName() + " - " + var16.getMessage());
         throw new DatabaseException("create", this.schema.getTableName(), var16);
      }
   }
}
