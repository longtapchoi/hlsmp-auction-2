package fr.maxlego08.zauctionhouse.api.configuration.records;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import org.bukkit.configuration.file.FileConfiguration;

public record SalesNotificationConfiguration(boolean enabled, long delayTicks) {
   public static SalesNotificationConfiguration of(AuctionPlugin var0, FileConfiguration var1) {
      boolean var2 = var1.getBoolean("sales-notification.enable", true);
      long var3 = var1.getLong("sales-notification.delay-ticks", 40L);
      return new SalesNotificationConfiguration(var2, var3);
   }
}
