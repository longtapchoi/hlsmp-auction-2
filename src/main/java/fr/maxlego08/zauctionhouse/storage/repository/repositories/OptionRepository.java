package fr.maxlego08.zauctionhouse.storage.repository.repositories;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.storage.Repository;
import fr.maxlego08.zauctionhouse.api.storage.dto.OptionDTO;
import fr.maxlego08.sarah.DatabaseConnection;
import java.util.List;
import java.util.UUID;

public class OptionRepository extends Repository {
   public OptionRepository(AuctionPlugin var1, DatabaseConnection var2) {
      super(var1, var2, "%prefix%options");
   }

   public void upsertOption(UUID var1, String var2, String var3) {
      this.upsert((var3x) -> {
         var3x.uuid("player_unique_id", var1).primary();
         var3x.string("option_name", var2).primary();
         var3x.string("option_value", var3);
      });
   }

   public List<OptionDTO> selectAll(UUID var1) {
      return this.select(OptionDTO.class, (var1x) -> var1x.where("player_unique_id", (Object)var1.toString()));
   }

   public void deleteOption(UUID var1, String var2) {
      this.delete((var2x) -> var2x.where("player_unique_id", (Object)var1.toString()).where("option_name", (Object)var2));
   }

   public void deleteAll(UUID var1) {
      this.delete((var1x) -> var1x.where("player_unique_id", (Object)var1.toString()));
   }
}
