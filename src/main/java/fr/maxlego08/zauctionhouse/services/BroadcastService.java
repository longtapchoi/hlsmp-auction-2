package fr.maxlego08.zauctionhouse.services;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.category.Category;
import fr.maxlego08.zauctionhouse.api.configuration.records.BroadcastConfiguration;
import fr.maxlego08.zauctionhouse.api.item.items.AuctionItem;
import fr.maxlego08.zauctionhouse.api.messages.Message;
import fr.maxlego08.zauctionhouse.api.option.PlayerOption;
import fr.maxlego08.zauctionhouse.api.services.AuctionOptionService;
import fr.maxlego08.zauctionhouse.utils.ZUtils;
import fr.maxlego08.zauctionhouse.utils.component.ComponentMessageHelper;
import java.util.Set;
import org.bukkit.entity.Player;

public class BroadcastService extends ZUtils {
   private final AuctionPlugin plugin;

   public BroadcastService(AuctionPlugin var1) {
      this.plugin = var1;
   }

   public void broadcastSell(Player var1, AuctionItem var2) {
      BroadcastConfiguration var3 = this.plugin.getConfiguration().getBroadcast();
      if (var3.sellEnabled()) {
         AuctionOptionService var4 = this.plugin.getAuctionManager().getOptionService();
         String var5 = this.getFirstCategoryName(var2);
         String var6 = var5 != null ? (String)var3.categoryMessagesSell().get(var5) : null;

         for(Player var8 : this.plugin.getServer().getOnlinePlayers()) {
            if ((!var3.excludeSeller() || !var8.equals(var1)) && var4.getOption(var8, PlayerOption.BROADCAST_SELL)) {
               if (var6 != null) {
                  String var9 = this.resolvePlaceholders(var6, var1.getName(), (String)null, var2, var5);
                  ComponentMessageHelper.componentMessage.sendMessage(var8, var9);
               } else {
                  this.message(this.plugin, var8, Message.BROADCAST_SELL, new Object[]{"%seller%", var1.getName(), "%items%", var2.getItemDisplay(), "%price%", var2.getFormattedPrice(), "%category%", var5 != null ? var5 : ""});
               }
            }
         }

      }
   }

   public void broadcastPurchase(Player var1, AuctionItem var2) {
      BroadcastConfiguration var3 = this.plugin.getConfiguration().getBroadcast();
      if (var3.purchaseEnabled()) {
         AuctionOptionService var4 = this.plugin.getAuctionManager().getOptionService();
         String var5 = this.getFirstCategoryName(var2);
         String var6 = var5 != null ? (String)var3.categoryMessagesPurchase().get(var5) : null;

         for(Player var8 : this.plugin.getServer().getOnlinePlayers()) {
            if ((!var3.excludeBuyer() || !var8.equals(var1)) && var4.getOption(var8, PlayerOption.BROADCAST_PURCHASE)) {
               if (var6 != null) {
                  String var9 = this.resolvePlaceholders(var6, var2.getSellerName(), var1.getName(), var2, var5);
                  ComponentMessageHelper.componentMessage.sendMessage(var8, var9);
               } else {
                  this.message(this.plugin, var8, Message.BROADCAST_PURCHASE, new Object[]{"%buyer%", var1.getName(), "%seller%", var2.getSellerName(), "%items%", var2.getItemDisplay(), "%price%", var2.getFormattedPrice(), "%category%", var5 != null ? var5 : ""});
               }
            }
         }

      }
   }

   private String getFirstCategoryName(AuctionItem var1) {
      Set var2 = var1.getCategories();
      return var2 != null && !var2.isEmpty() ? ((Category)var2.iterator().next()).getId() : null;
   }

   private String resolvePlaceholders(String var1, String var2, String var3, AuctionItem var4, String var5) {
      var1 = var1.replace("%seller%", var2 != null ? var2 : "");
      var1 = var1.replace("%buyer%", var3 != null ? var3 : "");
      var1 = var1.replace("%items%", var4.getItemDisplay());
      var1 = var1.replace("%price%", var4.getFormattedPrice());
      var1 = var1.replace("%category%", var5 != null ? var5 : "");
      return var1;
   }
}
