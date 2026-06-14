package fr.maxlego08.zauctionhouse.api.migration;

import java.util.Collection;
import java.util.Optional;

public interface MigrationRegistry {
   void register(MigrationProvider var1);

   boolean unregister(String var1);

   Optional<MigrationProvider> getProvider(String var1);

   Collection<MigrationProvider> getProviders();

   Collection<String> getProviderIds();

   default boolean hasProvider(String providerId) {
      return this.getProvider(providerId).isPresent();
   }
}
