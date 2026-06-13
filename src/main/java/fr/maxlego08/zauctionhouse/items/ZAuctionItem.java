package fr.maxlego08.zauctionhouse.items;

import fr.maxlego08.menu.api.utils.LoreType;
import fr.maxlego08.menu.api.utils.MetaUpdater;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.component.ComponentMessage;
import fr.maxlego08.zauctionhouse.api.configuration.records.ItemDisplayConfiguration;
import fr.maxlego08.zauctionhouse.api.configuration.records.ItemLoreConfiguration;
import fr.maxlego08.zauctionhouse.api.economy.AuctionEconomy;
import fr.maxlego08.zauctionhouse.api.item.ItemPlaceholder;
import fr.maxlego08.zauctionhouse.api.item.items.AuctionItem;
import fr.maxlego08.zauctionhouse.utils.component.ComponentMessageHelper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@SuppressWarnings({"unchecked", "rawtypes"})
public class ZAuctionItem extends ZItem implements AuctionItem {
   private final List<ItemStack> itemStacks;

   public ZAuctionItem(AuctionPlugin var1, int var2, String var3, UUID var4, String var5, BigDecimal var6, AuctionEconomy var7, Date var8, Date var9, List<ItemStack> var10) {
      super(var1, var2, var3, var4, var5, var6, var7, var8, var9);
      this.itemStacks = var10;
   }

   public List<ItemStack> getItemStacks() {
      return this.itemStacks;
   }

   public ItemStack buildItemStack(Player var1) {
      ItemLoreConfiguration var2 = this.plugin.getConfiguration().getItemLore();
      boolean var3 = this.itemStacks.size() == 1;
      return this.buildItemStack(var1, var3 ? var2.listedAuctionLore() : var2.multipleListedAuctionLore(), var3 ? var2.listedAuctionPlaceholders() : var2.multipleListedAuctionPlaceholders());
   }

   public ItemStack buildItemStack(Player var1, List<String> var2) {
      return this.buildItemStack(var1, var2, ItemPlaceholder.detect(var2));
   }

   public ItemStack buildItemStack(Player var1, List<String> var2, Set<ItemPlaceholder> var3) {
      return (ItemStack)this.performanceDebug.measureWithContext("item.BuildItemStack", () -> {
         MetaUpdater var4 = this.plugin.getInventoriesLoader().getInventoryManager().getMeta();
         ItemStack var5 = this.getItemStack(var1);
         ItemMeta var6 = var5.getItemMeta();
         Placeholders var7 = this.createPlaceholders(var1, var3);
         if (var3.contains(ItemPlaceholder.ITEM_COUNT)) {
            var7.register("item_count", ((Integer)this.itemStacks.stream().map(ItemStack::getAmount).reduce(0, Integer::sum)).toString());
         }

         Stream var10002 = var2.stream();
         Objects.requireNonNull(var7);
         var4.updateLore(var6, var10002.map(var7::parse).toList(), LoreType.APPEND);
         var5.setItemMeta(var6);
         return var5;
      }, () -> {
         String var10000 = var1.getName();
         return "for=" + var10000 + ", itemId=" + this.id;
      });
   }

   private ItemStack getItemStack(Player var1) {
      if (this.itemStacks.size() == 1) {
         ItemStack var2 = (ItemStack)this.itemStacks.getFirst();
         if (var2 == null) {
            this.plugin.getLogger().warning("Item #" + this.id + " has a null ItemStack, the item data may be corrupted.");
            return new ItemStack(Material.BARRIER);
         } else {
            ItemStack var3 = var2.clone();
            if (this.plugin.getConfiguration().getItemLore().forceAmountOne()) {
               var3.setAmount(1);
            }

            return var3;
         }
      } else {
         return this.plugin.getConfiguration().getSpecialItems().auctionItem().build(var1).clone();
      }
   }

   public String createStatus(Player var1) {
      ItemLoreConfiguration var2 = this.plugin.getConfiguration().getItemLore();
      boolean var3 = this.sellerUniqueId.equals(var1.getUniqueId());
      return this.itemStacks.size() == 1 ? (var3 ? var2.sellerStatus() : var2.buyerStatus()) : (var3 ? var2.rightSellerStatus() : var2.rightBuyerStatus());
   }

   public int getAmount() {
      if (this.itemStacks.size() == 1) {
         ItemStack var1 = (ItemStack)this.itemStacks.getFirst();
         return var1 != null ? var1.getAmount() : 0;
      } else {
         return 0;
      }
   }

   public String getTranslationKey() {
      if (this.itemStacks.size() == 1) {
         ItemStack var1 = (ItemStack)this.itemStacks.getFirst();
         return var1 != null ? var1.getType().translationKey() : "";
      } else {
         return "";
      }
   }

   public String getItemDisplay() {
      StringBuilder var1 = new StringBuilder();
      ComponentMessage var2 = ComponentMessageHelper.componentMessage;
      ItemDisplayConfiguration var3 = this.plugin.getConfiguration().getItemDisplay();
      Object var4 = this.itemStacks;
      if (var3.mergeSimilar()) {
         var4 = new ArrayList();

         for(ItemStack var6 : this.itemStacks) {
            boolean var7 = true;

            for(ItemStack var9 : var4) {
               if (var9.isSimilar(var6)) {
                  var9.setAmount(var9.getAmount() + var6.getAmount());
                  var7 = false;
                  break;
               }
            }

            if (var7) {
               ((List)var4).add(var6.clone());
            }
         }
      }

      int var10 = ((List)var4).size();

      for(int var11 = 0; var11 < var10; ++var11) {
         ItemStack var12 = (ItemStack)((List)var4).get(var11);
         if (var2.hasDisplayName(var12)) {
            var1.append(var3.itemNameDisplay().replace("%item-name%", var2.getItemStackDisplayName(var12)).replace("%amount%", String.valueOf(var12.getAmount())));
         } else {
            var1.append(var3.langDisplay().replace("%item-translation-key%", var12.translationKey()).replace("%amount%", String.valueOf(var12.getAmount())));
         }

         if (var10 > 1 && var11 < var10 - 1) {
            var1.append(var11 == var10 - 2 ? var3.and() : var3.between());
         }
      }

      return var1.toString();
   }

   public String getItemsAsString() {
      return (String)this.itemStacks.stream().map((var0) -> {
         int var10000 = var0.getAmount();
         return "x" + var10000 + " " + var0.getType().name();
      }).collect(Collectors.joining(", "));
   }
}
