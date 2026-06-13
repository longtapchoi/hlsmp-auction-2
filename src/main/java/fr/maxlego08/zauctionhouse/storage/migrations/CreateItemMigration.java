package fr.maxlego08.zauctionhouse.storage.migrations;

import fr.maxlego08.zauctionhouse.api.item.StorageType;
import fr.maxlego08.sarah.database.Migration;

public class CreateItemMigration extends Migration {
   public void up() {
      this.create("%prefix%items", (var0) -> {
         var0.autoIncrement("id");
         var0.string("item_type", 255);
         var0.string("seller_unique_id", 36).foreignKey("%prefix%players", "unique_id", true);
         var0.string("buyer_unique_id", 36).nullable().foreignKey("%prefix%players", "unique_id", true);
         var0.decimal("price", 65, 2);
         var0.string("economy_name", 255);
         var0.enumType("storage_type", StorageType.class);
         var0.string("server_name", 255);
         var0.timestamp("expired_at");
         var0.timestamps();
      });
   }
}
