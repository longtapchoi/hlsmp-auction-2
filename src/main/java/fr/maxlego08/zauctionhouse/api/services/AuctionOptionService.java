package fr.maxlego08.zauctionhouse.api.services;

import fr.maxlego08.zauctionhouse.api.option.PlayerOption;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.bukkit.entity.Player;

public interface AuctionOptionService {
   CompletableFuture<Void> loadPlayerOptions(UUID var1);

   boolean getOption(UUID var1, PlayerOption var2);

   boolean getOption(Player var1, PlayerOption var2);

   void setOption(UUID var1, PlayerOption var2, boolean var3);

   CompletableFuture<Void> setOptionAsync(UUID var1, PlayerOption var2, String var3);

   Map<PlayerOption, String> getPlayerOptions(UUID var1);

   void resetPlayerOptions(UUID var1);

   void clearPlayerOptions(UUID var1);
}
