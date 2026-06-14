package fr.maxlego08.zauctionhouse.migration.v3.items;

import java.util.UUID;

public class V3Transaction {
   private final int id;
   private final UUID seller;
   private final UUID buyer;
   private final String itemstack;
   private final long transactionDate;
   private final long price;
   private final String economy;
   private final boolean isRead;
   private final boolean needMoney;

   public V3Transaction(int var1, UUID var2, UUID var3, String var4, long var5, long var7, String var9, boolean var10, boolean var11) {
      this.id = var1;
      this.seller = var2;
      this.buyer = var3;
      this.itemstack = var4;
      this.transactionDate = var5;
      this.price = var7;
      this.economy = var9;
      this.isRead = var10;
      this.needMoney = var11;
   }

   public int getId() {
      return this.id;
   }

   public UUID getSeller() {
      return this.seller;
   }

   public UUID getBuyer() {
      return this.buyer;
   }

   public String getItemstack() {
      return this.itemstack;
   }

   public long getTransactionDate() {
      return this.transactionDate;
   }

   public long getPrice() {
      return this.price;
   }

   public String getEconomy() {
      return this.economy;
   }

   public boolean isRead() {
      return this.isRead;
   }

   public boolean isNeedMoney() {
      return this.needMoney;
   }

   public String toString() {
      int var10000 = this.id;
      return "V3Transaction{id=" + var10000 + ", seller=" + String.valueOf(this.seller) + ", buyer=" + String.valueOf(this.buyer) + ", price=" + this.price + ", date=" + this.transactionDate + "}";
   }
}
