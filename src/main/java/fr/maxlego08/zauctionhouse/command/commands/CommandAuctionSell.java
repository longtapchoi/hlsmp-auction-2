package fr.maxlego08.zauctionhouse.command.commands;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.command.CommandType;
import fr.maxlego08.zauctionhouse.api.command.VCommandArgument;
import fr.maxlego08.zauctionhouse.api.configuration.Configuration;
import fr.maxlego08.zauctionhouse.api.configuration.commands.arguments.CommandSellArguments;
import fr.maxlego08.zauctionhouse.api.economy.AuctionEconomy;
import fr.maxlego08.zauctionhouse.api.economy.EconomyManager;
import fr.maxlego08.zauctionhouse.api.event.events.sell.AuctionPreSellEvent;
import fr.maxlego08.zauctionhouse.api.item.ItemType;
import fr.maxlego08.zauctionhouse.api.messages.Message;
import fr.maxlego08.zauctionhouse.api.utils.Permission;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandAuctionSell extends VCommandArgument<CommandSellArguments> {
   public CommandAuctionSell(AuctionPlugin var1) {
      super(var1, CommandSellArguments.class);
      this.setPermission(Permission.ZAUCTIONHOUSE_SELL);
      this.setDescription(Message.COMMAND_DESCRIPTION_AUCTION_SELL);
      this.setConsoleCanUse(false);
   }

   public void createCommandArguments(AuctionPlugin var1, Class<CommandSellArguments> var2) {
      this.forEachArgument("commands.sell.", (var0) -> (var1, var2) -> var0.autoCompletion().stream().map((var1x) -> {
               if (var1x.contains("%max-stack-size%") && var1 instanceof Player var2) {
                  ItemStack var3 = var2.getInventory().getItemInMainHand();
                  return var1x.replace("%max-stack-size%", String.valueOf(var3.getType().isAir() ? 0 : var3.getMaxStackSize()));
               } else {
                  return var1x;
               }
            }).distinct().toList());
   }

   protected CommandType perform(AuctionPlugin var1) {
      EconomyManager var2 = var1.getEconomyManager();
      Configuration var3 = var1.getConfiguration();
      String var4 = this.argAsString(CommandSellArguments.PRICE);
      ItemStack var5 = this.player.getInventory().getItemInMainHand();
      if (var5.getType().isAir() && !var1.getConfiguration().isSellInventoryEnabled()) {
         this.message(var1, this.player, Message.SELL_ERROR_AIR, new Object[0]);
         return CommandType.DEFAULT;
      } else {
         int var6 = this.argAsInteger(CommandSellArguments.AMOUNT, var5.getAmount());
         var6 = var6 > var5.getAmount() ? var5.getAmount() : (var6 <= 0 ? 1 : var6);
         AuctionEconomy var7 = var2.getDefaultEconomy(ItemType.AUCTION);
         if (var7 == null) {
            this.message(var1, this.player, Message.SELL_ERROR_DEFAULT_ECONOMY, new Object[0]);
            return CommandType.DEFAULT;
         } else {
            String var8 = this.argAsString(CommandSellArguments.ECONOMY, var7.getName());
            if (var8 == null) {
               return CommandType.DEFAULT;
            } else {
               Optional var9 = var2.getEconomy(var8);
               if (var9.isEmpty()) {
                  this.message(var1, this.sender, Message.SELL_ERROR_ECONOMY, new Object[]{"%name%", var8});
                  return CommandType.DEFAULT;
               } else {
                  AuctionEconomy var10 = (AuctionEconomy)var9.get();
                  BigDecimal var11 = var3.getNumberMultiplicationConfiguration().parseNumber(var4);
                  if (var11 == null) {
                     return CommandType.SYNTAX_ERROR;
                  } else if (var1.getConfiguration().isSellInventoryEnabled()) {
                     this.auctionManager.getSellService().openSellCommandInventory(this.player, var11, var10);
                     return CommandType.SUCCESS;
                  } else {
                     long var12 = var3.getSellExpiration().getExpiration(this.player);
                     long var14 = var12 > 0L ? System.currentTimeMillis() + var12 * 1000L : 0L;
                     AuctionPreSellEvent var16 = new AuctionPreSellEvent(this.player, var6, var14, var5, var10, var11);
                     if (!var16.callEvent()) {
                        return CommandType.DEFAULT;
                     } else {
                        var11 = var16.getPrice();
                        var6 = var16.getAmount();
                        var14 = var16.getExpiredAt();
                        var10 = var16.getAuctionEconomy();
                        var5 = var16.getItemStack();
                        ItemStack var17 = var5.clone();
                        var17.setAmount(var6);
                        Map var18 = Map.of(-1, var17);
                        this.auctionManager.getSellService().sellAuctionItems(this.player, var11, var14, var18, var10);
                        return CommandType.SUCCESS;
                     }
                  }
               }
            }
         }
      }
   }
}
