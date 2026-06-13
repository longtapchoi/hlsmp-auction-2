package fr.maxlego08.zauctionhouse.api.configuration.discord;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bukkit.configuration.file.YamlConfiguration;

@SuppressWarnings({"unchecked", "rawtypes"})
public record DiscordConfiguration(boolean enabled, String serverName, String itemImageUrl, boolean extractDominantColor, String defaultColor, WebhookConfiguration sellWebhook, WebhookConfiguration purchaseWebhook) {
   public static DiscordConfiguration of(AuctionPlugin var0) {
      File var1 = new File(var0.getDataFolder(), "discord.yml");
      if (!var1.exists()) {
         var0.saveFile("discord.yml", false);
      }

      YamlConfiguration var2 = YamlConfiguration.loadConfiguration(var1);
      boolean var3 = var2.getBoolean("enabled", false);
      String var4 = var2.getString("server-name", "My Server");
      String var5 = var2.getString("item-image-url", "https://img.groupez.dev/minecraft/%item_material%.png");
      boolean var6 = var2.getBoolean("extract-dominant-color", true);
      String var7 = var2.getString("default-color", "#5865F2");
      WebhookConfiguration var8 = DiscordConfiguration.WebhookConfiguration.of(var2, "webhooks.sell");
      WebhookConfiguration var9 = DiscordConfiguration.WebhookConfiguration.of(var2, "webhooks.purchase");
      return new DiscordConfiguration(var3, var4, var5, var6, var7, var8, var9);
   }

   public static record WebhookConfiguration(boolean enabled, String url, String username, String avatarUrl, String content, EmbedConfiguration embed) {
      public static WebhookConfiguration of(YamlConfiguration var0, String var1) {
         boolean var2 = var0.getBoolean(var1 + ".enabled", false);
         String var3 = var0.getString(var1 + ".url", "");
         String var4 = var0.getString(var1 + ".username", "");
         String var5 = var0.getString(var1 + ".avatar-url", "");
         String var6 = var0.getString(var1 + ".content", "");
         EmbedConfiguration var7 = DiscordConfiguration.EmbedConfiguration.of(var0, var1 + ".embed");
         return new WebhookConfiguration(var2, var3, var4, var5, var6, var7);
      }

      public boolean isValid() {
         return this.enabled && this.url != null && !this.url.isEmpty() && this.url.startsWith("https://discord.com/api/webhooks/");
      }

      public boolean hasUsername() {
         return this.username != null && !this.username.isEmpty();
      }

      public boolean hasAvatarUrl() {
         return this.avatarUrl != null && !this.avatarUrl.isEmpty();
      }

      public boolean hasContent() {
         return this.content != null && !this.content.isEmpty();
      }
   }

   public static record EmbedConfiguration(String title, String description, String color, List<FieldConfiguration> fields, FooterConfiguration footer, AuthorConfiguration author, ThumbnailConfiguration thumbnail, ImageConfiguration image, boolean timestamp) {
      public static EmbedConfiguration of(YamlConfiguration var0, String var1) {
         String var2 = var0.getString(var1 + ".title", "");
         String var3 = var0.getString(var1 + ".description", "");
         String var4 = var0.getString(var1 + ".color", "#FFFFFF");
         ArrayList var5 = new ArrayList();

         for(Map var8 : var0.getMapList(var1 + ".fields")) {
            Object var9 = var8.get("name");
            Object var10 = var8.get("value");
            Object var11 = var8.get("inline");
            String var12 = var9 != null ? String.valueOf(var9) : "";
            String var13 = var10 != null ? String.valueOf(var10) : "";
            boolean var14 = var11 != null && Boolean.parseBoolean(String.valueOf(var11));
            var5.add(new FieldConfiguration(var12, var13, var14));
         }

         FooterConfiguration var15 = DiscordConfiguration.FooterConfiguration.of(var0, var1 + ".footer");
         AuthorConfiguration var16 = DiscordConfiguration.AuthorConfiguration.of(var0, var1 + ".author");
         ThumbnailConfiguration var17 = DiscordConfiguration.ThumbnailConfiguration.of(var0, var1 + ".thumbnail");
         ImageConfiguration var18 = DiscordConfiguration.ImageConfiguration.of(var0, var1 + ".image");
         boolean var19 = var0.getBoolean(var1 + ".timestamp", false);
         return new EmbedConfiguration(var2, var3, var4, var5, var15, var16, var17, var18, var19);
      }
   }

   public static record FieldConfiguration(String name, String value, boolean inline) {
   }

   public static record FooterConfiguration(String text, String iconUrl) {
      public static FooterConfiguration of(YamlConfiguration var0, String var1) {
         String var2 = var0.getString(var1 + ".text", "");
         String var3 = var0.getString(var1 + ".icon-url", "");
         return new FooterConfiguration(var2, var3);
      }

      public boolean hasContent() {
         return this.text != null && !this.text.isEmpty();
      }
   }

   public static record AuthorConfiguration(String name, String url, String iconUrl) {
      public static AuthorConfiguration of(YamlConfiguration var0, String var1) {
         String var2 = var0.getString(var1 + ".name", "");
         String var3 = var0.getString(var1 + ".url", "");
         String var4 = var0.getString(var1 + ".icon-url", "");
         return new AuthorConfiguration(var2, var3, var4);
      }

      public boolean hasContent() {
         return this.name != null && !this.name.isEmpty();
      }
   }

   public static record ThumbnailConfiguration(String url) {
      public static ThumbnailConfiguration of(YamlConfiguration var0, String var1) {
         String var2 = var0.getString(var1 + ".url", "");
         return new ThumbnailConfiguration(var2);
      }

      public boolean hasContent() {
         return this.url != null && !this.url.isEmpty();
      }
   }

   public static record ImageConfiguration(String url) {
      public static ImageConfiguration of(YamlConfiguration var0, String var1) {
         String var2 = var0.getString(var1 + ".url", "");
         return new ImageConfiguration(var2);
      }

      public boolean hasContent() {
         return this.url != null && !this.url.isEmpty();
      }
   }
}
