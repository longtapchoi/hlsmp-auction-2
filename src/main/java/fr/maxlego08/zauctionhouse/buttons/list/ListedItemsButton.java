package fr.maxlego08.zauctionhouse.buttons.list;

import fr.maxlego08.menu.api.button.PaginateButton;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.engine.ItemButton;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.zauctionhouse.api.AuctionManager;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCache;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCacheKey;
import fr.maxlego08.zauctionhouse.api.configuration.Configuration;
import fr.maxlego08.zauctionhouse.api.configuration.records.ActionConfiguration;
import fr.maxlego08.zauctionhouse.api.economy.AuctionEconomy;
import fr.maxlego08.zauctionhouse.api.inventories.Inventories;
import fr.maxlego08.zauctionhouse.api.item.Item;
import fr.maxlego08.zauctionhouse.api.item.ItemStatus;
import fr.maxlego08.zauctionhouse.api.item.StorageType;
import fr.maxlego08.zauctionhouse.api.item.items.AuctionItem;
import fr.maxlego08.zauctionhouse.api.messages.Message;
import fr.maxlego08.zauctionhouse.api.utils.IntArrayList;
import fr.maxlego08.zauctionhouse.api.utils.IntList;
import fr.maxlego08.zauctionhouse.api.utils.Permission;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Level;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NonNull;

public class ListedItemsButton extends PaginateButton {
   private final AuctionPlugin plugin;
   private final int emptySlot;

   public ListedItemsButton(Plugin var1, int var2) {
      this.plugin = (AuctionPlugin)var1;
      this.emptySlot = var2;
   }

   public void onRender(Player var1, InventoryEngine var2) {
      AuctionManager var3 = this.plugin.getAuctionManager();
      IntList var4 = var3.getItemIdsListedForSale(var1);
      if (var4.isEmpty()) {
         if (this.emptySlot != -1) {
            var2.addItem(this.emptySlot, this.getCustomItemStack(var1, false, new Placeholders()));
         }
      } else {
         int var5 = var2.getPage() - 1;
         ArrayList var6 = new ArrayList(this.getSlots());
         List var7 = var3.resolveItemsForPage(StorageType.LISTED, var4, var5, var6.size());

         for(int var8 = 0; var8 < var7.size(); ++var8) {
            Item var9 = (Item)var7.get(var8);
            int var10 = (Integer)var6.get(var8);
            ItemStack var11 = var9.buildItemStack(var1);
            ItemButton var12 = var2.addItem(var10, var11);
            if (var12 != null) {
               var12.setClick(this.createClick(var1, var2, var10, var9, var11));
            }
         }

      }
   }

   public int getPaginationSize(@NonNull Player var1) {
      return this.plugin.getAuctionManager().getItemIdsListedForSale(var1).size();
   }

   public Consumer<InventoryClickEvent> createClick(Player var1, InventoryEngine var2, int var3, Item var4, ItemStack var5) {
      AuctionManager var6 = this.plugin.getAuctionManager();
      return (var7) -> {
         if (var4.getStatus() != ItemStatus.AVAILABLE) {
            var6.clearPlayerCache(var1, PlayerCacheKey.ITEMS_SELLING, PlayerCacheKey.ITEMS_LISTED);
            var6.updateInventory(var1);
         } else if (var7.getClick() != ClickType.DROP && var7.getClick() != ClickType.MIDDLE || !var1.hasPermission(Permission.ZAUCTIONHOUSE_ADMIN_REMOVE_INVENTORY.asPermission())) {
            if (var4.getSellerUniqueId().equals(var1.getUniqueId())) {
               if (this.plugin.getConfiguration().getActions().listed().openConfirmInventory()) {
                  boolean var10000;
                  label39: {
                     if (var4 instanceof AuctionItem) {
                        AuctionItem var9 = (AuctionItem)var4;
                        if (var9.getItemStacks().size() > 1) {
                           var10000 = true;
                           break label39;
                        }
                     }

                     var10000 = false;
                  }

                  boolean var8 = var10000;
                  PlayerCache var11 = var6.getCache(var1);
                  var11.set(PlayerCacheKey.ITEM_SHOW, var4);
                  var11.set(PlayerCacheKey.CURRENT_PAGE, this.plugin.getInventoriesLoader().getInventoryManager().getPage(var1));
                  this.plugin.getAuctionClusterBridge().notifyItemStatusChange(var4, ItemStatus.AVAILABLE, ItemStatus.IS_REMOVE_CONFIRM).thenRun(() -> {
                     var4.setStatus(ItemStatus.IS_REMOVE_CONFIRM);
                     var6.updateListedItems(var4, false, (Player)null);
                     this.plugin.getInventoriesLoader().openInventory(var1, var8 ? Inventories.REMOVE_INVENTORY_CONFIRM : Inventories.REMOVE_CONFIRM);
                  });
               } else {
                  var6.getRemoveService().removeListedItem(var1, var4);
               }
            } else {
               boolean var14;
               label34: {
                  if (var4 instanceof AuctionItem) {
                     AuctionItem var12 = (AuctionItem)var4;
                     if (var12.getItemStacks().size() > 1) {
                        var14 = true;
                        break label34;
                     }
                  }

                  var14 = false;
               }

               boolean var10 = var14;
               Inventories var13 = var10 ? Inventories.PURCHASE_INVENTORY_CONFIRM : Inventories.PURCHASE_CONFIRM;
               this.processPurchase(var1, var2, var3, var4, var5, var13);
            }

         }
      };
   }

   private void processPurchase(Player var1, InventoryEngine var2, int var3, Item var4, ItemStack var5, Inventories var6) {
      Configuration var7 = this.plugin.getConfiguration();
      ActionConfiguration.PurchasedConfiguration var8 = var7.getActions().purchased();
      AuctionManager var9 = this.plugin.getAuctionManager();
      PlayerCache var10 = var9.getCache(var1);
      if (!(Boolean)var10.get(PlayerCacheKey.PURCHASE_ITEM)) {
         var10.set(PlayerCacheKey.PURCHASE_ITEM, true);
         AuctionEconomy var11 = var4.getAuctionEconomy();
         if (var11 == null) {
            this.plugin.getLogger().warning("Item " + var4.getId() + " has no economy set, cannot process purchase");
            var10.set(PlayerCacheKey.PURCHASE_ITEM, false);
         } else {
            var11.has(var1.getUniqueId(), var4.getPrice()).whenComplete((var10x, var11x) -> {
               if (var11x != null) {
                  this.plugin.getLogger().log(Level.WARNING, "Cannot verify the balance of " + var1.getName(), var11x);
                  var10.set(PlayerCacheKey.PURCHASE_ITEM, false);
               } else if (!var10x) {
                  if (var8.sendNoMoneyMessage()) {
                     var9.message(var1, Message.NOT_ENOUGH_MONEY);
                  }

                  var8.noMoneySound().play(var1);
                  if (var8.noMoney().enable()) {
                     Inventory var12 = var2.getSpigotInventory();
                     var12.setItem(var3, var8.noMoney().menuItemStack().build(var1));
                     this.plugin.getScheduler().runLater(() -> {
                        if (!var2.isClose()) {
                           var12.setItem(var3, var5);
                        }
                     }, (long)var8.noMoney().duration());
                  }

                  var10.set(PlayerCacheKey.PURCHASE_ITEM, false);
               } else {
                  var10.set(PlayerCacheKey.ITEM_SHOW, var4);
                  var10.set(PlayerCacheKey.CURRENT_PAGE, this.plugin.getInventoriesLoader().getInventoryManager().getPage(var1));
                  var10.set(PlayerCacheKey.PURCHASE_ITEM, false);
                  this.plugin.getAuctionClusterBridge().notifyItemStatusChange(var4, ItemStatus.AVAILABLE, ItemStatus.IS_PURCHASE_CONFIRM).thenRun(() -> {
                     var4.setStatus(ItemStatus.IS_PURCHASE_CONFIRM);
                     var9.updateListedItems(var4, false, var1);
                     this.plugin.getInventoriesLoader().openInventory(var1, var6);
                  });
               }
            });
         }
      }
   }

   public void updateInventory(Player var1, InventoryEngine var2, Item var3, boolean var4, AuctionManager var5) {
      int var6 = var2.getPage();
      IntList var7 = var5.getItemIdsListedForSale(var1);
      if (var7.size() <= 1) {
         this.plugin.getInventoriesLoader().getInventoryManager().updateInventory(var1);
      } else {
         ArrayList var8 = new ArrayList(this.getSlots());
         if (!var8.isEmpty() && var8.size() != 1) {
            int var9 = this.findIndexOf(var7, var3.getId());
            if (var9 != -1) {
               int var10 = var8.size();
               int var11 = (var6 - 1) * var10;
               int var12 = var11 + var10;
               if (var9 >= var11 && var9 < var12) {
                  if (!var4) {
                     Item var13 = null;
                     if (var12 < var7.size()) {
                        int var14 = var7.getInt(var12);
                        List var15 = var5.resolveItems(StorageType.LISTED, this.createSingleItemList(var14));
                        if (!var15.isEmpty()) {
                           var13 = (Item)var15.getFirst();
                        }
                     }

                     this.processRemove(var13, var9, var11, var8, var2, var1);
                  } else {
                     this.processAdd(var3, var9, var11, var8, var2, var1);
                  }

               }
            }
         }
      }
   }

   private int findIndexOf(IntList var1, int var2) {
      for(int var3 = 0; var3 < var1.size(); ++var3) {
         if (var1.getInt(var3) == var2) {
            return var3;
         }
      }

      return -1;
   }

   private IntList createSingleItemList(int var1) {
      IntArrayList var2 = new IntArrayList(1);
      var2.add(var1);
      return var2;
   }

   private void processAdd(Item var1, int var2, int var3, List<Integer> var4, InventoryEngine var5, Player var6) {
      Inventory var7 = var5.getSpigotInventory();
      Map var8 = var5.getItems();
      int var9 = var2 - var3;
      int var10 = (Integer)var4.get(var9);

      for(int var11 = var4.size() - 1; var11 > var9; --var11) {
         int var12 = (Integer)var4.get(var11 - 1);
         int var13 = (Integer)var4.get(var11);
         ItemButton var14 = (ItemButton)var8.get(var12);
         if (var14 != null) {
            var8.put(var13, var14);
            var7.setItem(var13, var14.getDisplayItem());
         }
      }

      ItemStack var15 = var1.buildItemStack(var6);
      ItemButton var16 = var5.addItem(var10, var15);
      if (var16 != null) {
         var16.setClick(this.createClick(var6, var5, var10, var1, var15));
      }

   }

   private void processRemove(Item var1, int var2, int var3, List<Integer> var4, InventoryEngine var5, Player var6) {
      Inventory var7 = var5.getSpigotInventory();
      Map var8 = var5.getItems();
      int var9 = var2 - var3;
      int var10 = (Integer)var4.get(var9);
      var8.remove(var10);
      var7.setItem(var10, (ItemStack)null);

      for(int var11 = var9 + 1; var11 < var4.size(); ++var11) {
         int var12 = (Integer)var4.get(var11);
         int var13 = (Integer)var4.get(var11 - 1);
         ItemButton var14 = (ItemButton)var8.remove(var12);
         if (var14 != null) {
            var8.put(var13, var14);
            var7.setItem(var13, var14.getDisplayItem());
            var7.setItem(var12, (ItemStack)null);
         }
      }

      if (var1 != null) {
         int var15 = (Integer)var4.getLast();
         ItemStack var16 = var1.buildItemStack(var6);
         ItemButton var17 = var5.addItem(var15, var16);
         if (var17 != null) {
            var17.setClick(this.createClick(var6, var5, var15, var1, var16));
         }
      }

   }
}
