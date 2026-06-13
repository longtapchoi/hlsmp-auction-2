package fr.maxlego08.zauctionhouse.api.messages;

public enum MessageType {
   ACTION,
   TCHAT,
   TITLE,
   CENTER,
   NONE,
   WITHOUT_PREFIX,
   BOSSBAR;

   public static MessageType fromString(String var0) {
      try {
         return valueOf(var0.toUpperCase());
      } catch (Exception var2) {
         return null;
      }
   }

   // $FF: synthetic method
   private static MessageType[] $values() {
      return new MessageType[]{ACTION, TCHAT, TITLE, CENTER, NONE, WITHOUT_PREFIX, BOSSBAR};
   }
}
