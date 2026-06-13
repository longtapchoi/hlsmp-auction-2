package fr.maxlego08.zauctionhouse.api.services.result;

import fr.maxlego08.zauctionhouse.api.economy.AuctionEconomy;

public record ClaimResult(boolean success, String message, double amountClaimed, AuctionEconomy economy) {
   public static ClaimResult success(String var0, double var1, AuctionEconomy var3) {
      return new ClaimResult(true, var0, var1, var3);
   }

   public static ClaimResult failure(String var0) {
      return new ClaimResult(false, var0, (double)0.0F, (AuctionEconomy)null);
   }

   public static ClaimResult nothingToClaim(String var0) {
      return new ClaimResult(true, var0, (double)0.0F, (AuctionEconomy)null);
   }

   public boolean isSuccess() {
      return this.success;
   }

   public String getMessage() {
      return this.message;
   }

   public double getAmountClaimed() {
      return this.amountClaimed;
   }

   public AuctionEconomy getEconomy() {
      return this.economy;
   }
}
