package fr.maxlego08.zauctionhouse.libs.sarah;

import fr.maxlego08.zauctionhouse.libs.sarah.database.DatabaseType;
import fr.maxlego08.zauctionhouse.libs.sarah.exceptions.DatabaseException;
import fr.maxlego08.zauctionhouse.libs.sarah.libs.hikari.HikariConfig;
import fr.maxlego08.zauctionhouse.libs.sarah.libs.hikari.HikariDataSource;
import fr.maxlego08.zauctionhouse.libs.sarah.logger.Logger;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class HikariDatabaseConnection extends DatabaseConnection {
   private static final AtomicInteger POOL_COUNTER = new AtomicInteger(0);
   private static final int MAXIMUM_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 2 + 1;
   private static final int MINIMUM_IDLE;
   private static final long MAX_LIFETIME;
   private static final long CONNECTION_TIMEOUT;
   private static final long LEAK_DETECTION_THRESHOLD;
   private HikariDataSource dataSource;

   public HikariDatabaseConnection(DatabaseConfiguration var1, Logger var2) {
      super(var1, var2);
      this.initializeDataSource();
   }

   private void initializeDataSource() {
      HikariConfig var1 = new HikariConfig();
      var1.setPoolName("sarah-" + POOL_COUNTER.getAndIncrement());
      DatabaseType var2 = this.databaseConfiguration.getDatabaseType();
      String var3;
      if (var2 == DatabaseType.MARIADB) {
         var3 = "jdbc:mariadb://" + this.databaseConfiguration.getHost() + ":" + this.databaseConfiguration.getPort() + "/" + this.databaseConfiguration.getDatabase() + "?allowMultiQueries=true";
         var1.setDriverClassName("org.mariadb.jdbc.Driver");
      } else {
         var3 = "jdbc:mysql://" + this.databaseConfiguration.getHost() + ":" + this.databaseConfiguration.getPort() + "/" + this.databaseConfiguration.getDatabase() + "?allowMultiQueries=true";
         var1.setDriverClassName("com.mysql.cj.jdbc.Driver");
      }

      var1.setJdbcUrl(var3);
      var1.setUsername(this.databaseConfiguration.getUser());
      var1.setPassword(this.databaseConfiguration.getPassword());
      int var4 = MAXIMUM_POOL_SIZE;
      Integer var5 = this.databaseConfiguration.getMaximumPoolSize();
      if (var5 != null && var5 > 0) {
         var4 = var5;
      }

      int var6 = Math.min(var4, MINIMUM_IDLE);
      Integer var7 = this.databaseConfiguration.getMinimumIdle();
      if (var7 != null && var7 >= 0) {
         var6 = Math.min(var4, var7);
      }

      var1.setMaximumPoolSize(var4);
      var1.setMinimumIdle(var6);
      var1.setMaxLifetime(MAX_LIFETIME);
      var1.setConnectionTimeout(CONNECTION_TIMEOUT);
      var1.setLeakDetectionThreshold(LEAK_DETECTION_THRESHOLD);
      HashMap<String, Object> var8 = new HashMap<>();
      var8.put("useSSL", "false");
      var8.put("useUnicode", "true");
      var8.put("characterEncoding", "utf8");
      var8.put("socketTimeout", String.valueOf(TimeUnit.SECONDS.toMillis(30L)));
      if (var2 == DatabaseType.MYSQL) {
         var8.put("cachePrepStmts", "true");
         var8.put("prepStmtCacheSize", "250");
         var8.put("prepStmtCacheSqlLimit", "2048");
         var8.put("useServerPrepStmts", "true");
         var8.put("useLocalSessionState", "true");
         var8.put("rewriteBatchedStatements", "true");
         var8.put("cacheResultSetMetadata", "true");
         var8.put("cacheServerConfiguration", "true");
         var8.put("elideSetAutoCommits", "true");
         var8.put("maintainTimeStats", "false");
         var8.put("alwaysSendSetIsolation", "false");
         var8.put("cacheCallableStmts", "true");
      }

      for(Map.Entry<String, Object> var10 : var8.entrySet()) {
         var1.addDataSourceProperty(var10.getKey(), var10.getValue());
      }

      this.dataSource = new HikariDataSource(var1);
   }

   public Connection connectToDatabase() throws java.sql.SQLException {
      return this.dataSource.getConnection();
   }

   public void connect() {
   }

   public void disconnect() {
      if (this.dataSource != null && !this.dataSource.isClosed()) {
         this.dataSource.close();
      }

   }

   public boolean isValid() {
      return this.dataSource != null && this.dataSource.isRunning();
   }

   public Connection getConnection() {
      try {
         return this.dataSource.getConnection();
      } catch (Exception var2) {
         this.logger.info("Failed to get connection from Hikari pool: " + var2.getMessage());
         throw new DatabaseException("getConnection", var2);
      }
   }

   protected boolean isConnected(Connection var1) {
      try {
         return var1 != null && var1.isValid(1);
      } catch (SQLException var3) {
         return false;
      }
   }

   static {
      MINIMUM_IDLE = Math.min(MAXIMUM_POOL_SIZE, 10);
      MAX_LIFETIME = TimeUnit.MINUTES.toMillis(30L);
      CONNECTION_TIMEOUT = TimeUnit.SECONDS.toMillis(10L);
      LEAK_DETECTION_THRESHOLD = TimeUnit.SECONDS.toMillis(10L);
   }
}
