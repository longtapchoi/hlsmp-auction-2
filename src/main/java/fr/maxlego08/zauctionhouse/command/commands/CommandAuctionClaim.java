package fr.maxlego08.zauctionhouse.command.commands;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.command.CommandType;
import fr.maxlego08.zauctionhouse.api.command.VCommand;
import fr.maxlego08.zauctionhouse.api.messages.Message;
import fr.maxlego08.zauctionhouse.api.utils.Permission;

public class CommandAuctionClaim extends VCommand {
   public CommandAuctionClaim(AuctionPlugin var1) {
      super(var1);
      this.setPermission(Permission.ZAUCTIONHOUSE_USE);
      this.setDescription(Message.COMMAND_DESCRIPTION_AUCTION_CLAIM);
      this.addSubCommand(var1.getConfiguration().loadCommandAliases("commands.claim."));
      this.setConsoleCanUse(false);
   }

   protected CommandType perform(AuctionPlugin var1) {
      var1.getAuctionManager().getClaimService().claimMoney(this.player);
      return CommandType.SUCCESS;
   }
}
