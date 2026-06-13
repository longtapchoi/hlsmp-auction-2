package fr.maxlego08.zauctionhouse.libs.sarah.requests;

import fr.maxlego08.zauctionhouse.libs.sarah.DatabaseConfiguration;
import fr.maxlego08.zauctionhouse.libs.sarah.DatabaseConnection;
import fr.maxlego08.zauctionhouse.libs.sarah.conditions.ColumnDefinition;
import fr.maxlego08.zauctionhouse.libs.sarah.database.Executor;
import fr.maxlego08.zauctionhouse.libs.sarah.database.Schema;
import fr.maxlego08.zauctionhouse.libs.sarah.exceptions.DatabaseException;
import fr.maxlego08.zauctionhouse.libs.sarah.logger.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class AlterRequest implements Executor {
   private final Schema schema;

   public AlterRequest(Schema var1) {
      this.schema = var1;
   }

   public int execute(DatabaseConnection var1, DatabaseConfiguration var2, Logger var3) {
      StringBuilder var4 = new StringBuilder("ALTER TABLE ");
      var4.append(this.schema.getTableName()).append(" ");
      ArrayList var5 = new ArrayList();

      for(ColumnDefinition var7 : this.schema.getColumns()) {
         var5.add("ADD COLUMN " + var7.build(var2));
      }

      var4.append(String.join(", ", var5));
      if (!this.schema.getPrimaryKeys().isEmpty()) {
         var4.append(", PRIMARY KEY (").append(String.join(", ", this.schema.getPrimaryKeys())).append(")");
      }

      for(String var18 : this.schema.getForeignKeys()) {
         var4.append(", ADD ").append(var18);
      }

      String var17 = var2.replacePrefix(var4.toString());
      if (var2.isDebug()) {
         var3.info("Executing SQL: " + var17);
      }

      try {
         Connection var19 = var1.getConnection();

         int var9;
         try {
            PreparedStatement var8 = var19.prepareStatement(var17);

            try {
               var8.execute();
               var9 = var8.getUpdateCount();
            } catch (Throwable var13) {
               if (var8 != null) {
                  try {
                     var8.close();
                  } catch (Throwable var12) {
                     var13.addSuppressed(var12);
                  }
               }

               throw var13;
            }

            if (var8 != null) {
               var8.close();
            }
         } catch (Throwable var14) {
            if (var19 != null) {
               try {
                  var19.close();
               } catch (Throwable var11) {
                  var14.addSuppressed(var11);
               }
            }

            throw var14;
         }

         if (var19 != null) {
            var19.close();
         }

         return var9;
      } catch (SQLException var15) {
         var3.info("Alter table operation failed on table: " + this.schema.getTableName() + " - " + var15.getMessage());
         throw new DatabaseException("alter", this.schema.getTableName(), var15);
      }
   }
}
