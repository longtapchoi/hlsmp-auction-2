package fr.maxlego08.zauctionhouse.api.event.events.sell;

import fr.maxlego08.zauctionhouse.api.economy.AuctionEconomy;
import fr.maxlego08.zauctionhouse.api.event.CancelledAuctionEvent;
import java.math.BigDecimal;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AuctionPreSellEvent extends CancelledAuctionEvent {
   private final Player player;
   private int amount;
   private long expiredAt;
   private ItemStack itemStack;
   private AuctionEconomy auctionEconomy;
   private BigDecimal price;

   public AuctionPreSellEvent(Player var1, int var2, long var3, ItemStack var5, AuctionEconomy var6, BigDecimal var7) {
      this.player = var1;
      this.amount = var2;
      this.expiredAt = var3;
      this.itemStack = var5;
      this.auctionEconomy = var6;
      this.price = var7;
   }

   public Player getPlayer() {
      return this.player;
   }

   public int getAmount() {
      return this.amount;
   }

   public void setAmount(int var1) {
      this.amount = var1;
   }

   public long getExpiredAt() {
      return this.expiredAt;
   }

   public void setExpiredAt(long var1) {
      this.expiredAt = var1;
   }

   public ItemStack getItemStack() {
      return this.itemStack;
   }

   public void setItemStack(ItemStack var1) {
      this.itemStack = var1;
   }

   public AuctionEconomy getAuctionEconomy() {
      return this.auctionEconomy;
   }

   public void setAuctionEconomy(AuctionEconomy var1) {
      this.auctionEconomy = var1;
   }

   public BigDecimal getPrice() {
      return this.price;
   }

   public void setPrice(BigDecimal var1) {
      this.price = var1;
   }
}
