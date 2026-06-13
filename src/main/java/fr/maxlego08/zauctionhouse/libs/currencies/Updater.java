package fr.maxlego08.zauctionhouse.libs.currencies;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Logger;

public class Updater {
   public static void checkUpdates() {
      if (!isUpToDate()) {
         Logger.getLogger("CurrenciesAPI").warning("The framework is not up to date, the latest version is " + fetchLatestVersion());
      }

   }

   public static String getVersion() {
      Properties var0 = new Properties();

      try {
         var0.load(Updater.class.getClassLoader().getResourceAsStream("currenciesapi.properties"));
         return var0.getProperty("version");
      } catch (IOException var2) {
         throw new RuntimeException(var2);
      }
   }

   public static boolean isUpToDate() {
      try {
         String var0 = fetchLatestVersion();
         return getVersion().equals(var0);
      } catch (Exception var1) {
         return false;
      }
   }

   public static String fetchLatestVersion() {
      try {
         URL var0 = URI.create("https://api.github.com/repos/Traqueur-dev/CurrenciesAPI/releases/latest").toURL();
         String var1 = getString(var0);
         int var2 = var1.indexOf("\"tag_name\"");
         int var3 = var1.indexOf(34, var2 + 10) + 1;
         int var4 = var1.indexOf(34, var3);
         return var1.substring(var3, var4);
      } catch (Exception var5) {
         return null;
      }
   }

   private static String getString(URL var0) {
      HttpURLConnection var1 = (HttpURLConnection)var0.openConnection();
      var1.setRequestMethod("GET");
      StringBuilder var2 = new StringBuilder();

      try {
         Scanner var3 = new Scanner(var1.getInputStream());

         try {
            while(var3.hasNext()) {
               var2.append(var3.nextLine());
            }
         } catch (Throwable var11) {
            try {
               var3.close();
            } catch (Throwable var10) {
               var11.addSuppressed(var10);
            }

            throw var11;
         }

         var3.close();
      } finally {
         var1.disconnect();
      }

      return var2.toString();
   }
}
