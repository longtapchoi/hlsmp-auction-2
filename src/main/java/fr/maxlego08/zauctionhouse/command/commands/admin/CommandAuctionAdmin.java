package fr.maxlego08.zauctionhouse.command.commands.admin;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.command.CommandType;
import fr.maxlego08.zauctionhouse.api.command.VCommand;
import fr.maxlego08.zauctionhouse.api.messages.Message;
import fr.maxlego08.zauctionhouse.api.utils.Permission;
import fr.maxlego08.zauctionhouse.command.commands.admin.cache.CommandAuctionAdminCache;

public class CommandAuctionAdmin extends VCommand {
   public CommandAuctionAdmin(AuctionPlugin var1) {
      super(var1);
      this.setPermission(Permission.ZAUCTIONHOUSE_ADMIN);
      this.setDescription(Message.COMMAND_DESCRIPTION_AUCTION_ADMIN);
      this.setConsoleCanUse(false);
      this.addSubCommand("admin");
      this.addSubCommand(new CommandAuctionAdminOpen(var1));
      this.addSubCommand(new CommandAuctionAdminForceOpen(var1));
      this.addSubCommand(new CommandAuctionAdminAdd(var1));
      this.addSubCommand(new CommandAuctionAdminGenerate(var1));
      this.addSubCommand(new CommandAuctionAdminCache(var1));
      this.addSubCommand(new CommandAuctionAdminHistory(var1));
      this.addSubCommand(new CommandAuctionAdminReload(var1));
      this.addSubCommand(new CommandAuctionAdminMigrate(var1));
      this.addSubCommand(new CommandAuctionAdminOption(var1));
   }

   protected CommandType perform(AuctionPlugin var1) {
      this.syntaxMessage(this.sender);
      return CommandType.SYNTAX_ERROR;
   }
}
