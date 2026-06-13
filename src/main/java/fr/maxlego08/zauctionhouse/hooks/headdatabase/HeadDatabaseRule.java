package fr.maxlego08.zauctionhouse.hooks.headdatabase;

import fr.maxlego08.zauctionhouse.api.rules.ItemRuleContext;
import fr.maxlego08.zauctionhouse.api.rules.Rule;
import java.util.List;
import java.util.regex.Pattern;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.inventory.ItemStack;

public class HeadDatabaseRule implements Rule {
   private final HeadDatabaseAPI api = new HeadDatabaseAPI();
   private final List<String> headIds;
   private final List<Pattern> patterns;

   public HeadDatabaseRule(List<String> var1) {
      this.headIds = var1.stream().filter((var0) -> !var0.contains("*")).toList();
      this.patterns = var1.stream().filter((var0) -> var0.contains("*")).map(this::wildcardToPattern).toList();
   }

   private Pattern wildcardToPattern(String var1) {
      String var2 = var1.replace(".", "\\.").replace("*", ".*");
      return Pattern.compile("^" + var2 + "$");
   }

   public boolean matches(ItemRuleContext var1) {
      ItemStack var2 = var1.getItemStack();
      if (var2 == null) {
         return false;
      } else {
         String var3 = this.api.getItemID(var2);
         if (var3 != null && !var3.isEmpty()) {
            if (this.headIds.contains(var3)) {
               return true;
            } else {
               for(Pattern var5 : this.patterns) {
                  if (var5.matcher(var3).matches()) {
                     return true;
                  }
               }

               return false;
            }
         } else {
            return false;
         }
      }
   }

   public boolean isValid() {
      return !this.headIds.isEmpty() || !this.patterns.isEmpty();
   }
}
