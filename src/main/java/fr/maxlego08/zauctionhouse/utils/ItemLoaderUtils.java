package fr.maxlego08.zauctionhouse.utils;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.category.CategoryManager;
import fr.maxlego08.zauctionhouse.api.economy.AuctionEconomy;
import fr.maxlego08.zauctionhouse.api.economy.EconomyManager;
import fr.maxlego08.zauctionhouse.api.item.Item;
import fr.maxlego08.zauctionhouse.api.item.ItemStatus;
import fr.maxlego08.zauctionhouse.api.item.ItemType;
import fr.maxlego08.zauctionhouse.api.item.StorageType;
import fr.maxlego08.zauctionhouse.api.item.items.AuctionItem;
import fr.maxlego08.zauctionhouse.api.storage.StorageManager;
import fr.maxlego08.zauctionhouse.api.storage.dto.AuctionItemDTO;
import fr.maxlego08.zauctionhouse.api.storage.dto.ItemDTO;
import fr.maxlego08.zauctionhouse.api.utils.Base64ItemStack;
import fr.maxlego08.zauctionhouse.items.ZAuctionItem;
import fr.maxlego08.zauctionhouse.storage.repository.repositories.AuctionItemRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.logging.Logger;

public abstract class ItemLoaderUtils {
   protected String getPlayerName(Map<UUID, String> var1, UUID var2) {
      String var3 = (String)var1.get(var2);
      if (var3 == null) {
         throw new IllegalStateException("Unknown player with UUID " + String.valueOf(var2));
      } else {
         return var3;
      }
   }

   protected List<AuctionItemDTO> getAuctionItems(List<AuctionItemDTO> var1, int var2) {
      return var1.stream().filter((var1x) -> var1x.item_id() == var2).toList();
   }

   protected List<String> getIDS(List<ItemDTO> var1, ItemType var2) {
      return var1.stream().filter((var1x) -> var1x.item_type() == var2).map(ItemDTO::id).map(String::valueOf).toList();
   }

   protected AuctionItem createAuctionItem(AuctionPlugin var1, ItemDTO var2, String var3, List<AuctionItemDTO> var4, AuctionEconomy var5) {
      List var6 = var4.stream().map((var0) -> Base64ItemStack.decode(var0.itemstack())).toList();
      ZAuctionItem var7 = new ZAuctionItem(var1, var2.id(), var2.server_name(), var2.seller_unique_id(), var3, var2.price(), var5, var2.created_at(), var2.expired_at(), var6);
      ItemStatus var10001;
      switch (var2.storage_type()) {
         case LISTED -> var10001 = ItemStatus.AVAILABLE;
         case PURCHASED -> var10001 = ItemStatus.PURCHASED;
         case EXPIRED -> var10001 = ItemStatus.REMOVED;
         case DELETED -> var10001 = ItemStatus.DELETED;
         default -> throw new MatchException((String)null, (Throwable)null);
      }

      var7.setStatus(var10001);
      return var7;
   }

   protected Result createItems(AuctionPlugin var1, Map<UUID, String> var2, List<ItemDTO> var3, PerformanceDebug var4, BiConsumer<StorageType, Item> var5) {
      CategoryManager var6 = var1.getCategoryManager();
      StorageManager var7 = var1.getStorageManager();
      EconomyManager var8 = var1.getEconomyManager();
      long var9 = var4.start();
      List var11 = ((AuctionItemRepository)var7.with(AuctionItemRepository.class)).select(this.getIDS(var3, ItemType.AUCTION));
      var4.end("loadItems.loadAuctionItemsFromDB", var9, "count=" + var11.size());
      int var12 = 0;
      long var13 = var4.start();

      for(ItemDTO var16 : var3) {
         String var17 = this.getPlayerName(var2, var16.seller_unique_id());
         String var18 = null;
         if (var16.buyer_unique_id() != null) {
            var18 = this.getPlayerName(var2, var16.buyer_unique_id());
         }

         Optional var19 = var8.getEconomy(var16.economy_name());
         if (var19.isEmpty()) {
            Logger var10000 = var1.getLogger();
            String var10001 = var16.economy_name();
            var10000.severe("Impossible to find the economy " + var10001 + " for auction item id " + var16.id() + ", skip it...");
         } else {
            switch (var16.item_type()) {
               case AUCTION:
                  List var20 = this.getAuctionItems(var11, var16.id());
                  AuctionItem var21 = this.createAuctionItem(var1, var16, var17, var20, (AuctionEconomy)var19.get());
                  if (var18 != null) {
                     var21.setBuyer(var16.buyer_unique_id(), var18);
                  }

                  var6.applyCategories(var21);
                  var5.accept(var16.storage_type(), var21);
                  break;
               case BID:
                  var1.getLogger().severe("Bid items not implemented");
                  break;
               case RENT:
                  var1.getLogger().severe("Rent items not implemented");
            }

            ++var12;
         }
      }

      var4.end("loadItems.processItems", var13, "processed=" + var12);
      return new Result(var12, var11.size(), 0, 0);
   }

   public static record Result(int amount, int auctionItems, int bidItems, int rentItems) {
   }
}
