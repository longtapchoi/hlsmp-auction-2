package fr.maxlego08.zauctionhouse.libs.sarah.requests;

import fr.maxlego08.zauctionhouse.libs.sarah.DatabaseConfiguration;
import fr.maxlego08.zauctionhouse.libs.sarah.DatabaseConnection;
import fr.maxlego08.zauctionhouse.libs.sarah.database.Executor;
import fr.maxlego08.zauctionhouse.libs.sarah.database.Schema;
import fr.maxlego08.zauctionhouse.libs.sarah.exceptions.DatabaseException;
import fr.maxlego08.zauctionhouse.libs.sarah.logger.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteRequest implements Executor {
   private final Schema schemaBuilder;

   public DeleteRequest(Schema var1) {
      this.schemaBuilder = var1;
   }

   public int execute(DatabaseConnection var1, DatabaseConfiguration var2, Logger var3) {
      StringBuilder var4 = (new StringBuilder("DELETE FROM ")).append(this.schemaBuilder.getTableName());
      this.schemaBuilder.whereConditions(var4);
      String var5 = var2.replacePrefix(var4.toString());
      if (var2.isDebug()) {
         var3.info("Executing SQL: " + var5);
      }

      try {
         Connection var6 = var1.getConnection();

         int var9;
         try {
            PreparedStatement var7 = var6.prepareStatement(var5);

            try {
               this.schemaBuilder.applyWhereConditions(var7, 1);
               int var8 = var7.executeUpdate();
               var9 = var8;
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

         return var9;
      } catch (SQLException var14) {
         var3.info("Delete operation failed on table: " + this.schemaBuilder.getTableName() + " - " + var14.getMessage());
         throw new DatabaseException("delete", this.schemaBuilder.getTableName(), var14);
      }
   }
}
