package fr.maxlego08.zauctionhouse.migration.v3;

import fr.maxlego08.zauctionhouse.api.migration.MigrationProvider;
import java.io.File;
import org.bukkit.configuration.ConfigurationSection;

public class V3MigrationConfig {
   private final SourceType sourceType;
   private final String host;
   private final int port;
   private final String database;
   private final String username;
   private final String password;
   private final String tablePrefix;
   private final String sqlitePath;
   private final File jsonFolder;

   private V3MigrationConfig(SourceType var1, String var2, int var3, String var4, String var5, String var6, String var7, String var8, File var9) {
      this.sourceType = var1;
      this.host = var2;
      this.port = var3;
      this.database = var4;
      this.username = var5;
      this.password = var6;
      this.tablePrefix = var7;
      this.sqlitePath = var8;
      this.jsonFolder = var9;
   }

   public static V3MigrationConfig mysql(String var0, int var1, String var2, String var3, String var4, String var5) {
      return new V3MigrationConfig(V3MigrationConfig.SourceType.MYSQL, var0, var1, var2, var3, var4, var5, (String)null, (File)null);
   }

   public static V3MigrationConfig sqlite(String var0, String var1) {
      return new V3MigrationConfig(V3MigrationConfig.SourceType.SQLITE, (String)null, 0, (String)null, (String)null, (String)null, var1, var0, (File)null);
   }

   public static V3MigrationConfig json(File var0) {
      return new V3MigrationConfig(V3MigrationConfig.SourceType.JSON, (String)null, 0, (String)null, (String)null, (String)null, (String)null, (String)null, var0);
   }

   public static V3MigrationConfig fromConfig(ConfigurationSection var0, File var1) {
      return fromConfig(var0, "zauctionhouse_", "plugins/zAuctionHouse/database.db", "plugins/zAuctionHouse");
   }

   public static V3MigrationConfig fromConfig(ConfigurationSection var0, File var1, MigrationProvider var2) {
      return fromConfig(var0, var2.getDefaultTablePrefix(), var2.getDefaultSqlitePath(), var2.getDefaultJsonFolder());
   }

   private static V3MigrationConfig fromConfig(ConfigurationSection var0, String var1, String var2, String var3) {
      String var4 = var0.getString("source-type", "SQLITE").toUpperCase();
      SourceType var5 = V3MigrationConfig.SourceType.valueOf(var4);
      String var6 = var0.getString("table-prefix", var1);
      V3MigrationConfig var10000;
      switch (var5.ordinal()) {
         case 0:
         case 1:
            var10000 = new V3MigrationConfig(var5, var0.getString("host", "localhost"), var0.getInt("port", 3306), var0.getString("database", "zauctionhouse"), var0.getString("user", "root"), var0.getString("password", ""), var6, (String)null, (File)null);
            break;
         case 2:
            var10000 = new V3MigrationConfig(var5, (String)null, 0, (String)null, (String)null, (String)null, var6, var0.getString("sqlite-path", var2), (File)null);
            break;
         case 3:
            var10000 = new V3MigrationConfig(var5, (String)null, 0, (String)null, (String)null, (String)null, (String)null, (String)null, new File(var0.getString("json-folder", var3)));
            break;
         default:
            throw new MatchException((String)null, (Throwable)null);
      }

      return var10000;
   }

   public SourceType getSourceType() {
      return this.sourceType;
   }

   public String getHost() {
      return this.host;
   }

   public int getPort() {
      return this.port;
   }

   public String getDatabase() {
      return this.database;
   }

   public String getUsername() {
      return this.username;
   }

   public String getPassword() {
      return this.password;
   }

   public String getTablePrefix() {
      return this.tablePrefix;
   }

   public String getSqlitePath() {
      return this.sqlitePath;
   }

   public File getJsonFolder() {
      return this.jsonFolder;
   }

   public String toString() {
      String var10000;
      switch (this.sourceType.ordinal()) {
         case 0:
         case 1:
            var10000 = "MySQL/MariaDB: " + this.host + ":" + this.port + "/" + this.database;
            break;
         case 2:
            var10000 = "SQLite: " + this.sqlitePath;
            break;
         case 3:
            var10000 = "JSON: " + this.jsonFolder.getPath();
            break;
         default:
            throw new MatchException((String)null, (Throwable)null);
      }

      return var10000;
   }

   public static enum SourceType {
      MYSQL,
      MARIADB,
      SQLITE,
      JSON;

      // $FF: synthetic method
      private static SourceType[] $values() {
         return new SourceType[]{MYSQL, MARIADB, SQLITE, JSON};
      }
   }
}
