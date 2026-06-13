package fr.maxlego08.zauctionhouse.libs.currencies;

import java.math.BigDecimal;
import java.util.UUID;

public interface CurrencyProvider {
   void deposit(UUID var1, BigDecimal var2, String var3);

   void withdraw(UUID var1, BigDecimal var2, String var3);

   BigDecimal getBalance(UUID var1);
}
