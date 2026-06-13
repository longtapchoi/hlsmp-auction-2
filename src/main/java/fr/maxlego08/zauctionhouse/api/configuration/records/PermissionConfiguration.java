package fr.maxlego08.zauctionhouse.api.configuration.records;

import fr.maxlego08.menu.api.utils.TypedMapAccessor;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.item.ItemType;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.permissions.Permissible;

public record PermissionConfiguration(EnumMap<ItemType, Map<String, Integer>> permissions) {
   public static PermissionConfiguration of(AuctionPlugin var0, FileConfiguration var1) {
      EnumMap<ItemType, Map<String, Integer>> var2 = new EnumMap<>(ItemType.class);

      for(ItemType var6 : ItemType.values()) {
         List<Map<?, ?>> var7 = var1.getMapList("permissions." + var6.name().toLowerCase());
         HashMap<String, Integer> var8 = new HashMap<>();

         for(Map<?, ?> var10 : var7) {
            TypedMapAccessor var11 = new TypedMapAccessor((Map<String, Object>)(Map<?, ?>) var10);
            String var12 = var11.getString("permission");
            int var13 = var11.getInt("limit");
            var8.put(var12, var13);
         }

         var2.put(var6, var8);
      }

      return new PermissionConfiguration(var2);
   }

   public int getLimit(ItemType var1, Permissible var2) {
      return this.permissions.get(var1).entrySet().stream().filter((var1x) -> var2.hasPermission(var1x.getKey())).mapToInt((e) -> (Integer) e.getValue()).max().orElse(0);
   }
}
