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
import java.util.List;

@SuppressWarnings({"unchecked", "rawtypes"})
public class UpsertBatchRequest implements Executor {
   private final List<Schema> schemas;

   public UpsertBatchRequest(List<Schema> var1) {
      this.schemas = var1;
   }

   public int execute(DatabaseConnection var1, DatabaseConfiguration var2, Logger var3) {
      if (this.schemas.isEmpty()) {
         return 0;
      } else {
         DatabaseType var4 = var2.getDatabaseType();
         Schema var5 = (Schema)this.schemas.get(0);
         StringBuilder var6 = new StringBuilder("INSERT INTO " + var5.getTableName() + " (");
         StringBuilder var7 = new StringBuilder("VALUES ");
         StringBuilder var8 = new StringBuilder();
         ArrayList var9 = new ArrayList();
         ArrayList var10 = new ArrayList();
         ArrayList var11 = new ArrayList();

         for(ColumnDefinition var13 : var5.getColumns()) {
            if (!var13.isAutoIncrement()) {
               var11.add(var13.getSafeName());
            }
         }

         var6.append(String.join(", ", var11)).append(") ");

         for(Schema var27 : this.schemas) {
            ArrayList var14 = new ArrayList();

            for(ColumnDefinition var16 : var27.getColumns()) {
               if (!var16.isAutoIncrement()) {
                  var14.add("?");
                  var9.add(var16.getObject());
               }
            }

            var10.add("(" + String.join(", ", var14) + ")");
         }

         var7.append(String.join(", ", var10));
         if (var4 == DatabaseType.SQLITE) {
            StringBuilder var24 = new StringBuilder(" ON CONFLICT (");
            List var28 = var5.getPrimaryKeys();
            var24.append(String.join(", ", var28)).append(") DO UPDATE SET ");

            for(int var30 = 0; var30 < var11.size(); ++var30) {
               if (var30 > 0) {
                  var8.append(", ");
               }

               var8.append((String)var11.get(var30)).append(" = excluded.").append((String)var11.get(var30));
            }

            var6.append(var7).append(var24).append(var8);
         } else {
            var8.append(" ON DUPLICATE KEY UPDATE ");

            for(int var25 = 0; var25 < var11.size(); ++var25) {
               if (var25 > 0) {
                  var8.append(", ");
               }

               var8.append((String)var11.get(var25)).append(" = VALUES(").append((String)var11.get(var25)).append(")");
            }

            var6.append(var7).append(var8);
         }

         String var26 = var2.replacePrefix(var6.toString());
         if (var2.isDebug()) {
            var3.info("Executing SQL: " + var26);
         }

         try {
            Connection var29 = var1.getConnection();

            int var34;
            try {
               PreparedStatement var31 = var29.prepareStatement(var26);

               try {
                  int var32 = 1;

                  for(Object var17 : var9) {
                     var31.setObject(var32++, var17);
                  }

                  var34 = var31.executeUpdate();
               } catch (Throwable var20) {
                  if (var31 != null) {
                     try {
                        var31.close();
                     } catch (Throwable var19) {
                        var20.addSuppressed(var19);
                     }
                  }

                  throw var20;
               }

               if (var31 != null) {
                  var31.close();
               }
            } catch (Throwable var21) {
               if (var29 != null) {
                  try {
                     var29.close();
                  } catch (Throwable var18) {
                     var21.addSuppressed(var18);
                  }
               }

               throw var21;
            }

            if (var29 != null) {
               var29.close();
            }

            return var34;
         } catch (SQLException var22) {
            var3.info("Upsert batch operation failed on table: " + var5.getTableName() + " - " + var22.getMessage());
            throw new DatabaseException("upsertBatch", var5.getTableName(), var22);
         }
      }
   }
}
