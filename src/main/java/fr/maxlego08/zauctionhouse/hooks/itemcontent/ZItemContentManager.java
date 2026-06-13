package fr.maxlego08.zauctionhouse.hooks.itemcontent;

import fr.maxlego08.zauctionhouse.api.hooks.itemcontent.ItemContentManager;
import fr.maxlego08.zauctionhouse.api.hooks.itemcontent.ItemContentProvider;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings({"unchecked", "rawtypes"})
public class ZItemContentManager implements ItemContentManager {
   private final List<ItemContentProvider> providers = new ArrayList();

   public void registerProvider(ItemContentProvider var1) {
      this.providers.add(var1);
      this.providers.sort(Comparator.comparingInt(ItemContentProvider::getPriority));
   }

   public void unregisterProvider(String var1) {
      this.providers.removeIf((var1x) -> var1x.getName().equalsIgnoreCase(var1));
   }

   public boolean isContainer(ItemStack var1) {
      if (var1 == null) {
         return false;
      } else {
         for(ItemContentProvider var3 : this.providers) {
            if (var3.isContainer(var1)) {
               return true;
            }
         }

         return false;
      }
   }

   public List<ItemStack> getContents(ItemStack var1) {
      if (var1 == null) {
         return List.of();
      } else {
         for(ItemContentProvider var3 : this.providers) {
            if (var3.isContainer(var1)) {
               return var3.getContents(var1);
            }
         }

         return List.of();
      }
   }

   public List<ItemStack> getContainers(List<ItemStack> var1) {
      return var1 == null ? List.of() : var1.stream().filter(this::isContainer).toList();
   }

   public boolean containsContainer(List<ItemStack> var1) {
      return var1 == null ? false : var1.stream().anyMatch(this::isContainer);
   }
}
