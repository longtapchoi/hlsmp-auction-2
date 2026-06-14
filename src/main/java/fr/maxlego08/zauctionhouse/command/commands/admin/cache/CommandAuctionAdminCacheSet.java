package fr.maxlego08.zauctionhouse.command.commands.admin.cache;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCache;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCacheKey;
import fr.maxlego08.zauctionhouse.api.command.CommandType;
import fr.maxlego08.zauctionhouse.api.command.VCommand;
import fr.maxlego08.zauctionhouse.api.item.SortItem;
import fr.maxlego08.zauctionhouse.api.messages.Message;
import fr.maxlego08.zauctionhouse.api.utils.Permission;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CommandAuctionAdminCacheSet extends VCommand {
   private static final Set<PlayerCacheKey> SETTABLE_KEYS;

   public CommandAuctionAdminCacheSet(AuctionPlugin var1) {
      super(var1);
      this.setPermission(Permission.ZAUCTIONHOUSE_ADMIN);
      this.setDescription(Message.COMMAND_DESCRIPTION_AUCTION_ADMIN_CACHE_SET);
      this.setConsoleCanUse(true);
      this.addSubCommand("set");
      this.addRequireArg("player", (var0, var1x) -> {
         ArrayList<String> var2 = new ArrayList<>();
         var2.add("*");
         Bukkit.getOnlinePlayers().forEach((var1p) -> var2.add(var1p.getName()));
         return var2;
      });
      this.addRequireArg("key", (var0, var1x) -> SETTABLE_KEYS.stream().map(Enum::name).sorted().toList());
      this.addRequireArg("value", (var1x, var2) -> {
         String var3 = var2.length >= 4 ? var2[3] : null;
         if (var3 == null) {
            return List.of();
         } else {
            try {
               PlayerCacheKey var4 = PlayerCacheKey.valueOf(var3.toUpperCase());
               return this.getValueSuggestions(var4);
            } catch (IllegalArgumentException var5) {
               return List.of();
            }
         }
      });
   }

   protected CommandType perform(AuctionPlugin var1) {
      String var2 = this.argAsString(0);
      if (var2 == null) {
         return CommandType.SYNTAX_ERROR;
      } else {
         String var3 = this.argAsString(1);
         if (var3 == null) {
            return CommandType.SYNTAX_ERROR;
         } else {
            PlayerCacheKey var4;
            try {
               var4 = PlayerCacheKey.valueOf(var3.toUpperCase());
            } catch (IllegalArgumentException var13) {
               this.message(this.plugin, this.sender, Message.ADMIN_CACHE_INVALID_KEY, new Object[]{"%key%", var3});
               return CommandType.DEFAULT;
            }

            if (!SETTABLE_KEYS.contains(var4)) {
               this.message(this.plugin, this.sender, Message.ADMIN_CACHE_KEY_NOT_SETTABLE, new Object[]{"%key%", var4.name()});
               return CommandType.DEFAULT;
            } else {
               String var5 = this.argAsString(2);
               if (var5 == null) {
                  return CommandType.SYNTAX_ERROR;
               } else {
                  Object var6;
                  try {
                     var6 = this.parseValue(var4, var5);
                  } catch (Exception var12) {
                     this.message(this.plugin, this.sender, Message.ADMIN_CACHE_INVALID_VALUE, new Object[]{"%value%", var5, "%key%", var4.name()});
                     return CommandType.DEFAULT;
                  }

                  if (!var2.equals("*")) {
                     Player var14 = Bukkit.getPlayerExact(var2);
                     if (var14 == null) {
                        this.message(this.plugin, this.sender, Message.ADMIN_CACHE_PLAYER_NOT_ONLINE, new Object[]{"%player%", var2});
                        return CommandType.DEFAULT;
                     } else {
                        PlayerCache var15 = this.auctionManager.getCache(var14);
                        var15.set(var4, var6);
                        this.message(this.plugin, this.sender, Message.ADMIN_CACHE_SET, new Object[]{"%key%", var4.name(), "%value%", var5, "%player%", var14.getName()});
                        return CommandType.SUCCESS;
                     }
                  } else {
                     Collection<? extends org.bukkit.entity.Player> var7 = Bukkit.getOnlinePlayers();
                     int var8 = var7.size();

                     for(Player var10 : var7) {
                        PlayerCache var11 = this.auctionManager.getCache(var10);
                        var11.set(var4, var6);
                     }

                     this.message(this.plugin, this.sender, Message.ADMIN_CACHE_SET_ALL_PLAYERS, new Object[]{"%key%", var4.name(), "%value%", var5, "%count%", String.valueOf(var8)});
                     return CommandType.SUCCESS;
                  }
               }
            }
         }
      }
   }

   private Object parseValue(PlayerCacheKey var1, String var2) {
      Object var10000;
      switch (var1) {
         case CURRENT_PAGE:
         case SELL_AMOUNT:
            var10000 = Integer.parseInt(var2);
            break;
         case ITEM_SORT_LOADING:
         case PURCHASE_ITEM:
            var10000 = Boolean.parseBoolean(var2);
            break;
         case SELL_EXPIRED_AT:
            var10000 = Long.parseLong(var2);
            break;
         case SELL_PRICE:
            var10000 = new BigDecimal(var2);
            break;
         case ITEM_SORT:
            var10000 = SortItem.valueOf(var2.toUpperCase());
            break;
         case ADMIN_TARGET_NAME:
            var10000 = var2;
            break;
         default:
            throw new IllegalArgumentException("Unsupported key: " + String.valueOf(var1));
      }

      return var10000;
   }

   private List<String> getValueSuggestions(PlayerCacheKey var1) {
      List var10000;
      switch (var1) {
         case CURRENT_PAGE:
            var10000 = List.of("1", "2", "3");
            break;
         case SELL_AMOUNT:
            var10000 = List.of("1", "16", "32", "64");
            break;
         case ITEM_SORT_LOADING:
         case PURCHASE_ITEM:
            var10000 = List.of("true", "false");
            break;
         case SELL_EXPIRED_AT:
            var10000 = List.of("0");
            break;
         case SELL_PRICE:
            var10000 = List.of("0", "100", "1000");
            break;
         case ITEM_SORT:
            var10000 = Arrays.stream(SortItem.values()).map(Enum::name).toList();
            break;
         case ADMIN_TARGET_NAME:
            var10000 = Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
            break;
         default:
            var10000 = List.of();
      }

      return var10000;
   }

   static {
      SETTABLE_KEYS = Set.of(PlayerCacheKey.CURRENT_PAGE, PlayerCacheKey.SELL_AMOUNT, PlayerCacheKey.ITEM_SORT_LOADING, PlayerCacheKey.PURCHASE_ITEM, PlayerCacheKey.SELL_EXPIRED_AT, PlayerCacheKey.SELL_PRICE, PlayerCacheKey.ITEM_SORT, PlayerCacheKey.ADMIN_TARGET_NAME);
   }
}
