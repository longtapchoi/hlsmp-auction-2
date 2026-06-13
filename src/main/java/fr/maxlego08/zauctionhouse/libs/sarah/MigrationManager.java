package fr.maxlego08.zauctionhouse.libs.sarah;

import fr.maxlego08.zauctionhouse.libs.sarah.conditions.ColumnDefinition;
import fr.maxlego08.zauctionhouse.libs.sarah.database.DatabaseType;
import fr.maxlego08.zauctionhouse.libs.sarah.database.Migration;
import fr.maxlego08.zauctionhouse.libs.sarah.database.Schema;
import fr.maxlego08.zauctionhouse.libs.sarah.exceptions.DatabaseException;
import fr.maxlego08.zauctionhouse.libs.sarah.logger.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class MigrationManager {
   private static final List<Schema> schemas = new ArrayList();
   private static final List<Migration> migrations = new ArrayList();
   private static String migrationTableName = "migrations";
   private static DatabaseConfiguration databaseConfiguration;

   public static String getMigrationTableName() {
      return migrationTableName;
   }

   public static void setMigrationTableName(String var0) {
      migrationTableName = var0;
   }

   public static DatabaseConfiguration getDatabaseConfiguration() {
      return databaseConfiguration;
   }

   public static void setDatabaseConfiguration(DatabaseConfiguration var0) {
      databaseConfiguration = var0;
   }

   public static void registerSchema(Schema var0) {
      schemas.add(var0);
   }

   public static void execute(DatabaseConnection var0, Logger var1) {
      createMigrationTable(var0, var1);
      List var2 = getMigrations(var0, var1);
      migrations.forEach(Migration::up);
      schemas.forEach((var3) -> {
         if (!var2.contains(var3.getMigration().getClass().getSimpleName())) {
            int var4;
            try {
               var4 = var3.execute(var0, var1);
            } catch (SQLException var17) {
               throw new RuntimeException(var17);
            }

            if (var4 != -1) {
               insertMigration(var0, var1, var3.getMigration());
            }
         } else {
            if (!var3.getMigration().isAlter()) {
               return;
            }

            ArrayList var22 = new ArrayList();
            String var5 = var3.getTableName();
            var5 = var5.replace("%prefix%", var0.getDatabaseConfiguration().getTablePrefix());
            if (var0.getDatabaseConfiguration().getDatabaseType() == DatabaseType.SQLITE) {
               try {
                  Connection var24 = var0.getConnection();

                  try {
                     PreparedStatement var26 = var24.prepareStatement(String.format("PRAGMA table_info(%s)", var5));

                     try {
                        List var27 = var3.getColumns();
                        var1.info("Executing SQL: " + String.format("PRAGMA table_info(%s)", var5));
                        ResultSet var28 = var26.executeQuery();

                        try {
                           while(var28.next()) {
                              String var10 = var28.getString("name");
                              var27.removeIf((var1x) -> var1x.getName().equals(var10));
                           }
                        } catch (Throwable var18) {
                           if (var28 != null) {
                              try {
                                 var28.close();
                              } catch (Throwable var14) {
                                 var18.addSuppressed(var14);
                              }
                           }

                           throw var18;
                        }

                        if (var28 != null) {
                           var28.close();
                        }

                        var22.addAll(var27);
                     } catch (Throwable var19) {
                        if (var26 != null) {
                           try {
                              var26.close();
                           } catch (Throwable var13) {
                              var19.addSuppressed(var13);
                           }
                        }

                        throw var19;
                     }

                     if (var26 != null) {
                        var26.close();
                     }
                  } catch (Throwable var20) {
                     if (var24 != null) {
                        try {
                           var24.close();
                        } catch (Throwable var12) {
                           var20.addSuppressed(var12);
                        }
                     }

                     throw var20;
                  }

                  if (var24 != null) {
                     var24.close();
                  }
               } catch (SQLException var21) {
                  var1.info("Failed to get table info for migration: " + var21.getMessage());
                  throw new DatabaseException("migration-table-info", var5, var21);
               }
            } else {
               for(ColumnDefinition var7 : var3.getColumns()) {
                  Schema var8 = SchemaBuilder.selectCount("information_schema.COLUMNS").where("TABLE_NAME", (Object)var5).where("TABLE_SCHEMA", (Object)var0.getDatabaseConfiguration().getDatabase()).where("COLUMN_NAME", (Object)var7.getName());

                  long var9;
                  try {
                     var9 = var8.executeSelectCount(var0, var1);
                  } catch (SQLException var16) {
                     throw new RuntimeException(var16);
                  }

                  if (var9 == 0L) {
                     var22.add(var7);
                  }
               }
            }

            if (var22.isEmpty()) {
               return;
            }

            try {
               int var25 = SchemaBuilder.alter((Migration)null, var5, (Consumer)((var1x) -> {
                  for(ColumnDefinition var3 : var22) {
                     var1x.addColumn(var3).nullable();
                  }

               })).execute(var0, var1);
               if (var25 == -1) {
                  insertMigration(var0, var1, var3.getMigration());
               }
            } catch (SQLException var15) {
               throw new RuntimeException(var15);
            }
         }

      });
   }

   public static List<Migration> getMigrations() {
      return migrations;
   }

   private static void createMigrationTable(DatabaseConnection var0, Logger var1) {
      Schema var2 = SchemaBuilder.create((Migration)null, migrationTableName, (Consumer)((var0x) -> {
         var0x.text("migration");
         var0x.createdAt();
      }));

      try {
         var2.execute(var0, var1);
      } catch (SQLException var4) {
         var1.info("Failed to create migration table: " + var4.getMessage());
         throw new DatabaseException("create-migration-table", migrationTableName, var4);
      }
   }

   private static List<String> getMigrations(DatabaseConnection var0, Logger var1) {
      Schema var2 = SchemaBuilder.select(migrationTableName);

      try {
         return (List)var2.executeSelect(MigrationTable.class, var0, var1).stream().map(MigrationTable::getMigration).collect(Collectors.toList());
      } catch (Exception var4) {
         var1.info("Failed to get migrations list: " + var4.getMessage());
         return new ArrayList();
      }
   }

   private static void insertMigration(DatabaseConnection var0, Logger var1, Migration var2) {
      try {
         SchemaBuilder.insert(migrationTableName, (var1x) -> var1x.string("migration", var2.getClass().getSimpleName())).execute(var0, var1);
      } catch (SQLException var4) {
         var1.info("Failed to insert migration record: " + var4.getMessage());
         throw new DatabaseException("insert-migration", migrationTableName, var4);
      }
   }

   public static void registerMigration(Migration var0) {
      migrations.add(var0);
   }

   public static class MigrationTable {
      @Column("migration")
      private final String migration;

      public MigrationTable(String var1) {
         this.migration = var1;
      }

      public String getMigration() {
         return this.migration;
      }
   }
}
