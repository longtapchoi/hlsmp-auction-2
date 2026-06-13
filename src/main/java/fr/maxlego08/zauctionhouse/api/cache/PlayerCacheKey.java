package fr.maxlego08.zauctionhouse.api.cache;

import com.google.common.reflect.TypeToken;
import fr.maxlego08.zauctionhouse.api.category.Category;
import fr.maxlego08.zauctionhouse.api.economy.AuctionEconomy;
import fr.maxlego08.zauctionhouse.api.filter.DateFilter;
import fr.maxlego08.zauctionhouse.api.history.HistorySortType;
import fr.maxlego08.zauctionhouse.api.item.Item;
import fr.maxlego08.zauctionhouse.api.item.SortItem;
import fr.maxlego08.zauctionhouse.api.log.AdminLogItem;
import fr.maxlego08.zauctionhouse.api.log.LogType;
import fr.maxlego08.zauctionhouse.api.storage.dto.LogDTO;
import fr.maxlego08.zauctionhouse.api.storage.dto.TransactionDTO;
import fr.maxlego08.zauctionhouse.api.transaction.TransactionStatus;
import fr.maxlego08.zauctionhouse.api.utils.IntArrayList;
import fr.maxlego08.zauctionhouse.api.utils.IntList;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;
import org.bukkit.inventory.ItemStack;

public enum PlayerCacheKey {
   ITEMS_LISTED(new TypeToken<IntList>() {
   }, IntArrayList::new),
   ITEMS_EXPIRED(new TypeToken<IntList>() {
   }, IntArrayList::new),
   ITEMS_PURCHASED(new TypeToken<IntList>() {
   }, IntArrayList::new),
   ITEMS_SELLING(new TypeToken<IntList>() {
   }, IntArrayList::new),
   ADMIN_TARGET_UUID(new TypeToken<UUID>() {
   }, () -> null),
   ADMIN_TARGET_NAME(new TypeToken<String>() {
   }, () -> ""),
   ITEM_SHOW(new TypeToken<Item>() {
   }, () -> null),
   CURRENT_PAGE(new TypeToken<Integer>() {
   }, () -> 1),
   ITEM_SORT(new TypeToken<SortItem>() {
   }, () -> SortItem.DECREASING_DATE),
   ITEM_SORT_LOADING(new TypeToken<Boolean>() {
   }, () -> false),
   PURCHASE_ITEM(new TypeToken<Boolean>() {
   }, () -> false),
   SELL_PRICE(new TypeToken<BigDecimal>() {
   }, () -> BigDecimal.ZERO),
   SELL_ECONOMY(new TypeToken<AuctionEconomy>() {
   }, () -> null),
   SELL_EXPIRED_AT(new TypeToken<Long>() {
   }, () -> 0L),
   SELL_AMOUNT(new TypeToken<Integer>() {
   }, () -> 1),
   SELL_ITEMS(new TypeToken<Map<Integer, ItemStack>>() {
   }, HashMap::new),
   CURRENT_CATEGORY(new TypeToken<Category>() {
   }, () -> null),
   ADMIN_LOGS_DATA(new TypeToken<List<LogDTO>>() {
   }, ArrayList::new),
   ADMIN_LOGS_LOADING(new TypeToken<Boolean>() {
   }, () -> false),
   ADMIN_LOGS_TYPE_FILTER(new TypeToken<LogType>() {
   }, () -> null),
   ADMIN_LOGS_DATE_FILTER(new TypeToken<DateFilter>() {
   }, () -> DateFilter.ALL),
   ADMIN_LOG_SELECTED(new TypeToken<AdminLogItem>() {
   }, () -> null),
   ADMIN_TRANSACTIONS_DATA(new TypeToken<List<TransactionDTO>>() {
   }, ArrayList::new),
   ADMIN_TRANSACTIONS_LOADING(new TypeToken<Boolean>() {
   }, () -> false),
   ADMIN_TRANSACTIONS_STATUS_FILTER(new TypeToken<TransactionStatus>() {
   }, () -> null),
   ADMIN_TRANSACTIONS_DATE_FILTER(new TypeToken<DateFilter>() {
   }, () -> DateFilter.ALL),
   PENDING_MONEY_DATA(new TypeToken<Map<String, BigDecimal>>() {
   }, HashMap::new),
   PENDING_MONEY_LOADING(new TypeToken<Boolean>() {
   }, () -> false),
   HISTORY_DATA(new TypeToken<List<LogDTO>>() {
   }, ArrayList::new),
   HISTORY_LOADING(new TypeToken<Boolean>() {
   }, () -> false),
   HISTORY_SORT(new TypeToken<HistorySortType>() {
   }, () -> HistorySortType.DATE_DESC),
   SHULKER_INDEX(new TypeToken<Integer>() {
   }, () -> 0),
   SHULKER_ITEMS(new TypeToken<List<ItemStack>>() {
   }, ArrayList::new),
   REFRESH_LOADING(new TypeToken<Boolean>() {
   }, () -> false),
   SEARCH_QUERY(new TypeToken<String>() {
   }, () -> null),
   ITEMS_SEARCH(new TypeToken<IntList>() {
   }, IntArrayList::new);

   private final TypeToken<?> type;
   private final Supplier<?> fallback;
   private final Class<?> rawType;

   private PlayerCacheKey(TypeToken<?> var3, Supplier<?> var4) {
      this.type = var3;
      this.fallback = var4;
      this.rawType = var3.getRawType();
   }

   public TypeToken<?> getType() {
      return this.type;
   }

   public Class<?> getRawType() {
      return this.rawType;
   }

   public <T> T getFallback() {
      return (T)this.fallback.get();
   }

   // $FF: synthetic method
   private static PlayerCacheKey[] $values() {
      return new PlayerCacheKey[]{ITEMS_LISTED, ITEMS_EXPIRED, ITEMS_PURCHASED, ITEMS_SELLING, ADMIN_TARGET_UUID, ADMIN_TARGET_NAME, ITEM_SHOW, CURRENT_PAGE, ITEM_SORT, ITEM_SORT_LOADING, PURCHASE_ITEM, SELL_PRICE, SELL_ECONOMY, SELL_EXPIRED_AT, SELL_AMOUNT, SELL_ITEMS, CURRENT_CATEGORY, ADMIN_LOGS_DATA, ADMIN_LOGS_LOADING, ADMIN_LOGS_TYPE_FILTER, ADMIN_LOGS_DATE_FILTER, ADMIN_LOG_SELECTED, ADMIN_TRANSACTIONS_DATA, ADMIN_TRANSACTIONS_LOADING, ADMIN_TRANSACTIONS_STATUS_FILTER, ADMIN_TRANSACTIONS_DATE_FILTER, PENDING_MONEY_DATA, PENDING_MONEY_LOADING, HISTORY_DATA, HISTORY_LOADING, HISTORY_SORT, SHULKER_INDEX, SHULKER_ITEMS, REFRESH_LOADING, SEARCH_QUERY, ITEMS_SEARCH};
   }
}
