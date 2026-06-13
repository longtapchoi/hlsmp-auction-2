package fr.maxlego08.zauctionhouse.libs.sarah;

import fr.maxlego08.zauctionhouse.libs.sarah.conditions.ColumnDefinition;
import fr.maxlego08.zauctionhouse.libs.sarah.conditions.JoinCondition;
import fr.maxlego08.zauctionhouse.libs.sarah.conditions.SelectCondition;
import fr.maxlego08.zauctionhouse.libs.sarah.conditions.WhereCondition;
import fr.maxlego08.zauctionhouse.libs.sarah.database.DatabaseType;
import fr.maxlego08.zauctionhouse.libs.sarah.database.Executor;
import fr.maxlego08.zauctionhouse.libs.sarah.database.Migration;
import fr.maxlego08.zauctionhouse.libs.sarah.database.Schema;
import fr.maxlego08.zauctionhouse.libs.sarah.database.SchemaType;
import fr.maxlego08.zauctionhouse.libs.sarah.exceptions.SarahException;
import fr.maxlego08.zauctionhouse.libs.sarah.logger.Logger;
import fr.maxlego08.zauctionhouse.libs.sarah.requests.AlterRequest;
import fr.maxlego08.zauctionhouse.libs.sarah.requests.CreateIndexRequest;
import fr.maxlego08.zauctionhouse.libs.sarah.requests.CreateRequest;
import fr.maxlego08.zauctionhouse.libs.sarah.requests.DeleteRequest;
import fr.maxlego08.zauctionhouse.libs.sarah.requests.DropTableRequest;
import fr.maxlego08.zauctionhouse.libs.sarah.requests.InsertRequest;
import fr.maxlego08.zauctionhouse.libs.sarah.requests.ModifyRequest;
import fr.maxlego08.zauctionhouse.libs.sarah.requests.RenameExecutor;
import fr.maxlego08.zauctionhouse.libs.sarah.requests.UpdateRequest;
import fr.maxlego08.zauctionhouse.libs.sarah.requests.UpsertRequest;
import fr.maxlego08.zauctionhouse.libs.sarah.security.SecureObjectInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@SuppressWarnings({"unchecked", "rawtypes"})
public class SchemaBuilder implements Schema {
   private final String tableName;
   private final SchemaType schemaType;
   private final List<ColumnDefinition> columns = new ArrayList();
   private final List<String> primaryKeys = new ArrayList();
   private final List<String> foreignKeys = new ArrayList();
   private final List<WhereCondition> whereConditions = new ArrayList();
   private final List<JoinCondition> joinConditions = new ArrayList();
   private final List<SelectCondition> selectColumns = new ArrayList();
   private String newTableName;
   private String orderBy;
   private Migration migration;
   private boolean isDistinct;

   private SchemaBuilder(String var1, SchemaType var2) {
      this.tableName = var1;
      this.schemaType = var2;
   }

   public static Schema copy(String var0, SchemaType var1, Schema var2) {
      SchemaBuilder var3 = new SchemaBuilder(var0, var1);
      var3.columns.addAll(var2.getColumns());
      var3.primaryKeys.addAll(var2.getPrimaryKeys());
      var3.foreignKeys.addAll(var2.getForeignKeys());
      var3.whereConditions.addAll(var2.getWhereConditions());
      var3.joinConditions.addAll(var2.getJoinConditions());
      var3.selectColumns.addAll(var2.getSelectColumns());
      var3.orderBy = var2.getOrderBy();
      var3.migration = var2.getMigration();
      var3.isDistinct = var2.isDistinct();
      var3.newTableName = var2.getNewTableName();
      return var3;
   }

   public static Schema rename(String var0, String var1) {
      return rename((Migration)null, var0, var1);
   }

   public static Schema rename(Migration var0, String var1, String var2) {
      SchemaBuilder var3 = new SchemaBuilder(var1, SchemaType.RENAME);
      var3.newTableName = var2;
      if (var0 != null) {
         var3.migration = var0;
         MigrationManager.registerSchema(var3);
      }

      return var3;
   }

   public static Schema create(Migration var0, String var1, Class<?> var2) {
      return create(var0, var1, ConsumerConstructor.createConsumerFromTemplate(var2, (Object)null));
   }

   public static Schema create(Migration var0, String var1, Consumer<Schema> var2) {
      SchemaBuilder var3 = new SchemaBuilder(var1, SchemaType.CREATE);
      if (var0 != null) {
         var3.migration = var0;
         MigrationManager.registerSchema(var3);
      }

      var2.accept(var3);
      return var3;
   }

   public static Schema createIndex(Migration var0, String var1, String var2) {
      SchemaBuilder var3 = new SchemaBuilder(var1, SchemaType.CREATE_INDEX);
      if (var0 != null) {
         var3.migration = var0;
         MigrationManager.registerSchema(var3);
      }

      var3.addColumn(new ColumnDefinition(var2, ""));
      return var3;
   }

   public static Schema modify(Migration var0, String var1, Consumer<Schema> var2) {
      SchemaBuilder var3 = new SchemaBuilder(var1, SchemaType.MODIFY);
      if (var0 != null) {
         var3.migration = var0;
         MigrationManager.registerSchema(var3);
      }

      var2.accept(var3);
      return var3;
   }

   public static Schema drop(Migration var0, String var1) {
      SchemaBuilder var2 = new SchemaBuilder(var1, SchemaType.DROP);
      if (var0 != null) {
         var2.migration = var0;
         MigrationManager.registerSchema(var2);
      }

      return var2;
   }

   public static Schema upsert(String var0, Consumer<Schema> var1) {
      SchemaBuilder var2 = new SchemaBuilder(var0, SchemaType.UPSERT);
      var1.accept(var2);
      return var2;
   }

   public static Schema alter(Migration var0, String var1, Class<?> var2) {
      return alter(var0, var1, ConsumerConstructor.createConsumerFromTemplate(var2, (Object)null));
   }

   public static Schema alter(Migration var0, String var1, Consumer<Schema> var2) {
      SchemaBuilder var3 = new SchemaBuilder(var1, SchemaType.ALTER);
      if (var0 != null) {
         var3.migration = var0;
         MigrationManager.registerSchema(var3);
      }

      var2.accept(var3);
      return var3;
   }

   public static Schema insert(String var0, Consumer<Schema> var1) {
      SchemaBuilder var2 = new SchemaBuilder(var0, SchemaType.INSERT);
      var1.accept(var2);
      return var2;
   }

   public static Schema update(String var0, Consumer<Schema> var1) {
      SchemaBuilder var2 = new SchemaBuilder(var0, SchemaType.UPDATE);
      var1.accept(var2);
      return var2;
   }

   public static Schema select(String var0) {
      return new SchemaBuilder(var0, SchemaType.SELECT);
   }

   public static Schema selectCount(String var0) {
      return new SchemaBuilder(var0, SchemaType.SELECT);
   }

   public static Schema delete(String var0) {
      return new SchemaBuilder(var0, SchemaType.DELETE);
   }

   public Schema where(String var1, Object var2) {
      return this.where((String)null, var1, "=", var2);
   }

   public Schema where(String var1, UUID var2) {
      return this.where(var1, (Object)var2.toString());
   }

   public Schema where(String var1, String var2, Object var3) {
      return this.where((String)null, var1, var2, var3);
   }

   public Schema where(String var1, String var2, String var3, Object var4) {
      this.whereConditions.add(new WhereCondition(var1, var2, var3, var4));
      return this;
   }

   public Schema whereNotNull(String var1) {
      this.whereConditions.add(new WhereCondition(var1, WhereCondition.WhereAction.IS_NOT_NULL));
      return this;
   }

   public Schema whereNull(String var1) {
      this.whereConditions.add(new WhereCondition(var1, WhereCondition.WhereAction.IS_NULL));
      return this;
   }

   public Schema whereIn(String var1, Object... var2) {
      List var3 = (List)Arrays.stream(var2).map(String::valueOf).collect(Collectors.toList());
      this.whereConditions.add(new WhereCondition((String)null, var1, var3));
      return this;
   }

   public Schema whereIn(String var1, List<String> var2) {
      this.whereConditions.add(new WhereCondition((String)null, var1, var2));
      return this;
   }

   public Schema whereIn(String var1, String var2, List<String> var3) {
      this.whereConditions.add(new WhereCondition(var1, var2, var3));
      return this;
   }

   public Schema uuid(String var1) {
      this.string(var1, 36);
      return this;
   }

   public Schema uuid(String var1, UUID var2) {
      return this.addColumn((new ColumnDefinition(var1)).setObject(var2.toString()));
   }

   public Schema string(String var1, int var2) {
      return this.addColumn((new ColumnDefinition(var1, "VARCHAR")).setLength(var2));
   }

   public Schema text(String var1) {
      return this.addColumn(new ColumnDefinition(var1, "TEXT"));
   }

   public Schema longText(String var1) {
      return this.addColumn(new ColumnDefinition(var1, "LONGTEXT"));
   }

   public Schema decimal(String var1) {
      return this.decimal(var1, 65, 30);
   }

   public Schema decimal(String var1, int var2, int var3) {
      return this.addColumn((new ColumnDefinition(var1, "DECIMAL")).setLength(var2).setDecimal(var3));
   }

   public Schema string(String var1, String var2) {
      return this.addColumn((new ColumnDefinition(var1)).setObject(var2));
   }

   public Schema decimal(String var1, Number var2) {
      return this.addColumn((new ColumnDefinition(var1)).setObject(var2));
   }

   public Schema date(String var1, Date var2) {
      return this.addColumn((new ColumnDefinition(var1)).setObject(var2));
   }

   public Schema object(String var1, Object var2) {
      return this.addColumn((new ColumnDefinition(var1)).setObject(var2));
   }

   public Schema bigInt(String var1) {
      return this.addColumn(new ColumnDefinition(var1, "BIGINT"));
   }

   public Schema integer(String var1) {
      return this.addColumn(new ColumnDefinition(var1, "INT"));
   }

   public Schema bigInt(String var1, long var2) {
      return this.addColumn((new ColumnDefinition(var1)).setObject(var2));
   }

   public Schema bool(String var1) {
      return this.addColumn(new ColumnDefinition(var1, "BOOLEAN"));
   }

   public Schema bool(String var1, boolean var2) {
      return this.addColumn((new ColumnDefinition(var1)).setObject(var2));
   }

   public Schema json(String var1) {
      return this.addColumn(new ColumnDefinition(var1, "JSON"));
   }

   public Schema blob(String var1) {
      return this.addColumn(new ColumnDefinition(var1, "BLOB"));
   }

   public Schema blob(String var1, byte[] var2) {
      return this.addColumn((new ColumnDefinition(var1, "BLOB")).setObject(var2));
   }

   public Schema enumValue(String var1) {
      return this.addColumn((new ColumnDefinition(var1, "VARCHAR")).setLength(255));
   }

   public Schema enumValue(String var1, Enum<?> var2) {
      return this.addColumn((new ColumnDefinition(var1)).setObject(var2.name()));
   }

   public Schema enumType(String var1, String... var2) {
      return this.addColumn((new ColumnDefinition(var1, "ENUM")).setEnumValues(var2));
   }

   public <E extends Enum<E>> Schema enumType(String var1, Class<E> var2) {
      return this.addColumn((new ColumnDefinition(var1, "ENUM")).setEnumValues(var2));
   }

   public Schema blob(String var1, Object var2) {
      try {
         byte[] var3 = this.serializeObject(var2);
         return this.addColumn((new ColumnDefinition(var1, "BLOB")).setObject(var3));
      } catch (IOException var4) {
         throw new RuntimeException("An error occurred while serializing object for BLOB column: " + var1, var4);
      }
   }

   public Schema foreignKey(String var1) {
      if (this.columns.isEmpty()) {
         throw new IllegalStateException("No column defined to apply foreign key.");
      } else {
         ColumnDefinition var2 = (ColumnDefinition)this.columns.get(this.columns.size() - 1);
         String var3 = String.format("FOREIGN KEY (%s) REFERENCES %s(%s) ON DELETE CASCADE", var2.getSafeName(), this.safeTable(var1), var2.getSafeName());
         this.foreignKeys.add(var3);
         return this;
      }
   }

   public Schema foreignKey(String var1, String var2, boolean var3) {
      if (this.columns.isEmpty()) {
         throw new IllegalStateException("No column defined to apply foreign key.");
      } else {
         ColumnDefinition var4 = (ColumnDefinition)this.columns.get(this.columns.size() - 1);
         String var5 = String.format("FOREIGN KEY (%s) REFERENCES %s(`%s`)%s", var4.getSafeName(), this.safeTable(var1), var2, var3 ? " ON DELETE CASCADE" : "");
         this.foreignKeys.add(var5);
         return this;
      }
   }

   private String safeTable(String var1) {
      return "`" + var1 + "`";
   }

   public Schema createdAt() {
      ColumnDefinition var1 = new ColumnDefinition("created_at", "TIMESTAMP");
      var1.setDefaultValue("CURRENT_TIMESTAMP");
      this.columns.add(var1);
      return this;
   }

   public Schema timestamp(String var1) {
      return this.addColumn(new ColumnDefinition(var1, "TIMESTAMP"));
   }

   public Schema autoIncrement(String var1) {
      return this.addColumn((new ColumnDefinition(var1, "INTEGER")).setAutoIncrement(true)).primary();
   }

   public Schema autoIncrementBigInt(String var1) {
      return this.addColumn((new ColumnDefinition(var1, "BIGINT")).setAutoIncrement(true)).primary();
   }

   public Schema updatedAt() {
      ColumnDefinition var1 = new ColumnDefinition("updated_at", "TIMESTAMP");
      DatabaseConfiguration var2 = MigrationManager.getDatabaseConfiguration();
      if (var2.getDatabaseType() == DatabaseType.SQLITE) {
         var1.setDefaultValue("CURRENT_TIMESTAMP");
      } else {
         var1.setDefaultValue("CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP");
      }

      this.columns.add(var1);
      return this;
   }

   public Schema nullable() {
      this.getLastColumn().setNullable(true);
      return this;
   }

   public Schema unique(boolean var1) {
      this.getLastColumn().setUnique(var1);
      return this;
   }

   public Schema unique() {
      return this.unique(true);
   }

   public Schema defaultValue(Object var1) {
      this.getLastColumn().setDefaultValue(var1.toString());
      return this;
   }

   public Schema defaultCurrentTimestamp() {
      return this.defaultValue("CURRENT_TIMESTAMP");
   }

   public Schema primary() {
      ColumnDefinition var1 = this.getLastColumn();
      var1.setPrimaryKey(true);
      this.primaryKeys.add(var1.getSafeName());
      return this;
   }

   public Schema addColumn(ColumnDefinition var1) {
      this.columns.add(var1);
      return this;
   }

   public Schema timestamps() {
      this.createdAt();
      this.updatedAt();
      return this;
   }

   private ColumnDefinition getLastColumn() {
      if (this.columns.isEmpty()) {
         throw new IllegalStateException("No columns defined.");
      } else {
         return (ColumnDefinition)this.columns.get(this.columns.size() - 1);
      }
   }

   public String getTableName() {
      return this.tableName;
   }

   public void whereConditions(StringBuilder var1) {
      if (!this.whereConditions.isEmpty()) {
         ArrayList var2 = new ArrayList();

         for(WhereCondition var4 : this.whereConditions) {
            var2.add(var4.getCondition());
         }

         var1.append(" WHERE ").append(String.join(" AND ", var2));
      }

   }

   public long executeSelectCount(DatabaseConnection var1, Logger var2) {
      StringBuilder var3 = new StringBuilder("SELECT COUNT(*) FROM " + this.tableName);
      this.whereConditions(var3);
      String var4 = var1.getDatabaseConfiguration().replacePrefix(var3.toString());
      if (var1.getDatabaseConfiguration().isDebug()) {
         var2.info("Executing SQL: " + var4);
      }

      try {
         Connection var5 = var1.getConnection();

         long var8;
         label104: {
            try {
               PreparedStatement var6;
               label106: {
                  var6 = var5.prepareStatement(var4);

                  try {
                     this.applyWhereConditions(var6, 1);
                     ResultSet var7 = var6.executeQuery();

                     label86: {
                        try {
                           if (var7.next()) {
                              var8 = (long)var7.getInt(1);
                              break label86;
                           }
                        } catch (Throwable var13) {
                           if (var7 != null) {
                              try {
                                 var7.close();
                              } catch (Throwable var12) {
                                 var13.addSuppressed(var12);
                              }
                           }

                           throw var13;
                        }

                        if (var7 != null) {
                           var7.close();
                        }
                        break label106;
                     }

                     if (var7 != null) {
                        var7.close();
                     }
                  } catch (Throwable var14) {
                     if (var6 != null) {
                        try {
                           var6.close();
                        } catch (Throwable var11) {
                           var14.addSuppressed(var11);
                        }
                     }

                     throw var14;
                  }

                  if (var6 != null) {
                     var6.close();
                  }
                  break label104;
               }

               if (var6 != null) {
                  var6.close();
               }
            } catch (Throwable var15) {
               if (var5 != null) {
                  try {
                     var5.close();
                  } catch (Throwable var10) {
                     var15.addSuppressed(var10);
                  }
               }

               throw var15;
            }

            if (var5 != null) {
               var5.close();
            }

            return 0L;
         }

         if (var5 != null) {
            var5.close();
         }

         return var8;
      } catch (SQLException var16) {
         var2.info("Failed to execute schema select count: " + var16.getMessage());
         throw new SQLException("Failed to execute schema select count: " + var16.getMessage(), var16);
      }
   }

   public List<Map<String, Object>> executeSelect(DatabaseConnection var1, Logger var2) {
      ArrayList var3 = new ArrayList();
      String var4 = "*";
      if (!this.selectColumns.isEmpty()) {
         var4 = (String)this.selectColumns.stream().map(SelectCondition::getSelectColumn).collect(Collectors.joining(","));
      }

      StringBuilder var5;
      if (this.isDistinct) {
         var5 = new StringBuilder("SELECT DISTINCT " + var4 + " FROM " + this.tableName);
      } else {
         var5 = new StringBuilder("SELECT " + var4 + " FROM " + this.tableName);
      }

      if (!this.joinConditions.isEmpty()) {
         for(JoinCondition var7 : this.joinConditions) {
            var5.append(" ").append(var7.getJoinClause());
         }
      }

      this.whereConditions(var5);
      if (this.orderBy != null) {
         var5.append(" ").append(this.orderBy);
      }

      DatabaseConfiguration var20 = var1.getDatabaseConfiguration();
      String var21 = var20.replacePrefix(var5.toString());
      if (var20.isDebug()) {
         var2.info("Executing SQL: " + var21);
      }

      try {
         Connection var8 = var1.getConnection();

         try {
            PreparedStatement var9 = var8.prepareStatement(var21);

            try {
               this.applyWhereConditions(var9, 1);
               ResultSet var10 = var9.executeQuery();

               try {
                  while(var10.next()) {
                     HashMap var11 = new HashMap();

                     for(int var12 = 1; var12 <= var10.getMetaData().getColumnCount(); ++var12) {
                        var11.put(var10.getMetaData().getColumnName(var12), var10.getObject(var12));
                     }

                     var3.add(var11);
                  }
               } catch (Throwable var16) {
                  if (var10 != null) {
                     try {
                        var10.close();
                     } catch (Throwable var15) {
                        var16.addSuppressed(var15);
                     }
                  }

                  throw var16;
               }

               if (var10 != null) {
                  var10.close();
               }
            } catch (Throwable var17) {
               if (var9 != null) {
                  try {
                     var9.close();
                  } catch (Throwable var14) {
                     var17.addSuppressed(var14);
                  }
               }

               throw var17;
            }

            if (var9 != null) {
               var9.close();
            }
         } catch (Throwable var18) {
            if (var8 != null) {
               try {
                  var8.close();
               } catch (Throwable var13) {
                  var18.addSuppressed(var13);
               }
            }

            throw var18;
         }

         if (var8 != null) {
            var8.close();
         }

         return var3;
      } catch (SQLException var19) {
         var2.info("Failed to execute schema select: " + var19.getMessage());
         throw new SQLException("Failed to execute schema select: " + var19.getMessage(), var19);
      }
   }

   public void applyWhereConditions(PreparedStatement var1, int var2) {
      for(WhereCondition var4 : this.whereConditions) {
         if (var4.getWhereAction() == WhereCondition.WhereAction.NORMAL) {
            var1.setObject(var2, var4.getValue());
            ++var2;
         } else if (var4.getWhereAction() == WhereCondition.WhereAction.IN) {
            for(String var6 : var4.getValues()) {
               var1.setObject(var2, var6);
               ++var2;
            }
         }
      }

   }

   public <T> List<T> executeSelect(Class<T> var1, DatabaseConnection var2, Logger var3) {
      List var4 = this.executeSelect(var2, var3);
      return this.<T>transformResults(var4, var1);
   }

   private <T> List<T> transformResults(List<Map<String, Object>> var1, Class<T> var2) {
      ArrayList var3 = new ArrayList();
      Constructor[] var4 = var2.getDeclaredConstructors();
      Constructor var5 = var4[0];
      var5.setAccessible(true);

      for(Map var7 : var1) {
         Object[] var8 = new Object[var5.getParameterCount()];
         Field[] var9 = var2.getDeclaredFields();

         for(int var10 = 0; var10 < var9.length; ++var10) {
            Field var11 = var9[var10];
            if (var11.isAnnotationPresent(Column.class)) {
               Column var12 = (Column)var11.getAnnotation(Column.class);
               var8[var10] = this.convertToRequiredType(var7.get(var12.value()), var11.getType());
            } else {
               var8[var10] = this.convertToRequiredType(var7.get(var11.getName()), var11.getType());
            }
         }

         Object var13 = var5.newInstance(var8);
         var3.add(var13);
      }

      return var3;
   }

   protected Object convertToRequiredType(Object var1, Class<?> var2) {
      if (var1 == null) {
         return null;
      } else if (var2.isEnum()) {
         return Enum.valueOf(var2, (String)var1);
      } else if (var2 == BigDecimal.class) {
         return new BigDecimal(var1.toString());
      } else if (var2 == UUID.class) {
         return UUID.fromString((String)var1);
      } else if (var2 != Boolean.class && var2 != Boolean.TYPE) {
         if (var2 != Long.class && var2 != Long.TYPE) {
            if (var2 != Double.class && var2 != Double.TYPE) {
               if (var2 != Integer.class && var2 != Integer.TYPE) {
                  if (Serializable.class.isAssignableFrom(var2) && var1 instanceof byte[]) {
                     return this.deserializeObject((byte[])var1, var2);
                  } else if (var2 == Date.class) {
                     if (var1 instanceof String) {
                        SimpleDateFormat var6 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                        try {
                           return var6.parse((String)var1);
                        } catch (ParseException var5) {
                           throw new SarahException("Failed to parse date: " + var1, var5);
                        }
                     } else if (var1 instanceof Number) {
                        return new Date(((Number)var1).longValue());
                     } else {
                        return var1 instanceof Timestamp ? (Date)var1 : null;
                     }
                  } else {
                     return var1;
                  }
               } else {
                  return Integer.parseInt(var1.toString());
               }
            } else {
               return Double.parseDouble(var1.toString());
            }
         } else {
            return Long.parseLong(var1.toString());
         }
      } else {
         String var3 = var1.toString();
         return var3.equalsIgnoreCase("true") || var3.equalsIgnoreCase("1");
      }
   }

   protected byte[] serializeObject(Object var1) {
      ByteArrayOutputStream var2 = new ByteArrayOutputStream();

      byte[] var4;
      try {
         ObjectOutputStream var3 = new ObjectOutputStream(var2);

         try {
            var3.writeObject(var1);
            var4 = var2.toByteArray();
         } catch (Throwable var8) {
            try {
               var3.close();
            } catch (Throwable var7) {
               var8.addSuppressed(var7);
            }

            throw var8;
         }

         var3.close();
      } catch (Throwable var9) {
         try {
            var2.close();
         } catch (Throwable var6) {
            var9.addSuppressed(var6);
         }

         throw var9;
      }

      var2.close();
      return var4;
   }

   protected <T> T deserializeObject(byte[] var1, Class<T> var2) {
      try {
         ByteArrayInputStream var3 = new ByteArrayInputStream(var1);

         Object var12;
         try {
            SecureObjectInputStream var4 = new SecureObjectInputStream(var3, new Class[]{var2});

            try {
               if (var2.getPackage() != null) {
                  String var5 = var2.getPackage().getName();
                  var4.allowPackagePrefix(var5);
               }

               var12 = var2.cast(var4.readObject());
            } catch (Throwable var9) {
               try {
                  var4.close();
               } catch (Throwable var8) {
                  var9.addSuppressed(var8);
               }

               throw var9;
            }

            var4.close();
         } catch (Throwable var10) {
            try {
               var3.close();
            } catch (Throwable var7) {
               var10.addSuppressed(var7);
            }

            throw var10;
         }

         var3.close();
         return (T)var12;
      } catch (ClassNotFoundException | IOException var11) {
         throw new SarahException("Failed to deserialize BLOB: " + ((Exception)var11).getMessage(), var11);
      }
   }

   public Migration getMigration() {
      return this.migration;
   }

   public void setMigration(Migration var1) {
      this.migration = var1;
   }

   public Schema leftJoin(String var1, String var2, String var3, String var4, String var5) {
      this.joinConditions.add(new JoinCondition(JoinCondition.JoinType.LEFT, var1, var2, var3, var4, var5, (JoinCondition)null));
      return this;
   }

   public Schema leftJoin(String var1, String var2, String var3, String var4, String var5, JoinCondition var6) {
      this.joinConditions.add(new JoinCondition(JoinCondition.JoinType.LEFT, var1, var2, var3, var4, var5, var6));
      return this;
   }

   public Schema rightJoin(String var1, String var2, String var3, String var4, String var5) {
      this.joinConditions.add(new JoinCondition(JoinCondition.JoinType.RIGHT, var1, var2, var3, var4, var5, (JoinCondition)null));
      return this;
   }

   public Schema innerJoin(String var1, String var2, String var3, String var4, String var5) {
      this.joinConditions.add(new JoinCondition(JoinCondition.JoinType.INNER, var1, var2, var3, var4, var5, (JoinCondition)null));
      return this;
   }

   public Schema fullJoin(String var1, String var2, String var3, String var4, String var5) {
      this.joinConditions.add(new JoinCondition(JoinCondition.JoinType.FULL, var1, var2, var3, var4, var5, (JoinCondition)null));
      return this;
   }

   public List<ColumnDefinition> getColumns() {
      return this.columns;
   }

   public List<String> getPrimaryKeys() {
      return this.primaryKeys;
   }

   public List<String> getForeignKeys() {
      return this.foreignKeys;
   }

   public List<JoinCondition> getJoinConditions() {
      return this.joinConditions;
   }

   public void orderBy(String var1) {
      this.orderBy = String.format("ORDER BY %s", var1);
   }

   public void orderByDesc(String var1) {
      this.orderBy = String.format("ORDER BY %s DESC", var1);
   }

   public String getOrderBy() {
      return this.orderBy;
   }

   public void distinct() {
      this.isDistinct = true;
   }

   public boolean isDistinct() {
      return this.isDistinct;
   }

   public int execute(DatabaseConnection var1, Logger var2) {
      Object var3;
      switch (this.schemaType) {
         case RENAME:
            var3 = new RenameExecutor(this);
            break;
         case MODIFY:
            var3 = new ModifyRequest(this);
            break;
         case CREATE:
            var3 = new CreateRequest(this);
            break;
         case DROP:
            var3 = new DropTableRequest(this);
            break;
         case ALTER:
            var3 = new AlterRequest(this);
            break;
         case UPSERT:
            var3 = new UpsertRequest(this);
            break;
         case UPDATE:
            var3 = new UpdateRequest(this);
            break;
         case INSERT:
            var3 = new InsertRequest(this);
            break;
         case DELETE:
            var3 = new DeleteRequest(this);
            break;
         case CREATE_INDEX:
            var3 = new CreateIndexRequest(this);
            break;
         case SELECT:
         case SELECT_COUNT:
            throw new IllegalArgumentException("Wrong method !");
         default:
            throw new Error("Schema type not found !");
      }

      return ((Executor)var3).execute(var1, var1.getDatabaseConfiguration(), var2);
   }

   public void addSelect(String var1) {
      this.selectColumns.add(new SelectCondition((String)null, var1, (String)null, false, (Object)null));
   }

   public void addSelect(String var1, String var2) {
      this.selectColumns.add(new SelectCondition(var1, var2, (String)null, false, (Object)null));
   }

   public void addSelect(String var1, String var2, String var3) {
      this.selectColumns.add(new SelectCondition((String)null, var2, var3, false, (Object)null));
   }

   public void addSelect(String var1, String var2, String var3, Object var4) {
      this.selectColumns.add(new SelectCondition((String)null, var2, var3, true, var4));
   }

   public SchemaType getSchemaType() {
      return this.schemaType;
   }

   public List<WhereCondition> getWhereConditions() {
      return this.whereConditions;
   }

   public List<SelectCondition> getSelectColumns() {
      return this.selectColumns;
   }

   public String getNewTableName() {
      return this.newTableName;
   }
}
