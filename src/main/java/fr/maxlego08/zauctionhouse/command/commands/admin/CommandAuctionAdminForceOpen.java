package fr.maxlego08.zauctionhouse.command.commands.admin;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.command.CommandType;
import fr.maxlego08.zauctionhouse.api.command.VCommand;
import fr.maxlego08.zauctionhouse.api.inventories.Inventories;
import fr.maxlego08.zauctionhouse.api.messages.Message;
import fr.maxlego08.zauctionhouse.api.utils.Permission;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@SuppressWarnings({"unchecked", "rawtypes"})
public class CommandAuctionAdminForceOpen extends VCommand {
   public CommandAuctionAdminForceOpen(AuctionPlugin var1) {
      super(var1);
      this.addSubCommand("forceopen");
      this.setPermission(Permission.ZAUCTIONHOUSE_ADMIN);
      this.setDescription(Message.COMMAND_DESCRIPTION_AUCTION_ADMIN_FORCEOPEN);
      this.addRequireArg("player", (var0, var1x) -> Bukkit.getOnlinePlayers().stream().map(Player::getName).toList());
      this.addRequireArg("inventory", (var0, var1x) -> {
         ArrayList var2 = new ArrayList();

         for(Inventories var6 : Inventories.values()) {
            var2.add(var6.getFileName());
         }

         return var2;
      });
      this.addOptionalArg("page");
      this.setConsoleCanUse(true);
   }

   protected CommandType perform(AuctionPlugin var1) {
      Player var2 = this.argAsPlayer(0);
      if (var2 == null) {
         this.message(this.plugin, this.sender, Message.ADMIN_TARGET_NOT_FOUND, new Object[]{"%target%", this.argAsString(0)});
         return CommandType.DEFAULT;
      } else {
         String var3 = this.argAsString(1);
         int var4 = this.argAsInteger(2, 1);
         Inventories var5 = null;

         for(Inventories var9 : Inventories.values()) {
            if (var9.getFileName().equalsIgnoreCase(var3)) {
               var5 = var9;
               break;
            }
         }

         if (var5 == null) {
            this.message(this.plugin, this.sender, Message.INVENTORY_NOT_FOUND, new Object[]{"%inventory-name%", var3});
            return CommandType.DEFAULT;
         } else {
            this.plugin.getScheduler().runAtEntity(var2, (var4x) -> this.plugin.getInventoriesLoader().openInventory(var2, var5, var4));
            this.message(this.plugin, this.sender, Message.ADMIN_FORCEOPEN_INVENTORY, new Object[]{"%target%", var2.getName(), "%inventory%", var5.getFileName(), "%page%", String.valueOf(var4)});
            return CommandType.SUCCESS;
         }
      }
   }
}
