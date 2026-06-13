package fr.maxlego08.zauctionhouse.libs.sarah;

import fr.maxlego08.zauctionhouse.libs.sarah.database.DatabaseType;
import fr.maxlego08.zauctionhouse.libs.sarah.database.Schema;
import fr.maxlego08.zauctionhouse.libs.sarah.exceptions.DatabaseException;
import fr.maxlego08.zauctionhouse.libs.sarah.logger.Logger;
import fr.maxlego08.zauctionhouse.libs.sarah.requests.InsertBatchRequest;
import fr.maxlego08.zauctionhouse.libs.sarah.requests.UpdateBatchRequest;
import fr.maxlego08.zauctionhouse.libs.sarah.requests.UpsertBatchRequest;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@SuppressWarnings({"unchecked", "rawtypes"})
public class RequestHelper {
   private final DatabaseConnection connection;
   private final Logger logger;

   public RequestHelper(DatabaseConnection var1, Logger var2) {
      this.connection = var1;
      this.logger = var2;
   }

   public <T> void upsert(String var1, Class<T> var2, T var3) {
      this.upsert(var1, ConsumerConstructor.createConsumerFromTemplate(var2, var3));
   }

   public void upsert(String var1, Consumer<Schema> var2) {
      try {
         SchemaBuilder.upsert(var1, var2).execute(this.connection, this.logger);
      } catch (SQLException var4) {
         this.logger.info("Upsert operation failed on table: " + var1 + " - " + var4.getMessage());
         throw new DatabaseException("upsert", var1, var4);
      }
   }

   public void update(String var1, Consumer<Schema> var2) {
      try {
         SchemaBuilder.update(var1, var2).execute(this.connection, this.logger);
      } catch (SQLException var4) {
         this.logger.info("Update operation failed on table: " + var1 + " - " + var4.getMessage());
         throw new DatabaseException("update", var1, var4);
      }
   }

   public <T> void insert(String var1, Class<T> var2, T var3) {
      this.insert(var1, ConsumerConstructor.createConsumerFromTemplate(var2, var3));
   }

   public void insert(String var1, Consumer<Schema> var2) {
      this.insert(var1, var2, (Consumer)((var0) -> {
      }));
   }

   public void insert(String var1, Consumer<Schema> var2, Consumer<Integer> var3) {
      this.insert(var1, var2, var3, () -> {
      });
   }

   public void insert(String var1, Consumer<Schema> var2, Consumer<Integer> var3, Runnable var4) {
      try {
         var3.accept(SchemaBuilder.insert(var1, var2).execute(this.connection, this.logger));
      } catch (SQLException var6) {
         this.logger.info("Insert operation failed on table: " + var1 + " - " + var6.getMessage());
         var4.run();
         throw new DatabaseException("insert", var1, var6);
      }
   }

   public long count(String var1, Consumer<Schema> var2) {
      Schema var3 = SchemaBuilder.selectCount(var1);
      var2.accept(var3);

      try {
         return var3.executeSelectCount(this.connection, this.logger);
      } catch (SQLException var5) {
         this.logger.info("Count operation failed on table: " + var1 + " - " + var5.getMessage());
         return 0L;
      }
   }

   public <T> List<T> select(String var1, Class<T> var2, Consumer<Schema> var3) {
      Schema var4 = SchemaBuilder.select(var1);
      var3.accept(var4);

      try {
         return var4.<T>executeSelect(var2, this.connection, this.logger);
      } catch (Exception var6) {
         this.logger.info("Select operation failed on table: " + var1 + " - " + var6.getMessage());
         return new ArrayList();
      }
   }

   public List<Map<String, Object>> select(String var1, Consumer<Schema> var2) {
      Schema var3 = SchemaBuilder.select(var1);
      var2.accept(var3);

      try {
         return var3.executeSelect(this.connection, this.logger);
      } catch (Exception var5) {
         this.logger.info("Select operation failed on table: " + var1 + " - " + var5.getMessage());
         return new ArrayList();
      }
   }

   public <T> List<T> selectAll(String var1, Class<T> var2) {
      Schema var3 = SchemaBuilder.select(var1);

      try {
         return var3.<T>executeSelect(var2, this.connection, this.logger);
      } catch (Exception var5) {
         this.logger.info("SelectAll operation failed on table: " + var1 + " - " + var5.getMessage());
         return new ArrayList();
      }
   }

   public void delete(String var1, Consumer<Schema> var2) {
      Schema var3 = SchemaBuilder.delete(var1);
      var2.accept(var3);

      try {
         var3.execute(this.connection, this.logger);
      } catch (SQLException var5) {
         this.logger.info("Delete operation failed on table: " + var1 + " - " + var5.getMessage());
         throw new DatabaseException("delete", var1, var5);
      }
   }

   public void upsertMultiple(List<Schema> var1) {
      UpsertBatchRequest var2 = new UpsertBatchRequest(var1);
      var2.execute(this.connection, this.connection.getDatabaseConfiguration(), this.logger);
   }

   public <T> void upsertMultiple(String var1, Class<T> var2, List<T> var3) {
      ArrayList var4 = new ArrayList();

      for(Object var6 : var3) {
         Schema var7 = SchemaBuilder.upsert(var1, ConsumerConstructor.createConsumerFromTemplate(var2, var6));
         var4.add(var7);
      }

      this.upsertMultiple(var4);
   }

   public void insertMultiple(List<Schema> var1) {
      InsertBatchRequest var2 = new InsertBatchRequest(var1);
      var2.execute(this.connection, this.connection.getDatabaseConfiguration(), this.logger);
   }

   public <T> void insertMultiple(String var1, Class<T> var2, List<T> var3) {
      ArrayList var4 = new ArrayList();

      for(Object var6 : var3) {
         Schema var7 = SchemaBuilder.insert(var1, ConsumerConstructor.createConsumerFromTemplate(var2, var6));
         var4.add(var7);
      }

      this.insertMultiple(var4);
   }

   public void updateMultiple(List<Schema> var1) {
      if (this.connection.getDatabaseConfiguration().getDatabaseType() != DatabaseType.SQLITE) {
         UpdateBatchRequest var6 = new UpdateBatchRequest(var1);
         var6.execute(this.connection, this.connection.getDatabaseConfiguration(), this.logger);
      } else {
         for(Schema var3 : var1) {
            try {
               var3.execute(this.connection, this.logger);
            } catch (SQLException var5) {
               this.logger.info("UpdateMultiple operation failed for schema: " + var3.getTableName() + " - " + var5.getMessage());
               throw new DatabaseException("updateMultiple", var3.getTableName(), var5);
            }
         }

      }
   }

   public <T> void updateMultiple(String var1, Class<T> var2, List<T> var3) {
      ArrayList var4 = new ArrayList();

      for(Object var6 : var3) {
         Schema var7 = SchemaBuilder.update(var1, ConsumerConstructor.createConsumerFromTemplate(var2, var6));
         var4.add(var7);
      }

      this.updateMultiple(var4);
   }

   public DatabaseConnection getConnection() {
      return this.connection;
   }
}
