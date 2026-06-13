package fr.maxlego08.zauctionhouse.libs.sarah;

import fr.maxlego08.zauctionhouse.libs.sarah.logger.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class MariaDbConnection extends DatabaseConnection {
   public MariaDbConnection(DatabaseConfiguration var1, Logger var2) throws Exception {
      super(var1, var2);
   }

   public Connection connectToDatabase() throws java.sql.SQLException {
      Properties var1 = new Properties();
      var1.setProperty("useSSL", "false");
      var1.setProperty("user", this.databaseConfiguration.getUser());
      var1.setProperty("password", this.databaseConfiguration.getPassword());
      return DriverManager.getConnection("jdbc:mariadb://" + this.databaseConfiguration.getHost() + ":" + this.databaseConfiguration.getPort() + "/" + this.databaseConfiguration.getDatabase() + "?allowMultiQueries=true", var1);
   }
}
