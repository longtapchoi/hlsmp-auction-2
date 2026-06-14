package fr.maxlego08.zauctionhouse.api.configuration.records;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import org.bukkit.configuration.file.FileConfiguration;

public record AutoClaimConfiguration(boolean enabled, long delayTicks, boolean notifyPlayer, boolean notifyPending, long notifyDelayTicks, String depositReason) {
   public static AutoClaimConfiguration of(AuctionPlugin var0, FileConfiguration var1) {
      boolean var2 = var1.getBoolean("auto-claim.enable", false);
      long var3 = var1.getLong("auto-claim.delay-ticks", 20L);
      boolean var5 = var1.getBoolean("auto-claim.notify-player", true);
      boolean var6 = var1.getBoolean("auto-claim.notify-pending", true);
      long var7 = var1.getLong("auto-claim.notify-delay-ticks", 40L);
      String var9 = var1.getString("auto-claim.deposit-reason", "Claimed pending auction money");
      return new AutoClaimConfiguration(var2, var3, var5, var6, var7, var9);
   }
}
