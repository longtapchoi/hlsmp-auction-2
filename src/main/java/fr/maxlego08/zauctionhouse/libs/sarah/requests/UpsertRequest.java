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

public class UpsertRequest implements Executor {
   private final Schema schema;

   public UpsertRequest(Schema var1) {
      this.schema = var1;
   }

   public int execute(DatabaseConnection var1, DatabaseConfiguration var2, Logger var3) {
      DatabaseType var4 = var2.getDatabaseType();
      StringBuilder var5 = new StringBuilder("INSERT INTO " + this.schema.getTableName() + " (");
      StringBuilder var6 = new StringBuilder("VALUES (");
      StringBuilder var7 = new StringBuilder();
      ArrayList var8 = new ArrayList();
      ArrayList var9 = new ArrayList();
      int var10 = 0;
      int var11 = 0;

      for(ColumnDefinition var13 : this.schema.getColumns()) {
         if (!var13.isAutoIncrement()) {
            var5.append(var10 > 0 ? ", " : "").append(var13.getSafeName());
            var6.append(var10 > 0 ? ", " : "").append("?");
            var8.add(var13.getObject());
            ++var10;
         }

         if (!var13.isAutoIncrement()) {
            if (var11 > 0) {
               var7.append(", ");
            }

            if (var4 == DatabaseType.SQLITE) {
               var7.append(var13.getSafeName()).append(" = excluded.").append(var13.getSafeName());
            } else {
               var7.append(var13.getSafeName()).append(" = ?");
               var9.add(var13.getObject());
            }

            ++var11;
         }
      }

      var5.append(") ");
      var6.append(")");
      String var24;
      if (var4 == DatabaseType.SQLITE) {
         StringBuilder var25 = new StringBuilder(" ON CONFLICT (");
         List var14 = this.getNonAutoIncrementPrimaryKeys();

         for(int var15 = 0; var15 < var14.size(); ++var15) {
            var25.append(var15 > 0 ? ", " : "").append((String)var14.get(var15));
         }

         var25.append(") DO UPDATE SET ");
         var24 = var5 + var6.toString() + var25 + var7;
      } else {
         var7.insert(0, " ON DUPLICATE KEY UPDATE ");
         var24 = var5 + var6.toString() + var7;
      }

      String var26 = var2.replacePrefix(var24);
      if (var2.isDebug()) {
         var3.info("Executing SQL: " + var26);
      }

      try {
         Connection var27 = var1.getConnection();

         int var30;
         try {
            PreparedStatement var28 = var27.prepareStatement(var26);

            try {
               int var16 = 1;

               for(Object var18 : var8) {
                  var28.setObject(var16++, var18);
               }

               if (var4 != DatabaseType.SQLITE) {
                  for(Object var31 : var9) {
                     var28.setObject(var16++, var31);
                  }
               }

               var28.executeUpdate();
               var30 = var28.getUpdateCount();
            } catch (Throwable var21) {
               if (var28 != null) {
                  try {
                     var28.close();
                  } catch (Throwable var20) {
                     var21.addSuppressed(var20);
                  }
               }

               throw var21;
            }

            if (var28 != null) {
               var28.close();
            }
         } catch (Throwable var22) {
            if (var27 != null) {
               try {
                  var27.close();
               } catch (Throwable var19) {
                  var22.addSuppressed(var19);
               }
            }

            throw var22;
         }

         if (var27 != null) {
            var27.close();
         }

         return var30;
      } catch (SQLException var23) {
         var3.info("Upsert operation failed on table: " + this.schema.getTableName() + " - " + var23.getMessage());
         throw new DatabaseException("upsert", this.schema.getTableName(), var23);
      }
   }

   private List<String> getNonAutoIncrementPrimaryKeys() {
      ArrayList var1 = new ArrayList();

      for(String var4 : this.schema.getPrimaryKeys()) {
         boolean var5 = false;

         for(ColumnDefinition var7 : this.schema.getColumns()) {
            if (var7.getSafeName().equals(var4) && var7.isAutoIncrement()) {
               var5 = true;
               break;
            }
         }

         if (!var5) {
            var1.add(var4);
         }
      }

      if (var1.isEmpty()) {
         for(ColumnDefinition var9 : this.schema.getColumns()) {
            if (var9.isUnique() && !var9.isAutoIncrement()) {
               var1.add(var9.getSafeName());
            }
         }
      }

      if (var1.isEmpty()) {
         throw new IllegalStateException("UPSERT requires at least one non-auto-increment primary key or unique constraint for SQLite");
      } else {
         return var1;
      }
   }
}
