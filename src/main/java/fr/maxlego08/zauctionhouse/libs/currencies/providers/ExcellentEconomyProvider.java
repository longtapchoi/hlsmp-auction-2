package fr.maxlego08.zauctionhouse.libs.currencies.providers;

import fr.maxlego08.zauctionhouse.libs.currencies.CurrencyProvider;
import java.math.BigDecimal;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import su.nightexpress.excellenteconomy.api.ExcellentEconomyAPI;
import su.nightexpress.excellenteconomy.api.currency.operation.OperationContext;

public class ExcellentEconomyProvider implements CurrencyProvider {
   private final ExcellentEconomyAPI api;
   private final String currencyName;

   public ExcellentEconomyProvider(String var1) {
      this.currencyName = var1;
      RegisteredServiceProvider var2 = Bukkit.getServer().getServicesManager().getRegistration(ExcellentEconomyAPI.class);
      if (var2 == null) {
         throw new IllegalStateException("ExcellentEconomy service not registered");
      } else {
         this.api = (ExcellentEconomyAPI)var2.getProvider();
      }
   }

   public void deposit(UUID var1, BigDecimal var2, String var3) {
      OperationContext var4 = OperationContext.custom(var3);
      Player var5 = Bukkit.getPlayer(var1);
      if (var5 != null) {
         this.api.deposit(var5, this.currencyName, var2.doubleValue(), var4);
      } else {
         this.api.depositAsync(var1, this.currencyName, var2.doubleValue(), var4);
      }

   }

   public void withdraw(UUID var1, BigDecimal var2, String var3) {
      OperationContext var4 = OperationContext.custom(var3);
      Player var5 = Bukkit.getPlayer(var1);
      if (var5 != null) {
         this.api.withdraw(var5, this.currencyName, var2.doubleValue(), var4);
      } else {
         this.api.withdrawAsync(var1, this.currencyName, var2.doubleValue(), var4);
      }

   }

   public BigDecimal getBalance(UUID var1) {
      Player var2 = Bukkit.getPlayer(var1);
      double var3 = var2 != null ? this.api.getBalance(var2, this.currencyName) : (Double)this.api.getBalanceAsync(var1, this.currencyName).join();
      return BigDecimal.valueOf(var3);
   }
}
