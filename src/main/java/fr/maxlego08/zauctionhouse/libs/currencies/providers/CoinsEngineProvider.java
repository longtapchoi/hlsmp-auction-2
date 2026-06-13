package fr.maxlego08.zauctionhouse.libs.currencies.providers;

import fr.maxlego08.zauctionhouse.libs.currencies.CurrencyProvider;
import java.math.BigDecimal;
import java.util.UUID;
import su.nightexpress.coinsengine.api.CoinsEngineAPI;

public class CoinsEngineProvider implements CurrencyProvider {
   private final String currencyName;

   public CoinsEngineProvider(String var1) {
      this.currencyName = var1;
   }

   public void deposit(UUID var1, BigDecimal var2, String var3) {
      CoinsEngineAPI.addBalance(var1, this.currencyName, var2.doubleValue());
   }

   public void withdraw(UUID var1, BigDecimal var2, String var3) {
      CoinsEngineAPI.removeBalance(var1, this.currencyName, var2.doubleValue());
   }

   public BigDecimal getBalance(UUID var1) {
      return BigDecimal.valueOf(CoinsEngineAPI.getBalance(var1, this.currencyName));
   }
}
