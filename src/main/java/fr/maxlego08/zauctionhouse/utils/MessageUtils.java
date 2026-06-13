package fr.maxlego08.zauctionhouse.utils;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.component.ComponentMessage;
import fr.maxlego08.zauctionhouse.api.messages.DefaultFontInfo;
import fr.maxlego08.zauctionhouse.api.messages.Message;
import fr.maxlego08.zauctionhouse.api.messages.MessageType;
import fr.maxlego08.zauctionhouse.api.messages.messages.BossBarMessage;
import fr.maxlego08.zauctionhouse.api.messages.messages.ClassicMessage;
import fr.maxlego08.zauctionhouse.api.messages.messages.TitleMessage;
import fr.maxlego08.zauctionhouse.utils.component.ComponentMessageHelper;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class MessageUtils extends PlaceholderUtils {
   protected final ComponentMessage componentMessage;

   public MessageUtils() {
      this.componentMessage = ComponentMessageHelper.componentMessage;
   }

   public static String getString(String var0, Object[] var1) {
      if (var1.length % 2 != 0) {
         throw new IllegalArgumentException("Number of invalid arguments. Arguments must be in pairs.");
      } else {
         for(int var2 = 0; var2 < var1.length; var2 += 2) {
            if (var1[var2] != null && var1[var2 + 1] != null) {
               var0 = var0.replace(var1[var2].toString(), var1[var2 + 1].toString());
            }
         }

         return var0;
      }
   }

   protected void message(CommandSender var1, String var2) {
      this.componentMessage.sendMessage(var1, var2);
   }

   protected void message(AuctionPlugin var1, CommandSender var2, Message var3, Object... var4) {
      if (var2 != null) {
         if (var2 instanceof Player) {
            Player var5 = (Player)var2;
            var3.getMessages().forEach((var5x) -> {
               if (var5x instanceof ClassicMessage) {
                  ClassicMessage var6 = (ClassicMessage)var5x;
                  switch (var5x.messageType()) {
                     case TCHAT:
                     case WITHOUT_PREFIX:
                        this.sendTchatMessage(var2, var6, var4);
                        break;
                     case ACTION:
                        var6.messages().forEach((var3) -> this.componentMessage.sendActionBar(var5, this.papi(getString(var3, var4), var5)));
                        break;
                     case CENTER:
                        var6.messages().forEach((var4x) -> this.componentMessage.sendMessage(var2, this.getCenteredMessage(this.papi(getString(var4x, var4), var5))));
                  }
               } else if (var5x instanceof BossBarMessage) {
                  BossBarMessage var7 = (BossBarMessage)var5x;
                  this.componentMessage.sendBossBar(var1, var5, var7);
               } else if (var5x instanceof TitleMessage) {
                  TitleMessage var8 = (TitleMessage)var5x;
                  this.componentMessage.sendTitle(var5, var8);
               }

            });
         } else {
            var3.getMessages().forEach((var3x) -> {
               if (var3x instanceof ClassicMessage var4x) {
                  this.sendTchatMessage(var2, var4x, var4);
               }

            });
         }

      }
   }

   private void sendTchatMessage(CommandSender var1, ClassicMessage var2, Object... var3) {
      boolean var4 = var2.messageType() == MessageType.WITHOUT_PREFIX || var2.messages().size() > 1;
      var2.messages().forEach((var4x) -> {
         String var10002 = var4 ? "" : Message.PREFIX.getMessageAsString();
         this.componentMessage.sendMessage(var1, var10002 + this.papi(getString(var4x, var3), var1));
      });
   }

   protected String getMessage(Message var1, Object... var2) {
      return getString(String.join("\n", var1.getMessageAsStringList()), var2);
   }

   protected String args(String var1, Object... var2) {
      return getString(var1, var2);
   }

   protected String getCenteredMessage(String var1) {
      if (var1 != null && !var1.equals("")) {
         short var2 = 154;
         var1 = ChatColor.translateAlternateColorCodes('&', var1);
         int var12 = 0;
         boolean var4 = false;
         boolean var5 = false;

         for(char var9 : var1.toCharArray()) {
            if (var9 == 167) {
               var4 = true;
            } else if (var4) {
               var4 = false;
               var5 = var9 == 'l' || var9 == 'L';
            } else {
               DefaultFontInfo var10 = DefaultFontInfo.getDefaultFontInfo(var9);
               var12 += var5 ? var10.getBoldLength() : var10.getLength();
               ++var12;
            }
         }

         int var13 = var12 / 2;
         int var14 = var2 - var13;
         int var15 = DefaultFontInfo.SPACE.getLength() + 1;
         int var16 = 0;

         StringBuilder var17;
         for(var17 = new StringBuilder(); var16 < var14; var16 += var15) {
            var17.append(" ");
         }

         String var10000 = String.valueOf(var17);
         return var10000 + var1;
      } else {
         return "";
      }
   }
}
