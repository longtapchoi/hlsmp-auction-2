package fr.maxlego08.zauctionhouse.api.option;

import fr.maxlego08.zauctionhouse.api.messages.Message;

public enum PlayerOption {
   BROADCAST_SELL("broadcast_sell", "true", Message.OPTION_BROADCAST_SELL_ENABLED, Message.OPTION_BROADCAST_SELL_DISABLED),
   BROADCAST_PURCHASE("broadcast_purchase", "true", Message.OPTION_BROADCAST_PURCHASE_ENABLED, Message.OPTION_BROADCAST_PURCHASE_DISABLED);

   private final String key;
   private final String defaultValue;
   private final Message enabledMessage;
   private final Message disabledMessage;

   private PlayerOption(String var3, String var4, Message var5, Message var6) {
      this.key = var3;
      this.defaultValue = var4;
      this.enabledMessage = var5;
      this.disabledMessage = var6;
   }

   public String getKey() {
      return this.key;
   }

   public String getDefaultValue() {
      return this.defaultValue;
   }

   public Message getEnabledMessage() {
      return this.enabledMessage;
   }

   public Message getDisabledMessage() {
      return this.disabledMessage;
   }

   public boolean isDefaultValue(String var1) {
      return this.defaultValue.equalsIgnoreCase(var1);
   }

   public static PlayerOption fromKey(String var0) {
      for(PlayerOption var4 : values()) {
         if (var4.key.equalsIgnoreCase(var0)) {
            return var4;
         }
      }

      return null;
   }

   // $FF: synthetic method
   private static PlayerOption[] $values() {
      return new PlayerOption[]{BROADCAST_SELL, BROADCAST_PURCHASE};
   }
}
