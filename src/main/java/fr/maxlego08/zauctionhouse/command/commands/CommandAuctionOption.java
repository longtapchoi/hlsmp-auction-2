package fr.maxlego08.zauctionhouse.command.commands;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.command.CommandType;
import fr.maxlego08.zauctionhouse.api.command.VCommand;
import fr.maxlego08.zauctionhouse.api.inventories.Inventories;
import fr.maxlego08.zauctionhouse.api.messages.Message;
import fr.maxlego08.zauctionhouse.api.option.PlayerOption;
import fr.maxlego08.zauctionhouse.api.utils.Permission;
import java.util.Arrays;
import java.util.List;

public class CommandAuctionOption extends VCommand {
   public CommandAuctionOption(AuctionPlugin var1) {
      super(var1);
      this.setPermission(Permission.ZAUCTIONHOUSE_OPTION);
      this.setDescription(Message.COMMAND_DESCRIPTION_AUCTION_OPTION);
      this.setConsoleCanUse(false);
      this.addSubCommand("option");
      this.addSubCommand("options");
      this.addSubCommand("opt");
      this.addOptionalArg("option_name", (var0, var1x) -> Arrays.stream(PlayerOption.values()).map(PlayerOption::getKey).toList());
      this.addOptionalArg("value", (var0, var1x) -> List.of("true", "false"));
   }

   protected CommandType perform(AuctionPlugin var1) {
      String var2 = this.argAsString(0, (String)null);
      if (var2 == null) {
         var1.getInventoriesLoader().openInventory(this.player, Inventories.OPTIONS);
         return CommandType.SUCCESS;
      } else {
         PlayerOption var3 = PlayerOption.fromKey(var2);
         if (var3 == null) {
            return CommandType.DEFAULT;
         } else {
            String var4 = this.argAsString(1, (String)null);
            boolean var5;
            if (var4 != null) {
               var5 = Boolean.parseBoolean(var4);
            } else {
               var5 = !this.auctionManager.getOptionService().getOption(this.player, var3);
            }

            this.auctionManager.getOptionService().setOption(this.player.getUniqueId(), var3, var5);
            this.message(var1, this.sender, var5 ? var3.getEnabledMessage() : var3.getDisabledMessage(), new Object[0]);
            return CommandType.SUCCESS;
         }
      }
   }
}
