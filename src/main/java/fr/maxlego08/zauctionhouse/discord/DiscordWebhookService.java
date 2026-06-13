package fr.maxlego08.zauctionhouse.discord;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.configuration.discord.DiscordConfiguration;
import fr.maxlego08.zauctionhouse.api.configuration.discord.DiscordEmbed;
import fr.maxlego08.zauctionhouse.api.item.items.AuctionItem;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;
import org.bukkit.entity.Player;

public class DiscordWebhookService {
   private final AuctionPlugin plugin;
   private final HttpClient httpClient;
   private DiscordConfiguration configuration;
   private ColorExtractor colorExtractor;

   public DiscordWebhookService(AuctionPlugin var1) {
      this.plugin = var1;
      this.httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10L)).build();
      this.loadConfiguration();
   }

   public void loadConfiguration() {
      this.configuration = DiscordConfiguration.of(this.plugin);
      if (this.configuration.enabled()) {
         this.plugin.getLogger().info("Discord webhooks enabled");
         if (this.configuration.extractDominantColor()) {
            this.colorExtractor = new ColorExtractor(this.plugin.getDataFolder(), this.plugin.getLogger(), this.configuration.defaultColor());
            this.plugin.getLogger().info("Dominant color extraction enabled (cache: .cache/material-colors.json)");
         } else {
            this.colorExtractor = null;
         }
      }

   }

   public boolean isEnabled() {
      return this.configuration != null && this.configuration.enabled();
   }

   public CompletableFuture<Void> notifyItemSold(Player var1, AuctionItem var2) {
      if (!this.isEnabled()) {
         return CompletableFuture.<Void>completedFuture(null);
      } else {
         DiscordConfiguration.WebhookConfiguration var3 = this.configuration.sellWebhook();
         return !var3.isValid() ? CompletableFuture.<Void>completedFuture(null) : this.sendWebhook(var3, var2, var1, (Player)null);
      }
   }

   public CompletableFuture<Void> notifyItemPurchased(Player var1, AuctionItem var2) {
      if (!this.isEnabled()) {
         return CompletableFuture.<Void>completedFuture(null);
      } else {
         DiscordConfiguration.WebhookConfiguration var3 = this.configuration.purchaseWebhook();
         return !var3.isValid() ? CompletableFuture.<Void>completedFuture(null) : this.sendWebhook(var3, var2, (Player)null, var1);
      }
   }

   private CompletableFuture<Void> sendWebhook(DiscordConfiguration.WebhookConfiguration var1, AuctionItem var2, Player var3, Player var4) {
      return CompletableFuture.runAsync(() -> {
         try {
            DiscordPlaceholderResolver var5 = new DiscordPlaceholderResolver(this.configuration.serverName(), var2, var3, var4, this.configuration.itemImageUrl(), this.configuration.extractDominantColor(), this.configuration.defaultColor(), this.colorExtractor);
            String var6 = this.buildWebhookJson(var1, var5);
            HttpRequest var7 = HttpRequest.newBuilder().uri(URI.create(var1.url())).header("Content-Type", "application/json").POST(BodyPublishers.ofString(var6)).timeout(Duration.ofSeconds(30L)).build();
            HttpResponse<String> var8 = this.httpClient.send(var7, BodyHandlers.ofString());
            if (var8.statusCode() >= 400) {
               Logger var10000 = this.plugin.getLogger();
               int var10001 = var8.statusCode();
               var10000.warning("Discord webhook failed with status " + var10001 + ": " + var8.body());
            }
         } catch (Exception var9) {
            this.plugin.getLogger().warning("Failed to send Discord webhook: " + var9.getMessage());
         }

      }, this.plugin.getExecutorService());
   }

   private String buildWebhookJson(DiscordConfiguration.WebhookConfiguration var1, DiscordPlaceholderResolver var2) {
      DiscordConfiguration.EmbedConfiguration var3 = var1.embed();
      String var4 = var2.resolve(var3.color());
      DiscordEmbed.Builder var5 = DiscordEmbed.builder().title(var2.resolve(var3.title())).description(var2.resolve(var3.description())).color(var4).timestamp(var3.timestamp());
      if (var1.hasUsername()) {
         var5.username(var2.resolve(var1.username()));
      }

      if (var1.hasAvatarUrl()) {
         var5.avatarUrl(var2.resolve(var1.avatarUrl()));
      }

      if (var1.hasContent()) {
         var5.content(var2.resolve(var1.content()));
      }

      for(DiscordConfiguration.FieldConfiguration var7 : var3.fields()) {
         var5.addField(var2.resolve(var7.name()), var2.resolve(var7.value()), var7.inline());
      }

      if (var3.footer().hasContent()) {
         var5.footer(var2.resolve(var3.footer().text()), var2.resolve(var3.footer().iconUrl()));
      }

      if (var3.author().hasContent()) {
         var5.author(var2.resolve(var3.author().name()), var2.resolve(var3.author().url()), var2.resolve(var3.author().iconUrl()));
      }

      if (var3.thumbnail().hasContent()) {
         var5.thumbnail(var2.resolve(var3.thumbnail().url()));
      }

      if (var3.image().hasContent()) {
         var5.image(var2.resolve(var3.image().url()));
      }

      return var5.build().toJson();
   }
}
