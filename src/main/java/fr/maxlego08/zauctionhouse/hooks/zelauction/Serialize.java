package fr.maxlego08.zauctionhouse.hooks.zelauction;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Map;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

public class Serialize {
   public static String serialize(ItemStack var0, AuctionPlugin var1, boolean var2) {
      if (var0 == null) {
         return "";
      } else if (var2) {
         try {
            Map var13 = var0.serialize();
            YamlConfiguration var14 = new YamlConfiguration();

            for(Map.Entry var6 : var13.entrySet()) {
               var14.set((String)var6.getKey(), var6.getValue());
            }

            return Base64.getEncoder().encodeToString(var14.saveToString().getBytes());
         } catch (Exception var12) {
            var1.getLogger().info("Failed to serialize ItemStack with modern method: " + var12.getMessage());
            return null;
         }
      } else {
         try {
            ByteArrayOutputStream var3 = new ByteArrayOutputStream();

            try {
               BukkitObjectOutputStream var4 = new BukkitObjectOutputStream(var3);

               try {
                  var4.writeObject(var0);
                  var4.flush();
                  String var5 = Base64.getEncoder().encodeToString(var3.toByteArray());
                  var4.close();
                  var3.close();
                  return var5;
               } catch (Throwable var9) {
                  try {
                     var4.close();
                  } catch (Throwable var8) {
                     var9.addSuppressed(var8);
                  }

                  throw var9;
               }
            } catch (Throwable var10) {
               try {
                  var3.close();
               } catch (Throwable var7) {
                  var10.addSuppressed(var7);
               }

               throw var10;
            }
         } catch (Exception var11) {
            var1.getLogger().info("Failed to serialize ItemStack with legacy method: " + var11.getMessage());
            return null;
         }
      }
   }

   public static ItemStack deserialize(String var0, AuctionPlugin var1, boolean var2) {
      if (var0 != null && !var0.isBlank()) {
         return var2 ? deserializeLegacy(var0, var1) : deserializeModern(var0, var1);
      } else {
         return null;
      }
   }

   public static ItemStack deserializeLegacy(String var0, AuctionPlugin var1) {
      if (var0 != null && !var0.isBlank()) {
         try {
            ByteArrayInputStream var2 = new ByteArrayInputStream(Base64.getDecoder().decode(var0));

            try {
               BukkitObjectInputStream var3 = new BukkitObjectInputStream(var2);

               try {
                  ItemStack var4 = (ItemStack)var3.readObject();
                  var3.close();
                  var2.close();
                  return var4;
               } catch (Throwable var8) {
                  try {
                     var3.close();
                  } catch (Throwable var7) {
                     var8.addSuppressed(var7);
                  }

                  throw var8;
               }
            } catch (Throwable var9) {
               try {
                  var2.close();
               } catch (Throwable var6) {
                  var9.addSuppressed(var6);
               }

               throw var9;
            }
         } catch (Exception var10) {
            var1.getLogger().info("Failed to deserialize ItemStack with legacy method");
            Logger var10000 = var1.getLogger();
            String var10001 = var10.getClass().getSimpleName();
            var10000.info("Error: " + var10001 + " - " + var10.getMessage());
            var1.getLogger().info("Data length: " + var0.length());
            var1.getLogger().info("Data preview (first 100 chars): " + var0.substring(0, Math.min(100, var0.length())));
            var1.getLogger().info("Server version: " + Bukkit.getVersion());
            var1.getLogger().info("Bukkit version: " + Bukkit.getBukkitVersion());
            var1.getLogger().info("Full stack trace:");
            var10.printStackTrace();
            return null;
         }
      } else {
         return null;
      }
   }

   public static ItemStack deserializeModern(String var0, AuctionPlugin var1) {
      if (var0 != null && !var0.isBlank() && !var0.isEmpty()) {
         try {
            String var2;
            try {
               byte[] var3 = Base64.getDecoder().decode(var0);
               var2 = new String(var3);
            } catch (IllegalArgumentException var5) {
               var2 = var0;
            }

            YamlConfiguration var7 = new YamlConfiguration();
            var7.loadFromString(var2);
            Map var4 = var7.getValues(false);
            return ItemStack.deserialize(var4);
         } catch (Exception var6) {
            var1.getLogger().info("Failed to deserialize ItemStack with modern method");
            Logger var10000 = var1.getLogger();
            String var10001 = var6.getClass().getSimpleName();
            var10000.info("Error: " + var10001 + " - " + var6.getMessage());
            var1.getLogger().info("Data length: " + var0.length());
            if (var6.getMessage() != null && var6.getMessage().contains("Material")) {
               var1.getLogger().info("This appears to be a material-related error. The item may be from a different Minecraft version.");
            }

            return null;
         }
      } else {
         return null;
      }
   }

   public static String serializeLegacy(ItemStack var0, AuctionPlugin var1) {
      if (var0 == null) {
         return "";
      } else {
         try {
            ByteArrayOutputStream var2 = new ByteArrayOutputStream();

            try {
               BukkitObjectOutputStream var3 = new BukkitObjectOutputStream(var2);

               try {
                  var3.writeObject(var0);
                  var3.flush();
                  String var4 = Base64.getEncoder().encodeToString(var2.toByteArray());
                  var3.close();
                  var2.close();
                  return var4;
               } catch (Throwable var8) {
                  try {
                     var3.close();
                  } catch (Throwable var7) {
                     var8.addSuppressed(var7);
                  }

                  throw var8;
               }
            } catch (Throwable var9) {
               try {
                  var2.close();
               } catch (Throwable var6) {
                  var9.addSuppressed(var6);
               }

               throw var9;
            }
         } catch (Exception var10) {
            var1.getLogger().info("Failed to serialize ItemStack with legacy method: " + var10.getMessage());
            return null;
         }
      }
   }

   public static String serializeModern(ItemStack var0, AuctionPlugin var1) {
      if (var0 == null) {
         return "";
      } else {
         try {
            Map var2 = var0.serialize();
            YamlConfiguration var3 = new YamlConfiguration();

            for(Map.Entry var5 : var2.entrySet()) {
               var3.set((String)var5.getKey(), var5.getValue());
            }

            return Base64.getEncoder().encodeToString(var3.saveToString().getBytes());
         } catch (Exception var6) {
            var1.getLogger().info("Failed to serialize ItemStack with modern method: " + var6.getMessage());
            return null;
         }
      }
   }
}
