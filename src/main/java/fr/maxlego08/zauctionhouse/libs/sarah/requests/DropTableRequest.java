package fr.maxlego08.zauctionhouse.libs.sarah.requests;

import fr.maxlego08.zauctionhouse.libs.sarah.DatabaseConfiguration;
import fr.maxlego08.zauctionhouse.libs.sarah.DatabaseConnection;
import fr.maxlego08.zauctionhouse.libs.sarah.database.Executor;
import fr.maxlego08.zauctionhouse.libs.sarah.database.Schema;
import fr.maxlego08.zauctionhouse.libs.sarah.logger.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DropTableRequest implements Executor {
   private final Schema schema;

   public DropTableRequest(Schema var1) {
      this.schema = var1;
   }

   public int execute(DatabaseConnection var1, DatabaseConfiguration var2, Logger var3) {
      String var4 = this.schema.getTableName();
      if (var4 != null && !var4.trim().isEmpty()) {
         String var5 = var2.replacePrefix("DROP TABLE IF EXISTS " + var4);
         if (var2.isDebug()) {
            var3.info("Executing SQL: " + var5);
         }

         try {
            Connection var6 = var1.getConnection();

            byte var8;
            try {
               PreparedStatement var7 = var6.prepareStatement(var5);

               try {
                  var7.execute();
                  var8 = 0;
               } catch (Throwable var12) {
                  if (var7 != null) {
                     try {
                        var7.close();
                     } catch (Throwable var11) {
                        var12.addSuppressed(var11);
                     }
                  }

                  throw var12;
               }

               if (var7 != null) {
                  var7.close();
               }
            } catch (Throwable var13) {
               if (var6 != null) {
                  try {
                     var6.close();
                  } catch (Throwable var10) {
                     var13.addSuppressed(var10);
                  }
               }

               throw var13;
            }

            if (var6 != null) {
               var6.close();
            }

            return var8;
         } catch (SQLException var14) {
            var3.info("Error while executing SQL query: " + var14.getMessage());
            return -1;
         }
      } else {
         var3.info("Invalid table name.");
         return -1;
      }
   }
}
