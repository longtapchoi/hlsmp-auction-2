package fr.maxlego08.zauctionhouse.rule.rules;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.rules.ItemRuleContext;
import fr.maxlego08.zauctionhouse.api.rules.Rule;
import fr.maxlego08.zauctionhouse.utils.tags.TagRegistry;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import org.bukkit.Material;
import org.bukkit.Tag;

public class TagRule implements Rule {
   private final List<Tag<Material>> tags;

   public TagRule(AuctionPlugin var1, List<String> var2) {
      this.tags = var2.stream().map((var1x) -> resolveTag(var1, var1x)).filter(Objects::nonNull).toList();
   }

   private static Tag<Material> resolveTag(AuctionPlugin var0, String var1) {
      if (var1 == null) {
         return null;
      } else {
         try {
            return TagRegistry.getTag(var1.toLowerCase(Locale.ROOT));
         } catch (Exception var3) {
            var0.getLogger().severe("Failed to resolve tag '" + var1 + "': " + var3.getMessage());
            return null;
         }
      }
   }

   public boolean matches(ItemRuleContext var1) {
      Material var2 = var1.getMaterial();

      for(Tag var4 : this.tags) {
         if (var4.isTagged(var2)) {
            return true;
         }
      }

      return false;
   }

   public boolean isValid() {
      return !this.tags.isEmpty();
   }
}
