package fr.maxlego08.zauctionhouse.utils;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.messages.Message;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class VersionChecker extends ZUtils implements Listener {
   private final String URL_API = "https://groupez.dev/api/v1/resource/version/%s";
   private final String URL_RESOURCE = "https://groupez.dev/resources/%s";
   private final AuctionPlugin plugin;
   private final int pluginID;
   private boolean useLastVersion = false;
   private String lastVersion;

   public VersionChecker(AuctionPlugin var1, int var2) {
      this.plugin = var1;
      this.pluginID = var2;
      this.lastVersion = var1.getPluginMeta().getVersion();
   }

   public void unregister() {
      HandlerList.unregisterAll(this);
   }

   public void useLastVersion() {
      Bukkit.getPluginManager().registerEvents(this, this.plugin);
      String var1 = this.plugin.getDescription().getVersion();
      AtomicBoolean var2 = new AtomicBoolean();
      this.getVersion((var3) -> {
         long var4 = Long.parseLong(var3.replace(".", ""));
         long var6 = Long.parseLong(var1.replace(".", ""));
         var2.set(var6 >= var4);
         this.useLastVersion = var2.get();
         this.lastVersion = var3;
         if (this.useLastVersion) {
            this.plugin.getLogger().info("No update available.");
         } else {
            this.plugin.getLogger().info("New update available. Your version: " + var1 + ", latest version: " + var3);
            Logger var10000 = this.plugin.getLogger();
            Object[] var10002 = new Object[]{this.pluginID};
            var10000.info("Download plugin here: " + String.format("https://groupez.dev/resources/%s", var10002));
         }

      });
   }

   @EventHandler
   public void onConnect(PlayerJoinEvent var1) {
      Player var2 = var1.getPlayer();
      if (!this.useLastVersion && var1.getPlayer().hasPermission("zplugin.notifs")) {
         this.plugin.getScheduler().runAtLocationLater(var2.getLocation(), () -> {
            String var2x = this.plugin.getPluginMeta().getVersion();
            this.message(this.plugin, var2, Message.VERSION_AVAILABLE, new Object[]{"%version%", var2x, "%latest%", this.lastVersion});
         }, 20L);
      }

   }

   public void getVersion(Consumer<String> var1) {
      this.plugin.getScheduler().runAsync((var2) -> {
         String var3 = String.format("https://groupez.dev/api/v1/resource/version/%s", this.pluginID);

         try {
            URL var4 = URI.create(var3).toURL();
            URLConnection var5 = var4.openConnection();
            var5.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
            Scanner var6 = new Scanner(var5.getInputStream());

            try {
               if (var6.hasNext()) {
                  var1.accept(var6.next());
               }
            } catch (Throwable var10) {
               try {
                  var6.close();
               } catch (Throwable var9) {
                  var10.addSuppressed(var9);
               }

               throw var10;
            }

            var6.close();
         } catch (IOException var11) {
            this.plugin.getLogger().info("Cannot look for updates: " + var11.getMessage());
         }

      });
   }
}
