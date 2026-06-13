package fr.maxlego08.zauctionhouse.libs.currencies.providers;

import fr.maxlego08.zauctionhouse.libs.currencies.CurrencyProvider;
import java.math.BigDecimal;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class LevelProvider implements CurrencyProvider {
   public void deposit(UUID var1, BigDecimal var2, String var3) {
      Player var4 = Bukkit.getPlayer(var1);
      if (var4 != null) {
         int var5 = var4.getLevel();
         var4.setLevel(var5 + var2.intValue());
      }

   }

   public void withdraw(UUID var1, BigDecimal var2, String var3) {
      Player var4 = Bukkit.getPlayer(var1);
      if (var4 != null) {
         int var5 = var4.getLevel();
         var4.setLevel(var5 - var2.intValue());
      }

   }

   public BigDecimal getBalance(UUID var1) {
      Player var2 = Bukkit.getPlayer(var1);
      return BigDecimal.valueOf(var2 != null ? (long)var2.getLevel() : 0L);
   }
}
