package fr.maxlego08.zauctionhouse.storage.repository;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.storage.Repository;
import fr.maxlego08.zauctionhouse.libs.sarah.DatabaseConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@SuppressWarnings({"unchecked", "rawtypes"})
public class Repositories {
   private final AuctionPlugin plugin;
   private final DatabaseConnection connection;
   private final Map<Class<? extends Repository>, Repository> tables = new HashMap();

   public Repositories(AuctionPlugin var1, DatabaseConnection var2) {
      this.plugin = var1;
      this.connection = var2;
   }

   public void register(Class<? extends Repository> var1) {
      try {
         Repository var2 = (Repository)var1.getConstructor(AuctionPlugin.class, DatabaseConnection.class).newInstance(this.plugin, this.connection);
         this.tables.put(var1, var2);
      } catch (Exception var3) {
         Logger var10000 = this.plugin.getLogger();
         String var10001 = var1.getSimpleName();
         var10000.severe("Failed to register repository " + var10001 + ": " + var3.getMessage());
      }

   }

   public <T extends Repository> T getTable(Class<T> var1) {
      return (T)(this.tables.get(var1));
   }
}
