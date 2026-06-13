package fr.maxlego08.zauctionhouse.services;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.category.Category;
import fr.maxlego08.zauctionhouse.api.filter.SearchField;
import fr.maxlego08.zauctionhouse.api.filter.SearchFilterType;
import fr.maxlego08.zauctionhouse.api.filter.SearchQuery;
import fr.maxlego08.zauctionhouse.api.item.Item;
import fr.maxlego08.zauctionhouse.api.item.SortItem;
import fr.maxlego08.zauctionhouse.api.item.StorageType;
import fr.maxlego08.zauctionhouse.api.item.items.AuctionItem;
import fr.maxlego08.zauctionhouse.api.utils.IntArrayList;
import fr.maxlego08.zauctionhouse.api.utils.IntList;
import fr.maxlego08.zauctionhouse.utils.cache.SortedItemsCache;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SearchService {
   private final AuctionPlugin plugin;

   public SearchService(AuctionPlugin var1) {
      this.plugin = var1;
   }

   public IntList search(SortedItemsCache var1, String var2, SortItem var3, Category var4) {
      SearchQuery var5 = SearchQuery.parse(var2, this.plugin.getConfiguration().getSearchFilter());
      if (var5.value().isEmpty()) {
         return var1.getSortedIds(var4, var3);
      } else {
         IntList var6 = var1.getSortedIds(var4, var3);
         IntArrayList var7 = new IntArrayList();
         List<? extends fr.maxlego08.zauctionhouse.api.item.Item> var8 = this.plugin.getAuctionManager().getItems(StorageType.LISTED);
         HashMap<Integer, fr.maxlego08.zauctionhouse.api.item.Item> var9 = new HashMap<>(var8.size());

         for(Item var11 : var8) {
            var9.put(var11.getId(), var11);
         }

         String var14 = var5.value().toLowerCase();

         for(int var12 : var6) {
            Item var13 = var9.get(var12);
            if (var13 != null) {
               if (var5.isDefault()) {
                  if (this.matchDefault(var13, var14)) {
                     var7.add(var12);
                  }
               } else if (this.matchWithFilter(var13, var5.field(), var5.type(), var5.value())) {
                  var7.add(var12);
               }
            }
         }

         return var7;
      }
   }

   private boolean matchDefault(Item var1, String var2) {
      if (var1.getTranslationKey() != null && var1.getTranslationKey().toLowerCase().contains(var2)) {
         return true;
      } else {
         if (var1 instanceof AuctionItem) {
            AuctionItem var3 = (AuctionItem)var1;
            ItemStack var4 = var3.getItemStack();
            if (var4 != null && var4.getType().name().toLowerCase().contains(var2)) {
               return true;
            }
         }

         String var7 = this.getDisplayName(var1);
         if (var7 != null && var7.toLowerCase().contains(var2)) {
            return true;
         } else {
            List<String> var8 = this.getLore(var1);
            if (var8 != null) {
               for(String var6 : var8) {
                  if (var6.toLowerCase().contains(var2)) {
                     return true;
                  }
               }
            }

            return var1.getSellerName() != null && var1.getSellerName().equalsIgnoreCase(var2);
         }
      }
   }

   private boolean matchWithFilter(Item var1, SearchField var2, SearchFilterType var3, String var4) {
      String var5 = this.getFieldValue(var1, var2);
      if (var5 == null) {
         if (var2 == SearchField.LORE) {
            List<String> var6 = this.getLore(var1);
            if (var6 == null) {
               return false;
            } else {
               for(String var8 : var6) {
                  if (this.matchValue(var8, var3, var4)) {
                     return true;
                  }
               }

               return false;
            }
         } else {
            return false;
         }
      } else {
         return this.matchValue(var5, var3, var4);
      }
   }

   private boolean matchValue(String var1, SearchFilterType var2, String var3) {
      boolean var10000;
      switch (var2) {
         case CONTAINS -> var10000 = var1.contains(var3);
         case EQUALS -> var10000 = var1.equals(var3);
         case CONTAINS_IGNORE_CASE -> var10000 = var1.toLowerCase().contains(var3.toLowerCase());
         case EQUALS_IGNORE_CASE -> var10000 = var1.equalsIgnoreCase(var3);
         default -> throw new MatchException((String)null, (Throwable)null);
      }

      return var10000;
   }

   private String getFieldValue(Item var1, SearchField var2) {
      String var10000;
      switch (var2) {
         case NAME:
            var10000 = this.getDisplayName(var1);
            break;
         case MATERIAL:
            if (var1 instanceof AuctionItem var3) {
               ItemStack var4 = var3.getItemStack();
               var10000 = var4 != null ? var4.getType().name() : null;
            } else {
               var10000 = null;
            }
            break;
         case SELLER:
            var10000 = var1.getSellerName();
            break;
         case LORE:
            var10000 = null;
            break;
         default:
            throw new MatchException((String)null, (Throwable)null);
      }

      return var10000;
   }

   private String getDisplayName(Item var1) {
      if (var1 instanceof AuctionItem var2) {
         ItemStack var3 = var2.getItemStack();
         if (var3 != null && var3.hasItemMeta()) {
            ItemMeta var4 = var3.getItemMeta();
            Component var5 = var4.displayName();
            if (var5 != null) {
               return PlainTextComponentSerializer.plainText().serialize(var5);
            }
         }
      }

      return null;
   }

   private List<String> getLore(Item var1) {
      if (var1 instanceof AuctionItem var2) {
         ItemStack var3 = var2.getItemStack();
         if (var3 != null && var3.hasItemMeta()) {
            ItemMeta var4 = var3.getItemMeta();
            List<net.kyori.adventure.text.Component> var5 = var4.lore();
            if (var5 != null) {
               Stream<net.kyori.adventure.text.Component> var10000 = var5.stream();
               PlainTextComponentSerializer var10001 = PlainTextComponentSerializer.plainText();
               Objects.requireNonNull(var10001);
               return var10000.map(var10001::serialize).toList();
            }
         }
      }

      return null;
   }
}
