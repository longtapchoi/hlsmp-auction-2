package fr.maxlego08.zauctionhouse.command.commands.admin;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.command.CommandType;
import fr.maxlego08.zauctionhouse.api.command.VCommand;
import fr.maxlego08.zauctionhouse.api.messages.Message;
import fr.maxlego08.zauctionhouse.api.option.PlayerOption;
import fr.maxlego08.zauctionhouse.api.utils.Permission;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CommandAuctionAdminOptionSet extends VCommand {
   public CommandAuctionAdminOptionSet(AuctionPlugin var1) {
      super(var1);
      this.setPermission(Permission.ZAUCTIONHOUSE_ADMIN);
      this.setDescription(Message.COMMAND_DESCRIPTION_AUCTION_ADMIN_OPTION_SET);
      this.setConsoleCanUse(true);
      this.addSubCommand("set");
      this.addRequireArg("player", (var0, var1x) -> Bukkit.getOnlinePlayers().stream().map(Player::getName).toList());
      this.addRequireArg("option", (var0, var1x) -> Arrays.stream(PlayerOption.values()).map(PlayerOption::getKey).toList());
      this.addRequireArg("value", (var0, var1x) -> List.of("true", "false"));
   }

   protected CommandType perform(AuctionPlugin var1) {
      String var2 = this.argAsString(0);
      if (var2 == null) {
         return CommandType.SYNTAX_ERROR;
      } else {
         String var3 = this.argAsString(1);
         if (var3 == null) {
            return CommandType.SYNTAX_ERROR;
         } else {
            String var4 = this.argAsString(2);
            if (var4 == null) {
               return CommandType.SYNTAX_ERROR;
            } else {
               Player var5 = Bukkit.getPlayerExact(var2);
               if (var5 == null) {
                  this.message(var1, this.sender, Message.ADMIN_TARGET_NOT_FOUND, new Object[]{"%target%", var2});
                  return CommandType.DEFAULT;
               } else {
                  PlayerOption var6 = PlayerOption.fromKey(var3);
                  if (var6 == null) {
                     this.message(var1, this.sender, Message.ADMIN_OPTION_SET, new Object[]{"%option%", var3, "%value%", "unknown", "%player%", var2});
                     return CommandType.DEFAULT;
                  } else {
                     boolean var7 = Boolean.parseBoolean(var4);
                     this.auctionManager.getOptionService().setOption(var5.getUniqueId(), var6, var7);
                     this.message(var1, this.sender, Message.ADMIN_OPTION_SET, new Object[]{"%option%", var6.getKey(), "%value%", String.valueOf(var7), "%player%", var5.getName()});
                     return CommandType.SUCCESS;
                  }
               }
            }
         }
      }
   }
}
