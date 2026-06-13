package fr.maxlego08.zauctionhouse.storage.repository.repositories;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.economy.AuctionEconomy;
import fr.maxlego08.zauctionhouse.api.item.Item;
import fr.maxlego08.zauctionhouse.api.item.ItemType;
import fr.maxlego08.zauctionhouse.api.item.StorageType;
import fr.maxlego08.zauctionhouse.api.storage.Repository;
import fr.maxlego08.zauctionhouse.api.storage.dto.ItemDTO;
import fr.maxlego08.zauctionhouse.libs.sarah.DatabaseConnection;
import fr.maxlego08.zauctionhouse.libs.sarah.database.Schema;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import org.bukkit.entity.Player;

public class ItemRepository extends Repository {
   public ItemRepository(AuctionPlugin var1, DatabaseConnection var2) {
      super(var1, var2, "%prefix%items");
   }

   public int create(Player var1, ItemType var2, BigDecimal var3, long var4, AuctionEconomy var6) {
      return this.create(var1.getUniqueId(), var2, var3, var4, var6);
   }

   public int create(UUID var1, ItemType var2, BigDecimal var3, long var4, AuctionEconomy var6) {
      Date var7 = new Date(var4);
      String var8 = this.plugin.getConfiguration().getServerName();
      return this.insertSync((var6x) -> {
         var6x.string("item_type", var2.name());
         var6x.uuid("seller_unique_id", var1);
         var6x.string("economy_name", var6.getName());
         var6x.decimal("price", var3);
         var6x.object("expired_at", var7);
         var6x.object("storage_type", StorageType.LISTED.name());
         var6x.string("server_name", var8);
      });
   }

   public List<ItemDTO> select() {
      return this.select(ItemDTO.class, (var0) -> var0.where("storage_type", "!=", StorageType.DELETED.name()));
   }

   public void updateItem(Item var1, StorageType var2) {
      this.update(this.createUpdateSchema(var1, var2));
   }

   public Optional<ItemDTO> select(int var1) {
      return this.select(ItemDTO.class, (var1x) -> {
         var1x.where("id", (Object)var1);
         var1x.where("storage_type", "!=", StorageType.DELETED.name());
      }).stream().findFirst();
   }

   public void updateItems(Map<StorageType, List<Item>> var1) {
      for(Map.Entry<StorageType, List<Item>> var3 : var1.entrySet()) {
         StorageType var4 = var3.getKey();
         List<Item> var5 = var3.getValue();
         if (!var5.isEmpty()) {
            for(Item var7 : var5) {
               this.update(this.createUpdateSchema(var7, var4));
            }
         }
      }

   }

   private Consumer<Schema> createUpdateSchema(Item var1, StorageType var2) {
      return (var2x) -> {
         var2x.where("id", (Object)var1.getId());
         var2x.string("storage_type", var2.name());
         if (var2 != StorageType.DELETED) {
            var2x.object("expired_at", var1.getExpiredAt());
         }

         if ((var2 == StorageType.PURCHASED || var2 == StorageType.DELETED) && var1.getBuyerUniqueId() != null) {
            var2x.uuid("buyer_unique_id", var1.getBuyerUniqueId());
         }

      };
   }

   public List<ItemDTO> select(List<String> var1) {
      return this.select(ItemDTO.class, (var1x) -> var1x.whereIn("id", var1));
   }
}
