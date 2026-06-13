package fr.maxlego08.zauctionhouse.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.ShulkerBox;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

public final class ShulkerHelper {
   private static final Set<Material> SHULKER_BOX_MATERIALS;

   private ShulkerHelper() {
   }

   public static boolean isShulkerBox(Material var0) {
      return SHULKER_BOX_MATERIALS.contains(var0);
   }

   public static boolean isShulkerBox(ItemStack var0) {
      return var0 != null && isShulkerBox(var0.getType());
   }

   public static List<ItemStack> getShulkerBoxes(List<ItemStack> var0) {
      return var0 == null ? List.of() : var0.stream().filter(ShulkerHelper::isShulkerBox).toList();
   }

   public static boolean containsShulkerBox(List<ItemStack> var0) {
      return var0 == null ? false : var0.stream().anyMatch(ShulkerHelper::isShulkerBox);
   }

   public static List<ItemStack> getShulkerContent(ItemStack var0) {
      if (var0 != null && isShulkerBox(var0)) {
         ItemMeta var2 = var0.getItemMeta();
         if (var2 instanceof BlockStateMeta) {
            BlockStateMeta var1 = (BlockStateMeta)var2;
            BlockState var3 = var1.getBlockState();
            if (var3 instanceof ShulkerBox) {
               ShulkerBox var9 = (ShulkerBox)var3;
               ItemStack[] var10 = var9.getInventory().getContents();
               ArrayList var4 = new ArrayList();

               for(ItemStack var8 : var10) {
                  if (var8 != null && !var8.getType().isAir()) {
                     var4.add(var8.clone());
                  }
               }

               return var4;
            } else {
               return List.of();
            }
         } else {
            return List.of();
         }
      } else {
         return List.of();
      }
   }

   public static int getShulkerContentCount(ItemStack var0) {
      return getShulkerContent(var0).size();
   }

   public static boolean isShulkerEmpty(ItemStack var0) {
      return getShulkerContent(var0).isEmpty();
   }

   static {
      SHULKER_BOX_MATERIALS = Set.of(Material.SHULKER_BOX, Material.WHITE_SHULKER_BOX, Material.ORANGE_SHULKER_BOX, Material.MAGENTA_SHULKER_BOX, Material.LIGHT_BLUE_SHULKER_BOX, Material.YELLOW_SHULKER_BOX, Material.LIME_SHULKER_BOX, Material.PINK_SHULKER_BOX, Material.GRAY_SHULKER_BOX, Material.LIGHT_GRAY_SHULKER_BOX, Material.CYAN_SHULKER_BOX, Material.PURPLE_SHULKER_BOX, Material.BLUE_SHULKER_BOX, Material.BROWN_SHULKER_BOX, Material.GREEN_SHULKER_BOX, Material.RED_SHULKER_BOX, Material.BLACK_SHULKER_BOX);
   }
}
