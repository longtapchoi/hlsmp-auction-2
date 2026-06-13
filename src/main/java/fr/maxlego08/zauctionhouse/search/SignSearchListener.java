package fr.maxlego08.zauctionhouse.search;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SignSearchListener implements Listener {
    private final AuctionPlugin plugin;
    private final Map<UUID, SignSession> activeSessions = new HashMap<>();
    private final Map<UUID, Location> signLocations = new HashMap<>();

    public SignSearchListener(AuctionPlugin plugin) { this.plugin = plugin; }

    public void startSearch(Player player) {
        UUID uuid = player.getUniqueId();
        closeSession(player);
        player.closeInventory();
        Location signLoc = player.getLocation().getBlock().getLocation();
        Block block = signLoc.getBlock();
        SignSession session = new SignSession();
        session.oldBlockType = block.getType();
        session.oldBlockData = block.getBlockData();
        activeSessions.put(uuid, session);
        signLocations.put(uuid, signLoc);
        block.setType(Material.PALE_OAK_SIGN);
        String[] lines = new String[]{"> TÌM KIẾM <", "", "", ""};
        BlockState state = block.getState();
        if (state instanceof Sign sign) {
            for (int i = 0; i < 4; i++) sign.setLine(i, lines[i]);
            sign.update();
        }
        player.sendSignChange(signLoc, lines);
        Bukkit.getScheduler().runTaskLater((org.bukkit.plugin.Plugin) plugin, () -> {
            if (!player.isOnline()) return;
            Location loc = signLocations.get(uuid);
            if (loc == null || loc.getWorld() == null) return;
            BlockState bs = loc.getBlock().getState();
            if (bs instanceof Sign s) { try { player.openSign(s); } catch (Exception ignored) {} }
        }, 2L);
    }

    public void closeSession(Player player) {
        UUID uuid = player.getUniqueId();
        SignSession session = activeSessions.remove(uuid);
        if (session == null) return;
        Location loc = signLocations.remove(uuid);
        if (loc == null || loc.getWorld() == null) return;
        Block block = loc.getBlock();
        if (block.getType() == Material.PALE_OAK_SIGN) {
            if (session.oldBlockType != null && session.oldBlockType != Material.AIR) {
                block.setType(session.oldBlockType);
                if (session.oldBlockData != null) block.setBlockData(session.oldBlockData);
            } else { block.setType(Material.AIR); }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onSignChange(SignChangeEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        if (!activeSessions.containsKey(uuid)) return;
        event.setCancelled(true);
        Player player = event.getPlayer();
        String input = event.getLine(0);
        if (input == null || input.isEmpty()) input = event.getLine(1);
        closeSession(player);
        if (input != null && !input.isEmpty() && !input.equals("> TÌM KIẾM <")) {
            final String query = input;
            Bukkit.getScheduler().runTask((org.bukkit.plugin.Plugin) plugin, () -> {
                if (player.isOnline()) plugin.getAuctionManager().startSearch(player, query);
            });
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!activeSessions.containsKey(event.getPlayer().getUniqueId())) return;
        if ((event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK)
                && event.getClickedBlock() != null
                && event.getClickedBlock().getType() == Material.PALE_OAK_SIGN) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        if (!activeSessions.containsKey(uuid)) return;
        Location signLoc = signLocations.get(uuid);
        if (signLoc != null && event.getBlock().getLocation().equals(signLoc)) event.setCancelled(true);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) { closeSession(event.getPlayer()); }

    private static class SignSession {
        Material oldBlockType = Material.AIR;
        BlockData oldBlockData;
    }
}
