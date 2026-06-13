package fr.maxlego08.zauctionhouse.libs.currencies.providers;

import com.willfp.ecobits.currencies.Currencies;
import com.willfp.ecobits.currencies.Currency;
import com.willfp.ecobits.currencies.CurrencyUtils;
import fr.maxlego08.zauctionhouse.libs.currencies.CurrencyProvider;
import java.math.BigDecimal;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class EcoBitProvider implements CurrencyProvider {
   private Currency currency;
   private final String currencyName;

   public EcoBitProvider(String var1) {
      this.currencyName = var1;
   }

   private void initialize() {
      if (this.currency == null) {
         this.currency = Currencies.getByID(this.currencyName);
      }

   }

   public void deposit(UUID var1, BigDecimal var2, String var3) {
      this.initialize();
      OfflinePlayer var4 = Bukkit.getOfflinePlayer(var1);
      CurrencyUtils.adjustBalance(var4, this.currency, var2);
   }

   public void withdraw(UUID var1, BigDecimal var2, String var3) {
      this.initialize();
      OfflinePlayer var4 = Bukkit.getOfflinePlayer(var1);
      CurrencyUtils.adjustBalance(var4, this.currency, var2.negate());
   }

   public BigDecimal getBalance(UUID var1) {
      this.initialize();
      OfflinePlayer var2 = Bukkit.getOfflinePlayer(var1);
      return CurrencyUtils.getBalance(var2, this.currency);
   }
}
