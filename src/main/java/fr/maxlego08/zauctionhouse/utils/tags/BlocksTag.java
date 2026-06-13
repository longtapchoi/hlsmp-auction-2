package fr.maxlego08.zauctionhouse.utils.tags;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public class BlocksTag implements Tag<Material> {
   public boolean isTagged(@NonNull Material var1) {
      return var1.isBlock();
   }

   public @NotNull Set<Material> getValues() {
      return (Set)Arrays.stream(Material.values()).filter((var0) -> var0.isBlock() && !var0.isLegacy()).collect(Collectors.toSet());
   }

   public @NotNull NamespacedKey getKey() {
      return new NamespacedKey("zauctionhouse", "blocks");
   }
}
