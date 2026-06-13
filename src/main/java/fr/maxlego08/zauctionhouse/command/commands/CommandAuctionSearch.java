package fr.maxlego08.zauctionhouse.command.commands;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.command.CommandType;
import fr.maxlego08.zauctionhouse.api.command.VCommand;
import fr.maxlego08.zauctionhouse.api.configuration.commands.SimpleArgumentConfiguration;
import fr.maxlego08.zauctionhouse.api.configuration.commands.SimpleCommandConfiguration;
import fr.maxlego08.zauctionhouse.api.messages.Message;
import fr.maxlego08.zauctionhouse.api.utils.Permission;

public class CommandAuctionSearch extends VCommand {
   public CommandAuctionSearch(AuctionPlugin var1) {
      super(var1);
      this.setPermission(Permission.ZAUCTIONHOUSE_USE);
      this.setDescription(Message.COMMAND_DESCRIPTION_AUCTION_SEARCH);
      this.setConsoleCanUse(false);
      this.setIgnoreArgs(true);
      SimpleCommandConfiguration var2 = var1.getConfiguration().loadSimpleCommandConfiguration("commands.search.");
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
      if (this.args.length < 2) {
         return CommandType.SYNTAX_ERROR;
      } else {
         StringBuilder var2 = new StringBuilder();

         for(int var3 = 1; var3 < this.args.length; ++var3) {
            if (var3 > 1) {
               var2.append(" ");
            }

            var2.append(this.args[var3]);
         }

         String var4 = var2.toString();
         var1.getAuctionManager().startSearch(this.player, var4);
         return CommandType.SUCCESS;
      }
   }
}
