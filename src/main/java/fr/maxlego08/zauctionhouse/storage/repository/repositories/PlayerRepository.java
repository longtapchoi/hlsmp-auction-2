package fr.maxlego08.zauctionhouse.storage.repository.repositories;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.storage.Repository;
import fr.maxlego08.zauctionhouse.api.storage.dto.PlayerDTO;
import fr.maxlego08.zauctionhouse.libs.sarah.DatabaseConnection;
import java.util.List;
import java.util.UUID;
import org.bukkit.entity.Player;

public class PlayerRepository extends Repository {
   public PlayerRepository(AuctionPlugin var1, DatabaseConnection var2) {
      super(var1, var2, "%prefix%players");
   }

   public void upsertPlayer(Player var1) {
      this.upsert((var1x) -> {
         var1x.uuid("unique_id", var1.getUniqueId()).primary();
         var1x.string("name", var1.getName());
      });
   }

   public void upsertPlayer(UUID var1, String var2) {
      this.upsert((var2x) -> {
         var2x.uuid("unique_id", var1).primary();
         var2x.string("name", var2);
      });
   }

   public List<PlayerDTO> select() {
      return this.selectAll(PlayerDTO.class);
   }

   public List<PlayerDTO> select(List<String> var1) {
      return this.select(PlayerDTO.class, (var1x) -> var1x.whereIn("unique_id", var1));
   }

   public String select(UUID var1) {
      return (String)this.select(PlayerDTO.class, (var1x) -> var1x.where("unique_id", (Object)var1.toString())).stream().findFirst().map(PlayerDTO::name).orElse((Object)null);
   }

   public UUID selectByName(String var1) {
      return (UUID)this.select(PlayerDTO.class, (var1x) -> var1x.where("name", (Object)var1)).stream().findFirst().map(PlayerDTO::unique_id).orElse((Object)null);
   }
}
