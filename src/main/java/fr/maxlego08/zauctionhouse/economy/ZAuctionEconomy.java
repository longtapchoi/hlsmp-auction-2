package fr.maxlego08.zauctionhouse.economy;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.economy.AuctionEconomy;
import fr.maxlego08.zauctionhouse.api.economy.PriceFormat;
import fr.maxlego08.zauctionhouse.api.item.ItemType;
import fr.maxlego08.zauctionhouse.api.tax.TaxConfiguration;
import fr.maxlego08.zauctionhouse.libs.currencies.CurrencyProvider;
import fr.maxlego08.zauctionhouse.tax.ZTaxConfiguration;
import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.Nullable;

public class ZAuctionEconomy implements AuctionEconomy {
   private final AuctionPlugin plugin;
   private final CurrencyProvider currencyProvider;
   private final String name;
   private final String displayName;
   private final String format;
   private final String symbol;
   private final String permission;
   private final String depositReason;
   private final String withdrawReason;
   private final PriceFormat priceFormat;
   private final EnumMap<ItemType, BigDecimal> minPrices;
   private final EnumMap<ItemType, BigDecimal> maxPrices;
   private final boolean autoClaim;
   private final boolean mustBeOnline;
   private final TaxConfiguration taxConfiguration;

   public ZAuctionEconomy(AuctionPlugin var1, CurrencyProvider var2, String var3, String var4, String var5, String var6, String var7, String var8, String var9, PriceFormat var10, EnumMap<ItemType, BigDecimal> var11, EnumMap<ItemType, BigDecimal> var12, boolean var13, boolean var14, TaxConfiguration var15) {
      this.plugin = var1;
      this.currencyProvider = var2;
      this.name = var3;
      this.displayName = var4;
      this.format = var5;
      this.symbol = var6;
      this.permission = var7;
      this.depositReason = var8;
      this.withdrawReason = var9;
      this.priceFormat = var10;
      this.minPrices = var11;
      this.maxPrices = var12;
      this.autoClaim = var13;
      this.mustBeOnline = var14;
      this.taxConfiguration = (TaxConfiguration)(var15 != null ? var15 : ZTaxConfiguration.disabled());
   }

   public AuctionPlugin getPlugin() {
      return this.plugin;
   }

   public CurrencyProvider getCurrencyProvider() {
      return this.currencyProvider;
   }

   public String getName() {
      return this.name;
   }

   public String getDisplayName() {
      return this.displayName;
   }

   public String getFormat() {
      return this.format;
   }

   public CompletableFuture<BigDecimal> get(UUID var1) {
      return CompletableFuture.completedFuture(this.currencyProvider.getBalance(var1));
   }

   public CompletableFuture<Boolean> has(UUID var1, BigDecimal var2) {
      return this.get(var1).thenApply((var1x) -> var1x.compareTo(var2) >= 0);
   }

   public void deposit(UUID var1, BigDecimal var2, String var3) {
      this.currencyProvider.deposit(var1, var2, var3);
   }

   public void withdraw(UUID var1, BigDecimal var2, String var3) {
      this.currencyProvider.withdraw(var1, var2, var3);
   }

   public String getSymbol() {
      return this.symbol;
   }

   public @Nullable String getPermission() {
      return this.permission;
   }

   public PriceFormat getPriceFormat() {
      return this.priceFormat;
   }

   public String getDepositReason() {
      return this.depositReason;
   }

   public String getWithdrawReason() {
      return this.withdrawReason;
   }

   public boolean isAutoClaim() {
      return this.autoClaim;
   }

   public boolean mustBeOnline() {
      return this.mustBeOnline;
   }

   public BigDecimal getMaxPrice(ItemType var1) {
      return (BigDecimal)this.maxPrices.get(var1);
   }

   public BigDecimal getMinPrice(ItemType var1) {
      return (BigDecimal)this.minPrices.get(var1);
   }

   public TaxConfiguration getTaxConfiguration() {
      return this.taxConfiguration;
   }
}
