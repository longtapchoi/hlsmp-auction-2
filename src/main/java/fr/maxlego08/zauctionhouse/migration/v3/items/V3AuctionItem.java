package fr.maxlego08.zauctionhouse.migration.v3.items;

import fr.maxlego08.zauctionhouse.migration.v3.V3StorageType;
import java.util.UUID;

public class V3AuctionItem {
   private final UUID id;
   private final String itemstack;
   private final long price;
   private final UUID seller;
   private final UUID buyer;
   private final String economy;
   private final String auctionType;
   private final long expireAt;
   private final V3StorageType storageType;
   private final String sellerName;
   private final String serverName;
   private final int priority;

   public V3AuctionItem(UUID var1, String var2, long var3, UUID var5, UUID var6, String var7, String var8, long var9, V3StorageType var11, String var12, String var13, int var14) {
      this.id = var1;
      this.itemstack = var2;
      this.price = var3;
      this.seller = var5;
      this.buyer = var6;
      this.economy = var7;
      this.auctionType = var8;
      this.expireAt = var9;
      this.storageType = var11;
      this.sellerName = var12;
      this.serverName = var13;
      this.priority = var14;
   }

   public UUID getId() {
      return this.id;
   }

   public String getItemstack() {
      return this.itemstack;
   }

   public long getPrice() {
      return this.price;
   }

   public UUID getSeller() {
      return this.seller;
   }

   public UUID getBuyer() {
      return this.buyer;
   }

   public String getEconomy() {
      return this.economy;
   }

   public String getAuctionType() {
      return this.auctionType;
   }

   public boolean isInventoryType() {
      return "INVENTORY".equalsIgnoreCase(this.auctionType);
   }

   public long getExpireAt() {
      return this.expireAt;
   }

   public V3StorageType getStorageType() {
      return this.storageType;
   }

   public String getSellerName() {
      return this.sellerName;
   }

   public String getServerName() {
      return this.serverName;
   }

   public int getPriority() {
      return this.priority;
   }

   public String toString() {
      String var10000 = String.valueOf(this.id);
      return "V3AuctionItem{id=" + var10000 + ", seller=" + String.valueOf(this.seller) + ", price=" + this.price + ", storageType=" + String.valueOf(this.storageType) + ", auctionType=" + this.auctionType + "}";
   }
}
