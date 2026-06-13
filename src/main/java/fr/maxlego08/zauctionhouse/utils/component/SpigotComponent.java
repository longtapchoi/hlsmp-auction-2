package fr.maxlego08.zauctionhouse.utils.component;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.component.ComponentMessage;
import fr.maxlego08.zauctionhouse.api.messages.messages.BossBarMessage;
import fr.maxlego08.zauctionhouse.api.messages.messages.TitleMessage;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SpigotComponent implements ComponentMessage {
   public void sendMessage(CommandSender var1, String var2) {
      var1.sendMessage(var2);
   }

   public void sendActionBar(Player var1, String var2) {
      var1.sendActionBar(var2);
   }

   public void sendTitle(Player var1, TitleMessage var2, Object... var3) {
   }

   public void sendBossBar(AuctionPlugin var1, Player var2, BossBarMessage var3) {
   }

   public String getItemStackName(ItemStack var1) {
      ItemMeta var2 = var1.getItemMeta();
      return var2 != null && var2.hasDisplayName() ? ChatColor.stripColor(var2.getDisplayName()) : "";
   }

   public List<String> getItemStackLore(ItemStack var1) {
      ItemMeta var2 = var1.getItemMeta();
      if (var2 != null && var2.hasLore()) {
         List<String> var3 = var2.getLore();
         return var3 != null && !var3.isEmpty() ? var3.stream().map(ChatColor::stripColor).toList() : List.of();
      } else {
         return List.of();
      }
   }

   public boolean hasDisplayName(ItemStack var1) {
      return var1.hasItemMeta() && var1.getItemMeta().hasDisplayName();
   }

   public String getItemStackDisplayName(ItemStack var1) {
      return var1.getItemMeta().getDisplayName().replace("§", "&");
   }

   public String stripColor(String var1) {
      return var1.replace("§", "&");
   }
}
