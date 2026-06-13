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

public class InsertAllRequest implements Executor {
   private final Schema schema;
   private final String toTableName;

   public InsertAllRequest(Schema var1, String var2) {
      this.schema = var1;
      this.toTableName = var2;
   }

   public int execute(DatabaseConnection var1, DatabaseConfiguration var2, Logger var3) {
      StringBuilder var4 = new StringBuilder("INSERT INTO " + this.toTableName + " (");
      StringBuilder var5 = new StringBuilder();
      int var6 = 0;

      for(ColumnDefinition var8 : this.schema.getColumns()) {
         if (!var8.isAutoIncrement()) {
            if (var6 > 0) {
               var5.append(",");
            }

            var5.append(var8.getSafeName());
            ++var6;
         }
      }

      var4.append(var5).append(") ");
      var4.append("SELECT ").append(var5);
      var4.append(" FROM ");
      var4.append(this.schema.getTableName());
      String var17 = var2.replacePrefix(var4.toString());
      if (var2.isDebug()) {
         var3.info("Executing SQL: " + var17);
      }

      try {
         Connection var18 = var1.getConnection();

         try {
            PreparedStatement var9 = var18.prepareStatement(var17);

            try {
               var9.executeUpdate();
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
            if (var18 != null) {
               try {
                  var18.close();
               } catch (Throwable var12) {
                  var15.addSuppressed(var12);
               }
            }

            throw var15;
         }

         if (var18 != null) {
            var18.close();
         }

         return 0;
      } catch (SQLException var16) {
         var3.info("Insert all operation failed from table: " + this.schema.getTableName() + " to table: " + this.toTableName + " - " + var16.getMessage());
         throw new DatabaseException("insertAll", this.toTableName, var16);
      }
   }
}
