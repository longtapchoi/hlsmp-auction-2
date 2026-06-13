package fr.maxlego08.zauctionhouse.api.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.util.zip.GZIPInputStream;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

public class ItemStackUtils {
   private static final NmsVersion NMS_VERSION;

   public static String serializeItemStack(ItemStack var0) {
      if (var0 == null) {
         return "null";
      } else {
         ByteArrayOutputStream var1 = null;

         try {
            Class var2 = ItemStackUtils.EnumReflectionItemStack.NBTTAGCOMPOUND.getClassz();
            Constructor var3 = var2.getConstructor();
            Object var4 = var3.newInstance();
            Object var5 = ItemStackUtils.EnumReflectionItemStack.CRAFTITEMSTACK.getClassz().getMethod("asNMSCopy", ItemStack.class).invoke((Object)null, var0);
            ItemStackUtils.EnumReflectionItemStack.ITEMSTACK.getClassz().getMethod("b", var2).invoke(var5, var4);
            var1 = new ByteArrayOutputStream();
            ItemStackUtils.EnumReflectionItemStack.NBTCOMPRESSEDSTREAMTOOLS.getClassz().getMethod("a", var2, OutputStream.class).invoke((Object)null, var4, var1);
         } catch (Exception var6) {
            var6.printStackTrace();
         }

         return Base64.encode(var1.toByteArray());
      }
   }

   public static ItemStack safeDeserializeItemStack(String var0) {
      try {
         return tryDeserializeItemStack(var0);
      } catch (Exception var2) {
         return Base64ItemStack.decode(var0);
      }
   }

   public static ItemStack tryDeserializeItemStack(String var0) {
      ByteArrayInputStream var1 = new ByteArrayInputStream(Base64.decode(var0));
      Class var2 = ItemStackUtils.EnumReflectionItemStack.NBTTAGCOMPOUND.getClassz();
      Class var3 = ItemStackUtils.EnumReflectionItemStack.ITEMSTACK.getClassz();
      DataInputStream var7 = new DataInputStream(new BufferedInputStream(new GZIPInputStream(var1)));
      Object var4 = ItemStackUtils.EnumReflectionItemStack.NBTCOMPRESSEDSTREAMTOOLS.getClassz().getMethod("a", DataInput.class).invoke((Object)null, var7);
      Object var6 = var3.getMethod("a", var2).invoke((Object)null, var4);
      ItemStack var5 = (ItemStack)ItemStackUtils.EnumReflectionItemStack.CRAFTITEMSTACK.getClassz().getMethod("asBukkitCopy", var3).invoke((Object)null, var6);
      return var5;
   }

   static {
      NMS_VERSION = NmsVersion.nmsVersion;
   }

   public static enum EnumReflectionItemStack {
      ITEMSTACK("ItemStack", "net.minecraft.world.item.ItemStack"),
      CRAFTITEMSTACK("inventory.CraftItemStack", true),
      NBTCOMPRESSEDSTREAMTOOLS("NBTCompressedStreamTools", "net.minecraft.nbt.NBTCompressedStreamTools"),
      NBTTAGCOMPOUND("NBTTagCompound", "net.minecraft.nbt.NBTTagCompound");

      private final String oldClassName;
      private final String newClassName;
      private final boolean isBukkit;

      private EnumReflectionItemStack(String var3, String var4, boolean var5) {
         this.oldClassName = var3;
         this.newClassName = var4;
         this.isBukkit = var5;
      }

      private EnumReflectionItemStack(String var3, String var4) {
         this(var3, var4, false);
      }

      private EnumReflectionItemStack(String var3, boolean var4) {
         this(var3, (String)null, var4);
      }

      public Class<?> getClassz() {
         String var1 = Bukkit.getServer().getClass().getPackage().getName();
         String var2 = var1.replace(".", ",").split(",")[3];
         String var3 = ItemStackUtils.NMS_VERSION.isNewNMSVersion() ? (this.isBukkit ? "org.bukkit.craftbukkit." + var2 + "." + this.oldClassName : this.newClassName) : (this.isBukkit ? "org.bukkit.craftbukkit." : "net.minecraft.server.") + var2 + "." + this.oldClassName;
         Class var4 = null;

         try {
            var4 = Class.forName(var3);
         } catch (ClassNotFoundException var6) {
            var6.printStackTrace();
         }

         return var4;
      }

      // $FF: synthetic method
      private static EnumReflectionItemStack[] $values() {
         return new EnumReflectionItemStack[]{ITEMSTACK, CRAFTITEMSTACK, NBTCOMPRESSEDSTREAMTOOLS, NBTTAGCOMPOUND};
      }
   }
}
