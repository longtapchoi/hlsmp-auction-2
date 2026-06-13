package fr.maxlego08.zauctionhouse.libs.currencies.providers;

import fr.maxlego08.zauctionhouse.libs.currencies.CurrencyProvider;
import java.math.BigDecimal;
import java.util.UUID;
import me.mraxetv.beasttokens.api.BeastTokensAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class BeastTokenProvider implements CurrencyProvider {
   public void deposit(UUID var1, BigDecimal var2, String var3) {
      OfflinePlayer var4 = Bukkit.getOfflinePlayer(var1);
      if (var4.isOnline()) {
         BeastTokensAPI.getTokensManager().addTokens(var4.getPlayer(), var2.doubleValue());
      } else {
         BeastTokensAPI.getTokensManager().addTokens(var4, var2.doubleValue());
      }

   }

   public void withdraw(UUID var1, BigDecimal var2, String var3) {
      OfflinePlayer var4 = Bukkit.getOfflinePlayer(var1);
      if (var4.isOnline()) {
         BeastTokensAPI.getTokensManager().removeTokens(var4.getPlayer(), var2.doubleValue());
      } else {
         BeastTokensAPI.getTokensManager().removeTokens(var4, var2.doubleValue());
      }

   }

   public BigDecimal getBalance(UUID var1) {
      OfflinePlayer var2 = Bukkit.getOfflinePlayer(var1);
      return BigDecimal.valueOf(var2.isOnline() ? BeastTokensAPI.getTokensManager().getTokens(var2.getPlayer()) : BeastTokensAPI.getTokensManager().getTokens(var2));
   }
}
