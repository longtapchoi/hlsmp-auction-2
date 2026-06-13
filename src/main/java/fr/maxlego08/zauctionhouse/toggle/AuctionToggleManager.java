package fr.maxlego08.zauctionhouse.toggle;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class AuctionToggleManager {
    private final Plugin plugin;
    private final Set<UUID> disabledPlayers = new HashSet<>();
    private final File dataFile;
    private YamlConfiguration config;

    public AuctionToggleManager(Plugin plugin) {
        this.plugin = plugin;
        this.dataFile = new File(plugin.getDataFolder(), "auction-toggle.yml");
        load();
    }

    private void load() {
        if (!dataFile.exists()) { config = new YamlConfiguration(); return; }
        config = YamlConfiguration.loadConfiguration(dataFile);
        for (String uuid : config.getStringList("disabled")) {
            try { disabledPlayers.add(UUID.fromString(uuid)); } catch (IllegalArgumentException ignored) {}
        }
    }

    private void save() {
        config.set("disabled", disabledPlayers.stream().map(UUID::toString).toList());
        try { config.save(dataFile); } catch (IOException e) {
            plugin.getLogger().severe("Lỗi lưu auction-toggle.yml: " + e.getMessage());
        }
    }

    public boolean isDisabled(UUID uuid) { return disabledPlayers.contains(uuid); }

    public boolean toggle(UUID uuid) {
        if (disabledPlayers.contains(uuid)) { disabledPlayers.remove(uuid); save(); return false; }
        else { disabledPlayers.add(uuid); save(); return true; }
    }
}
