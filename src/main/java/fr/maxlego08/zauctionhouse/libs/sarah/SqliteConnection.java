package fr.maxlego08.zauctionhouse.libs.sarah;

import fr.maxlego08.zauctionhouse.libs.sarah.logger.Logger;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;

public class SqliteConnection extends DatabaseConnection {
   private final File folder;
   private String fileName = "database.db";

   public SqliteConnection(DatabaseConfiguration var1, File var2, Logger var3) throws Exception {
      super(var1, var3);
      this.folder = var2;
   }

   public Connection connectToDatabase() throws java.sql.SQLException {
      Files.createDirectories(this.folder.toPath());
      Path var1 = this.folder.toPath().resolve(this.fileName);
      String var2 = "jdbc:sqlite:" + var1.toAbsolutePath();

      try {
         Class.forName("org.sqlite.JDBC");
      } catch (ClassNotFoundException var4) {
      }

      return DriverManager.getConnection(var2);
   }

   public File getFolder() {
      return this.folder;
   }

   public String getFileName() {
      return this.fileName;
   }

   public void setFileName(String var1) {
      this.fileName = var1;
   }

   public Connection getConnection() {
      try {
         return this.connectToDatabase();
      } catch (Exception var2) {
         this.connect();
         return this.connection;
      }
   }
}
