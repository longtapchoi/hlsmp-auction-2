package fr.maxlego08.zauctionhouse.command.commands;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.command.CommandType;
import fr.maxlego08.zauctionhouse.api.command.VCommand;
import fr.maxlego08.zauctionhouse.api.messages.Message;
import fr.maxlego08.zauctionhouse.api.utils.Permission;
import fr.maxlego08.zauctionhouse.command.commands.admin.CommandAuctionAdmin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandAuction extends VCommand {
   public CommandAuction(AuctionPlugin var1) {
      super(var1);
      this.setDescription(Message.COMMAND_DESCRIPTION_AUCTION);
      this.addSubCommand(new CommandAuctionSell(var1));
      this.addSubCommand(new CommandAuctionClaim(var1));
      this.addSubCommand(new CommandAuctionPage(var1));
      this.addSubCommand(new CommandAuctionSearch(var1));
      this.addSubCommand(new CommandAuctionOption(var1));
      this.addSubCommand(new CommandAuctionAdmin(var1));
      var1.getConfiguration().getInventoryCommands().forEach((var2) -> this.addSubCommand(new CommandAuctionInventories(var1, var2)));
   }

   protected CommandType perform(AuctionPlugin var1) {
      if (!this.hasPermission(this.sender, Permission.ZAUCTIONHOUSE_USE.asPermission())) {
         this.message(var1, this.sender, Message.COMMAND_NO_PERMISSION, new Object[0]);
         return CommandType.DEFAULT;
      } else {
         CommandSender var3 = this.sender;
         if (var3 instanceof Player) {
            Player var2 = (Player)var3;
            this.auctionManager.openMainAuction(var2);
         } else {
            this.syntaxMessage(this.sender);
         }

         return CommandType.SUCCESS;
      }
   }
}
