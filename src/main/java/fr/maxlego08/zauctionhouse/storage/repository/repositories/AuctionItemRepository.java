package fr.maxlego08.zauctionhouse.storage.repository.repositories;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.economy.AuctionEconomy;
import fr.maxlego08.zauctionhouse.api.item.items.AuctionItem;
import fr.maxlego08.zauctionhouse.api.storage.Repository;
import fr.maxlego08.zauctionhouse.api.storage.dto.AuctionItemDTO;
import fr.maxlego08.zauctionhouse.api.utils.Base64ItemStack;
import fr.maxlego08.zauctionhouse.items.ZAuctionItem;
import fr.maxlego08.zauctionhouse.libs.sarah.DatabaseConnection;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AuctionItemRepository extends Repository {
   public AuctionItemRepository(AuctionPlugin var1, DatabaseConnection var2) {
      super(var1, var2, "%prefix%auction_items");
   }

   public AuctionItem create(Player var1, int var2, BigDecimal var3, long var4, List<ItemStack> var6, AuctionEconomy var7) {
      return this.create(var1.getUniqueId(), var1.getName(), var2, var3, var4, var6, var7);
   }

   public AuctionItem create(UUID var1, String var2, int var3, BigDecimal var4, long var5, List<ItemStack> var7, AuctionEconomy var8) {
      for(ItemStack var10 : var7) {
         this.insert((var2x) -> {
            var2x.object("item_id", var3);
            var2x.string("itemstack", Base64ItemStack.encode(var10));
         });
      }

      return new ZAuctionItem(this.plugin, var3, this.plugin.getConfiguration().getServerName(), var1, var2, var4, var8, new Date(), new Date(var5), var7);
   }

   public List<AuctionItemDTO> select(List<String> var1) {
      return var1.isEmpty() ? List.of() : this.select(AuctionItemDTO.class, (var1x) -> var1x.whereIn("item_id", var1));
   }
}
