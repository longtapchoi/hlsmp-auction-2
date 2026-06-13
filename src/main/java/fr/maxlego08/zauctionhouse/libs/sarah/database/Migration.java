package fr.maxlego08.zauctionhouse.libs.sarah.database;

import fr.maxlego08.zauctionhouse.libs.sarah.SchemaBuilder;
import java.util.function.Consumer;

public abstract class Migration {
   private boolean alter = false;

   public abstract void up();

   protected void create(String var1, Consumer<Schema> var2) {
      SchemaBuilder.create(this, var1, var2);
   }

   protected void create(String var1, Class<?> var2) {
      SchemaBuilder.create(this, var1, var2);
   }

   protected void index(String var1, String var2) {
      SchemaBuilder.createIndex(this, var1, var2);
   }

   protected void drop(String var1) {
      SchemaBuilder.drop(this, var1);
   }

   protected void modify(String var1, Consumer<Schema> var2) {
      SchemaBuilder.modify(this, var1, var2);
   }

   protected void createOrAlter(String var1, Consumer<Schema> var2) {
      this.create(var1, var2);
      this.alter = true;
   }

   protected void createOrAlter(String var1, Class<?> var2) {
      this.create(var1, var2);
      this.alter = true;
   }

   public boolean isAlter() {
      return this.alter;
   }
}
