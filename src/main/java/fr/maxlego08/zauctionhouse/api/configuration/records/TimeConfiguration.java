package fr.maxlego08.zauctionhouse.api.configuration.records;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import org.bukkit.configuration.file.FileConfiguration;

public record TimeConfiguration(String second, String seconds, String minute, String minutes, String hour, String hours, String day, String days, String timeDay, String timeHour, String timeMinute, String timeSecond) {
   public static TimeConfiguration of(AuctionPlugin var0, FileConfiguration var1) {
      String var2 = getOrDefault(var0, var1, "time.second", "second");
      String var3 = getOrDefault(var0, var1, "time.seconds", "seconds");
      String var4 = getOrDefault(var0, var1, "time.minute", "minute");
      String var5 = getOrDefault(var0, var1, "time.minutes", "minutes");
      String var6 = getOrDefault(var0, var1, "time.hour", "hour");
      String var7 = getOrDefault(var0, var1, "time.hours", "hours");
      String var8 = getOrDefault(var0, var1, "time.day", "day");
      String var9 = getOrDefault(var0, var1, "time.days", "days");
      String var10 = getOrDefault(var0, var1, "time.time-day", "%02dᴅ %02dʜ %02dᴍ");
      String var11 = getOrDefault(var0, var1, "time.time-hour", "%02dʜ %02dᴍ %02ds");
      String var12 = getOrDefault(var0, var1, "time.time-minute", "%02dᴍ %02ds");
      String var13 = getOrDefault(var0, var1, "time.time-second", "%02ds");
      return new TimeConfiguration(var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13);
   }

   private static String getOrDefault(AuctionPlugin var0, FileConfiguration var1, String var2, String var3) {
      String var4 = var1.getString(var2, var3);
      if (var4 != null && !var4.isEmpty()) {
         return var4;
      } else {
         var0.getLogger().severe("The time key '" + var2 + "' is null or empty! You need to fix that in config.yml.");
         return var3;
      }
   }

   public String getFormatLongDays(long var1) {
      TimeParts var3 = TimeConfiguration.TimeParts.fromMillis(var1);
      String var4 = this.applyUnitLabels(this.timeDay, var3);
      var4 = String.format(var4, var3.days(), var3.hours(), var3.minutes(), var3.seconds());
      return this.format(var4);
   }

   public String getFormatLongHours(long var1) {
      TimeParts var3 = TimeConfiguration.TimeParts.fromMillis(var1);
      String var4 = this.applyUnitLabels(this.timeHour, var3);
      var4 = String.format(var4, var3.hours(), var3.minutes(), var3.seconds());
      return this.format(var4);
   }

   public String getFormatLongMinutes(long var1) {
      TimeParts var3 = TimeConfiguration.TimeParts.fromMillis(var1);
      String var4 = this.applyUnitLabels(this.timeMinute, var3);
      var4 = String.format(var4, var3.minutes(), var3.seconds());
      return this.format(var4);
   }

   public String getFormatLongSecondes(long var1) {
      TimeParts var3 = TimeConfiguration.TimeParts.fromMillis(var1);
      String var4 = this.applyUnitLabels(this.timeSecond, var3);
      var4 = String.format(var4, var3.seconds());
      return this.format(var4);
   }

   public String getStringTime(long var1) {
      if (var1 < 60000L) {
         return this.getFormatLongSecondes(var1);
      } else if (var1 < 3600000L) {
         return this.getFormatLongMinutes(var1);
      } else {
         return var1 < 86400000L ? this.getFormatLongHours(var1) : this.getFormatLongDays(var1);
      }
   }

   public String format(String var1) {
      var1 = var1.replace(" 00 " + this.second, "");
      var1 = var1.replace(" 00 " + this.hour, "");
      var1 = var1.replace(" 00 " + this.day, "");
      var1 = var1.replace(" 00 " + this.minute, "");
      return var1.trim();
   }

   private String applyUnitLabels(String var1, TimeParts var2) {
      String var3 = var1.replace("%second%", var2.seconds() <= 1L ? this.second : this.seconds);
      var3 = var3.replace("%minute%", var2.minutes() <= 1L ? this.minute : this.minutes);
      var3 = var3.replace("%hour%", var2.hours() <= 1L ? this.hour : this.hours);
      var3 = var3.replace("%day%", var2.days() <= 1L ? this.day : this.days);
      return var3;
   }

   private static record TimeParts(long days, long hours, long minutes, long seconds) {
      static TimeParts fromMillis(long var0) {
         long var2 = var0 / 1000L;
         long var4 = var2 / 86400L;
         long var6 = var2 % 86400L / 3600L;
         long var8 = var2 % 3600L / 60L;
         long var10 = var2 % 60L;
         return new TimeParts(var4, var6, var8, var10);
      }
   }
}
