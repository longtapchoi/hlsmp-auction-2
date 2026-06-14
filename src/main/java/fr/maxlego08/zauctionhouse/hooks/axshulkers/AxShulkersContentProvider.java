package fr.maxlego08.zauctionhouse.hooks.axshulkers;

import com.artillexstudios.axshulkers.AxShulkers;
import com.artillexstudios.axshulkers.cache.Shulkerbox;
import com.artillexstudios.axshulkers.cache.Shulkerboxes;
import com.artillexstudios.axshulkers.utils.ShulkerUtils;
import fr.maxlego08.zauctionhouse.api.hooks.itemcontent.ItemContentProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.inventory.ItemStack;

public class AxShulkersContentProvider implements ItemContentProvider {
   public String getName() {
      return "AxShulkers";
   }

   public int getPriority() {
      return 50;
   }

   public boolean isContainer(ItemStack var1) {
      return ShulkerUtils.isShulker(var1) && ShulkerUtils.getShulkerUUID(var1) != null;
   }

   public List<ItemStack> getContents(ItemStack var1) {
      if (!ShulkerUtils.isShulker(var1)) {
         return List.of();
      } else {
         UUID var2 = ShulkerUtils.getShulkerUUID(var1);
         if (var2 == null) {
            return List.of();
         } else {
            Shulkerbox var4 = (Shulkerbox)Shulkerboxes.getShulkerMap().get(var2);
            ItemStack[] var3;
            if (var4 != null) {
               var3 = var4.getShulkerInventory().getStorageContents();
            } else {
               var3 = AxShulkers.getDB().getShulker(var2);
            }

            if (var3 == null) {
               return List.of();
            } else {
               ArrayList var5 = new ArrayList();

               for(ItemStack var9 : var3) {
                  if (var9 != null && !var9.getType().isAir()) {
                     var5.add(var9.clone());
                  }
               }

               return var5;
            }
         }
      }
   }
}
