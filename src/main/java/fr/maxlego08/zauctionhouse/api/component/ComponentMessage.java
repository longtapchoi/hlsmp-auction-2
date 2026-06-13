package fr.maxlego08.zauctionhouse.api.component;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.messages.messages.BossBarMessage;
import fr.maxlego08.zauctionhouse.api.messages.messages.TitleMessage;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface ComponentMessage {
   void sendMessage(CommandSender var1, String var2);

   void sendActionBar(Player var1, String var2);

   void sendTitle(Player var1, TitleMessage var2, Object... var3);

   void sendBossBar(AuctionPlugin var1, Player var2, BossBarMessage var3);

   String getItemStackName(ItemStack var1);

   List<String> getItemStackLore(ItemStack var1);

   boolean hasDisplayName(ItemStack var1);

   String getItemStackDisplayName(ItemStack var1);

   String stripColor(String var1);
}
