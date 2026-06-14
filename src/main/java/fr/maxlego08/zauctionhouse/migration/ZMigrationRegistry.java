package fr.maxlego08.zauctionhouse.migration;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.migration.MigrationProvider;
import fr.maxlego08.zauctionhouse.api.migration.MigrationRegistry;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class ZMigrationRegistry implements MigrationRegistry {
   private final AuctionPlugin plugin;
   private final Map<String, MigrationProvider> providers = new ConcurrentHashMap();

   public ZMigrationRegistry(AuctionPlugin var1) {
      this.plugin = var1;
   }

   public void register(MigrationProvider var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Provider cannot be null");
      } else {
         String var2 = var1.getId().toLowerCase();
         if (this.providers.containsKey(var2)) {
            throw new IllegalArgumentException("A migration provider with ID '" + var2 + "' is already registered");
         } else {
            this.providers.put(var2, var1);
            Logger var10000 = this.plugin.getLogger();
            String var10001 = var1.getDisplayName();
            var10000.info("Registered migration provider: " + var10001 + " (" + var2 + ")");
         }
      }
   }

   public boolean unregister(String var1) {
      if (var1 == null) {
         return false;
      } else {
         MigrationProvider var2 = (MigrationProvider)this.providers.remove(var1.toLowerCase());
         if (var2 != null) {
            this.plugin.getLogger().info("Unregistered migration provider: " + var2.getDisplayName());
            return true;
         } else {
            return false;
         }
      }
   }

   public Optional<MigrationProvider> getProvider(String var1) {
      if (var1 == null) {
         return Optional.empty();
      } else {
         MigrationProvider var2 = (MigrationProvider)this.providers.get(var1.toLowerCase());
         if (var2 != null) {
            return Optional.of(var2);
         } else {
            for(MigrationProvider var4 : this.providers.values()) {
               if (var4.matches(var1)) {
                  return Optional.of(var4);
               }
            }

            return Optional.empty();
         }
      }
   }

   public Collection<MigrationProvider> getProviders() {
      return Collections.unmodifiableCollection(this.providers.values());
   }

   public Collection<String> getProviderIds() {
      return Collections.unmodifiableCollection(this.providers.keySet());
   }
}
