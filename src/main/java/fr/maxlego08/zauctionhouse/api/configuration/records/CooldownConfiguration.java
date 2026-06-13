package fr.maxlego08.zauctionhouse.api.configuration.records;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public record CooldownConfiguration(long defaultCooldown, String bypassPermission, Map<String, Long> perCommandCooldowns) {
   public static final long DEFAULT_COOLDOWN = 100L;
   public static final String DEFAULT_BYPASS_PERMISSION = "zauctionhouse.cooldown.command.bypass";

   public static CooldownConfiguration of(AuctionPlugin var0, FileConfiguration var1) {
      long var2 = var1.getLong("commands.cooldown.default-cooldown", 100L);
      String var4 = var1.getString("commands.cooldown.bypass-permission", "zauctionhouse.cooldown.command.bypass");
      HashMap var5 = new HashMap();
      ConfigurationSection var6 = var1.getConfigurationSection("commands.cooldown.per-command");
      if (var6 != null) {
         for(String var8 : var6.getKeys(false)) {
            var5.put(var8.toLowerCase(), var6.getLong(var8));
         }
      }

      if (var2 == 0L && var5.isEmpty()) {
         var0.getLogger().warning("Command cooldown is disabled (set to 0). It is recommended to keep a cooldown (e.g., 100ms) to prevent command spam.");
      }

      return new CooldownConfiguration(var2, var4, var5);
   }

   public long getCooldownForCommand(String var1) {
      return (Long)this.perCommandCooldowns.getOrDefault(var1.toLowerCase(), this.defaultCooldown);
   }
}
