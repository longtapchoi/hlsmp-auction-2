package fr.maxlego08.zauctionhouse.storage.migrations;

import fr.maxlego08.sarah.database.Migration;

public class CreatePlayerMigration extends Migration {
   public void up() {
      this.create("%prefix%players", (var0) -> {
         var0.uuid("unique_id").primary().unique();
         var0.string("name", 16);
         var0.timestamps();
      });
   }
}
