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

public class RenameExecutor implements Executor {
   private final Schema schema;

   public RenameExecutor(Schema var1) {
      this.schema = var1;
   }

   public int execute(DatabaseConnection var1, DatabaseConfiguration var2, Logger var3) {
      StringBuilder var4 = new StringBuilder("ALTER TABLE ");
      var4.append(this.schema.getTableName());
      var4.append(" RENAME TO ");
      var4.append(this.schema.getNewTableName());
      String var5 = var2.replacePrefix(var4.toString());
      if (var2.isDebug()) {
         var3.info("Executing SQL: " + var5);
      }

      try {
         Connection var6 = var1.getConnection();

         int var8;
         try {
            PreparedStatement var7 = var6.prepareStatement(var5);

            try {
               var7.execute();
               var8 = var7.getUpdateCount();
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
         var3.info("Rename table operation failed: " + this.schema.getTableName() + " to " + this.schema.getNewTableName() + " - " + var14.getMessage());
         throw new DatabaseException("rename", this.schema.getTableName(), var14);
      }
   }
}
