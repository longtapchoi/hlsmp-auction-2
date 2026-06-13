package fr.maxlego08.zauctionhouse.command.commands;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.command.CommandType;
import fr.maxlego08.zauctionhouse.api.command.VCommand;
import fr.maxlego08.zauctionhouse.api.configuration.commands.SimpleArgumentConfiguration;
import fr.maxlego08.zauctionhouse.api.configuration.commands.SimpleCommandConfiguration;
import fr.maxlego08.zauctionhouse.api.messages.Message;
import fr.maxlego08.zauctionhouse.api.utils.Permission;

public class CommandAuctionPage extends VCommand {
   public CommandAuctionPage(AuctionPlugin var1) {
      super(var1);
      this.setPermission(Permission.ZAUCTIONHOUSE_USE);
      this.setDescription(Message.COMMAND_DESCRIPTION_AUCTION_PAGE);
      this.setConsoleCanUse(false);
      SimpleCommandConfiguration var2 = var1.getConfiguration().loadSimpleCommandConfiguration("commands.page.");
      this.addSubCommand(var2.aliases());

      for(SimpleArgumentConfiguration var4 : var2.arguments()) {
         if (var4.required()) {
            this.addRequireArg(var4.displayName(), (var1x, var2x) -> var4.autoCompletion());
         } else {
            this.addOptionalArg(var4.displayName(), (var1x, var2x) -> var4.autoCompletion());
         }
      }

   }

   protected CommandType perform(AuctionPlugin var1) {
      int var2 = this.argAsInteger(0, 1);
      if (var2 < 1) {
         var2 = 1;
      }

      this.auctionManager.openMainAuction(this.player, var2);
      return CommandType.SUCCESS;
   }
}
