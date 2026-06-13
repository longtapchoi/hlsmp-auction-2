package fr.maxlego08.zauctionhouse.api.command;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public abstract class Arguments {
   protected String[] args;
   protected int parentCount = 0;

   protected String argAsString(int var1) {
      try {
         return this.args[var1 + this.parentCount];
      } catch (Exception var3) {
         return null;
      }
   }

   protected String argAsString(int var1, String var2) {
      if (var1 == -1) {
         return var2;
      } else {
         try {
            return this.args[var1 + this.parentCount];
         } catch (Exception var4) {
            return var2;
         }
      }
   }

   protected boolean argAsBoolean(int var1) {
      return Boolean.valueOf(this.argAsString(var1));
   }

   protected boolean argAsBoolean(int var1, boolean var2) {
      try {
         return Boolean.valueOf(this.argAsString(var1));
      } catch (Exception var4) {
         return var2;
      }
   }

   protected int argAsInteger(int var1) {
      return Integer.valueOf(this.argAsString(var1));
   }

   protected int argAsInteger(int var1, int var2) {
      try {
         return Integer.valueOf(this.argAsString(var1));
      } catch (Exception var4) {
         return var2;
      }
   }

   protected long argAsLong(int var1) {
      return Long.valueOf(this.argAsString(var1));
   }

   protected long argAsLong(int var1, long var2) {
      try {
         return Long.valueOf(this.argAsString(var1));
      } catch (Exception var5) {
         return var2;
      }
   }

   protected double argAsDouble(int var1) {
      return Double.valueOf(this.argAsString(var1).replace(",", "."));
   }

   protected double argAsDouble(int var1, double var2) {
      try {
         return Double.valueOf(this.argAsString(var1).replace(",", "."));
      } catch (Exception var5) {
         return var2;
      }
   }

   protected Player argAsPlayer(int var1) {
      return Bukkit.getPlayer(this.argAsString(var1));
   }

   protected Player argAsPlayer(int var1, Player var2) {
      try {
         return Bukkit.getPlayer(this.argAsString(var1));
      } catch (Exception var4) {
         return var2;
      }
   }

   protected OfflinePlayer argAsOfflinePlayer(int var1) {
      return Bukkit.getOfflinePlayer(this.argAsString(var1));
   }

   protected OfflinePlayer argAsOfflinePlayer(int var1, OfflinePlayer var2) {
      try {
         return Bukkit.getOfflinePlayer(this.argAsString(var1));
      } catch (Exception var4) {
         return var2;
      }
   }

   protected EntityType argAsEntityType(int var1) {
      return EntityType.valueOf(this.argAsString(var1).toUpperCase());
   }

   protected EntityType argAsEntityType(int var1, EntityType var2) {
      try {
         return EntityType.valueOf(this.argAsString(var1).toUpperCase());
      } catch (Exception var4) {
         return var2;
      }
   }

   protected World argAsWorld(int var1) {
      try {
         return Bukkit.getWorld(this.argAsString(var1));
      } catch (Exception var3) {
         return null;
      }
   }

   protected World argAsWorld(int var1, World var2) {
      try {
         return Bukkit.getWorld(this.argAsString(var1));
      } catch (Exception var4) {
         return var2;
      }
   }
}
