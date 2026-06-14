package fr.maxlego08.zauctionhouse.migration.v3;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.migration.MigrationCallback;
import fr.maxlego08.zauctionhouse.api.migration.MigrationProvider;
import fr.maxlego08.zauctionhouse.api.migration.MigrationResult;
import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import org.bukkit.configuration.ConfigurationSection;

public class V3MigrationProvider implements MigrationProvider {
   public String getId() {
      return "zauctionhousev3";
   }

   public String getDisplayName() {
      return "zAuctionHouse V3";
   }

   public String getDescription() {
      return "zAuctionHouse V3 (previous version)";
   }

   public List<String> getAliases() {
      return List.of("zah", "zahv3", "v3", "zauctionhouse");
   }

   public String getConfigSection() {
      return "zauctionhouse-v3";
   }

   public String getDefaultSqlitePath() {
      return "plugins/zAuctionHouseV3/database.db";
   }

   public String getDefaultJsonFolder() {
      return "plugins/zAuctionHouseV3";
   }

   public String getDefaultTablePrefix() {
      return "zauctionhouse_";
   }

   public String validateConfig(ConfigurationSection var1) {
      if (var1 == null) {
         return "Configuration section is missing";
      } else {
         String var10000;
         switch (var1.getString("source-type", "SQLITE").toUpperCase()) {
            case "SQLITE":
               String var8 = var1.getString("sqlite-path", this.getDefaultSqlitePath());
               File var9 = new File(var8);
               var10000 = !var9.exists() ? "SQLite database file not found: " + var8 : null;
               break;
            case "JSON":
               String var7 = var1.getString("json-folder", this.getDefaultJsonFolder());
               File var6 = new File(var7);
               var10000 = var6.exists() && var6.isDirectory() ? null : "JSON folder not found: " + var7;
               break;
            case "MYSQL":
            case "MARIADB":
               String var5 = var1.getString("host");
               var10000 = var5 != null && !var5.isEmpty() ? null : "Database host is not configured";
               break;
            default:
               var10000 = "Unknown source type: " + var1.getString("source-type", "SQLITE");
         }

         return var10000;
      }
   }

   public CompletableFuture<MigrationResult> migrate(AuctionPlugin var1, ConfigurationSection var2, MigrationCallback var3) {
      V3MigrationConfig var4 = V3MigrationConfig.fromConfig(var2, var1.getDataFolder(), this);
      V3MigrationService var5 = new V3MigrationService(var1);
      Objects.requireNonNull(var3);
      var5.onProgress(var3::onProgress);
      CompletableFuture var10000;
      switch (var4.getSourceType()) {
         case MYSQL:
         case MARIADB:
            var10000 = var5.migrateFromSql(var4.getHost(), var4.getPort(), var4.getDatabase(), var4.getUsername(), var4.getPassword(), var4.getTablePrefix());
            break;
         case SQLITE:
            var10000 = var5.migrateFromSqlite(var4.getSqlitePath(), var4.getTablePrefix());
            break;
         case JSON:
            var10000 = var5.migrateFromJson(var4.getJsonFolder());
            break;
         default:
            throw new MatchException((String)null, (Throwable)null);
      }

      CompletableFuture<V3MigrationResult> var6 = var10000;
      return var6.thenApply((v3r) -> MigrationResult.success(v3r.getPlayersImported(), v3r.getItemsImported(), v3r.getTransactionsImported(), v3r.getErrors(), v3r.getDurationMs())).exceptionally((err) -> MigrationResult.failure(err.getMessage()));
   }
}
