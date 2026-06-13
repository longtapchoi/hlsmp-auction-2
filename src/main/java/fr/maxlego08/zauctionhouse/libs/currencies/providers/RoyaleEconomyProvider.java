package fr.maxlego08.zauctionhouse.libs.currencies.providers;

import fr.maxlego08.zauctionhouse.libs.currencies.CurrencyProvider;
import java.math.BigDecimal;
import java.util.UUID;
import me.qKing12.RoyaleEconomy.API.Currency;
import me.qKing12.RoyaleEconomy.API.MultiCurrencyHandler;

public class RoyaleEconomyProvider implements CurrencyProvider {
   private Currency currency;
   private final String currencyId;

   public RoyaleEconomyProvider(String var1) {
      this.currencyId = var1;
   }

   private void initialize() {
      if (this.currency == null) {
         if (MultiCurrencyHandler.getCurrencies() == null) {
            throw new NullPointerException("RoyaleEconomy multi-currency not enabled.");
         } else {
            this.currency = MultiCurrencyHandler.findCurrencyById(this.currencyId);
            if (this.currency == null) {
               throw new NullPointerException("RoyaleEconomy currency " + this.currencyId + " not found");
            }
         }
      }
   }

   public void deposit(UUID var1, BigDecimal var2, String var3) {
      this.initialize();
      this.currency.addAmount(var1.toString(), var2.doubleValue());
   }

   public void withdraw(UUID var1, BigDecimal var2, String var3) {
      this.initialize();
      this.currency.removeAmount(var1.toString(), var2.doubleValue());
   }

   public BigDecimal getBalance(UUID var1) {
      this.initialize();
      return BigDecimal.valueOf(this.currency.getAmount(var1.toString()));
   }
}
