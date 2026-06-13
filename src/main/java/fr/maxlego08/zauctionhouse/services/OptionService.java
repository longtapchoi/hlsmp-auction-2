package fr.maxlego08.zauctionhouse.services;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.option.PlayerOption;
import fr.maxlego08.zauctionhouse.api.services.AuctionOptionService;
import fr.maxlego08.zauctionhouse.api.storage.dto.OptionDTO;
import fr.maxlego08.zauctionhouse.storage.repository.repositories.OptionRepository;
import fr.maxlego08.zauctionhouse.utils.ZUtils;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.entity.Player;

@SuppressWarnings({"unchecked", "rawtypes"})
public class OptionService extends ZUtils implements AuctionOptionService {
   private final AuctionPlugin plugin;
   private final Map<UUID, Map<PlayerOption, String>> optionsCache = new ConcurrentHashMap();

   public OptionService(AuctionPlugin var1) {
      this.plugin = var1;
   }

   public CompletableFuture<Void> loadPlayerOptions(UUID var1) {
      return CompletableFuture.runAsync(() -> {
         List var2 = ((OptionRepository)this.plugin.getStorageManager().with(OptionRepository.class)).selectAll(var1);
         EnumMap var3 = new EnumMap(PlayerOption.class);

         for(OptionDTO var5 : var2) {
            PlayerOption var6 = PlayerOption.fromKey(var5.option_name());
            if (var6 != null) {
               var3.put(var6, var5.option_value());
            }
         }

         this.optionsCache.put(var1, var3);
      }, this.plugin.getExecutorService());
   }

   public boolean getOption(UUID var1, PlayerOption var2) {
      Map var3 = (Map)this.optionsCache.get(var1);
      return var3 != null && var3.containsKey(var2) ? Boolean.parseBoolean((String)var3.get(var2)) : Boolean.parseBoolean(var2.getDefaultValue());
   }

   public boolean getOption(Player var1, PlayerOption var2) {
      return this.getOption(var1.getUniqueId(), var2);
   }

   public void setOption(UUID var1, PlayerOption var2, boolean var3) {
      String var4 = String.valueOf(var3);
      if (var2.isDefaultValue(var4)) {
         Map var5 = (Map)this.optionsCache.get(var1);
         if (var5 != null) {
            var5.remove(var2);
         }

         this.plugin.getScheduler().runAsync((var3x) -> ((OptionRepository)this.plugin.getStorageManager().with(OptionRepository.class)).deleteOption(var1, var2.getKey()));
      } else {
         ((Map)this.optionsCache.computeIfAbsent(var1, (var0) -> new EnumMap(PlayerOption.class))).put(var2, var4);
         this.plugin.getScheduler().runAsync((var4x) -> ((OptionRepository)this.plugin.getStorageManager().with(OptionRepository.class)).upsertOption(var1, var2.getKey(), var4));
      }

   }

   public CompletableFuture<Void> setOptionAsync(UUID var1, PlayerOption var2, String var3) {
      if (var2.isDefaultValue(var3)) {
         Map var4 = (Map)this.optionsCache.get(var1);
         if (var4 != null) {
            var4.remove(var2);
         }

         return CompletableFuture.runAsync(() -> ((OptionRepository)this.plugin.getStorageManager().with(OptionRepository.class)).deleteOption(var1, var2.getKey()), this.plugin.getExecutorService());
      } else {
         ((Map)this.optionsCache.computeIfAbsent(var1, (var0) -> new EnumMap(PlayerOption.class))).put(var2, var3);
         return CompletableFuture.runAsync(() -> ((OptionRepository)this.plugin.getStorageManager().with(OptionRepository.class)).upsertOption(var1, var2.getKey(), var3), this.plugin.getExecutorService());
      }
   }

   public Map<PlayerOption, String> getPlayerOptions(UUID var1) {
      Map var2 = (Map)this.optionsCache.get(var1);
      if (var2 != null) {
         return new EnumMap(var2);
      } else {
         EnumMap var3 = new EnumMap(PlayerOption.class);

         for(PlayerOption var7 : PlayerOption.values()) {
            var3.put(var7, var7.getDefaultValue());
         }

         return var3;
      }
   }

   public void resetPlayerOptions(UUID var1) {
      this.optionsCache.remove(var1);
      this.plugin.getScheduler().runAsync((var2) -> ((OptionRepository)this.plugin.getStorageManager().with(OptionRepository.class)).deleteAll(var1));
   }

   public void clearPlayerOptions(UUID var1) {
      this.optionsCache.remove(var1);
   }
}
