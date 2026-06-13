package fr.maxlego08.zauctionhouse.command.commands;

import fr.maxlego08.zauctionhouse.ZAuctionPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandAuctionToggle implements CommandExecutor {
    private final ZAuctionPlugin plugin;

    public CommandAuctionToggle(ZAuctionPlugin plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Chỉ người chơi mới dùng được!");
            return true;
        }
        boolean nowDisabled = plugin.getAuctionToggleManager().toggle(player.getUniqueId());
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
            nowDisabled ? "&fĐã &cTẮT &fthông báo nhà đấu giá." : "&fĐã &aBẬT &fthông báo nhà đấu giá."));
        return true;
    }
}
