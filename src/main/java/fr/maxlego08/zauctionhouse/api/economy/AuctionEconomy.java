package fr.maxlego08.zauctionhouse.api.economy;

import fr.maxlego08.zauctionhouse.api.item.ItemType;
import fr.maxlego08.zauctionhouse.api.tax.TaxConfiguration;
import fr.maxlego08.zauctionhouse.api.tax.TaxResult;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface AuctionEconomy {
   String getName();

   String getDisplayName();

   String getSymbol();

   String getFormat();

   default String format(String priceAsString, long amount) {
      return this.getFormat().replace("%price%", priceAsString).replace("%s%", amount > 1L ? "s" : "");
   }

   CompletableFuture<BigDecimal> get(UUID var1);

   CompletableFuture<Boolean> has(UUID var1, BigDecimal var2);

   default boolean hasSync(Player player, BigDecimal price) {
      return (Boolean)this.has(player.getUniqueId(), price).join();
   }

   void deposit(UUID var1, BigDecimal var2, String var3);

   void withdraw(UUID var1, BigDecimal var2, String var3);

   String getDepositReason();

   String getWithdrawReason();

   @Nullable String getPermission();

   PriceFormat getPriceFormat();

   boolean isAutoClaim();

   boolean mustBeOnline();

   BigDecimal getMaxPrice(ItemType var1);

   BigDecimal getMinPrice(ItemType var1);

   TaxConfiguration getTaxConfiguration();

   default TaxResult calculateSellTax(Player player, BigDecimal price, ItemStack itemStack) {
      return this.getTaxConfiguration().calculateSellTax(player, price, itemStack);
   }

   default TaxResult calculatePurchaseTax(Player player, BigDecimal price, ItemStack itemStack) {
      return this.getTaxConfiguration().calculatePurchaseTax(player, price, itemStack);
   }
}
