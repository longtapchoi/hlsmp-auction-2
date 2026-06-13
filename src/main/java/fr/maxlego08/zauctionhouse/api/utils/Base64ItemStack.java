package fr.maxlego08.zauctionhouse.api.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

public class Base64ItemStack {
   public static String encode(ItemStack var0) {
      if (!NmsVersion.getCurrentVersion().isAttributItemStack()) {
         return ItemStackUtils.serializeItemStack(var0);
      } else {
         java.util.Base64.Encoder var1 = java.util.Base64.getEncoder();
         try {
            ByteArrayOutputStream var2 = new ByteArrayOutputStream();
            GZIPOutputStream var3 = new GZIPOutputStream(var2);
            BukkitObjectOutputStream var4 = new BukkitObjectOutputStream(var3);
            ((ObjectOutputStream)var4).writeObject(var0);
            ((ObjectOutputStream)var4).close();
            return var1.encodeToString(var2.toByteArray());
         } catch (IOException var5) {
            var5.printStackTrace();
            return null;
         }
      }
   }

   public static ItemStack decode(String var0) {
      if (!NmsVersion.getCurrentVersion().isAttributItemStack()) {
         return ItemStackUtils.safeDeserializeItemStack(var0);
      } else {
         java.util.Base64.Decoder var1 = java.util.Base64.getDecoder();
         try {
            byte[] var2 = var1.decode(var0);
            ByteArrayInputStream var3 = new ByteArrayInputStream(var2);
            GZIPInputStream var4 = new GZIPInputStream(var3);
            BukkitObjectInputStream var5 = new BukkitObjectInputStream(var4);
            ItemStack var6 = (ItemStack)((ObjectInputStream)var5).readObject();
            ((ObjectInputStream)var5).close();
            return var6;
         } catch (ClassNotFoundException | IOException var7) {
            ((Exception)var7).printStackTrace();
            return null;
         }
      }
   }
}
