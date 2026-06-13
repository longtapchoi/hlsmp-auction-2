package fr.maxlego08.zauctionhouse.api.storage;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.libs.sarah.DatabaseConnection;
import fr.maxlego08.zauctionhouse.libs.sarah.SchemaBuilder;
import fr.maxlego08.zauctionhouse.libs.sarah.database.Schema;
import fr.maxlego08.zauctionhouse.libs.sarah.logger.JULogger;
import fr.maxlego08.zauctionhouse.libs.sarah.logger.Logger;
import fr.maxlego08.zauctionhouse.libs.sarah.requests.InsertBatchRequest;
import fr.maxlego08.zauctionhouse.libs.sarah.requests.UpdateBatchRequest;
import fr.maxlego08.zauctionhouse.libs.sarah.requests.UpsertBatchRequest;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class Repository {
   protected final AuctionPlugin plugin;
   protected final DatabaseConnection connection;
   private final String tableName;
   private final Logger logger;

   public Repository(AuctionPlugin var1, DatabaseConnection var2, String var3) {
      this.plugin = var1;
      this.connection = var2;
      this.tableName = var3;
      this.logger = JULogger.from(var1.getLogger());
   }

   public AuctionPlugin getPlugin() {
      return this.plugin;
   }

   public DatabaseConnection getConnection() {
      return this.connection;
   }

   public String getTableName() {
      return this.tableName;
   }

   public Logger getLogger() {
      return this.logger;
   }

   protected void upsert(Consumer<Schema> var1) {
      try {
         SchemaBuilder.upsert(this.getTableName(), var1).execute(this.connection, this.logger);
      } catch (SQLException var3) {
         var3.printStackTrace();
      }

   }

   protected void update(Consumer<Schema> var1) {
      try {
         SchemaBuilder.update(this.getTableName(), var1).execute(this.connection, this.logger);
      } catch (SQLException var3) {
         var3.printStackTrace();
      }

   }

   protected void insert(Consumer<Schema> var1) {
      this.insert(var1, (var0) -> {
      });
   }

   protected void insert(Consumer<Schema> var1, Consumer<Integer> var2) {
      try {
         var2.accept(SchemaBuilder.insert(this.getTableName(), var1).execute(this.connection, this.logger));
      } catch (SQLException var4) {
         var4.printStackTrace();
      }

   }

   protected int insertSync(Consumer<Schema> var1) {
      try {
         return SchemaBuilder.insert(this.getTableName(), var1).execute(this.connection, this.logger);
      } catch (SQLException var3) {
         var3.printStackTrace();
         return -1;
      }
   }

   protected long select(Consumer<Schema> var1) {
      Schema var2 = SchemaBuilder.selectCount(this.getTableName());
      var1.accept(var2);

      try {
         return var2.executeSelectCount(this.connection, this.logger);
      } catch (SQLException var4) {
         var4.printStackTrace();
         return 0L;
      }
   }

   protected <T> List<T> select(Class<T> var1, Consumer<Schema> var2) {
      Schema var3 = SchemaBuilder.select(this.getTableName());
      var2.accept(var3);

      try {
         return var3.<T>executeSelect(var1, this.connection, this.logger);
      } catch (Exception var5) {
         var5.printStackTrace();
         return new ArrayList();
      }
   }

   protected <T> List<T> selectAll(Class<T> var1) {
      Schema var2 = SchemaBuilder.select(this.getTableName());

      try {
         return var2.<T>executeSelect(var1, this.connection, this.logger);
      } catch (Exception var4) {
         var4.printStackTrace();
         return new ArrayList();
      }
   }

   protected int delete(Consumer<Schema> var1) {
      Schema var2 = SchemaBuilder.delete(this.getTableName());
      var1.accept(var2);

      try {
         return var2.execute(this.connection, this.logger);
      } catch (SQLException var4) {
         var4.printStackTrace();
         return -1;
      }
   }

   protected void insert(List<Schema> var1) {
      InsertBatchRequest var2 = new InsertBatchRequest(var1);
      var2.execute(this.connection, this.connection.getDatabaseConfiguration(), this.logger);
   }

   protected void upsert(List<Schema> var1) {
      UpsertBatchRequest var2 = new UpsertBatchRequest(var1);
      var2.execute(this.connection, this.connection.getDatabaseConfiguration(), this.logger);
   }

   protected void update(List<Schema> var1) {
      UpdateBatchRequest var2 = new UpdateBatchRequest(var1);
      var2.execute(this.connection, this.connection.getDatabaseConfiguration(), this.logger);
   }

   protected Schema createInsertSchema(Consumer<Schema> var1) {
      return SchemaBuilder.insert(this.getTableName(), var1);
   }

   protected Schema createUpdateSchema(Consumer<Schema> var1) {
      return SchemaBuilder.update(this.getTableName(), var1);
   }
}
