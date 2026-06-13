package fr.maxlego08.zauctionhouse.command.commands.admin.cache;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCacheKey;
import fr.maxlego08.zauctionhouse.api.command.CommandType;
import fr.maxlego08.zauctionhouse.api.command.VCommand;
import fr.maxlego08.zauctionhouse.api.messages.Message;
import fr.maxlego08.zauctionhouse.api.utils.Permission;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Stream;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CommandAuctionAdminCacheClear extends VCommand {
   public CommandAuctionAdminCacheClear(AuctionPlugin var1) {
      super(var1);
      this.setPermission(Permission.ZAUCTIONHOUSE_ADMIN);
      this.setDescription(Message.COMMAND_DESCRIPTION_AUCTION_ADMIN_CACHE_CLEAR);
      this.setConsoleCanUse(true);
      this.addSubCommand("clear");
      this.addRequireArg("player", (var0, var1x) -> {
         ArrayList<String> var2 = new ArrayList<>();
         var2.add("*");
         Bukkit.getOnlinePlayers().forEach((var1p) -> var2.add(var1p.getName()));
         return var2;
      });
      this.addOptionalArg("key", (var0, var1x) -> {
         ArrayList<String> var2 = new ArrayList<>();
         var2.add("all");
         Stream<String> var10000 = Arrays.stream(PlayerCacheKey.values()).map(Enum::name);
         Objects.requireNonNull(var2);
         var10000.forEach(var2::add);
         return var2;
      });
   }

   protected CommandType perform(AuctionPlugin var1) {
      String var2 = this.argAsString(0);
      if (var2 == null) {
         return CommandType.SYNTAX_ERROR;
      } else {
         String var3 = this.argAsString(1);
         if (var2.equals("*")) {
            Collection<? extends Player> var11 = Bukkit.getOnlinePlayers();
            int var12 = var11.size();
            if (var3 != null && !var3.equalsIgnoreCase("all")) {
               PlayerCacheKey var13;
               try {
                  var13 = PlayerCacheKey.valueOf(var3.toUpperCase());
               } catch (IllegalArgumentException var9) {
                  this.message(this.plugin, this.sender, Message.ADMIN_CACHE_INVALID_KEY, new Object[]{"%key%", var3});
                  return CommandType.DEFAULT;
               }

               for(Player var8 : var11) {
                  this.auctionManager.clearPlayerCache(var8, var13);
               }

               this.message(this.plugin, this.sender, Message.ADMIN_CACHE_CLEARED_ALL_PLAYERS, new Object[]{"%key%", var13.name(), "%count%", String.valueOf(var12)});
            } else {
               for(Player var7 : var11) {
                  this.auctionManager.clearPlayerCache(var7, PlayerCacheKey.values());
               }

               this.message(this.plugin, this.sender, Message.ADMIN_CACHE_CLEARED_ALL_PLAYERS_ALL, new Object[]{"%count%", String.valueOf(var12)});
            }

            return CommandType.SUCCESS;
         } else {
            Player var4 = Bukkit.getPlayerExact(var2);
            if (var4 == null) {
               this.message(this.plugin, this.sender, Message.ADMIN_CACHE_PLAYER_NOT_ONLINE, new Object[]{"%player%", var2});
               return CommandType.DEFAULT;
            } else {
               if (var3 != null && !var3.equalsIgnoreCase("all")) {
                  PlayerCacheKey var5;
                  try {
                     var5 = PlayerCacheKey.valueOf(var3.toUpperCase());
                  } catch (IllegalArgumentException var10) {
                     this.message(this.plugin, this.sender, Message.ADMIN_CACHE_INVALID_KEY, new Object[]{"%key%", var3});
                     return CommandType.DEFAULT;
                  }

                  this.auctionManager.clearPlayerCache(var4, var5);
                  this.message(this.plugin, this.sender, Message.ADMIN_CACHE_CLEARED, new Object[]{"%key%", var5.name(), "%player%", var4.getName()});
               } else {
                  this.auctionManager.clearPlayerCache(var4, PlayerCacheKey.values());
                  this.message(this.plugin, this.sender, Message.ADMIN_CACHE_CLEARED_ALL, new Object[]{"%player%", var4.getName()});
               }

               return CommandType.SUCCESS;
            }
         }
      }
   }
}
