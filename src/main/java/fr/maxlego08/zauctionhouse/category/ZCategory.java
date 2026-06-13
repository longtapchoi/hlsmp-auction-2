package fr.maxlego08.zauctionhouse.category;

import fr.maxlego08.zauctionhouse.api.category.Category;
import fr.maxlego08.zauctionhouse.api.rules.ItemRuleContext;
import fr.maxlego08.zauctionhouse.api.rules.Rule;
import java.util.List;
import java.util.Objects;

public class ZCategory implements Category {
   private final String id;
   private final String displayName;
   private final List<Rule> rules;
   private final List<Rule> bannedRules;
   private final boolean miscellaneous;

   public ZCategory(String var1, String var2, List<Rule> var3, List<Rule> var4, boolean var5) {
      this.id = (String)Objects.requireNonNull(var1, "Category id cannot be null");
      this.displayName = var2 != null ? var2 : var1;
      this.rules = var3 != null ? List.copyOf(var3) : List.of();
      this.bannedRules = var4 != null ? List.copyOf(var4) : List.of();
      this.miscellaneous = var5;
   }

   public static ZCategory miscellaneous(String var0, String var1) {
      return new ZCategory(var0, var1, List.of(), List.of(), true);
   }

   public String getId() {
      return this.id;
   }

   public String getDisplayName() {
      return this.displayName;
   }

   public List<Rule> getRules() {
      return this.rules;
   }

   public List<Rule> getBannedRules() {
      return this.bannedRules;
   }

   public boolean isMiscellaneous() {
      return this.miscellaneous;
   }

   public boolean matches(ItemRuleContext var1) {
      if (var1 == null) {
         return false;
      } else {
         for(Rule var3 : this.bannedRules) {
            if (var3.matches(var1)) {
               return false;
            }
         }

         if (this.miscellaneous) {
            return true;
         } else {
            for(Rule var5 : this.rules) {
               if (var5.matches(var1)) {
                  return true;
               }
            }

            return false;
         }
      }
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 instanceof ZCategory) {
         ZCategory var2 = (ZCategory)var1;
         return Objects.equals(this.id, var2.id);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.id});
   }

   public String toString() {
      String var10000 = this.id;
      return "ZCategory{id='" + var10000 + "', displayName='" + this.displayName + "', rulesCount=" + this.rules.size() + ", miscellaneous=" + this.miscellaneous + "}";
   }
}
