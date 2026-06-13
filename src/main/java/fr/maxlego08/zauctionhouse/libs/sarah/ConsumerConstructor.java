package fr.maxlego08.zauctionhouse.libs.sarah;

import fr.maxlego08.zauctionhouse.libs.sarah.database.Schema;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;
import java.util.function.Consumer;

public class ConsumerConstructor {
   public static Consumer<Schema> createConsumerFromTemplate(Class<?> var0, Object var1) {
      Constructor[] var2 = var0.getDeclaredConstructors();
      Constructor var3 = var2[0];
      var3.setAccessible(true);
      try {
      Field[] var4;
      try {
         var4 = var0.getDeclaredFields();
      } catch (NoSuchFieldException | IllegalAccessException var_ex) {
         throw new RuntimeException(var_ex);
      }
      Field[] var5 = (Field[])Arrays.stream(var4).filter((var0x) -> !var0x.isSynthetic()).toArray((var0x) -> new Field[var0x]);
      long var6 = Arrays.stream(var3.getParameters()).filter((var0x) -> !var0x.isSynthetic()).count();
      if ((long)var5.length != var6) {
         throw new IllegalArgumentException("Fields count does not match constructor parameters count");
      } else {
         return (var2x) -> {
            boolean var3L = false;

            for(int var4L = 0; var4L < var5.length; ++var4L) {
               Field var5x = var5[var4L];
               var5x.setAccessible(true);
               Class<?> var6L = var5x.getType();
               String var7 = var5x.getName();
               String var8 = var6L.getTypeName().substring(var6L.getTypeName().lastIndexOf(46) + 1);
               Column var9 = null;
               if (var5x.isAnnotationPresent(Column.class)) {
                  var9 = (Column)var5x.getAnnotation(Column.class);
               }

               if (var9 != null && !var9.type().isEmpty()) {
                  var8 = var9.type();
               }

               if (var9 != null && !var9.value().isEmpty()) {
                  var7 = var9.value();
               }

               if (var9 != null && var9.autoIncrement()) {
                  if (!var6L.equals(Long.TYPE) && !var6L.equals(Long.class)) {
                     if (!var6L.equals(Integer.TYPE) && !var6L.equals(Integer.class)) {
                        throw new IllegalArgumentException("Auto increment is only supported for long and int types");
                     }

                     var2x.autoIncrement(var9.value());
                     var3L = true;
                  } else {
                     var2x.autoIncrementBigInt(var9.value());
                     var3L = true;
                  }
               } else {
                  try {
                     Class<?> var10 = var6L;
                     if (isEnumType(var10)) {
                        boolean var11 = var9 != null && var9.useNativeEnum();
                        handleEnumType(var2x, var7, var10, var1 == null ? null : var5x.get(var1), var11);
                     } else {
                        schemaFromType(var2x, var8, var7, var1 == null ? null : var5x.get(var1));
                     }
                  } catch (IllegalAccessException var12) {
                     throw new RuntimeException(var12);
                  }
               }

               if (var9 != null) {
                  if (var9.primary() && var9.autoIncrement()) {
                     throw new IllegalArgumentException("A column cannot be both primary and auto increment");
                  }

                  if (var9.primary()) {
                     var3L = true;
                     var2x.primary();
                  }

                  if (var9.foreignKey()) {
                     if (var9.foreignKeyReference().isEmpty()) {
                        throw new IllegalArgumentException("Foreign key reference is empty");
                     }

                     var2x.foreignKey(var9.foreignKeyReference());
                  }

                  if (var9.nullable()) {
                     var2x.nullable();
                  }

                  if (var9.unique()) {
                     var2x.unique();
                  }
               }

               if (var4L == 0 && !var3L) {
                  var3L = true;
                  var2x.primary();
               }
            }

         };
      }
   }

   private static void schemaFromType(Schema var0, String var1, String var2, Object var3) {
      switch (var1.toLowerCase()) {
         case "string":
            if (var3 == null) {
               var0.string(var2, 255);
            } else {
               var0.string(var2, var3.toString());
            }
            break;
         case "longtext":
            var0.longText(var2);
            break;
         case "integer":
         case "int":
         case "long":
         case "bigint":
            if (var3 == null) {
               var0.bigInt(var2);
            } else {
               var0.bigInt(var2, Long.parseLong(var3.toString()));
            }
            break;
         case "boolean":
            if (var3 == null) {
               var0.bool(var2);
            } else {
               var0.bool(var2, (Boolean)var3);
            }
            break;
         case "double":
         case "float":
         case "bigdecimal":
            if (var3 == null) {
               var0.decimal(var2);
            } else {
               var0.decimal(var2, (Double)var3);
            }
            break;
         case "uuid":
            if (var3 == null) {
               var0.uuid(var2);
            } else {
               var0.uuid(var2, (UUID)var3);
            }
            break;
         case "date":
            var0.date(var2, (Date)var3).nullable();
            break;
         case "timestamp":
            var0.timestamp(var2).nullable();
            break;
         default:
            throw new IllegalArgumentException("Type " + var1 + " is not supported");
      }

   }

   public static boolean isEnumType(Class<?> var0) {
      return var0.isEnum();
   }

   public static void handleEnumType(Schema var0, String var1, Object var2) {
      handleEnumType(var0, var1, (Class)null, var2, false);
   }

   public static void handleEnumType(Schema var0, String var1, Class<?> var2, Object var3, boolean var4) {
      if (var4 && var2 != null && var2.isEnum()) {
         var0.enumType(var1, (Class<Enum>) var2);
      } else if (var3 == null) {
         var0.enumValue(var1);
      } else {
         var0.enumValue(var1, (Enum)var3);
      }

   }
}
