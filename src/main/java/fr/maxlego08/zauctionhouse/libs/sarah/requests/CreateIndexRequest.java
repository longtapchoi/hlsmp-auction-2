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

public class CreateIndexRequest implements Executor {
   private final Schema schema;

   public CreateIndexRequest(Schema var1) {
      this.schema = var1;
   }

   public int execute(DatabaseConnection var1, DatabaseConfiguration var2, Logger var3) {
      StringBuilder var4 = new StringBuilder("CREATE INDEX ");
      String var5 = this.schema.getTableName();
      ColumnDefinition var6 = (ColumnDefinition)this.schema.getColumns().get(0);
      String var7 = "idx_" + var5 + "_" + var6.getName();
      var4.append(var7);
      var4.append(" ON ");
      var4.append(String.format("`%s`", var5));
      var4.append(" (");
      var4.append(var6.getSafeName());
      var4.append(" )");
      String var8 = var2.replacePrefix(var4.toString());
      if (var2.isDebug()) {
         var3.info("Executing SQL: " + var8);
      }

      try {
         Connection var9 = var1.getConnection();

         int var11;
         try {
            PreparedStatement var10 = var9.prepareStatement(var8);

            try {
               var10.execute();
               var11 = var10.getUpdateCount();
            } catch (Throwable var15) {
               if (var10 != null) {
                  try {
                     var10.close();
                  } catch (Throwable var14) {
                     var15.addSuppressed(var14);
                  }
               }

               throw var15;
            }

            if (var10 != null) {
               var10.close();
            }
         } catch (Throwable var16) {
            if (var9 != null) {
               try {
                  var9.close();
               } catch (Throwable var13) {
                  var16.addSuppressed(var13);
               }
            }

            throw var16;
         }

         if (var9 != null) {
            var9.close();
         }

         return var11;
      } catch (SQLException var17) {
         var3.info("Create index operation failed on table: " + var5 + " - " + var17.getMessage());
         throw new DatabaseException("createIndex", var5, var17);
      }
   }
}
