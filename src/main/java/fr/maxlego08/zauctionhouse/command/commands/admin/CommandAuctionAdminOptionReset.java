package fr.maxlego08.zauctionhouse.command.commands.admin;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.command.CommandType;
import fr.maxlego08.zauctionhouse.api.command.VCommand;
import fr.maxlego08.zauctionhouse.api.messages.Message;
import fr.maxlego08.zauctionhouse.api.utils.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CommandAuctionAdminOptionReset extends VCommand {
   public CommandAuctionAdminOptionReset(AuctionPlugin var1) {
      super(var1);
      this.setPermission(Permission.ZAUCTIONHOUSE_ADMIN);
      this.setDescription(Message.COMMAND_DESCRIPTION_AUCTION_ADMIN_OPTION_RESET);
      this.setConsoleCanUse(true);
      this.addSubCommand("reset");
      this.addRequireArg("player", (var0, var1x) -> Bukkit.getOnlinePlayers().stream().map(Player::getName).toList());
   }

   protected CommandType perform(AuctionPlugin var1) {
      String var2 = this.argAsString(0);
      if (var2 == null) {
         return CommandType.SYNTAX_ERROR;
      } else {
         Player var3 = Bukkit.getPlayerExact(var2);
         if (var3 == null) {
            this.message(var1, this.sender, Message.ADMIN_TARGET_NOT_FOUND, new Object[]{"%target%", var2});
            return CommandType.DEFAULT;
         } else {
            this.auctionManager.getOptionService().resetPlayerOptions(var3.getUniqueId());
            this.message(var1, this.sender, Message.ADMIN_OPTION_RESET, new Object[]{"%player%", var3.getName()});
            return CommandType.SUCCESS;
         }
      }
   }
}
