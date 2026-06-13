package fr.maxlego08.zauctionhouse.storage.repository.repositories;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.log.LogType;
import fr.maxlego08.zauctionhouse.api.storage.Repository;
import fr.maxlego08.zauctionhouse.api.storage.dto.LogDTO;
import fr.maxlego08.zauctionhouse.libs.sarah.DatabaseConnection;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class LogRepository extends Repository {
   public LogRepository(AuctionPlugin var1, DatabaseConnection var2) {
      super(var1, var2, "%prefix%logs");
   }

   public void createLog(LogType var1, int var2, UUID var3, UUID var4, String var5, BigDecimal var6, String var7, String var8, Date var9) {
      this.insert((var9x) -> {
         var9x.string("log_type", var1.name());
         var9x.object("item_id", var2);
         var9x.uuid("player_unique_id", var3);
         if (var4 != null) {
            var9x.uuid("target_unique_id", var4);
         }

         if (var5 != null) {
            var9x.string("itemstack", var5);
         }

         var9x.decimal("price", var6 == null ? BigDecimal.ZERO : var6);
         if (var7 != null) {
            var9x.string("economy_name", var7);
         }

         if (var8 != null) {
            var9x.string("additional_data", var8);
         }

         if (var9 != null) {
            var9x.object("readed_at", var9);
         }

      });
   }

   public List<LogDTO> selectByPlayer(UUID var1) {
      return this.select(LogDTO.class, (var1x) -> var1x.where("player_unique_id", (Object)var1.toString()).orderByDesc("created_at"));
   }

   public List<LogDTO> selectByTarget(UUID var1) {
      return this.select(LogDTO.class, (var1x) -> var1x.where("target_unique_id", (Object)var1.toString()).orderByDesc("created_at"));
   }

   public List<LogDTO> selectByPlayerOrTarget(UUID var1) {
      List<LogDTO> var2 = this.selectByPlayer(var1);
      List<fr.maxlego08.zauctionhouse.api.storage.dto.LogDTO> var3 = this.selectByTarget(var1);
      HashSet<Integer> var4 = new HashSet<>();
      ArrayList<fr.maxlego08.zauctionhouse.api.storage.dto.LogDTO> var5 = new ArrayList<>();

      for(LogDTO var7 : var2) {
         if (var4.add(var7.id())) {
            var5.add(var7);
         }
      }

      for(LogDTO var9 : var3) {
         if (var4.add(var9.id())) {
            var5.add(var9);
         }
      }

      var5.sort((logA, logB) -> logB.created_at().compareTo(logA.created_at()));
      return var5;
   }

   public List<LogDTO> selectUnreadSales(UUID var1) {
      return this.select(LogDTO.class, (var1x) -> var1x.where("target_unique_id", (Object)var1.toString()).where("log_type", (Object)LogType.PURCHASE.name()).whereNull("readed_at").orderByDesc("created_at"));
   }

   public List<LogDTO> selectSalesHistory(UUID var1) {
      return this.select(LogDTO.class, (var1x) -> var1x.where("target_unique_id", (Object)var1.toString()).where("log_type", (Object)LogType.PURCHASE.name()).orderByDesc("created_at"));
   }

   public void markAsRead(Collection<Integer> var1) {
      if (var1 != null && !var1.isEmpty()) {
         Date var2 = new Date();
         List var3 = var1.stream().map((var2x) -> this.createUpdateSchema((var2xx) -> {
               var2xx.where("id", (Object)var2x);
               var2xx.object("readed_at", var2);
            })).toList();
         this.update(var3);
      }
   }

   public void markAsRead(int var1) {
      this.markAsRead(List.of(var1));
   }

   public void markPurchaseLogsAsReadByItem(int var1, UUID var2) {
      this.update((var2x) -> {
         var2x.where("item_id", (Object)var1);
         var2x.where("target_unique_id", (Object)var2.toString());
         var2x.where("log_type", (Object)LogType.PURCHASE.name());
         var2x.whereNull("readed_at");
         var2x.object("readed_at", new Date());
      });
   }
}
