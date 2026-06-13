package fr.maxlego08.zauctionhouse.libs.currencies.providers;

import dev.unnm3d.rediseconomy.api.RedisEconomyAPI;
import dev.unnm3d.rediseconomy.currency.Currency;
import fr.maxlego08.zauctionhouse.libs.currencies.CurrencyProvider;
import java.math.BigDecimal;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class RedisEconomyProvider implements CurrencyProvider {
   private final String economyName;

   public RedisEconomyProvider(String var1) {
      this.economyName = var1;
   }

   private Currency getCurrency() {
      RedisEconomyAPI var1 = RedisEconomyAPI.getAPI();
      if (var1 == null) {
         Bukkit.getLogger().info("RedisEconomyAPI not found!");
         return null;
      } else {
         Currency var2 = var1.getCurrencyByName(this.economyName);
         if (var2 == null) {
            Bukkit.getLogger().info("Currency " + this.economyName + " not found!");
         }

         return var2;
      }
   }

   public void deposit(UUID var1, BigDecimal var2, String var3) {
      Currency var4 = this.getCurrency();
      if (var4 != null) {
         OfflinePlayer var5 = Bukkit.getOfflinePlayer(var1);
         var4.depositPlayer(var5, var2.doubleValue());
      }

   }

   public void withdraw(UUID var1, BigDecimal var2, String var3) {
      Currency var4 = this.getCurrency();
      if (var4 != null) {
         OfflinePlayer var5 = Bukkit.getOfflinePlayer(var1);
         var4.withdrawPlayer(var5, var2.doubleValue());
      }

   }

   public BigDecimal getBalance(UUID var1) {
      Currency var2 = this.getCurrency();
      if (var2 != null) {
         OfflinePlayer var3 = Bukkit.getOfflinePlayer(var1);
         return BigDecimal.valueOf(var2.getBalance(var3));
      } else {
         return BigDecimal.ZERO;
      }
   }
}
