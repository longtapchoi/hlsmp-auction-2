package fr.maxlego08.zauctionhouse.hooks.mmoitems;

import fr.maxlego08.zauctionhouse.api.rules.ItemRuleContext;
import fr.maxlego08.zauctionhouse.api.rules.Rule;
import io.lumine.mythic.lib.api.item.NBTItem;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import org.bukkit.inventory.ItemStack;

public class MMOItemsRule implements Rule {
   private final List<String> itemIds;
   private final List<Pattern> patterns;

   public MMOItemsRule(List<String> var1) {
      this.itemIds = var1.stream().filter((var0) -> !var0.contains("*")).map((var0) -> var0.toUpperCase(Locale.ROOT)).toList();
      this.patterns = var1.stream().filter((var0) -> var0.contains("*")).map(this::wildcardToPattern).toList();
   }

   private Pattern wildcardToPattern(String var1) {
      String var2 = var1.toUpperCase(Locale.ROOT).replace(".", "\\.").replace("*", ".*");
      return Pattern.compile("^" + var2 + "$");
   }

   public boolean matches(ItemRuleContext var1) {
      ItemStack var2 = var1.getItemStack();
      if (var2 == null) {
         return false;
      } else {
         NBTItem var3 = NBTItem.get(var2);
         if (!var3.hasType()) {
            return false;
         } else {
            String var4 = var3.getType();
            String var5 = var3.getString("MMOITEMS_ITEM_ID");
            if (var4 != null && var5 != null) {
               String var10000 = var4.toUpperCase(Locale.ROOT);
               String var6 = var10000 + ":" + var5.toUpperCase(Locale.ROOT);
               String var7 = var5.toUpperCase(Locale.ROOT);
               if (!this.itemIds.contains(var6) && !this.itemIds.contains(var7)) {
                  for(Pattern var9 : this.patterns) {
                     if (var9.matcher(var6).matches() || var9.matcher(var7).matches()) {
                        return true;
                     }
                  }

                  return false;
               } else {
                  return true;
               }
            } else {
               return false;
            }
         }
      }
   }

   public boolean isValid() {
      return !this.itemIds.isEmpty() || !this.patterns.isEmpty();
   }
}
