package fr.maxlego08.zauctionhouse.api.configuration.records;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import java.util.logging.Logger;
import org.bukkit.SoundCategory;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public record SoundConfiguration(boolean enable, SoundCategory category, String sound, float volume, float pitch) {
   public static SoundConfiguration of(AuctionPlugin var0, FileConfiguration var1, String var2) {
      Logger var3 = var0.getLogger();
      SoundCategory var4 = SoundCategory.MASTER;

      try {
         var4 = SoundCategory.valueOf(var1.getString(var2 + "category", "MASTER"));
      } catch (Exception var8) {
         var3.warning("The noMoneySound category is not valid for '" + var2 + "' !, you need to fix that ");
      }

      String var5 = var1.getString(var2 + "sound", "minecraft:entity.villager.no");
      float var6 = (float)var1.getDouble(var2 + "volume");
      float var7 = (float)var1.getDouble(var2 + "pitch");
      return new SoundConfiguration(var1.getBoolean(var2 + "enable"), var4, var5, var6, var7);
   }

   public void play(Player var1) {
      if (this.enable) {
         var1.playSound(var1.getLocation(), this.sound, this.category, this.volume, this.pitch);
      }
   }
}
