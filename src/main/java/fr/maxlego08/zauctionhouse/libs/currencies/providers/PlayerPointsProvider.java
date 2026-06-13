package fr.maxlego08.zauctionhouse.libs.currencies.providers;

import fr.maxlego08.zauctionhouse.libs.currencies.CurrencyProvider;
import java.math.BigDecimal;
import java.util.UUID;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerPointsProvider implements CurrencyProvider {
   private PlayerPointsAPI playerPointsAPI;

   private PlayerPointsAPI getAPI() {
      if (this.playerPointsAPI == null) {
         PlayerPoints var1 = (PlayerPoints)JavaPlugin.getPlugin(PlayerPoints.class);
         this.playerPointsAPI = var1.getAPI();
      }

      return this.playerPointsAPI;
   }

   public void deposit(UUID var1, BigDecimal var2, String var3) {
      this.getAPI().give(var1, var2.intValue());
   }

   public void withdraw(UUID var1, BigDecimal var2, String var3) {
      this.getAPI().take(var1, var2.intValue());
   }

   public BigDecimal getBalance(UUID var1) {
      return BigDecimal.valueOf((long)this.getAPI().look(var1));
   }
}
