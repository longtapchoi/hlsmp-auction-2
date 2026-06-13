package fr.maxlego08.zauctionhouse.hooks.executableblocks;

import com.ssomar.score.api.executableblocks.ExecutableBlocksAPI;
import com.ssomar.score.api.executableblocks.config.ExecutableBlockInterface;
import fr.maxlego08.zauctionhouse.api.rules.ItemRuleContext;
import fr.maxlego08.zauctionhouse.api.rules.Rule;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;
import org.bukkit.inventory.ItemStack;

public class ExecutableBlocksRule implements Rule {
   private final List<String> blockIds;
   private final List<Pattern> patterns;

   public ExecutableBlocksRule(List<String> var1) {
      this.blockIds = var1.stream().filter((var0) -> !var0.contains("*")).map((var0) -> var0.toLowerCase(Locale.ROOT)).toList();
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
         Optional var3 = ExecutableBlocksAPI.getExecutableBlocksManager().getExecutableBlock(var2);
         if (var3.isEmpty()) {
            return false;
         } else {
            String var4 = ((ExecutableBlockInterface)var3.get()).getId().toLowerCase(Locale.ROOT);
            if (this.blockIds.contains(var4)) {
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
      return !this.blockIds.isEmpty() || !this.patterns.isEmpty();
   }
}
