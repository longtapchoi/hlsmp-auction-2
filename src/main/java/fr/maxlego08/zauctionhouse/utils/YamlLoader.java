package fr.maxlego08.zauctionhouse.utils;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.configuration.Loadable;
import fr.maxlego08.zauctionhouse.api.configuration.NonLoadable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public abstract class YamlLoader extends ZUtils {
   protected void loadYamlConfirmation(AuctionPlugin var1, FileConfiguration var2) {
      try { for(Field var6 : this.getClass().getDeclaredFields()) { } catch (Exception e) { throw new RuntimeException(e); }
         if (!var6.isAnnotationPresent(NonLoadable.class)) {
            var6.setAccessible(true);

            try {
               String var7 = var6.getName().replaceAll("([A-Z])", "-$1").toLowerCase();
               if (!var6.getType().equals(Boolean.TYPE) && !var6.getType().equals(Boolean.class)) {
                  if (!var6.getType().equals(Integer.TYPE) && !var6.getType().equals(Integer.class)) {
                     if (!var6.getType().equals(Long.TYPE) && !var6.getType().equals(Long.class)) {
                        if (var6.getType().equals(String.class)) {
                           var6.set(this, var2.getString(var7));
                        } else if (!var6.getType().equals(Double.TYPE) && !var6.getType().equals(Double.class)) {
                           if (!var6.getType().equals(Float.TYPE) && !var6.getType().equals(Float.class)) {
                              if (var6.getType().equals(BigDecimal.class)) {
                                 var6.set(this, new BigDecimal(var2.getString(var7, "0")));
                              } else if (var6.getType().isEnum()) {
                                 Class var13 = var6.getType();
                                 var6.set(this, Enum.valueOf(var13, var2.getString(var7, "").toUpperCase()));
                              } else if (var6.getType().equals(List.class)) {
                                 Type var12 = var6.getGenericType();
                                 if (var12 instanceof ParameterizedType) {
                                    ParameterizedType var14 = (ParameterizedType)var12;
                                    Class var10 = (Class)var14.getActualTypeArguments()[0];
                                    if (Loadable.class.isAssignableFrom(var10)) {
                                       var6.set(this, this.loadObjects(var1.getLogger(), var10, var2.getMapList(var7)));
                                       continue;
                                    }

                                    if (NonLoadable.class.isAssignableFrom(var10)) {
                                       continue;
                                    }
                                 }

                                 var6.set(this, var2.getStringList(var7));
                              } else {
                                 ConfigurationSection var8 = var2.getConfigurationSection(var7);
                                 if (var8 != null) {
                                    HashMap var9 = new HashMap();
                                    var8.getKeys(false).forEach((var2x) -> var9.put(var2x, var8.get(var2x)));
                                    var6.set(this, this.createInstanceFromMap(var1.getLogger(), ((Class)var6.getGenericType()).getConstructors()[0], var9));
                                 }
                              }
                           } else {
                              var6.set(this, (float)var2.getDouble(var7));
                           }
                        } else {
                           var6.set(this, var2.getDouble(var7));
                        }
                     } else {
                        var6.setLong(this, var2.getLong(var7));
                     }
                  } else {
                     var6.setInt(this, var2.getInt(var7));
                  }
               } else {
                  var6.setBoolean(this, var2.getBoolean(var7));
               }
            } catch (Exception var11) {
               Logger var10000 = var1.getLogger();
               String var10001 = var6.getName();
               var10000.severe("An error with loading field " + var10001 + ": " + var11.getMessage());
            }
         }
      }

   }

   private List<Object> loadObjects(Logger var1, Class<?> var2, List<Map<?, ?>> var3) {
      Constructor var4 = var2.getConstructors()[0];
      return (List)var3.stream().map((var3x) -> this.createInstanceFromMap(var1, var4, var3x)).collect(Collectors.toList());
   }
}
