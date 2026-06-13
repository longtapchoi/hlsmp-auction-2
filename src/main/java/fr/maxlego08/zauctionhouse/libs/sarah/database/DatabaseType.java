package fr.maxlego08.zauctionhouse.libs.sarah.database;

public enum DatabaseType {
   MYSQL,
   MARIADB,
   SQLITE;

   // $FF: synthetic method
   private static DatabaseType[] $values() {
      return new DatabaseType[]{MYSQL, MARIADB, SQLITE};
   }
}
