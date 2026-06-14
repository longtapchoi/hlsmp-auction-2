package fr.maxlego08.zauctionhouse.storage.repository.repositories;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.item.Item;
import fr.maxlego08.zauctionhouse.api.storage.Repository;
import fr.maxlego08.zauctionhouse.api.storage.dto.TransactionDTO;
import fr.maxlego08.zauctionhouse.api.transaction.TransactionStatus;
import fr.maxlego08.sarah.DatabaseConnection;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class TransactionRepository extends Repository {
   public TransactionRepository(AuctionPlugin var1, DatabaseConnection var2) {
      super(var1, var2, "%prefix%transactions");
   }

   public void create(Item var1, UUID var2, String var3, BigDecimal var4, BigDecimal var5, BigDecimal var6, TransactionStatus var7) {
      this.insert((var7x) -> {
         var7x.object("item_id", var1.getId());
         var7x.uuid("player_unique_id", var2);
         var7x.string("economy_name", var3);
         var7x.decimal("before", var4);
         var7x.decimal("after", var5);
         var7x.decimal("value", var6);
         var7x.string("status", var7.name());
      });
   }

   public List<TransactionDTO> selectByPlayer(UUID var1) {
      return this.select(TransactionDTO.class, (var1x) -> var1x.where("player_unique_id", (Object)var1.toString()));
   }

   public List<TransactionDTO> selectByPlayerAndStatus(UUID var1, TransactionStatus var2) {
      return this.select(TransactionDTO.class, (var2x) -> var2x.where("player_unique_id", (Object)var1.toString()).where("status", (Object)var2.name()));
   }

   public void updateStatus(int var1, TransactionStatus var2) {
      this.updateStatus(List.of(var1), var2);
   }

   public void updateStatus(Collection<Integer> var1, TransactionStatus var2) {
      if (var1 != null && !var1.isEmpty()) {
         List var3 = var1.stream().map((var2x) -> this.createUpdateSchema((var2xx) -> {
               var2xx.where("id", (Object)var2x);
               var2xx.string("status", var2.name());
            })).toList();
         this.update(var3);
      }
   }
}
