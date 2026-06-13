package fr.maxlego08.zauctionhouse.discord;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class ColorExtractor {
   private static final Type MAP_TYPE = (new TypeToken<Map<String, String>>() {
   }).getType();
   private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
   private final HttpClient httpClient;
   private final Logger logger;
   private final Map<String, String> colorCache = new ConcurrentHashMap();
   private final String defaultColor;
   private final File cacheFile;

   public ColorExtractor(File var1, Logger var2, String var3) {
      this.logger = var2;
      this.defaultColor = var3;
      this.httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10L)).build();
      File var4 = new File(var1, ".cache");
      if (!var4.exists()) {
         var4.mkdirs();
      }

      this.cacheFile = new File(var4, "material-colors.json");
      this.loadCache();
   }

   private void loadCache() {
      if (this.cacheFile.exists()) {
         try {
            FileReader var1 = new FileReader(this.cacheFile);

            try {
               Map var2 = (Map)GSON.fromJson(var1, MAP_TYPE);
               if (var2 != null) {
                  this.colorCache.putAll(var2);
                  this.logger.info("Loaded " + this.colorCache.size() + " cached material colors from material-colors.json");
               }
            } catch (Throwable var5) {
               try {
                  var1.close();
               } catch (Throwable var4) {
                  var5.addSuppressed(var4);
               }

               throw var5;
            }

            var1.close();
         } catch (Exception var6) {
            this.logger.warning("Failed to load color cache: " + var6.getMessage());
         }

      }
   }

   private void saveCache() {
      try {
         FileWriter var1 = new FileWriter(this.cacheFile);

         try {
            GSON.toJson(this.colorCache, var1);
         } catch (Throwable var5) {
            try {
               var1.close();
            } catch (Throwable var4) {
               var5.addSuppressed(var4);
            }

            throw var5;
         }

         var1.close();
      } catch (Exception var6) {
         this.logger.warning("Failed to save color cache: " + var6.getMessage());
      }

   }

   public String getColorForMaterial(String var1, String var2) {
      if (var1 != null && !var1.isEmpty()) {
         String var3 = (String)this.colorCache.get(var1);
         if (var3 != null) {
            return var3;
         } else {
            String var4 = this.extractColorFromUrl(var2);
            this.colorCache.put(var1, var4);
            this.saveCache();
            return var4;
         }
      } else {
         return this.defaultColor;
      }
   }

   private String extractColorFromUrl(String var1) {
      if (var1 != null && !var1.isEmpty()) {
         try {
            HttpRequest var2 = HttpRequest.newBuilder().uri(URI.create(var1)).timeout(Duration.ofSeconds(10L)).GET().build();
            HttpResponse var3 = this.httpClient.send(var2, BodyHandlers.ofByteArray());
            if (var3.statusCode() != 200) {
               return this.defaultColor;
            } else {
               BufferedImage var4 = ImageIO.read(new ByteArrayInputStream((byte[])var3.body()));
               return var4 == null ? this.defaultColor : this.calculateDominantColor(var4);
            }
         } catch (Exception var5) {
            this.logger.warning("Failed to extract dominant color from " + var1 + ": " + var5.getMessage());
            return this.defaultColor;
         }
      } else {
         return this.defaultColor;
      }
   }

   private String calculateDominantColor(BufferedImage var1) {
      HashMap var2 = new HashMap();
      int var3 = var1.getWidth();
      int var4 = var1.getHeight();
      int var5 = Math.max(1, Math.min(var3, var4) / 50);

      for(int var6 = 0; var6 < var4; var6 += var5) {
         for(int var7 = 0; var7 < var3; var7 += var5) {
            int var8 = var1.getRGB(var7, var6);
            int var9 = var8 >> 24 & 255;
            if (var9 >= 128) {
               int var10 = this.quantizeColor(var8);
               var2.merge(var10, 1, Integer::sum);
            }
         }
      }

      if (var2.isEmpty()) {
         return this.defaultColor;
      } else {
         int var11 = (Integer)var2.entrySet().stream().max(Entry.comparingByValue()).map(Map.Entry::getKey).orElse(16777215);
         int var12 = var11 >> 16 & 255;
         int var13 = var11 >> 8 & 255;
         int var14 = var11 & 255;
         return String.format("#%02X%02X%02X", var12, var13, var14);
      }
   }

   private int quantizeColor(int var1) {
      int var2 = var1 >> 16 & 255;
      int var3 = var1 >> 8 & 255;
      int var4 = var1 & 255;
      var2 = var2 / 8 * 8;
      var3 = var3 / 8 * 8;
      var4 = var4 / 8 * 8;
      return var2 << 16 | var3 << 8 | var4;
   }
}
