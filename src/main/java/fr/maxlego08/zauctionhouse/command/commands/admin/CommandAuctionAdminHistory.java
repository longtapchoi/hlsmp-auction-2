package fr.maxlego08.zauctionhouse.command.commands.admin;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCache;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCacheKey;
import fr.maxlego08.zauctionhouse.api.command.CommandType;
import fr.maxlego08.zauctionhouse.api.command.VCommand;
import fr.maxlego08.zauctionhouse.api.inventories.Inventories;
import fr.maxlego08.zauctionhouse.api.messages.Message;
import fr.maxlego08.zauctionhouse.api.utils.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CommandAuctionAdminHistory extends VCommand {
   public CommandAuctionAdminHistory(AuctionPlugin var1) {
      super(var1);
      this.addSubCommand("history");
      this.setPermission(Permission.ZAUCTIONHOUSE_ADMIN_ITEMS);
      this.setDescription(Message.COMMAND_DESCRIPTION_AUCTION_ADMIN_HISTORY);
      this.addRequireArg("player", (var0, var1x) -> Bukkit.getOnlinePlayers().stream().map(Player::getName).toList());
      this.setConsoleCanUse(false);
   }

   protected CommandType perform(AuctionPlugin var1) {
      String var2 = this.argAsString(0);
      if (var2 == null) {
         this.auctionManager.message(this.player, Message.ADMIN_TARGET_REQUIRED);
         return CommandType.SYNTAX_ERROR;
      } else {
         var1.getStorageManager().findUniqueId(var2).thenAccept((var2x) -> {
            if (this.player.isOnline()) {
               if (var2x == null) {
                  this.auctionManager.message(this.player, Message.ADMIN_TARGET_NOT_FOUND, "%target%", var2);
               } else {
                  PlayerCache var3 = this.auctionManager.getCache(this.player);
                  var3.set(PlayerCacheKey.ADMIN_TARGET_UUID, var2x);
                  var3.set(PlayerCacheKey.ADMIN_TARGET_NAME, var2);
                  this.plugin.getScheduler().runAtEntity(this.player, (var1) -> {
                     if (this.player.isOnline()) {
                        this.plugin.getInventoriesLoader().openInventory(this.player, Inventories.ADMIN_HISTORY_MAIN);
                     }

                  });
                  this.auctionManager.message(this.player, Message.ADMIN_OPEN_HISTORY, "%target%", var2);
               }
            }
         });
         return CommandType.SUCCESS;
      }
   }
}
