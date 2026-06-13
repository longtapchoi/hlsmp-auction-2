package fr.maxlego08.zauctionhouse.buttons.list;

import fr.maxlego08.menu.api.button.PaginateButton;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.zauctionhouse.api.AuctionManager;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.configuration.records.ItemLoreConfiguration;
import fr.maxlego08.zauctionhouse.api.item.Item;
import fr.maxlego08.zauctionhouse.api.item.ItemPlaceholder;
import fr.maxlego08.zauctionhouse.api.item.ItemStatus;
import fr.maxlego08.zauctionhouse.api.item.StorageType;
import fr.maxlego08.zauctionhouse.api.services.AuctionRemoveService;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

@SuppressWarnings({"unchecked", "rawtypes"})
public class CombinedItemsButton extends PaginateButton {
   private final AuctionPlugin plugin;
   private final int emptySlot;
   private final boolean includeSelling;
   private final boolean includeExpired;
   private final boolean includePurchased;

   public CombinedItemsButton(AuctionPlugin var1, int var2, boolean var3, boolean var4, boolean var5) {
      this.plugin = var1;
      this.emptySlot = var2;
      this.includeSelling = var3;
      this.includeExpired = var4;
      this.includePurchased = var5;
   }

   public void onRender(Player var1, InventoryEngine var2) {
      List var3 = this.getCombinedItems(var1);
      if (var3.isEmpty()) {
         if (this.emptySlot != -1) {
            var2.addItem(this.emptySlot, this.getCustomItemStack(var1, false, new Placeholders()));
         }
      } else {
         ItemLoreConfiguration var4 = this.plugin.getConfiguration().getItemLore();
         AuctionManager var5 = this.plugin.getAuctionManager();
         AuctionRemoveService var6 = var5.getRemoveService();
         this.paginate(var3, var2, (var5x, var6x) -> {
            StorageType var7 = this.getItemStorageType(var6x, var1);
            List var8 = this.getLoreForStorageType(var4, var6x, var7);
            Set var9 = this.getPlaceholdersForStorageType(var4, var6x, var7);
            var2.addItem(var5x, var6x.buildItemStack(var1, var8, var9)).setClick((var4x) -> {
               switch (var7) {
                  case LISTED -> var6.removeSellingItem(var1, var6x);
                  case EXPIRED -> var6.removeExpiredItem(var1, var6x);
                  case PURCHASED -> var6.removePurchasedItem(var1, var6x);
               }

            });
         });
      }
   }

   public int getPaginationSize(@NonNull Player var1) {
      return this.getCombinedItems(var1).size();
   }

   private List<Item> getCombinedItems(Player var1) {
      AuctionManager var2 = this.plugin.getAuctionManager();
      ArrayList var3 = new ArrayList();
      if (this.includeSelling) {
         var3.addAll(var2.getPlayerSellingItems(var1));
      }

      if (this.includeExpired) {
         var3.addAll(var2.getExpiredItems(var1));
      }

      if (this.includePurchased) {
         var3.addAll(var2.getPurchasedItems(var1));
      }

      var3.sort(Comparator.comparing(Item::getExpiredAt).reversed());
      return var3;
   }

   private StorageType getItemStorageType(Item var1, Player var2) {
      StorageType var10000;
      switch (var1.getStatus()) {
         case AVAILABLE:
         case IS_REMOVE_CONFIRM:
         case IS_BEING_REMOVED:
         case IS_PURCHASE_CONFIRM:
         case IS_BEING_PURCHASED:
            var10000 = StorageType.LISTED;
            break;
         case REMOVED:
            var10000 = StorageType.EXPIRED;
            break;
         case PURCHASED:
            var10000 = StorageType.PURCHASED;
            break;
         case DELETED:
            var10000 = StorageType.DELETED;
            break;
         default:
            throw new MatchException((String)null, (Throwable)null);
      }

      return var10000;
   }

   private List<String> getLoreForStorageType(ItemLoreConfiguration var1, Item var2, StorageType var3) {
      List var10000;
      switch (var3) {
         case LISTED -> var10000 = var2.getStatus() == ItemStatus.AVAILABLE ? var1.sellingLore() : var1.beingPurchasedLore();
         case EXPIRED -> var10000 = var1.expiredLore();
         case PURCHASED -> var10000 = var1.purchasedLore();
         default -> var10000 = var1.sellingLore();
      }

      return var10000;
   }

   private Set<ItemPlaceholder> getPlaceholdersForStorageType(ItemLoreConfiguration var1, Item var2, StorageType var3) {
      Set var10000;
      switch (var3) {
         case LISTED -> var10000 = var2.getStatus() == ItemStatus.AVAILABLE ? var1.sellingPlaceholders() : var1.beingPurchasedPlaceholders();
         case EXPIRED -> var10000 = var1.expiredPlaceholders();
         case PURCHASED -> var10000 = var1.purchasedPlaceholders();
         default -> var10000 = var1.sellingPlaceholders();
      }

      return var10000;
   }
}
