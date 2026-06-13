package fr.maxlego08.zauctionhouse.api.rules;

public interface Rule {
   boolean matches(ItemRuleContext var1);

   default boolean isValid() {
      return true;
   }
}
