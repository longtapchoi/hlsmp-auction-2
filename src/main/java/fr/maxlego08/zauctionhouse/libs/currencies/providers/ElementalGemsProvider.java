package fr.maxlego08.zauctionhouse.libs.currencies.providers;

import fr.maxlego08.zauctionhouse.libs.currencies.CurrencyProvider;
import java.math.BigDecimal;
import java.util.UUID;
import me.elementalgaming.ElementalGems.GemAPI;

public class ElementalGemsProvider implements CurrencyProvider {
   public void deposit(UUID var1, BigDecimal var2, String var3) {
      GemAPI.addGems(var1, var2.doubleValue());
   }

   public void withdraw(UUID var1, BigDecimal var2, String var3) {
      GemAPI.removeGems(var1, var2.doubleValue());
   }

   public BigDecimal getBalance(UUID var1) {
      return BigDecimal.valueOf(GemAPI.getGems(var1));
   }
}
