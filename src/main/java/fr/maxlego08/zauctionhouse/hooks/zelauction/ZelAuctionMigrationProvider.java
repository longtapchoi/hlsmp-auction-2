package fr.maxlego08.zauctionhouse.hooks.zelauction;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.migration.MigrationCallback;
import fr.maxlego08.zauctionhouse.api.migration.MigrationProvider;
import fr.maxlego08.zauctionhouse.api.migration.MigrationResult;
import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import org.bukkit.configuration.ConfigurationSection;

public class ZelAuctionMigrationProvider implements MigrationProvider {
   public String getId() {
      return "zelauction";
   }

   public String getDisplayName() {
      return "ZelAuction";
   }

   public String getDescription() {
      return "ZelAuction auction plugin (reads from plugins/ZelAuction/)";
   }

   public List<String> getAliases() {
      return List.of("zel", "zelauctions");
   }

   public String getConfigSection() {
      return null;
   }

   public String getDefaultSqlitePath() {
      return null;
   }

   public String getDefaultJsonFolder() {
      return null;
   }

   public String getDefaultTablePrefix() {
      return null;
   }

   public String validateConfig(ConfigurationSection var1) {
      File var2 = new File("plugins/ZelAuction");
      if (var2.exists() && var2.isDirectory()) {
         File var3 = new File(var2, "database.yml");
         return !var3.exists() ? "ZelAuction database.yml not found at plugins/ZelAuction/database.yml" : null;
      } else {
         return "ZelAuction plugin folder not found at plugins/ZelAuction/";
      }
   }

   public CompletableFuture<MigrationResult> migrate(AuctionPlugin var1, ConfigurationSection var2, MigrationCallback var3) {
      ZelAuctionMigrationService var4 = new ZelAuctionMigrationService(var1);
      Objects.requireNonNull(var3);
      var4.onProgress(var3::onProgress);
      return var4.migrate().thenApply((var0) -> !var0.isSuccess() ? MigrationResult.failure(var0.getErrorMessage()) : MigrationResult.success(var0.getPlayersImported(), var0.getItemsImported(), var0.getTransactionsImported(), var0.getErrors(), var0.getDurationMs())).exceptionally((var0) -> MigrationResult.failure(var0.getMessage()));
   }
}
