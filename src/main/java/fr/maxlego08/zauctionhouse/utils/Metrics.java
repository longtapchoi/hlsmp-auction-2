package fr.maxlego08.zauctionhouse.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.zip.GZIPOutputStream;
import javax.net.ssl.HttpsURLConnection;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;

public class Metrics {
   public static final int B_STATS_VERSION = 1;
   private static final String URL = "https://bStats.org/submitData/bukkit";
   private boolean enabled;
   private static boolean logFailedRequests;
   private static boolean logSentData;
   private static boolean logResponseStatusText;
   private static String serverUUID;
   private final Plugin plugin;
   private final int pluginId;
   private final List<CustomChart> charts = new ArrayList();

   public Metrics(Plugin var1, int var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("Plugin cannot be null!");
      } else {
         this.plugin = var1;
         this.pluginId = var2;
         File var3 = new File(var1.getDataFolder().getParentFile(), "bStats");
         File var4 = new File(var3, "config.yml");
         YamlConfiguration var5 = YamlConfiguration.loadConfiguration(var4);
         if (!var5.isSet("serverUuid")) {
            var5.addDefault("enabled", true);
            var5.addDefault("serverUuid", UUID.randomUUID().toString());
            var5.addDefault("logFailedRequests", false);
            var5.addDefault("logSentData", false);
            var5.addDefault("logResponseStatusText", false);
            var5.options().header("bStats collects some data for plugin authors like how many servers are using their plugins.\nTo honor their work, you should not disable it.\nThis has nearly no effect on the server performance!\nCheck out https://bStats.org/ to learn more :)").copyDefaults(true);

            try {
               var5.save(var4);
            } catch (IOException var10) {
            }
         }

         this.enabled = var5.getBoolean("enabled", true);
         serverUUID = var5.getString("serverUuid");
         logFailedRequests = var5.getBoolean("logFailedRequests", false);
         logSentData = var5.getBoolean("logSentData", false);
         logResponseStatusText = var5.getBoolean("logResponseStatusText", false);
         if (this.enabled) {
            boolean var6 = false;

            for(Class var8 : Bukkit.getServicesManager().getKnownServices()) {
               try {
                  var8.getField("B_STATS_VERSION");
                  var6 = true;
                  break;
               } catch (NoSuchFieldException var11) {
               }
            }

            Bukkit.getServicesManager().register(Metrics.class, this, var1, ServicePriority.Normal);
            if (!var6) {
               this.startSubmitting();
            }
         }

      }
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void addCustomChart(CustomChart var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Chart cannot be null!");
      } else {
         this.charts.add(var1);
      }
   }

   private void startSubmitting() {
      final Timer var1 = new Timer(true);
      var1.scheduleAtFixedRate(new TimerTask() {
         public void run() {
            if (!Metrics.this.plugin.isEnabled()) {
               var1.cancel();
            } else {
               Bukkit.getScheduler().runTask(Metrics.this.plugin, () -> Metrics.this.submitData());
            }
         }
      }, 300000L, 1800000L);
   }

   public JsonObject getPluginData() {
      JsonObject var1 = new JsonObject();
      String var2 = "zAuctionHouseV2";
      String var3 = this.plugin.getDescription().getVersion();
      var1.addProperty("pluginName", var2);
      var1.addProperty("id", this.pluginId);
      var1.addProperty("pluginVersion", var3);
      JsonArray var4 = new JsonArray();

      for(CustomChart var6 : this.charts) {
         JsonObject var7 = var6.getRequestJsonObject();
         if (var7 != null) {
            var4.add(var7);
         }
      }

      var1.add("customCharts", var4);
      return var1;
   }

   private JsonObject getServerData() {
      int var1;
      try {
         Method var2 = Class.forName("org.bukkit.Server").getMethod("getOnlinePlayers");
         var1 = var2.getReturnType().equals(Collection.class) ? ((Collection)var2.invoke(Bukkit.getServer())).size() : ((Player[])var2.invoke(Bukkit.getServer())).length;
      } catch (Exception var11) {
         var1 = Bukkit.getOnlinePlayers().size();
      }

      int var12 = Bukkit.getOnlineMode() ? 1 : 0;
      String var3 = Bukkit.getVersion();
      String var4 = Bukkit.getName();
      String var5 = System.getProperty("java.version");
      String var6 = System.getProperty("os.name");
      String var7 = System.getProperty("os.arch");
      String var8 = System.getProperty("os.version");
      int var9 = Runtime.getRuntime().availableProcessors();
      JsonObject var10 = new JsonObject();
      var10.addProperty("serverUUID", serverUUID);
      var10.addProperty("playerAmount", var1);
      var10.addProperty("onlineMode", var12);
      var10.addProperty("bukkitVersion", var3);
      var10.addProperty("bukkitName", var4);
      var10.addProperty("javaVersion", var5);
      var10.addProperty("osName", var6);
      var10.addProperty("osArch", var7);
      var10.addProperty("osVersion", var8);
      var10.addProperty("coreCount", var9);
      return var10;
   }

   private void submitData() {
      JsonObject var1 = this.getServerData();
      JsonArray var2 = new JsonArray();

      for(Class var4 : Bukkit.getServicesManager().getKnownServices()) {
         try {
            var4.getField("B_STATS_VERSION");

            for(Object var6obj : Bukkit.getServicesManager().getRegistrations(var4)) {
               org.bukkit.plugin.RegisteredServiceProvider<?> var6 = (org.bukkit.plugin.RegisteredServiceProvider<?>) var6obj;
               try {
                  Object var7 = var6.getService().getMethod("getPluginData").invoke(var6.getProvider());
                  if (var7 instanceof JsonObject) {
                     var2.add((JsonObject)var7);
                  } else {
                     try {
                        Class var8 = Class.forName("org.json.simple.JSONObject");
                        if (var7.getClass().isAssignableFrom(var8)) {
                           Method var9 = var8.getDeclaredMethod("toJSONString");
                           var9.setAccessible(true);
                           String var10 = (String)var9.invoke(var7);
                           JsonObject var11 = (new JsonParser()).parse(var10).getAsJsonObject();
                           var2.add(var11);
                        }
                     } catch (ClassNotFoundException var12) {
                        if (logFailedRequests) {
                           this.plugin.getLogger().log(Level.SEVERE, "Encountered unexpected exception", var12);
                        }
                     }
                  }
               } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | NullPointerException var13) {
               }
            }
         } catch (NoSuchFieldException var14) {
         }
      }

      var1.add("plugins", var2);
      (new Thread(() -> {
         try {
            sendData(this.plugin, var1);
         } catch (Exception var3) {
            if (logFailedRequests) {
               this.plugin.getLogger().log(Level.WARNING, "Could not submit plugin stats of " + this.plugin.getName(), var3);
            }
         }

      })).start();
   }

   private static void sendData(Plugin var0, JsonObject var1) throws Exception {
      if (var1 == null) {
         throw new IllegalArgumentException("Data cannot be null!");
      } else if (Bukkit.isPrimaryThread()) {
         throw new IllegalAccessException("This method must not be called from the main thread!");
      } else {
         if (logSentData) {
            var0.getLogger().info("Sending data to bStats: " + String.valueOf(var1));
         }

         HttpsURLConnection var2 = (HttpsURLConnection)(new URL("https://bStats.org/submitData/bukkit")).openConnection();
         byte[] var3 = compress(var1.toString());
         var2.setRequestMethod("POST");
         var2.addRequestProperty("Accept", "application/json");
         var2.addRequestProperty("Connection", "close");
         var2.addRequestProperty("Content-Encoding", "gzip");
         var2.addRequestProperty("Content-Length", String.valueOf(var3.length));
         var2.setRequestProperty("Content-Type", "application/json");
         var2.setRequestProperty("User-Agent", "MC-Server/1");
         var2.setDoOutput(true);
         DataOutputStream var4 = new DataOutputStream(var2.getOutputStream());

         try {
            var4.write(var3);
         } catch (Throwable var10) {
            try {
               var4.close();
            } catch (Throwable var9) {
               var10.addSuppressed(var9);
            }

            throw var10;
         }

         var4.close();
         StringBuilder var12 = new StringBuilder();
         BufferedReader var5 = new BufferedReader(new InputStreamReader(var2.getInputStream()));

         String var6;
         try {
            while((var6 = var5.readLine()) != null) {
               var12.append(var6);
            }
         } catch (Throwable var11) {
            try {
               var5.close();
            } catch (Throwable var8) {
               var11.addSuppressed(var8);
            }

            throw var11;
         }

         var5.close();
         if (logResponseStatusText) {
            var0.getLogger().info("Sent data to bStats and received response: " + String.valueOf(var12));
         }

      }
   }

   private static byte[] compress(String var0) throws java.io.IOException {
      if (var0 == null) {
         return null;
      } else {
         ByteArrayOutputStream var1 = new ByteArrayOutputStream();
         GZIPOutputStream var2 = new GZIPOutputStream(var1);

         try {
            var2.write(var0.getBytes(StandardCharsets.UTF_8));
         } catch (Throwable var6) {
            try {
               var2.close();
            } catch (Throwable var5) {
               var6.addSuppressed(var5);
            }

            throw var6;
         }

         var2.close();
         return var1.toByteArray();
      }
   }

   static {
      if (System.getProperty("bstats.relocatecheck") == null || !System.getProperty("bstats.relocatecheck").equals("false")) {
         String var0 = new String(new byte[]{111, 114, 103, 46, 98, 115, 116, 97, 116, 115, 46, 98, 117, 107, 107, 105, 116});
         String var1 = new String(new byte[]{121, 111, 117, 114, 46, 112, 97, 99, 107, 97, 103, 101});
         if (Metrics.class.getPackage().getName().equals(var0) || Metrics.class.getPackage().getName().equals(var1)) {
            throw new IllegalStateException("bStats Metrics class has not been relocated correctly!");
         }
      }

   }

   public abstract static class CustomChart {
      final String chartId;

      CustomChart(String var1) {
         if (var1 != null && !var1.isEmpty()) {
            this.chartId = var1;
         } else {
            throw new IllegalArgumentException("ChartId cannot be null or empty!");
         }
      }

      private JsonObject getRequestJsonObject() {
         JsonObject var1 = new JsonObject();
         var1.addProperty("chartId", this.chartId);

         try {
            JsonObject var2 = this.getChartData();
            if (var2 == null) {
               return null;
            } else {
               var1.add("data", var2);
               return var1;
            }
         } catch (Throwable var3) {
            if (Metrics.logFailedRequests) {
               Bukkit.getLogger().log(Level.WARNING, "Failed to get data for custom chart with id " + this.chartId, var3);
            }

            return null;
         }
      }

      protected abstract JsonObject getChartData() throws Exception;
   }

   public static class SimplePie extends CustomChart {
      private final Callable<String> callable;

      public SimplePie(String var1, Callable<String> var2) {
         super(var1);
         this.callable = var2;
      }

      protected JsonObject getChartData() throws Exception {
         JsonObject var1 = new JsonObject();
         String var2 = (String)this.callable.call();
         if (var2 != null && !var2.isEmpty()) {
            var1.addProperty("value", var2);
            return var1;
         } else {
            return null;
         }
      }
   }

   public static class AdvancedPie extends CustomChart {
      private final Callable<Map<String, Integer>> callable;

      public AdvancedPie(String var1, Callable<Map<String, Integer>> var2) {
         super(var1);
         this.callable = var2;
      }

      protected JsonObject getChartData() throws Exception {
         JsonObject var1 = new JsonObject();
         JsonObject var2 = new JsonObject();
         Map<?, ?> var3 = (Map<?, ?>) this.callable.call();
         if (var3 != null && !var3.isEmpty()) {
            boolean var4 = true;

            for(Map.Entry<?, ?> var6 : var3.entrySet()) {
               if ((Integer) var6.getValue() != 0) {
                  var4 = false;
                  var2.addProperty(String.valueOf(var6.getKey()), (Number)var6.getValue());
               }
            }

            if (var4) {
               return null;
            } else {
               var1.add("values", var2);
               return var1;
            }
         } else {
            return null;
         }
      }
   }

   public static class DrilldownPie extends CustomChart {
      private final Callable<Map<String, Map<String, Integer>>> callable;

      public DrilldownPie(String var1, Callable<Map<String, Map<String, Integer>>> var2) {
         super(var1);
         this.callable = var2;
      }

      public JsonObject getChartData() throws Exception {
         JsonObject var1 = new JsonObject();
         JsonObject var2 = new JsonObject();
         Map<?, ?> var3 = (Map<?, ?>) this.callable.call();
         if (var3 != null && !var3.isEmpty()) {
            boolean var4 = true;

            for(Map.Entry<?, ?> var6 : var3.entrySet()) {
               JsonObject var7 = new JsonObject();
               boolean var8 = true;

               for(Map.Entry<?, ?> var10 : ((Map<?, ?>)var3.get(var6.getKey())).entrySet()) {
                  var7.addProperty(String.valueOf(var10.getKey()), (Number)var10.getValue());
                  var8 = false;
               }

               if (!var8) {
                  var4 = false;
                  var2.add(String.valueOf(var6.getKey()), var7);
               }
            }

            if (var4) {
               return null;
            } else {
               var1.add("values", var2);
               return var1;
            }
         } else {
            return null;
         }
      }
   }

   public static class SingleLineChart extends CustomChart {
      private final Callable<Integer> callable;

      public SingleLineChart(String var1, Callable<Integer> var2) {
         super(var1);
         this.callable = var2;
      }

      protected JsonObject getChartData() throws Exception {
         JsonObject var1 = new JsonObject();
         int var2 = (Integer)this.callable.call();
         if (var2 == 0) {
            return null;
         } else {
            var1.addProperty("value", var2);
            return var1;
         }
      }
   }

   public static class MultiLineChart extends CustomChart {
      private final Callable<Map<String, Integer>> callable;

      public MultiLineChart(String var1, Callable<Map<String, Integer>> var2) {
         super(var1);
         this.callable = var2;
      }

      protected JsonObject getChartData() throws Exception {
         JsonObject var1 = new JsonObject();
         JsonObject var2 = new JsonObject();
         Map<?, ?> var3 = (Map<?, ?>) this.callable.call();
         if (var3 != null && !var3.isEmpty()) {
            boolean var4 = true;

            for(Map.Entry<?, ?> var6 : var3.entrySet()) {
               if ((Integer) var6.getValue() != 0) {
                  var4 = false;
                  var2.addProperty(String.valueOf(var6.getKey()), (Number)var6.getValue());
               }
            }

            if (var4) {
               return null;
            } else {
               var1.add("values", var2);
               return var1;
            }
         } else {
            return null;
         }
      }
   }

   public static class SimpleBarChart extends CustomChart {
      private final Callable<Map<String, Integer>> callable;

      public SimpleBarChart(String var1, Callable<Map<String, Integer>> var2) {
         super(var1);
         this.callable = var2;
      }

      protected JsonObject getChartData() throws Exception {
         JsonObject var1 = new JsonObject();
         JsonObject var2 = new JsonObject();
         Map<?, ?> var3 = (Map<?, ?>) this.callable.call();
         if (var3 != null && !var3.isEmpty()) {
            for(Map.Entry<?, ?> var5 : var3.entrySet()) {
               JsonArray var6 = new JsonArray();
               var6.add(new JsonPrimitive((Number)var5.getValue()));
               var2.add(String.valueOf(var5.getKey()), var6);
            }

            var1.add("values", var2);
            return var1;
         } else {
            return null;
         }
      }
   }

   public static class AdvancedBarChart extends CustomChart {
      private final Callable<Map<String, int[]>> callable;

      public AdvancedBarChart(String var1, Callable<Map<String, int[]>> var2) {
         super(var1);
         this.callable = var2;
      }

      protected JsonObject getChartData() throws Exception {
         JsonObject var1 = new JsonObject();
         JsonObject var2 = new JsonObject();
         Map<?, ?> var3 = (Map<?, ?>) this.callable.call();
         if (var3 != null && !var3.isEmpty()) {
            boolean var4 = true;

            for(Map.Entry<?, ?> var6 : var3.entrySet()) {
               if (((int[])var6.getValue()).length != 0) {
                  var4 = false;
                  JsonArray var7 = new JsonArray();

                  for(int var11 : (int[])var6.getValue()) {
                     var7.add(new JsonPrimitive(var11));
                  }

                  var2.add(String.valueOf(var6.getKey()), var7);
               }
            }

            if (var4) {
               return null;
            } else {
               var1.add("values", var2);
               return var1;
            }
         } else {
            return null;
         }
      }
   }
}
