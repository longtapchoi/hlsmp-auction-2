package fr.maxlego08.zauctionhouse.storage.migrations;

import fr.maxlego08.zauctionhouse.libs.sarah.database.Migration;

public class CreateOptionsMigration extends Migration {
   public void up() {
      this.create("%prefix%options", (var0) -> {
         var0.string("player_unique_id", 36).primary().foreignKey("%prefix%players", "unique_id", true);
         var0.string("option_name", 64).primary();
         var0.longText("option_value");
         var0.timestamps();
      });
   }
}
