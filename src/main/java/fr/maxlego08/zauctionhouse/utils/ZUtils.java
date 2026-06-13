package fr.maxlego08.zauctionhouse.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.permissions.Permissible;

public abstract class ZUtils extends MessageUtils {
   protected Object createInstanceFromMap(Logger var1, Constructor<?> var2, Map<?, ?> var3) {
      try {
         Object[] var4 = new Object[var2.getParameterCount()];
         Parameter[] var5 = var2.getParameters();

         for(int var6 = 0; var6 < var5.length; ++var6) {
            Class var7 = var5[var6].getType();
            String var8 = var5[var6].getName();
            String var9 = var8.replaceAll("([A-Z])", "-$1").toLowerCase();
            Object var10 = var3.containsKey(var8) ? var3.get(var8) : var3.get(var9);
            if (var10 == null && Number.class.isAssignableFrom(var7)) {
               var10 = 0;
            }

            if (var10 == null && Boolean.class.isAssignableFrom(var7)) {
               var10 = false;
            }

            if (var10 != null) {
               try {
                  if (!var7.isArray()) {
                     var10 = this.convertToRequiredType(var1, var10, var7);
                  } else {
                     Class var11 = var7.getComponentType();
                     List var12 = (List)var10;
                     Object var13 = Array.newInstance(var11, var12.size());

                     for(int var14 = 0; var14 < var12.size(); ++var14) {
                        Object var15 = var12.get(var14);
                        var15 = this.convertToRequiredType(var1, var15, var11);
                        Array.set(var13, var14, var15);
                     }

                     var10 = var13;
                  }
               } catch (Exception var16) {
                  var1.log(Level.SEVERE, String.format("Error converting value '%s' for parameter '%s' to type '%s'", var10, var8, var7.getName()), var16);
               }
            }

            var4[var6] = var10;
         }

         return var2.newInstance(var4);
      } catch (Exception var17) {
         var1.log(Level.SEVERE, String.format("Failed to create instance from map with constructor %s", var2), var17);
         var1.log(Level.SEVERE, String.format("Constructor parameters: %s", var2.getParameters()));
         var1.log(Level.SEVERE, String.format("Map content: %s", var3));
         throw new RuntimeException("Failed to create instance from map with constructor " + String.valueOf(var2), var17);
      }
   }

   protected Object convertToRequiredType(Logger var1, Object var2, Class<?> var3) {
      if (var2 == null) {
         if (var3 != Integer.class && var3 != Integer.TYPE) {
            if (var3 != Double.class && var3 != Double.TYPE) {
               if (var3 != Long.class && var3 != Long.TYPE) {
                  if (var3 != Float.class && var3 != Float.TYPE) {
                     return var3 != Boolean.class && var3 != Boolean.TYPE ? null : false;
                  } else {
                     return 0.0F;
                  }
               } else {
                  return 0L;
               }
            } else {
               return (double)0.0F;
            }
         } else {
            return 0;
         }
      } else {
         if (var3.isEnum()) {
            try {
               return Enum.valueOf((Class<Enum>) var3, (String)var2);
            } catch (IllegalArgumentException var8) {
               var1.log(Level.SEVERE, String.format("Failed to convert '%s' to enum type '%s'", var2, var3.getName()), var8);
            }
         } else if (var3 == BigDecimal.class) {
            try {
               return new BigDecimal(var2.toString());
            } catch (NumberFormatException var7) {
               var1.log(Level.SEVERE, String.format("Failed to convert '%s' to BigDecimal", var2), var7);
            }
         } else if (var3 == UUID.class) {
            try {
               return UUID.fromString((String)var2);
            } catch (IllegalArgumentException var6) {
               var1.log(Level.SEVERE, String.format("Failed to convert '%s' to UUID", var2), var6);
            }
         } else {
            if (var3 == Integer.class || var3 == Integer.TYPE) {
               try {
                  return Integer.parseInt(var2.toString());
               } catch (NumberFormatException var5) {
                  var1.log(Level.SEVERE, String.format("Failed to convert '%s' to Integer", var2), var5);
                  throw var5;
               }
            }

            if (var3 != Double.class && var3 != Double.TYPE) {
               if (var3 != Long.class && var3 != Long.TYPE) {
                  if (var3 != Boolean.class && var3 != Boolean.TYPE) {
                     if (var3 == Float.class || var3 == Float.TYPE) {
                        try {
                           return Float.parseFloat(var2.toString());
                        } catch (NumberFormatException var9) {
                           var1.log(Level.SEVERE, String.format("Failed to convert '%s' to Float", var2), var9);
                        }
                     }
                  } else {
                     try {
                        return Boolean.parseBoolean(var2.toString());
                     } catch (Exception var10) {
                        var1.log(Level.SEVERE, String.format("Failed to convert '%s' to Boolean", var2), var10);
                     }
                  }
               } else {
                  try {
                     return Long.parseLong(var2.toString());
                  } catch (NumberFormatException var11) {
                     var1.log(Level.SEVERE, String.format("Failed to convert '%s' to Long", var2), var11);
                  }
               }
            } else {
               try {
                  return Double.parseDouble(var2.toString());
               } catch (NumberFormatException var12) {
                  var1.log(Level.SEVERE, String.format("Failed to convert '%s' to Double", var2), var12);
               }
            }
         }

         return var2;
      }
   }

   protected boolean hasPermission(Permissible var1, String var2) {
      return var1.hasPermission(var2);
   }

   protected void files(File var1, Consumer<File> var2) {
      try {
         Stream<Path> var3 = Files.walk(Paths.get(var1.getPath()));

         try {
            var3.skip(1L).map(Path::toFile).filter(File::isFile).filter((var0) -> var0.getName().endsWith(".yml")).forEach(var2);
         } catch (Throwable var7) {
            if (var3 != null) {
               try {
                  var3.close();
               } catch (Throwable var6) {
                  var7.addSuppressed(var6);
               }
            }

            throw var7;
         }

         if (var3 != null) {
            var3.close();
         }
      } catch (IOException var8) {
         Logger.getLogger(ZUtils.class.getName()).log(Level.SEVERE, "Failed to walk folder " + var1.getPath(), var8);
      }

   }

   protected void removeItemInHand(Player var1, int var2) {
      PlayerInventory var3 = var1.getInventory();
      if (var3.getItemInMainHand().getAmount() > var2) {
         var3.getItemInMainHand().setAmount(var3.getItemInMainHand().getAmount() - var2);
      } else {
         var3.setItemInMainHand(new ItemStack(Material.AIR));
      }

      var1.updateInventory();
   }
}
