package fr.maxlego08.zauctionhouse.hooks.denizen;

import com.denizenscript.denizen.objects.ItemTag;
import fr.maxlego08.zauctionhouse.api.rules.ItemRuleContext;
import fr.maxlego08.zauctionhouse.api.rules.Rule;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import org.bukkit.inventory.ItemStack;

public class DenizenRule implements Rule {
   private final List<String> scriptNames;
   private final List<Pattern> patterns;

   public DenizenRule(List<String> var1) {
      this.scriptNames = var1.stream().filter((var0) -> !var0.contains("*")).map((var0) -> var0.toLowerCase(Locale.ROOT)).toList();
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
         ItemTag var3 = new ItemTag(var2);
         String var4 = var3.getScriptName();
         if (var4 != null && !var4.isEmpty()) {
            String var5 = var4.toLowerCase(Locale.ROOT);
            if (this.scriptNames.contains(var5)) {
               return true;
            } else {
               for(Pattern var7 : this.patterns) {
                  if (var7.matcher(var5).matches()) {
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
      return !this.scriptNames.isEmpty() || !this.patterns.isEmpty();
   }
}
