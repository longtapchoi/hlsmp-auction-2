package fr.maxlego08.zauctionhouse.hooks.nova;

import fr.maxlego08.zauctionhouse.api.rules.ItemRuleContext;
import fr.maxlego08.zauctionhouse.api.rules.Rule;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import org.bukkit.inventory.ItemStack;
import xyz.xenondevs.nova.api.Nova;
import xyz.xenondevs.nova.api.item.NovaItem;

public class NovaRule implements Rule {
   private final List<String> itemIds;
   private final List<Pattern> patterns;

   public NovaRule(List<String> var1) {
      this.itemIds = var1.stream().filter((var0) -> !var0.contains("*")).map((var0) -> var0.toLowerCase(Locale.ROOT)).toList();
      this.patterns = var1.stream().filter((var0) -> var0.contains("*")).map(this::wildcardToPattern).toList();
   }

   private Pattern wildcardToPattern(String var1) {
      String var2 = var1.toLowerCase(Locale.ROOT).replace(".", "\\.").replace("*", ".*");
      return Pattern.compile("^" + var2 + "$");
   }

   public boolean matches(ItemRuleContext var1) {
      ItemStack var2 = var1.getItemStack();
      if (var2 == null) {
         return false;
      } else {
         NovaItem var3 = Nova.getNova().getItemRegistry().getOrNull(var2);
         if (var3 == null) {
            return false;
         } else {
            String var4 = var3.getId().toString().toLowerCase(Locale.ROOT);
            if (this.itemIds.contains(var4)) {
               return true;
            } else {
               for(Pattern var6 : this.patterns) {
                  if (var6.matcher(var4).matches()) {
                     return true;
                  }
               }

               return false;
            }
         }
      }
   }

   public boolean isValid() {
      return !this.itemIds.isEmpty() || !this.patterns.isEmpty();
   }
}
