package fr.maxlego08.zauctionhouse.api.configuration.records;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.item.ItemType;
import java.util.EnumMap;
import java.util.List;
import org.bukkit.configuration.file.FileConfiguration;

@SuppressWarnings({"unchecked", "rawtypes"})
public record WorldConfiguration(EnumMap<ItemType, List<String>> worlds) {
   public static WorldConfiguration of(AuctionPlugin var0, FileConfiguration var1) {
      EnumMap var2 = new EnumMap(ItemType.class);

      for(ItemType var6 : ItemType.values()) {
         List var7 = var1.getStringList("banned-worlds." + var6.name().toLowerCase());
         var2.put(var6, var7);
      }

      return new WorldConfiguration(var2);
   }

   public boolean isWorldBanned(ItemType var1, String var2) {
      return ((List)this.worlds.get(var1)).contains(var2);
   }
}
