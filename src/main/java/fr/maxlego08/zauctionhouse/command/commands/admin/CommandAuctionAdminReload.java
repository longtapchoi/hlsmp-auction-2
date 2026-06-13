package fr.maxlego08.zauctionhouse.command.commands.admin;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.command.CommandType;
import fr.maxlego08.zauctionhouse.api.command.VCommand;
import fr.maxlego08.zauctionhouse.api.messages.Message;
import fr.maxlego08.zauctionhouse.api.utils.Permission;

public class CommandAuctionAdminReload extends VCommand {
   public CommandAuctionAdminReload(AuctionPlugin var1) {
      super(var1);
      this.addSubCommand(new String[]{"reload", "rl"});
      this.setPermission(Permission.ZAUCTIONHOUSE_RELOAD);
      this.setDescription(Message.COMMAND_DESCRIPTION_AUCTION_RELOAD);
   }

   protected CommandType perform(AuctionPlugin var1) {
      var1.reload();
      this.message(var1, this.sender, Message.RELOAD_SUCCESS, new Object[0]);
      return CommandType.SUCCESS;
   }
}
