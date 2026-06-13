package fr.maxlego08.zauctionhouse.libs.sarah;

import fr.maxlego08.zauctionhouse.libs.sarah.database.DatabaseType;
import fr.maxlego08.zauctionhouse.libs.sarah.exceptions.DatabaseException;
import fr.maxlego08.zauctionhouse.libs.sarah.logger.Logger;
import fr.maxlego08.zauctionhouse.libs.sarah.transaction.Transaction;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class DatabaseConnection {
   protected final DatabaseConfiguration databaseConfiguration;
   protected final Logger logger;
   protected Connection connection;

   public DatabaseConnection(DatabaseConfiguration var1, Logger var2) {
      this.databaseConfiguration = var1;
      this.logger = var2;
   }

   public DatabaseConfiguration getDatabaseConfiguration() {
      return this.databaseConfiguration;
   }

   public boolean isValid() {
      DatabaseType var1 = this.databaseConfiguration.getDatabaseType();

      try {
         if (var1 == DatabaseType.MARIADB) {
            Class.forName("org.mariadb.jdbc.Driver");
         } else {
            Class.forName("com.mysql.cj.jdbc.Driver");
         }
      } catch (Exception var6) {
      }

      if (!this.isConnected(this.connection)) {
         try {
            Connection var2 = this.connectToDatabase();

            boolean var3;
            try {
               var3 = this.isConnected(var2);
            } catch (Throwable var7) {
               if (var2 != null) {
                  try {
                     var2.close();
                  } catch (Throwable var5) {
                     var7.addSuppressed(var5);
                  }
               }

               throw var7;
            }

            if (var2 != null) {
               var2.close();
            }

            return var3;
         } catch (Exception var8) {
            this.logger.info("Failed to validate database connection: " + var8.getMessage());
            return false;
         }
      } else {
         return true;
      }
   }

   protected boolean isConnected(Connection var1) {
      if (var1 == null) {
         return false;
      } else {
         try {
            return !var1.isClosed() && var1.isValid(1);
         } catch (Exception var3) {
            return false;
         }
      }
   }

   public void disconnect() {
      if (this.isConnected(this.connection)) {
         try {
            this.connection.close();
         } catch (SQLException var2) {
            this.logger.info("Failed to disconnect from database: " + var2.getMessage());
         }
      }

   }

   public void connect() {
      if (!this.isConnected(this.connection)) {
         try {
            this.connection = this.connectToDatabase();
         } catch (Exception var2) {
            this.logger.info("Failed to connect to database: " + var2.getMessage());
            throw new DatabaseException("connect", var2);
         }
      }

   }

   public abstract Connection connectToDatabase();

   public Connection getConnection() {
      this.connect();
      return this.connection;
   }

   public Transaction beginTransaction() {
      try {
         return new Transaction(this.getConnection());
      } catch (Exception var2) {
         this.logger.info("Failed to begin transaction: " + var2.getMessage());
         throw new DatabaseException("begin-transaction", var2);
      }
   }
}
