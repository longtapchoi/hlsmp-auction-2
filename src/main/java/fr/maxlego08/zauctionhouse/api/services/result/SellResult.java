package fr.maxlego08.zauctionhouse.api.services.result;

import fr.maxlego08.zauctionhouse.api.item.items.AuctionItem;
import java.util.Optional;

public record SellResult(boolean success, String message, AuctionItem auctionItem, SellFailReason failReason) {
   public static SellResult success(String var0, AuctionItem var1) {
      return new SellResult(true, var0, var1, SellFailReason.NONE);
   }

   public static SellResult failure(String var0, SellFailReason var1) {
      return new SellResult(false, var0, (AuctionItem)null, var1);
   }

   public boolean isSuccess() {
      return this.success;
   }

   public String getMessage() {
      return this.message;
   }

   public Optional<AuctionItem> getAuctionItem() {
      return Optional.ofNullable(this.auctionItem);
   }

   public SellFailReason getFailReason() {
      return this.failReason;
   }
}
