package fr.maxlego08.zauctionhouse.api.services;

import fr.maxlego08.zauctionhouse.api.economy.AuctionEconomy;
import fr.maxlego08.zauctionhouse.api.services.result.SellResult;
import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface AuctionSellService {
   int MAIN_HAND_SLOT = -1;

   CompletableFuture<SellResult> sellAuctionItems(Player var1, BigDecimal var2, long var3, Map<Integer, ItemStack> var5, AuctionEconomy var6);

   void openSellCommandInventory(Player var1, BigDecimal var2, AuctionEconomy var3);
}
