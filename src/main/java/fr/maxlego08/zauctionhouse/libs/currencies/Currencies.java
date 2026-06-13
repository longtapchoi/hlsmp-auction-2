package fr.maxlego08.zauctionhouse.libs.currencies;

import fr.maxlego08.zauctionhouse.libs.currencies.providers.ExperienceProvider;
import fr.maxlego08.zauctionhouse.libs.currencies.providers.ItemProvider;
import fr.maxlego08.zauctionhouse.libs.currencies.providers.LevelProvider;
import fr.maxlego08.zauctionhouse.libs.currencies.providers.PlayerPointsProvider;
import fr.maxlego08.zauctionhouse.libs.currencies.providers.VaultProvider;
import fr.maxlego08.zauctionhouse.libs.currencies.providers.VotingProvider;
import fr.maxlego08.zauctionhouse.libs.currencies.providers.ZEssentialsProvider;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;

@SuppressWarnings({"unchecked", "rawtypes"})
public enum Currencies {
   VAULT("Vault", VaultProvider.class),
   PLAYERPOINTS("PlayerPoints", PlayerPointsProvider.class),
   ITEM("self", ItemProvider.class, false),
   LEVEL("self", LevelProvider.class),
   EXPERIENCE("self", ExperienceProvider.class),
   ZESSENTIALS("zEssentials", ZEssentialsProvider.class, true, true),
   VOTINGPLUGIN("VotingPlugin", VotingProvider.class);

   private final String name;
   private final Class<? extends CurrencyProvider> providerClass;
   private final boolean autoCreate;
   private final boolean currencySpecific;
   private final Map<String, CurrencyProvider> providers;

   Currencies(String name, Class<? extends CurrencyProvider> providerClass) {
      this(name, providerClass, true, false);
   }

   Currencies(String name, Class<? extends CurrencyProvider> providerClass, boolean autoCreate) {
      this(name, providerClass, autoCreate, false);
   }

   Currencies(String name, Class<? extends CurrencyProvider> providerClass, boolean autoCreate, boolean currencySpecific) {
      this.name = name;
      this.providerClass = providerClass;
      this.autoCreate = autoCreate;
      this.currencySpecific = currencySpecific;
      this.providers = new HashMap<>();
   }

   public void registerProvider(String currencyId, Object... args) {
      if (!this.providers.containsKey(currencyId)) {
         this.providers.put(currencyId, createProvider(args));
      }
   }

   public CurrencyProvider createProvider(Object... args) {
      try {
         if (args.length == 0) return this.providerClass.newInstance();
         Constructor constructor = this.providerClass.getConstructor(
            Arrays.stream(args).map(Object::getClass).toArray(Class[]::new)
         );
         return (CurrencyProvider) constructor.newInstance(args);
      } catch (Exception e) {
         throw new RuntimeException("Cannot create provider for " + this.name, e);
      }
   }

   private boolean isDisabled() {
      if (this.name.equalsIgnoreCase("self")) return false;
      return !Bukkit.getPluginManager().isPluginEnabled(this.name);
   }

   public void deposit(UUID uuid, BigDecimal amount, String reason) {
      deposit(uuid, amount, "default", reason);
   }

   public void withdraw(UUID uuid, BigDecimal amount, String reason) {
      withdraw(uuid, amount, "default", reason);
   }

   public void deposit(UUID uuid, BigDecimal amount) {
      deposit(uuid, amount, "default", "No reason");
   }

   public void withdraw(UUID uuid, BigDecimal amount) {
      withdraw(uuid, amount, "default", "No reason");
   }

   public BigDecimal getBalance(UUID uuid) {
      return getBalance(uuid, "default");
   }

   public void deposit(UUID uuid, BigDecimal amount, String currencyId, String reason) {
      canBeUsed(currencyId);
      this.providers.get(currencyId).deposit(uuid, amount, reason);
   }

   public void withdraw(UUID uuid, BigDecimal amount, String currencyId, String reason) {
      canBeUsed(currencyId);
      this.providers.get(currencyId).withdraw(uuid, amount, reason);
   }

   public BigDecimal getBalance(UUID uuid, String currencyId) {
      canBeUsed(currencyId);
      return this.providers.get(currencyId).getBalance(uuid);
   }

   private void canBeUsed(String currencyId) {
      if (isDisabled()) throw new IllegalStateException("Plugin " + this.name + " is not enabled.");
      if (this.autoCreate) {
         registerProvider(currencyId, this.currencySpecific ? new Object[]{currencyId} : new Object[0]);
      } else if (!this.providers.containsKey(currencyId)) {
         throw new IllegalStateException("Provider not registered for " + this.name);
      }
   }
}
