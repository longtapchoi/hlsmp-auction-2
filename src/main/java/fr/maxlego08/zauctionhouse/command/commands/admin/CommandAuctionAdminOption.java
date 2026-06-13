package fr.maxlego08.zauctionhouse.command.commands.admin;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.command.CommandType;
import fr.maxlego08.zauctionhouse.api.command.VCommand;
import fr.maxlego08.zauctionhouse.api.messages.Message;
import fr.maxlego08.zauctionhouse.api.utils.Permission;

public class CommandAuctionAdminOption extends VCommand {
   public CommandAuctionAdminOption(AuctionPlugin var1) {
      super(var1);
      this.setPermission(Permission.ZAUCTIONHOUSE_ADMIN);
      this.setDescription(Message.COMMAND_DESCRIPTION_AUCTION_ADMIN_OPTION);
      this.setConsoleCanUse(true);
      this.addSubCommand("option");
      this.addSubCommand(new CommandAuctionAdminOptionSet(var1));
      this.addSubCommand(new CommandAuctionAdminOptionList(var1));
      this.addSubCommand(new CommandAuctionAdminOptionReset(var1));
   }

   protected CommandType perform(AuctionPlugin var1) {
      this.syntaxMessage(this.sender);
      return CommandType.DEFAULT;
   }
}
