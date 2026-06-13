package fr.maxlego08.zauctionhouse.libs.currencies.providers;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.economy.Economy;
import fr.maxlego08.essentials.api.economy.EconomyManager;
import fr.maxlego08.zauctionhouse.libs.currencies.CurrencyProvider;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.Bukkit;

public class ZEssentialsProvider implements CurrencyProvider {
   private EconomyManager economyManager;
   private Economy economy;
   private final String economyName;

   public ZEssentialsProvider(String var1) {
      this.economyName = var1;
   }

   private void initialize() {
      if (this.economyManager == null || this.economy == null) {
         EssentialsPlugin var1 = (EssentialsPlugin)Bukkit.getPluginManager().getPlugin("zEssentials");
         this.economyManager = var1.getEconomyManager();
         Optional var2 = this.economyManager.getEconomy(this.economyName);
         if (!var2.isPresent()) {
            throw new NullPointerException("ZEssentials economy " + this.economyName + " not found");
         }

         this.economy = (Economy)var2.get();
      }

   }

   public void deposit(UUID var1, BigDecimal var2, String var3) {
      this.initialize();
      this.economyManager.deposit(var1, this.economy, var2, var3);
   }

   public void withdraw(UUID var1, BigDecimal var2, String var3) {
      this.initialize();
      this.economyManager.withdraw(var1, this.economy, var2, var3);
   }

   public BigDecimal getBalance(UUID var1) {
      this.initialize();
      return this.economyManager.getBalance(Bukkit.getOfflinePlayer(var1), this.economy);
   }
}
