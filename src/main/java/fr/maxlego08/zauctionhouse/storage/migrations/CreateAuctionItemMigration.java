package fr.maxlego08.zauctionhouse.storage.migrations;

import fr.maxlego08.zauctionhouse.libs.sarah.database.Migration;

public class CreateAuctionItemMigration extends Migration {
   public void up() {
      this.create("%prefix%auction_items", (var0) -> {
         var0.autoIncrement("id");
         var0.integer("item_id").foreignKey("%prefix%items", "id", true);
         var0.longText("itemstack");
         var0.timestamps();
      });
   }
}
