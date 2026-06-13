package fr.maxlego08.zauctionhouse.libs.currencies.providers;

import fr.maxlego08.zauctionhouse.libs.currencies.CurrencyProvider;
import java.math.BigDecimal;
import java.util.UUID;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultProvider implements CurrencyProvider {
   private Economy economy;

   private Economy getEconomy() {
      if (this.economy == null) {
         RegisteredServiceProvider var1 = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
         if (var1 != null) {
            this.economy = (Economy)var1.getProvider();
            return this.economy;
         } else {
            throw new NullPointerException("Vault Economy interface not found");
         }
      } else {
         return this.economy;
      }
   }

   public void deposit(UUID var1, BigDecimal var2, String var3) {
      OfflinePlayer var4 = Bukkit.getOfflinePlayer(var1);
      this.getEconomy().depositPlayer(var4, var2.doubleValue());
   }

   public void withdraw(UUID var1, BigDecimal var2, String var3) {
      OfflinePlayer var4 = Bukkit.getOfflinePlayer(var1);
      this.getEconomy().withdrawPlayer(var4, var2.doubleValue());
   }

   public BigDecimal getBalance(UUID var1) {
      OfflinePlayer var2 = Bukkit.getOfflinePlayer(var1);
      return BigDecimal.valueOf(this.getEconomy().getBalance(var2));
   }
}
