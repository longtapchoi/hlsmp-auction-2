package fr.maxlego08.zauctionhouse.command.commands.admin.cache;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.command.CommandType;
import fr.maxlego08.zauctionhouse.api.command.VCommand;
import fr.maxlego08.zauctionhouse.api.messages.Message;
import fr.maxlego08.zauctionhouse.api.utils.Permission;

public class CommandAuctionAdminCache extends VCommand {
   public CommandAuctionAdminCache(AuctionPlugin var1) {
      super(var1);
      this.setPermission(Permission.ZAUCTIONHOUSE_ADMIN);
      this.setDescription(Message.COMMAND_DESCRIPTION_AUCTION_ADMIN_CACHE);
      this.setConsoleCanUse(false);
      this.addSubCommand("cache");
      this.addSubCommand(new CommandAuctionAdminCacheShow(var1));
      this.addSubCommand(new CommandAuctionAdminCacheClear(var1));
      this.addSubCommand(new CommandAuctionAdminCacheSet(var1));
   }

   protected CommandType perform(AuctionPlugin var1) {
      this.syntaxMessage(this.sender);
      return CommandType.SYNTAX_ERROR;
   }
}
