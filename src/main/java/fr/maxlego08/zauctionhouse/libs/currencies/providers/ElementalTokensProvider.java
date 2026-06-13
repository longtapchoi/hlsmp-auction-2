package fr.maxlego08.zauctionhouse.libs.currencies.providers;

import fr.maxlego08.zauctionhouse.libs.currencies.CurrencyProvider;
import java.math.BigDecimal;
import java.util.UUID;
import me.elementalgaming.ElementalTokens.TokenAPI;

public class ElementalTokensProvider implements CurrencyProvider {
   public void deposit(UUID var1, BigDecimal var2, String var3) {
      TokenAPI.addTokens(var1, var2.longValue());
   }

   public void withdraw(UUID var1, BigDecimal var2, String var3) {
      TokenAPI.removeTokens(var1, var2.longValue());
   }

   public BigDecimal getBalance(UUID var1) {
      return BigDecimal.valueOf(TokenAPI.getTokens(var1));
   }
}
