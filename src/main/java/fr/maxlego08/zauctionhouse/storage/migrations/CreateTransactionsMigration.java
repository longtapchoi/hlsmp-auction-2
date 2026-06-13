package fr.maxlego08.zauctionhouse.storage.migrations;

import fr.maxlego08.zauctionhouse.libs.sarah.database.Migration;

public class CreateTransactionsMigration extends Migration {
   public void up() {
      this.create("%prefix%transactions", (var0) -> {
         var0.autoIncrement("id");
         var0.integer("item_id").foreignKey("%prefix%items", "id", true);
         var0.string("player_unique_id", 36).foreignKey("%prefix%players", "unique_id", true);
         var0.string("economy_name", 255);
         var0.decimal("before", 65, 2);
         var0.decimal("after", 65, 2);
         var0.decimal("value", 65, 2);
         var0.string("status", 32);
         var0.timestamps();
      });
   }
}
