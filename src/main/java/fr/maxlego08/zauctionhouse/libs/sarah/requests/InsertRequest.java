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

@SuppressWarnings({"unchecked", "rawtypes"})
public class InsertRequest implements Executor {
   private final Schema schema;

   public InsertRequest(Schema var1) {
      this.schema = var1;
   }

   public int execute(DatabaseConnection var1, DatabaseConfiguration var2, Logger var3) {
      StringBuilder var4 = new StringBuilder("INSERT INTO " + this.schema.getTableName() + " (");
      StringBuilder var5 = new StringBuilder("VALUES (");
      ArrayList var6 = new ArrayList();
      int var7 = 0;

      for(ColumnDefinition var9 : this.schema.getColumns()) {
         if (!var9.isAutoIncrement()) {
            var4.append(var7 > 0 ? ", " : "").append(var9.getSafeName());
            var5.append(var7 > 0 ? ", " : "").append("?");
            var6.add(var9.getObject());
            ++var7;
         }
      }

      var4.append(") ");
      var5.append(")");
      String var22 = var2.replacePrefix(var4 + var5.toString());
      if (var2.isDebug()) {
         var3.info("Executing SQL: " + var22);
      }

      try {
         Connection var23 = var1.getConnection();

         int var25;
         label149: {
            try {
               PreparedStatement var10;
               label151: {
                  var10 = var23.prepareStatement(var22, 1);

                  try {
                     for(int var11 = 0; var11 < var6.size(); ++var11) {
                        var10.setObject(var11 + 1, var6.get(var11));
                     }

                     var10.executeUpdate();

                     try {
                        ResultSet var24 = var10.getGeneratedKeys();

                        label115: {
                           try {
                              if (var24.next()) {
                                 var25 = var24.getInt(1);
                                 break label115;
                              }

                              var25 = 0;
                           } catch (Throwable var17) {
                              if (var24 != null) {
                                 try {
                                    var24.close();
                                 } catch (Throwable var16) {
                                    var17.addSuppressed(var16);
                                 }
                              }

                              throw var17;
                           }

                           if (var24 != null) {
                              var24.close();
                           }
                           break label151;
                        }

                        if (var24 != null) {
                           var24.close();
                        }
                     } catch (Exception var18) {
                        var3.info("Insert operation failed on table: " + this.schema.getTableName() + " - Failed to retrieve generated keys - " + var18.getMessage());
                        throw new DatabaseException("insert", this.schema.getTableName(), var18);
                     }
                  } catch (Throwable var19) {
                     if (var10 != null) {
                        try {
                           var10.close();
                        } catch (Throwable var15) {
                           var19.addSuppressed(var15);
                        }
                     }

                     throw var19;
                  }

                  if (var10 != null) {
                     var10.close();
                  }
                  break label149;
               }

               if (var10 != null) {
                  var10.close();
               }
            } catch (Throwable var20) {
               if (var23 != null) {
                  try {
                     var23.close();
                  } catch (Throwable var14) {
                     var20.addSuppressed(var14);
                  }
               }

               throw var20;
            }

            if (var23 != null) {
               var23.close();
            }

            return var25;
         }

         if (var23 != null) {
            var23.close();
         }

         return var25;
      } catch (SQLException var21) {
         var3.info("Insert operation failed on table: " + this.schema.getTableName() + " - " + var21.getMessage());
         throw new DatabaseException("insert", this.schema.getTableName(), var21);
      }
   }
}
