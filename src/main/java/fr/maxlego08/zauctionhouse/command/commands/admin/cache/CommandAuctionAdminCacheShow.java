package fr.maxlego08.zauctionhouse.command.commands.admin.cache;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCache;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCacheKey;
import fr.maxlego08.zauctionhouse.api.command.CommandType;
import fr.maxlego08.zauctionhouse.api.command.VCommand;
import fr.maxlego08.zauctionhouse.api.item.Item;
import fr.maxlego08.zauctionhouse.api.messages.Message;
import fr.maxlego08.zauctionhouse.api.utils.IntList;
import fr.maxlego08.zauctionhouse.api.utils.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CommandAuctionAdminCacheShow extends VCommand {
   public CommandAuctionAdminCacheShow(AuctionPlugin var1) {
      super(var1);
      this.setPermission(Permission.ZAUCTIONHOUSE_ADMIN);
      this.setDescription(Message.COMMAND_DESCRIPTION_AUCTION_ADMIN_CACHE_SHOW);
      this.setConsoleCanUse(false);
      this.addSubCommand("show");
      this.addRequireArg("player", (var0, var1x) -> Bukkit.getOnlinePlayers().stream().map(Player::getName).toList());
   }

   protected CommandType perform(AuctionPlugin var1) {
      String var2 = this.argAsString(0);
      if (var2 == null) {
         return CommandType.SYNTAX_ERROR;
      } else {
         Player var3 = Bukkit.getPlayerExact(var2);
         if (var3 == null) {
            this.message(this.plugin, this.sender, Message.ADMIN_CACHE_PLAYER_NOT_ONLINE, new Object[]{"%player%", var2});
            return CommandType.DEFAULT;
         } else {
            PlayerCache var4 = this.auctionManager.getCache(var3);
            this.message(this.plugin, this.sender, Message.ADMIN_CACHE_SHOW_HEADER, new Object[]{"%player%", var3.getName()});
            boolean var5 = false;

            for(PlayerCacheKey var9 : PlayerCacheKey.values()) {
               if (var4.has(var9)) {
                  var5 = true;
                  String var10 = this.formatValue(var4.get(var9));
                  this.message(this.plugin, this.sender, Message.ADMIN_CACHE_SHOW_ENTRY, new Object[]{"%key%", var9.name(), "%value%", var10.length() > 75 ? var10.substring(0, 75) + "..." : var10});
               }
            }

            if (!var5) {
               this.message(this.plugin, this.sender, Message.ADMIN_CACHE_SHOW_EMPTY, new Object[0]);
            }

            return CommandType.SUCCESS;
         }
      }
   }

   private String formatValue(Object var1) {
      byte var3 = 0;
      String var10000;
      //$FF: var3->value
      //0->fr/maxlego08/zauctionhouse/api/utils/IntList
      //1->fr/maxlego08/zauctionhouse/api/item/Item
      switch (((Class)var1).typeSwitch<invokedynamic>(var1, var3)) {
         case -1:
            var10000 = "not set";
            break;
         case 0:
            IntList var4 = (IntList)var1;
            var10000 = "IntList (size=" + var4.size() + ")";
            break;
         case 1:
            Item var5 = (Item)var1;
            var10000 = var5.getItemDisplay();
            break;
         default:
            var10000 = var1.toString();
      }

      return var10000;
   }
}
