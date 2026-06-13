package fr.maxlego08.zauctionhouse.libs.sarah;

import fr.maxlego08.zauctionhouse.libs.sarah.database.DatabaseType;
import java.util.Objects;

public class DatabaseConfiguration {
   private final String tablePrefix;
   private final String user;
   private final String password;
   private final int port;
   private final String host;
   private final String database;
   private final boolean debug;
   private final DatabaseType databaseType;
   private final Integer maximumPoolSize;
   private final Integer minimumIdle;

   public DatabaseConfiguration(String var1, String var2, String var3, int var4, String var5, String var6, boolean var7, DatabaseType var8) {
      this(var1, var2, var3, var4, var5, var6, var7, var8, (Integer)null, (Integer)null);
   }

   public DatabaseConfiguration(String var1, String var2, String var3, int var4, String var5, String var6, boolean var7, DatabaseType var8, Integer var9, Integer var10) {
      this.tablePrefix = var1;
      this.user = var2;
      this.password = var3;
      this.port = var4;
      this.host = var5;
      this.database = var6;
      this.debug = var7;
      this.databaseType = var8;
      this.maximumPoolSize = var9;
      this.minimumIdle = var10;
   }

   public static DatabaseConfiguration create(String var0, String var1, int var2, String var3, String var4, DatabaseType var5) {
      return new DatabaseConfiguration("", var0, var1, var2, var3, var4, false, var5);
   }

   public static DatabaseConfiguration create(String var0, String var1, int var2, String var3, String var4) {
      return new DatabaseConfiguration("", var0, var1, var2, var3, var4, false, DatabaseType.MYSQL);
   }

   public static DatabaseConfiguration createMariaDb(String var0, String var1, int var2, String var3, String var4) {
      return new DatabaseConfiguration("", var0, var1, var2, var3, var4, false, DatabaseType.MARIADB);
   }

   public static DatabaseConfiguration create(String var0, String var1, int var2, String var3, String var4, boolean var5) {
      return new DatabaseConfiguration("", var0, var1, var2, var3, var4, var5, DatabaseType.MYSQL);
   }

   public static DatabaseConfiguration createMariaDb(String var0, String var1, int var2, String var3, String var4, boolean var5) {
      return new DatabaseConfiguration("", var0, var1, var2, var3, var4, var5, DatabaseType.MARIADB);
   }

   public static DatabaseConfiguration create(String var0, String var1, String var2, String var3, DatabaseType var4) {
      return new DatabaseConfiguration("", var0, var1, 3306, var2, var3, false, var4);
   }

   public static DatabaseConfiguration create(String var0, String var1, int var2, String var3, String var4, boolean var5, DatabaseType var6) {
      return new DatabaseConfiguration("", var0, var1, var2, var3, var4, var5, var6);
   }

   public static DatabaseConfiguration createMariaDb(String var0, String var1, String var2, String var3) {
      return new DatabaseConfiguration("", var0, var1, 3306, var2, var3, false, DatabaseType.MARIADB);
   }

   public static DatabaseConfiguration sqlite(boolean var0) {
      return new DatabaseConfiguration("", (String)null, (String)null, 0, (String)null, (String)null, var0, DatabaseType.SQLITE);
   }

   public String replacePrefix(String var1) {
      return this.tablePrefix == null ? var1 : var1.replaceAll("%prefix%", this.tablePrefix);
   }

   public String getTablePrefix() {
      return this.tablePrefix;
   }

   public String getUser() {
      return this.user;
   }

   public String getPassword() {
      return this.password;
   }

   public int getPort() {
      return this.port;
   }

   public String getHost() {
      return this.host;
   }

   public String getDatabase() {
      return this.database;
   }

   public boolean isDebug() {
      return this.debug;
   }

   public DatabaseType getDatabaseType() {
      return this.databaseType;
   }

   public Integer getMaximumPoolSize() {
      return this.maximumPoolSize;
   }

   public Integer getMinimumIdle() {
      return this.minimumIdle;
   }

   public DatabaseConfiguration withPoolSettings(Integer var1, Integer var2) {
      return new DatabaseConfiguration(this.tablePrefix, this.user, this.password, this.port, this.host, this.database, this.debug, this.databaseType, var1, var2);
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         DatabaseConfiguration var2 = (DatabaseConfiguration)var1;
         return this.port == var2.port && this.debug == var2.debug && Objects.equals(this.tablePrefix, var2.tablePrefix) && Objects.equals(this.user, var2.user) && Objects.equals(this.password, var2.password) && Objects.equals(this.host, var2.host) && Objects.equals(this.database, var2.database) && this.databaseType == var2.databaseType && Objects.equals(this.maximumPoolSize, var2.maximumPoolSize) && Objects.equals(this.minimumIdle, var2.minimumIdle);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.tablePrefix, this.user, this.password, this.port, this.host, this.database, this.debug, this.databaseType, this.maximumPoolSize, this.minimumIdle});
   }

   public String toString() {
      return "DatabaseConfiguration{tablePrefix='" + this.tablePrefix + '\'' + ", user='" + this.user + '\'' + ", password='" + this.password + '\'' + ", port=" + this.port + ", host='" + this.host + '\'' + ", database='" + this.database + '\'' + ", debug=" + this.debug + ", databaseType=" + this.databaseType + ", maximumPoolSize=" + this.maximumPoolSize + ", minimumIdle=" + this.minimumIdle + '}';
   }
}
