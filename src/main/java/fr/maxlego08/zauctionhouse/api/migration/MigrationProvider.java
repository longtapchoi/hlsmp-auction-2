package fr.maxlego08.zauctionhouse.api.migration;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.bukkit.configuration.ConfigurationSection;

public interface MigrationProvider {
   String getId();

   String getDisplayName();

   String getDescription();

   List<String> getAliases();

   default String getConfigSection() {
      return this.getId();
   }

   String getDefaultSqlitePath();

   String getDefaultJsonFolder();

   String getDefaultTablePrefix();

   default boolean matches(String input) {
      if (input == null) {
         return false;
      } else {
         String lower = input.toLowerCase();
         return this.getId().equals(lower) || this.getDisplayName().equalsIgnoreCase(input) || this.getAliases().stream().anyMatch((alias) -> alias.equalsIgnoreCase(lower));
      }
   }

   default String validateConfig(ConfigurationSection config) {
      return null;
   }

   CompletableFuture<MigrationResult> migrate(AuctionPlugin var1, ConfigurationSection var2, MigrationCallback var3);
}
