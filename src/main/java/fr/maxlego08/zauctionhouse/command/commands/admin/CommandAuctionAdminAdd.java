package fr.maxlego08.zauctionhouse.command.commands.admin;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCacheKey;
import fr.maxlego08.zauctionhouse.api.command.CommandType;
import fr.maxlego08.zauctionhouse.api.command.VCommand;
import fr.maxlego08.zauctionhouse.api.economy.AuctionEconomy;
import fr.maxlego08.zauctionhouse.api.item.ItemStatus;
import fr.maxlego08.zauctionhouse.api.item.ItemType;
import fr.maxlego08.zauctionhouse.api.item.StorageType;
import fr.maxlego08.zauctionhouse.api.messages.Message;
import fr.maxlego08.zauctionhouse.api.utils.Permission;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class CommandAuctionAdminAdd extends VCommand {
   public CommandAuctionAdminAdd(AuctionPlugin var1) {
      super(var1);
      this.addSubCommand("add");
      this.setPermission(Permission.ZAUCTIONHOUSE_ADMIN_ITEMS);
      this.setDescription(Message.ADMIN_ITEM_ADDED);
      this.addRequireArg("player", (var0, var1x) -> Bukkit.getOnlinePlayers().stream().map(Player::getName).toList());
      this.addRequireArg("type", (var0, var1x) -> List.of("listed", "expired", "purchased"));
      this.addOptionalArg("price", (var0, var1x) -> List.of("1000", "0"));
      this.setConsoleCanUse(false);
   }

   protected CommandType perform(AuctionPlugin var1) {
      CommandSender var3 = this.sender;
      if (var3 instanceof Player var2) {
         String var14 = this.argAsString(0);
         if (var14 == null) {
            this.auctionManager.message(var2, Message.ADMIN_TARGET_REQUIRED);
            return CommandType.SYNTAX_ERROR;
         } else {
            Player var4 = Bukkit.getPlayerExact(var14);
            if (var4 == null) {
               this.auctionManager.message(var2, Message.ADMIN_TARGET_NOT_FOUND, "%target%", var14);
               return CommandType.DEFAULT;
            } else {
               ItemStack var5 = var2.getInventory().getItemInMainHand();
               if (var5.getType().isAir()) {
                  this.message(var1, var2, Message.SELL_ERROR_AIR, new Object[0]);
                  return CommandType.DEFAULT;
               } else {
                  String var6 = this.argAsString(1, "listed");
                  String var7 = this.argAsString(2, "0");
                  BigDecimal var8 = var1.getConfiguration().getNumberMultiplicationConfiguration().parseNumber(var7);
                  if (var8 == null) {
                     return CommandType.SYNTAX_ERROR;
                  } else {
                     AuctionEconomy var10 = var1.getEconomyManager().getDefaultEconomy(ItemType.AUCTION);
                     if (var10 == null) {
                        this.message(var1, var2, Message.SELL_ERROR_DEFAULT_ECONOMY, new Object[0]);
                        return CommandType.DEFAULT;
                     } else {
                        ItemStack var11 = var5.clone();
                        this.removeItemInHand(var2, var11.getAmount());
                        switch (var6.toLowerCase(Locale.ENGLISH)) {
                           case "expired" -> this.addExpired(var4, var11, var8, var10, var2);
                           case "purchased" -> this.addPurchased(var4, var11, var8, var10, var2);
                           default -> this.addListed(var4, var11, var8, var10, var2);
                        }

                        return CommandType.SUCCESS;
                     }
                  }
               }
            }
         }
      } else {
         return CommandType.DEFAULT;
      }
   }

   private void removeItemInHand(Player var1, int var2) {
      PlayerInventory var3 = var1.getInventory();
      if (var3.getItemInMainHand().getAmount() > var2) {
         var3.getItemInMainHand().setAmount(var3.getItemInMainHand().getAmount() - var2);
      } else {
         var3.setItemInMainHand(new ItemStack(Material.AIR));
      }

      var1.updateInventory();
   }

   private void addListed(Player var1, ItemStack var2, BigDecimal var3, AuctionEconomy var4, Player var5) {
      long var6 = this.plugin.getConfiguration().getSellExpiration().getExpiration(var1);
      var6 = var6 > 0L ? System.currentTimeMillis() + var6 * 1000L : 0L;
      this.plugin.getStorageManager().createAuctionItem(var1, var3, var6, List.of(var2), var4).thenAccept((var3x) -> {
         this.auctionManager.addItem(StorageType.LISTED, var3x);
         this.auctionManager.clearPlayersCache(PlayerCacheKey.ITEMS_LISTED, PlayerCacheKey.ITEMS_SELLING, PlayerCacheKey.ITEMS_SEARCH);
         this.auctionManager.updateListedItems(var3x, true, var1);
         this.auctionManager.message(var5, Message.ADMIN_ITEM_ADDED, "%items%", var3x.getItemDisplay(), "%target%", var1.getName(), "%type%", "listed");
      });
   }

   private void addExpired(Player var1, ItemStack var2, BigDecimal var3, AuctionEconomy var4, Player var5) {
      this.plugin.getStorageManager().createAuctionItem(var1, var3, System.currentTimeMillis(), List.of(var2), var4).thenAccept((var3x) -> {
         var3x.setStatus(ItemStatus.REMOVED);
         var3x.setExpiredAt(new Date());
         this.auctionManager.addItem(StorageType.EXPIRED, var3x);
         this.plugin.getStorageManager().updateItem(var3x, StorageType.EXPIRED);
         this.auctionManager.clearPlayersCache(PlayerCacheKey.ITEMS_EXPIRED, PlayerCacheKey.ITEMS_SELLING);
         this.auctionManager.message(var5, Message.ADMIN_ITEM_ADDED, "%items%", var3x.getItemDisplay(), "%target%", var1.getName(), "%type%", "expired");
      });
   }

   private void addPurchased(Player var1, ItemStack var2, BigDecimal var3, AuctionEconomy var4, Player var5) {
      this.plugin.getStorageManager().createAuctionItem(var5, var3, System.currentTimeMillis(), List.of(var2), var4).thenAccept((var3x) -> {
         var3x.setBuyer(var1);
         var3x.setStatus(ItemStatus.PURCHASED);
         this.auctionManager.addItem(StorageType.PURCHASED, var3x);
         this.plugin.getStorageManager().updateItem(var3x, StorageType.PURCHASED);
         this.auctionManager.clearPlayerCache(var1, PlayerCacheKey.ITEMS_PURCHASED);
         this.auctionManager.message(var5, Message.ADMIN_ITEM_ADDED, "%items%", var3x.getItemDisplay(), "%target%", var1.getName(), "%type%", "purchased");
      });
   }
}
