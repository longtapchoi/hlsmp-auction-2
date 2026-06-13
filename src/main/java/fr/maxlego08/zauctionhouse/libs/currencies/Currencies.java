package fr.maxlego08.zauctionhouse.libs.currencies;

import fr.maxlego08.zauctionhouse.libs.currencies.providers.ItemProvider;
import fr.maxlego08.zauctionhouse.libs.currencies.providers.PlayerPointsProvider;
import fr.maxlego08.zauctionhouse.libs.currencies.providers.VaultProvider;
import fr.maxlego08.zauctionhouse.libs.currencies.providers.VotingProvider;
import fr.maxlego08.zauctionhouse.libs.currencies.providers.ZMenuItemProvider;
import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;

public enum Currencies {
   VAULT("Vault", VaultProvider.class),
   PLAYERPOINTS("PlayerPoints", PlayerPointsProvider.class),
   ITEM("self", ItemProvider.class, false),
   ZMENUITEMS("zMenu", ZMenuItemProvider.class, false),
   VOTINGPLUGIN("VotingPlugin", VotingProvider.class);

   private final String name;
   private final Class<? extends CurrencyProvider> providerClass;
   private final boolean autoCreate;
   private final boolean currencySpecific;
   private final Map<String, CurrencyProvider> providers;

   private Currencies(String var3, Class<? extends CurrencyProvider> var4) {
      this(var3, var4, true, false);
   }

   private Currencies(String var3, Class<? extends CurrencyProvider> var4, boolean var5) {
      this(var3, var4, var5, false);
   }

   private Currencies(String var3, Class<? extends CurrencyProvider> var4, boolean var5, boolean var6) {
      this.name = var3;
      this.providerClass = var4;
      this.autoCreate = var5;
      this.providers = new HashMap();
      this.currencySpecific = var6;
   }

   public void registerProvider(String var1, Object... var2) {
      if (!this.providers.containsKey(var1)) {
         CurrencyProvider var3 = this.createProvider(var2);
         this.providers.put(var1, var3);
      }
   }

   public CurrencyProvider createProvider(Object... var1) {
      try {
         CurrencyProvider var2;
         if (var1.length == 0) {
            var2 = (CurrencyProvider)this.providerClass.newInstance();
         } else {
            Constructor var3 = this.providerClass.getConstructor((Class[])Arrays.stream(var1).map(Object::getClass).toArray((var0) -> new Class[var0]));
            var2 = (CurrencyProvider)var3.newInstance(var1);
         }

         return var2;
      } catch (Exception var4) {
         throw new RuntimeException("Cannot create the provider for the plugin " + this.name + ".", var4);
      }
   }

   private boolean isDisable() {
      if (this.name.equalsIgnoreCase("self")) {
         return false;
      } else {
         return !Bukkit.getPluginManager().isPluginEnabled(this.name);
      }
   }

   public void deposit(UUID var1, BigDecimal var2, String var3) {
      this.deposit(var1, var2, "default", var3);
   }

   public void withdraw(UUID var1, BigDecimal var2, String var3) {
      this.withdraw(var1, var2, "default", var3);
   }

   public void deposit(UUID var1, BigDecimal var2) {
      this.deposit(var1, var2, "default", "No reason");
   }

   public void withdraw(UUID var1, BigDecimal var2) {
      this.withdraw(var1, var2, "default", "No reason");
   }

   public BigDecimal getBalance(UUID var1) {
      return this.getBalance(var1, "default");
   }

   public void deposit(UUID var1, BigDecimal var2, String var3, String var4) {
      this.canBeUse(var3);
      ((CurrencyProvider)this.providers.get(var3)).deposit(var1, var2, var4);
   }

   public void withdraw(UUID var1, BigDecimal var2, String var3, String var4) {
      this.canBeUse(var3);
      ((CurrencyProvider)this.providers.get(var3)).withdraw(var1, var2, var4);
   }

   public BigDecimal getBalance(UUID var1, String var2) {
      this.canBeUse(var2);
      return ((CurrencyProvider)this.providers.get(var2)).getBalance(var1);
   }

   private void canBeUse(String var1) {
      if (this.isDisable()) {
         throw new IllegalStateException("The plugin " + this.name + " is not enable.");
      } else {
         if (this.autoCreate) {
            if (this.currencySpecific) {
               this.registerProvider(var1, var1);
            } else {
               this.registerProvider(var1);
            }
         } else if (!this.providers.containsKey(var1)) {
            String var2 = this.name.equalsIgnoreCase("default") ? "" : " and for the currency " + this.name;
            throw new IllegalStateException("You must create the provider for the plugin " + this.name + var2 + " before using it.");
         }

      }
   }

   // $FF: synthetic method
   private static Currencies[] $values() {
      return new Currencies[]{VAULT, PLAYERPOINTS, ITEM, ZMENUITEMS, VOTINGPLUGIN};
   }

   static {
   }
}
