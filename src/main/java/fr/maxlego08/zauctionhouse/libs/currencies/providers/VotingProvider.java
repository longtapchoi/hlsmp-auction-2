package fr.maxlego08.zauctionhouse.libs.currencies.providers;

import com.bencodez.votingplugin.VotingPluginHooks;
import com.bencodez.votingplugin.user.UserManager;
import fr.maxlego08.zauctionhouse.libs.currencies.CurrencyProvider;
import java.math.BigDecimal;
import java.util.UUID;

public class VotingProvider implements CurrencyProvider {
   private final UserManager userManager = VotingPluginHooks.getInstance().getUserManager();

   public void deposit(UUID var1, BigDecimal var2, String var3) {
      this.userManager.getVotingPluginUser(var1).addPoints(var2.intValue());
   }

   public void withdraw(UUID var1, BigDecimal var2, String var3) {
      this.userManager.getVotingPluginUser(var1).removePoints(var2.intValue());
   }

   public BigDecimal getBalance(UUID var1) {
      return BigDecimal.valueOf((long)this.userManager.getVotingPluginUser(var1).getPoints());
   }
}
