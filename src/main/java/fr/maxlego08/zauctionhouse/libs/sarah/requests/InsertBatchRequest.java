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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InsertBatchRequest implements Executor {
   private final List<Schema> schemas;

   public InsertBatchRequest(List<Schema> var1) {
      this.schemas = var1;
   }

   public int execute(DatabaseConnection var1, DatabaseConfiguration var2, Logger var3) {
      if (this.schemas.isEmpty()) {
         return 0;
      } else {
         Schema var4 = (Schema)this.schemas.get(0);
         StringBuilder var5 = new StringBuilder("INSERT INTO " + var4.getTableName() + " (");
         StringBuilder var6 = new StringBuilder("VALUES ");
         ArrayList var7 = new ArrayList();
         ArrayList var8 = new ArrayList();
         ArrayList var9 = new ArrayList();

         for(ColumnDefinition var11 : var4.getColumns()) {
            if (!var11.isAutoIncrement()) {
               var9.add(var11.getSafeName());
            }
         }

         var5.append(String.join(", ", var9)).append(") ");

         for(Schema var27 : this.schemas) {
            ArrayList var12 = new ArrayList();

            for(ColumnDefinition var14 : var27.getColumns()) {
               if (!var14.isAutoIncrement()) {
                  var12.add("?");
                  var7.add(var14.getObject());
               }
            }

            var8.add("(" + String.join(", ", var12) + ")");
         }

         var6.append(String.join(", ", var8));
         var5.append(var6);
         String var26 = var2.replacePrefix(var5.toString());
         if (var2.isDebug()) {
            var3.info("Executing SQL: " + var26);
         }

         try {
            Connection var28 = var1.getConnection();

            int var16;
            label153: {
               int var34;
               try {
                  PreparedStatement var29;
                  label155: {
                     var29 = var28.prepareStatement(var26, 1);

                     try {
                        int var30 = 1;

                        for(Object var15 : var7) {
                           var29.setObject(var30++, var15);
                        }

                        int var32 = var29.executeUpdate();
                        ResultSet var33 = var29.getGeneratedKeys();

                        label157: {
                           try {
                              if (var33.next()) {
                                 var16 = var33.getInt(1);
                                 break label157;
                              }
                           } catch (Throwable var21) {
                              if (var33 != null) {
                                 try {
                                    var33.close();
                                 } catch (Throwable var20) {
                                    var21.addSuppressed(var20);
                                 }
                              }

                              throw var21;
                           }

                           if (var33 != null) {
                              var33.close();
                           }

                           var34 = var32;
                           break label155;
                        }

                        if (var33 != null) {
                           var33.close();
                        }
                     } catch (Throwable var22) {
                        if (var29 != null) {
                           try {
                              var29.close();
                           } catch (Throwable var19) {
                              var22.addSuppressed(var19);
                           }
                        }

                        throw var22;
                     }

                     if (var29 != null) {
                        var29.close();
                     }
                     break label153;
                  }

                  if (var29 != null) {
                     var29.close();
                  }
               } catch (Throwable var23) {
                  if (var28 != null) {
                     try {
                        var28.close();
                     } catch (Throwable var18) {
                        var23.addSuppressed(var18);
                     }
                  }

                  throw var23;
               }

               if (var28 != null) {
                  var28.close();
               }

               return var34;
            }

            if (var28 != null) {
               var28.close();
            }

            return var16;
         } catch (SQLException var24) {
            var3.info("Insert batch operation failed on table: " + var4.getTableName() + " - " + var24.getMessage());
            throw new DatabaseException("insertBatch", var4.getTableName(), var24);
         }
      }
   }
}
