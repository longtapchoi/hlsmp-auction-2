package fr.maxlego08.zauctionhouse.storage.migrations;

import fr.maxlego08.sarah.database.Migration;

public class CreateLogsMigration extends Migration {
   public void up() {
      this.create("%prefix%logs", (var0) -> {
         var0.autoIncrement("id");
         var0.integer("item_id").foreignKey("%prefix%items", "id", true);
         var0.string("log_type", 255);
         var0.string("player_unique_id", 36).foreignKey("%prefix%players", "unique_id", true);
         var0.string("target_unique_id", 36).nullable().foreignKey("%prefix%players", "unique_id", true);
         var0.longText("itemstack").nullable();
         var0.decimal("price", 65, 2).defaultValue(0);
         var0.string("economy_name", 255).nullable();
         var0.longText("additional_data").nullable();
         var0.timestamp("readed_at").nullable();
         var0.timestamps();
      });
   }
}
