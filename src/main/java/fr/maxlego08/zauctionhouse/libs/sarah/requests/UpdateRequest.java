package fr.maxlego08.zauctionhouse.libs.sarah.requests;

import fr.maxlego08.zauctionhouse.libs.sarah.DatabaseConfiguration;
import fr.maxlego08.zauctionhouse.libs.sarah.DatabaseConnection;
import fr.maxlego08.zauctionhouse.libs.sarah.conditions.ColumnDefinition;
import fr.maxlego08.zauctionhouse.libs.sarah.conditions.JoinCondition;
import fr.maxlego08.zauctionhouse.libs.sarah.database.Executor;
import fr.maxlego08.zauctionhouse.libs.sarah.database.Schema;
import fr.maxlego08.zauctionhouse.libs.sarah.exceptions.DatabaseException;
import fr.maxlego08.zauctionhouse.libs.sarah.logger.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class UpdateRequest implements Executor {
   private final Schema schema;

   public UpdateRequest(Schema var1) {
      this.schema = var1;
   }

   public int execute(DatabaseConnection var1, DatabaseConfiguration var2, Logger var3) {
      StringBuilder var4 = new StringBuilder("UPDATE " + this.schema.getTableName());
      if (!this.schema.getJoinConditions().isEmpty()) {
         for(JoinCondition var6 : this.schema.getJoinConditions()) {
            var4.append(" ").append(var6.getJoinClause());
         }
      }

      var4.append(" SET ");
      ArrayList var16 = new ArrayList();

      for(int var17 = 0; var17 < this.schema.getColumns().size(); ++var17) {
         ColumnDefinition var7 = (ColumnDefinition)this.schema.getColumns().get(var17);
         var4.append(var17 > 0 ? ", " : "").append(var7.getSafeName()).append(" = ?");
         var16.add(var7.getObject());
      }

      this.schema.whereConditions(var4);
      String var18 = var2.replacePrefix(var4.toString());
      if (var2.isDebug()) {
         var3.info("Executing SQL: " + var18);
      }

      try {
         Connection var19 = var1.getConnection();

         int var20;
         try {
            PreparedStatement var8 = var19.prepareStatement(var18);

            try {
               for(int var9 = 0; var9 < var16.size(); ++var9) {
                  var8.setObject(var9 + 1, var16.get(var9));
               }

               this.schema.applyWhereConditions(var8, var16.size() + 1);
               var8.executeUpdate();
               var20 = var8.getUpdateCount();
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

         return var20;
      } catch (SQLException var15) {
         var3.info("Update operation failed on table: " + this.schema.getTableName() + " - " + var15.getMessage());
         throw new DatabaseException("update", this.schema.getTableName(), var15);
      }
   }
}
