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
import java.util.List;

public class UpdateBatchRequest implements Executor {
   private final List<Schema> schemas;

   public UpdateBatchRequest(List<Schema> var1) {
      this.schemas = var1;
   }

   public int execute(DatabaseConnection var1, DatabaseConfiguration var2, Logger var3) {
      if (this.schemas.isEmpty()) {
         return 0;
      } else {
         Schema var4 = (Schema)this.schemas.get(0);
         StringBuilder var5 = new StringBuilder("UPDATE " + var4.getTableName());
         if (!var4.getJoinConditions().isEmpty()) {
            for(JoinCondition var7 : var4.getJoinConditions()) {
               var5.append(" ").append(var7.getJoinClause());
            }
         }

         var5.append(" SET ");
         List var31 = var4.getColumns();

         for(int var32 = 0; var32 < var31.size(); ++var32) {
            ColumnDefinition var8 = (ColumnDefinition)var31.get(var32);
            var5.append(var32 > 0 ? ", " : "").append(var8.getSafeName()).append(" = ?");
         }

         var4.whereConditions(var5);
         String var33 = var2.replacePrefix(var5.toString());
         if (var2.isDebug()) {
            var3.info("Executing SQL Batch: " + var33);
         }

         Connection var34 = null;
         PreparedStatement var9 = null;
         boolean var10 = true;

         int var38;
         try {
            var34 = var1.getConnection();
            var10 = var34.getAutoCommit();
            var34.setAutoCommit(false);
            var9 = var34.prepareStatement(var33);

            for(Schema var12 : this.schemas) {
               List var13 = var12.getColumns();

               for(int var14 = 0; var14 < var13.size(); ++var14) {
                  var9.setObject(var14 + 1, ((ColumnDefinition)var13.get(var14)).getObject());
               }

               var12.applyWhereConditions(var9, var13.size() + 1);
               var9.addBatch();
            }

            int[] var35 = var9.executeBatch();
            var34.commit();
            int var36 = 0;

            for(int var16 : var35) {
               var36 += var16;
            }

            var38 = var36;
         } catch (SQLException var29) {
            if (var34 != null) {
               try {
                  var34.rollback();
               } catch (SQLException var28) {
                  var3.info("Rollback failed: " + var28.getMessage());
               }
            }

            var3.info("Update batch operation failed on table: " + var4.getTableName() + " - " + var29.getMessage());
            throw new DatabaseException("updateBatch", var4.getTableName(), var29);
         } finally {
            if (var9 != null) {
               try {
                  var9.close();
               } catch (SQLException var27) {
               }
            }

            if (var34 != null) {
               try {
                  var34.setAutoCommit(var10);
                  var34.close();
               } catch (SQLException var26) {
               }
            }

         }

         return var38;
      }
   }
}
