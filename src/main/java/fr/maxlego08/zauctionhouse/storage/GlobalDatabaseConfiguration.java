package fr.maxlego08.zauctionhouse.storage;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class GlobalDatabaseConfiguration {
   private static final Logger LOGGER = Logger.getLogger(GlobalDatabaseConfiguration.class.getName());
   private final FileConfiguration pluginConfiguration;
   private final FileConfiguration globalConfiguration;

   public GlobalDatabaseConfiguration(FileConfiguration var1) {
      this.pluginConfiguration = var1;
      File var2 = new File("database-configuration.yml");
      YamlConfiguration var3 = null;
      if (var2.exists()) {
         try {
            var3 = YamlConfiguration.loadConfiguration(var2);
         } catch (Exception var5) {
            LOGGER.log(Level.WARNING, "Failed to load global database configuration file, using plugin configuration instead", var5);
         }
      }

      this.globalConfiguration = var3;
   }

   public String getHost() {
      return this.globalConfiguration == null ? this.pluginConfiguration.getString("database-configuration.host") : this.globalConfiguration.getString("database-configuration.host", this.pluginConfiguration.getString("database-configuration.host", "192.168.10.10"));
   }

   public int getPort() {
      return this.globalConfiguration == null ? this.pluginConfiguration.getInt("database-configuration.port") : this.globalConfiguration.getInt("database-configuration.port", this.pluginConfiguration.getInt("database-configuration.port", 3306));
   }

   public String getDatabase() {
      return this.globalConfiguration == null ? this.pluginConfiguration.getString("database-configuration.database") : this.globalConfiguration.getString("database-configuration.database", this.pluginConfiguration.getString("database-configuration.database", "homestead"));
   }

   public String getUser() {
      return this.globalConfiguration == null ? this.pluginConfiguration.getString("database-configuration.user") : this.globalConfiguration.getString("database-configuration.user", this.pluginConfiguration.getString("database-configuration.user", "homestead"));
   }

   public String getPassword() {
      return this.globalConfiguration == null ? this.pluginConfiguration.getString("database-configuration.password") : this.globalConfiguration.getString("database-configuration.password", this.pluginConfiguration.getString("database-configuration.password", "secret"));
   }

   public String getTablePrefix() {
      return this.globalConfiguration == null ? this.pluginConfiguration.getString("database-configuration.table-prefix") : this.globalConfiguration.getString("database-configuration.table-prefix", this.pluginConfiguration.getString("database-configuration.table-prefix", "groupez_"));
   }

   public boolean isDebug() {
      return this.globalConfiguration == null ? this.pluginConfiguration.getBoolean("database-configuration.debug") : this.globalConfiguration.getBoolean("database-configuration.debug", this.pluginConfiguration.getBoolean("database-configuration.debug", false));
   }
}
