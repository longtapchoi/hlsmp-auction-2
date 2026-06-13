package fr.maxlego08.zauctionhouse.libs.currencies.providers;

import fr.maxlego08.zauctionhouse.libs.currencies.CurrencyProvider;
import java.math.BigDecimal;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ExperienceProvider implements CurrencyProvider {
   public void deposit(UUID var1, BigDecimal var2, String var3) {
      Player var4 = Bukkit.getPlayer(var1);
      if (var4 != null) {
         BigDecimal var5 = BigDecimal.valueOf((long)this.getTotalExperience(var4));
         this.setTotalExperience(var4, var5.add(var2).intValue());
      }

   }

   public void withdraw(UUID var1, BigDecimal var2, String var3) {
      Player var4 = Bukkit.getPlayer(var1);
      if (var4 != null) {
         BigDecimal var5 = BigDecimal.valueOf((long)this.getTotalExperience(var4));
         BigDecimal var6 = var5.subtract(var2);
         this.setTotalExperience(var4, var6.max(BigDecimal.ZERO).intValue());
      }

   }

   public BigDecimal getBalance(UUID var1) {
      Player var2 = Bukkit.getPlayer(var1);
      return var2 != null ? BigDecimal.valueOf((long)this.getTotalExperience(var2)) : BigDecimal.ZERO;
   }

   private void setTotalExperience(Player var1, int var2) {
      if (var2 < 0) {
         throw new IllegalArgumentException("Experience is negative!");
      } else {
         var1.setExp(0.0F);
         var1.setLevel(0);
         var1.setTotalExperience(0);
         int var3 = var2;

         while(var3 > 0) {
            int var4 = this.getExpAtLevel(var1);
            var3 -= var4;
            if (var3 >= 0) {
               var1.giveExp(var4);
            } else {
               var3 += var4;
               var1.giveExp(var3);
               var3 = 0;
            }
         }

      }
   }

   private int getExpAtLevel(Player var1) {
      return this.getExpAtLevel(var1.getLevel());
   }

   private int getExpAtLevel(int var1) {
      if (var1 <= 15) {
         return 2 * var1 + 7;
      } else {
         return var1 <= 30 ? 5 * var1 - 38 : 9 * var1 - 158;
      }
   }

   private int getTotalExperience(Player var1) {
      int var2 = Math.round((float)this.getExpAtLevel(var1) * var1.getExp());

      for(int var3 = var1.getLevel(); var3 > 0; var2 += this.getExpAtLevel(var3)) {
         --var3;
      }

      if (var2 < 0) {
         var2 = Integer.MAX_VALUE;
      }

      return var2;
   }
}
