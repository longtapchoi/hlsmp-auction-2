package fr.maxlego08.zauctionhouse.command.commands;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.command.CommandType;
import fr.maxlego08.zauctionhouse.api.command.VCommand;
import fr.maxlego08.zauctionhouse.api.configuration.commands.InventoryCommandConfiguration;

public class CommandAuctionInventories extends VCommand {
   private final InventoryCommandConfiguration configuration;

   public CommandAuctionInventories(AuctionPlugin var1, InventoryCommandConfiguration var2) {
      super(var1);
      this.configuration = var2;
      if (var2.permission() != null) {
         this.setPermission(var2.permission());
      }

      if (var2.description() != null) {
         this.setDescription(var2.description());
      }

      this.addSubCommand(var2.aliases());
      this.setConsoleCanUse(false);
      if (var2.enablePage()) {
         this.addOptionalArg(var2.pageName());
      }

   }

   protected CommandType perform(AuctionPlugin var1) {
      int var2 = this.argAsInteger(0, 1);
      if (var2 <= 1) {
         var2 = 1;
      }

      this.plugin.getInventoriesLoader().openInventory(this.player, this.configuration.inventories(), var2);
      return CommandType.SUCCESS;
   }
}
