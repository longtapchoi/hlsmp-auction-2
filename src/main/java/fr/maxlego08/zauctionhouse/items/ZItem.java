package fr.maxlego08.zauctionhouse.items;

import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.category.Category;
import fr.maxlego08.zauctionhouse.api.economy.AuctionEconomy;
import fr.maxlego08.zauctionhouse.api.economy.PriceFormat;
import fr.maxlego08.zauctionhouse.api.item.Item;
import fr.maxlego08.zauctionhouse.api.item.ItemPlaceholder;
import fr.maxlego08.zauctionhouse.api.item.ItemStatus;
import fr.maxlego08.zauctionhouse.utils.PerformanceDebug;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public abstract class ZItem implements Item {
   protected final AuctionPlugin plugin;
   protected final int id;
   protected final String serverName;
   protected final UUID sellerUniqueId;
   protected final String sellerName;
   protected final BigDecimal price;
   protected final String economyName;
   protected final Date createdAt;
   protected final PerformanceDebug performanceDebug;
   protected AuctionEconomy auctionEconomy;
   protected Date expiredAt;
   protected ItemStatus itemStatus;
   protected UUID buyerUniqueId;
   protected String buyerName;
   protected Set<Category> categories;

   public ZItem(AuctionPlugin var1, int var2, String var3, UUID var4, String var5, BigDecimal var6, AuctionEconomy var7, Date var8, Date var9) {
      this.itemStatus = ItemStatus.AVAILABLE;
      this.categories = new HashSet();
      this.plugin = var1;
      this.id = var2;
      this.serverName = var3;
      this.sellerUniqueId = var4;
      this.sellerName = var5;
      this.price = var6;
      this.economyName = var7.getName();
      this.auctionEconomy = var7;
      this.createdAt = var8;
      this.expiredAt = var9;
      this.performanceDebug = new PerformanceDebug(var1);
   }

   public int getId() {
      return this.id;
   }

   public String getServerName() {
      return this.serverName;
   }

   public UUID getSellerUniqueId() {
      return this.sellerUniqueId;
   }

   public String getSellerName() {
      return this.sellerName;
   }

   public BigDecimal getPrice() {
      return this.price;
   }

   public AuctionEconomy getAuctionEconomy() {
      return this.auctionEconomy;
   }

   public void setAuctionEconomy(AuctionEconomy var1) {
      this.auctionEconomy = var1;
   }

   public String getEconomyName() {
      return this.economyName;
   }

   public Date getCreatedAt() {
      return this.createdAt;
   }

   public Date getExpiredAt() {
      return this.expiredAt;
   }

   public void setExpiredAt(Date var1) {
      this.expiredAt = var1;
   }

   public Placeholders createPlaceholders(Player var1) {
      return this.createPlaceholders(var1, ItemPlaceholder.all());
   }

   public Placeholders createPlaceholders(Player var1, Set<ItemPlaceholder> var2) {
      return (Placeholders)this.performanceDebug.measureWithContext("item.CreatePlaceholders", () -> {
         Placeholders var3 = new Placeholders();
         if (var2.contains(ItemPlaceholder.ECONOMY_NAME)) {
            var3.register("economy-name", this.auctionEconomy.getName());
         }

         if (var2.contains(ItemPlaceholder.ECONOMY_DISPLAY_NAME)) {
            var3.register("economy-display-name", this.auctionEconomy.getDisplayName());
         }

         if (var2.contains(ItemPlaceholder.SELLER)) {
            var3.register("seller", this.getSellerName());
         }

         if (var2.contains(ItemPlaceholder.STATUS)) {
            var3.register("status", this.createStatus(var1));
         }

         if (var2.contains(ItemPlaceholder.PRICE)) {
            var3.register("price", this.getFormattedPrice());
         }

         if (var2.contains(ItemPlaceholder.PRICE_RAW)) {
            var3.register("price-price-raw", this.getFormattedPrice(PriceFormat.PRICE_RAW));
         }

         if (var2.contains(ItemPlaceholder.PRICE_WITH_DECIMAL_FORMAT)) {
            var3.register("price-price-with-decimal-format", this.getFormattedPrice(PriceFormat.PRICE_WITH_DECIMAL_FORMAT));
         }

         if (var2.contains(ItemPlaceholder.PRICE_WITH_REDUCTION)) {
            var3.register("price-price-with-reduction", this.getFormattedPrice(PriceFormat.PRICE_WITH_REDUCTION));
         }

         if (var2.contains(ItemPlaceholder.PRICE_WITHOUT_DECIMAL)) {
            var3.register("price-price-without-decimal", this.getFormattedPrice(PriceFormat.PRICE_WITHOUT_DECIMAL));
         }

         if (var2.contains(ItemPlaceholder.TIME_REMAINING)) {
            var3.register("time-remaining", this.getRemainingTime());
         }

         if (var2.contains(ItemPlaceholder.FORMATTED_EXPIRE_DATE)) {
            var3.register("formatted-expire-date", this.getFormattedExpireDate());
         }

         return var3;
      }, () -> {
         String var10000 = var1.getName();
         return "for=" + var10000 + ", itemId=" + this.id;
      });
   }

   public OfflinePlayer getSeller() {
      return Bukkit.getOfflinePlayer(this.sellerUniqueId);
   }

   public String getFormattedExpireDate() {
      return this.plugin.getConfiguration().getDateFormat().format(this.expiredAt);
   }

   public String getFormattedPrice() {
      return this.plugin.getEconomyManager().format((AuctionEconomy)this.auctionEconomy, this.price);
   }

   public String getFormattedPrice(PriceFormat var1) {
      return this.plugin.getEconomyManager().format((PriceFormat)var1, this.price);
   }

   public String getRemainingTime() {
      long var1 = this.expiredAt.getTime() - System.currentTimeMillis();
      return this.plugin.getConfiguration().getTime().getStringTime(var1);
   }

   public boolean isExpired() {
      return System.currentTimeMillis() >= this.expiredAt.getTime() && this.expiredAt.getTime() != 0L;
   }

   public boolean canReceiveItem(Player var1) {
      return var1.getInventory().firstEmpty() != -1;
   }

   public ItemStatus getStatus() {
      return this.itemStatus;
   }

   public void setStatus(ItemStatus var1) {
      this.itemStatus = var1;
   }

   public UUID getBuyerUniqueId() {
      return this.buyerUniqueId;
   }

   public String getBuyerName() {
      return this.buyerName;
   }

   public void setBuyer(Player var1) {
      this.buyerUniqueId = var1.getUniqueId();
      this.buyerName = var1.getName();
   }

   public void setBuyer(UUID var1, String var2) {
      this.buyerUniqueId = var1;
      this.buyerName = var2;
   }

   public Set<Category> getCategories() {
      return this.categories;
   }

   public void setCategories(Set<Category> var1) {
      this.categories = (Set<Category>)(var1 != null ? var1 : new HashSet());
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else {
         boolean var10000;
         if (var1 instanceof Item) {
            Item var2 = (Item)var1;
            if (this.id == var2.getId()) {
               var10000 = true;
               return var10000;
            }
         }

         var10000 = false;
         return var10000;
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.id);
   }
}
