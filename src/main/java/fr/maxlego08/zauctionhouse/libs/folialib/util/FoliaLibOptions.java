package fr.maxlego08.zauctionhouse.libs.folialib.util;

public class FoliaLibOptions {
   private boolean disableNotifications = false;
   private boolean invalidTickDebugMode = false;
   private boolean useIsValidOnNonFolia = false;

   public boolean isDisableNotifications() {
      return this.disableNotifications;
   }

   public void disableNotifications() {
      this.disableNotifications = true;
   }

   public boolean isInvalidTickDebugMode() {
      return this.invalidTickDebugMode;
   }

   public void enableInvalidTickDebugMode() {
      this.invalidTickDebugMode = true;
   }

   public void disableInvalidTickDebugMode() {
      this.invalidTickDebugMode = false;
   }

   public boolean useIsValidOnNonFolia() {
      return this.useIsValidOnNonFolia;
   }

   public void disableIsValidOnNonFolia() {
      this.useIsValidOnNonFolia = false;
   }
}
