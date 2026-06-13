package fr.maxlego08.zauctionhouse.libs.sarah.logger;

public class JULogger {
   public static Logger from(final java.util.logging.Logger var0) {
      return new Logger() {
         public void info(String var1) {
            var0.info(var1);
         }
      };
   }
}
