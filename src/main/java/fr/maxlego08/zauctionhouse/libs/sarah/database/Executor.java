package fr.maxlego08.zauctionhouse.libs.sarah.database;

import fr.maxlego08.zauctionhouse.libs.sarah.DatabaseConfiguration;
import fr.maxlego08.zauctionhouse.libs.sarah.DatabaseConnection;
import fr.maxlego08.zauctionhouse.libs.sarah.logger.Logger;

public interface Executor {
   int execute(DatabaseConnection var1, DatabaseConfiguration var2, Logger var3);
}
