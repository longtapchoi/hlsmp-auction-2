package fr.maxlego08.zauctionhouse.command.commands.admin;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCache;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCacheKey;
import fr.maxlego08.zauctionhouse.api.command.CommandType;
import fr.maxlego08.zauctionhouse.api.command.VCommand;
import fr.maxlego08.zauctionhouse.api.inventories.Inventories;
import fr.maxlego08.zauctionhouse.api.messages.Message;
import fr.maxlego08.zauctionhouse.api.utils.Permission;
import java.util.List;
import java.util.Locale;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CommandAuctionAdminOpen extends VCommand {
   public CommandAuctionAdminOpen(AuctionPlugin var1) {
      super(var1);
      this.addSubCommand("open");
      this.setPermission(Permission.ZAUCTIONHOUSE_ADMIN_ITEMS);
      this.setDescription(Message.COMMAND_DESCRIPTION_AUCTION_ADMIN_OPEN);
      this.addRequireArg("player", (var0, var1x) -> Bukkit.getOnlinePlayers().stream().map(Player::getName).toList());
      this.addRequireArg("type", (var0, var1x) -> List.of("listed", "expired", "purchased"));
      this.setConsoleCanUse(false);
   }

   protected CommandType perform(AuctionPlugin var1) {
      String var2 = this.argAsString(0);
      if (var2 == null) {
         this.auctionManager.message(this.player, Message.ADMIN_TARGET_REQUIRED);
         return CommandType.SYNTAX_ERROR;
      } else {
         var1.getStorageManager().findUniqueId(var2).thenAccept((var2x) -> {
            if (var2x == null) {
               this.auctionManager.message(this.player, Message.ADMIN_TARGET_NOT_FOUND, "%target%", var2);
            } else {
               String var3 = this.argAsString(1, "listed");
               Inventories var10000;
               switch (var3.toLowerCase(Locale.ENGLISH)) {
                  case "expired" -> var10000 = Inventories.ADMIN_EXPIRED_ITEMS;
                  case "purchased" -> var10000 = Inventories.ADMIN_PURCHASED_ITEMS;
                  default -> var10000 = Inventories.ADMIN_SELLING_ITEMS;
               }

               Inventories var4 = var10000;
               PlayerCache var7 = this.auctionManager.getCache(this.player);
               var7.set(PlayerCacheKey.ADMIN_TARGET_UUID, var2x);
               var7.set(PlayerCacheKey.ADMIN_TARGET_NAME, var2);
               this.plugin.getScheduler().runAtEntity(this.player, (var2xx) -> this.plugin.getInventoriesLoader().openInventory(this.player, var4));
               this.auctionManager.message(this.player, Message.ADMIN_OPEN_INVENTORY, "%target%", var2, "%type%", var3);
            }
         });
         return CommandType.SUCCESS;
      }
   }
}
