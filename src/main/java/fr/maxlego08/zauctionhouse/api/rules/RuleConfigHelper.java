package fr.maxlego08.zauctionhouse.api.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class RuleConfigHelper {
   private RuleConfigHelper() {
   }

   public static String getString(Map<?, ?> var0, String var1) {
      Object var2 = var0.get(var1);
      return var2 != null ? String.valueOf(var2) : null;
   }

   public static String getString(Map<?, ?> var0, String var1, String var2) {
      String var3 = getString(var0, var1);
      return var3 != null ? var3 : var2;
   }

   public static boolean getBoolean(Map<?, ?> var0, String var1, boolean var2) {
      Object var3 = var0.get(var1);
      if (var3 instanceof Boolean var4) {
         return var4;
      } else {
         return var3 != null ? Boolean.parseBoolean(String.valueOf(var3)) : var2;
      }
   }

   public static int getInt(Map<?, ?> var0, String var1, int var2) {
      Object var3 = var0.get(var1);
      if (var3 instanceof Number var4) {
         return var4.intValue();
      } else {
         if (var3 != null) {
            try {
               return Integer.parseInt(String.valueOf(var3));
            } catch (NumberFormatException var5) {
            }
         }

         return var2;
      }
   }

   public static List<String> getStringList(Map<?, ?> var0, String var1) {
      Object var2 = var0.get(var1);
      if (var2 instanceof List var3) {
         ArrayList var4 = new ArrayList();

         for(Object var6 : var3) {
            if (var6 != null) {
               var4.add(String.valueOf(var6));
            }
         }

         return var4;
      } else {
         return List.of();
      }
   }

   public static List<Integer> getIntegerList(Map<?, ?> var0, String var1) {
      Object var2 = var0.get(var1);
      if (var2 instanceof List var3) {
         ArrayList var4 = new ArrayList();

         for(Object var6 : var3) {
            if (var6 instanceof Number var7) {
               var4.add(var7.intValue());
            } else if (var6 != null) {
               try {
                  var4.add(Integer.parseInt(String.valueOf(var6)));
               } catch (NumberFormatException var9) {
               }
            }
         }

         return var4;
      } else {
         return List.of();
      }
   }

   public static List<Map<?, ?>> getMapList(Map<?, ?> var0, String var1) {
      Object var2 = var0.get(var1);
      if (var2 instanceof List var3) {
         ArrayList var4 = new ArrayList();

         for(Object var6 : var3) {
            if (var6 instanceof Map var7) {
               var4.add(var7);
            }
         }

         return var4;
      } else {
         return List.of();
      }
   }
}
