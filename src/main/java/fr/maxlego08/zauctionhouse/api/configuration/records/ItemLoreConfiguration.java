package fr.maxlego08.zauctionhouse.api.configuration.records;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.item.ItemPlaceholder;
import fr.maxlego08.zauctionhouse.api.log.LogType;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public record ItemLoreConfiguration(boolean forceAmountOne, List<String> listedAuctionLore, Set<ItemPlaceholder> listedAuctionPlaceholders, List<String> multipleListedAuctionLore, Set<ItemPlaceholder> multipleListedAuctionPlaceholders, List<String> purchasedLore, Set<ItemPlaceholder> purchasedPlaceholders, List<String> expiredLore, Set<ItemPlaceholder> expiredPlaceholders, List<String> sellingLore, Set<ItemPlaceholder> sellingPlaceholders, List<String> beingPurchasedLore, Set<ItemPlaceholder> beingPurchasedPlaceholders, List<String> historyLore, List<String> adminLogLore, List<String> adminLogMultipleLore, List<String> sellInventoryRemoveItem, Map<LogType, String> logTypeNames, String sellerStatus, String buyerStatus, String rightSellerStatus, String rightBuyerStatus) {
   public static ItemLoreConfiguration of(AuctionPlugin var0, FileConfiguration var1) {
      List var2 = var1.getStringList("item-lore.listed-auction-item");
      List var3 = var1.getStringList("item-lore.multiple-listed-auction-item");
      List var4 = var1.getStringList("item-lore.purchased-item");
      List var5 = var1.getStringList("item-lore.expired-item");
      List var6 = var1.getStringList("item-lore.selling-item");
      List var7 = var1.getStringList("item-lore.being-purchased-item");
      return new ItemLoreConfiguration(var1.getBoolean("item-lore.force-amount-one", false), var2, ItemPlaceholder.detect(var2), var3, ItemPlaceholder.detect(var3), var4, ItemPlaceholder.detect(var4), var5, ItemPlaceholder.detect(var5), var6, ItemPlaceholder.detect(var6), var7, ItemPlaceholder.detect(var7), var1.getStringList("item-lore.history-item"), var1.getStringList("item-lore.admin-log-item"), var1.getStringList("item-lore.admin-log-multiple-item"), var1.getStringList("item-lore.sell-inventory-remove-item"), loadLogTypeNames(var1), var1.getString("item-lore.status.seller", "#8c8c8c• #2CCED2ᴄʟɪᴄᴋ #92ffffᴛᴏ ʀᴇᴛʀɪᴇᴠᴇ ᴛʜɪs ɪᴛᴇᴍ"), var1.getString("item-lore.status.buyer", "#8c8c8c• #2CCED2ᴄʟɪᴄᴋ #92ffffᴛᴏ ʙᴜʏ ᴛʜɪs ɪᴛᴇᴍ"), var1.getString("item-lore.status.right-seller", "#8c8c8c• #2CCED2ʀɪɢʜᴛ ᴄʟɪᴄᴋ #92ffffᴛᴏ ʀᴇᴛʀɪᴇᴠᴇ ᴛʜɪs ɪᴛᴇᴍ"), var1.getString("item-lore.status.right-buyer", "#8c8c8c• #2CCED2ʀɪɢʜᴛ ᴄʟɪᴄᴋ #92ffffᴛᴏ ʙᴜʏ ᴛʜɪs ɪᴛᴇᴍ"));
   }

   private static Map<LogType, String> loadLogTypeNames(FileConfiguration var0) {
      EnumMap var1 = new EnumMap(LogType.class);
      ConfigurationSection var2 = var0.getConfigurationSection("item-lore.log-type-names");
      var1.put(LogType.SALE, "Item Listed");
      var1.put(LogType.PURCHASE, "Item Purchased");
      var1.put(LogType.REMOVE_LISTED, "Removed from Listing");
      var1.put(LogType.REMOVE_SELLING, "Retrieved Selling Item");
      var1.put(LogType.REMOVE_EXPIRED, "Retrieved Expired");
      var1.put(LogType.REMOVE_PURCHASED, "Retrieved Purchase");
      if (var2 != null) {
         for(LogType var6 : LogType.values()) {
            String var7 = var2.getString(var6.name());
            if (var7 != null) {
               var1.put(var6, var7);
            }
         }
      }

      return var1;
   }

   public String getLogTypeName(LogType var1) {
      return (String)this.logTypeNames.getOrDefault(var1, var1.name());
   }
}
