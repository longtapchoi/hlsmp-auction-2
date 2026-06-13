package fr.maxlego08.zauctionhouse.api.configuration.discord;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unchecked", "rawtypes"})
public class DiscordEmbed {
   private final List<Field> fields = new ArrayList();
   private String title;
   private String description;
   private int color;
   private Footer footer;
   private Author author;
   private Thumbnail thumbnail;
   private Image image;
   private String timestamp;
   private String username;
   private String avatarUrl;
   private String content;

   public static Builder builder() {
      return new Builder();
   }

   public String toJson() {
      StringBuilder var1 = new StringBuilder();
      var1.append("{");
      ArrayList var2 = new ArrayList();
      if (this.username != null && !this.username.isEmpty()) {
         var2.add("\"username\":" + this.escapeJson(this.username));
      }

      if (this.avatarUrl != null && !this.avatarUrl.isEmpty()) {
         var2.add("\"avatar_url\":" + this.escapeJson(this.avatarUrl));
      }

      if (this.content != null && !this.content.isEmpty()) {
         var2.add("\"content\":" + this.escapeJson(this.content));
      }

      StringBuilder var3 = new StringBuilder("\"embeds\":[{");
      ArrayList var4 = new ArrayList();
      if (this.title != null && !this.title.isEmpty()) {
         var4.add("\"title\":" + this.escapeJson(this.title));
      }

      if (this.description != null && !this.description.isEmpty()) {
         var4.add("\"description\":" + this.escapeJson(this.description));
      }

      var4.add("\"color\":" + this.color);
      if (!this.fields.isEmpty()) {
         StringBuilder var5 = new StringBuilder("\"fields\":[");
         ArrayList var6 = new ArrayList();

         for(Field var8 : this.fields) {
            String var10001 = this.escapeJson(var8.name);
            var6.add("{\"name\":" + var10001 + ",\"value\":" + this.escapeJson(var8.value) + ",\"inline\":" + var8.inline + "}");
         }

         var5.append(String.join(",", var6));
         var5.append("]");
         var4.add(var5.toString());
      }

      if (this.footer != null && this.footer.text != null && !this.footer.text.isEmpty()) {
         StringBuilder var9 = new StringBuilder("\"footer\":{\"text\":" + this.escapeJson(this.footer.text));
         if (this.footer.iconUrl != null && !this.footer.iconUrl.isEmpty()) {
            var9.append(",\"icon_url\":").append(this.escapeJson(this.footer.iconUrl));
         }

         var9.append("}");
         var4.add(var9.toString());
      }

      if (this.author != null && this.author.name != null && !this.author.name.isEmpty()) {
         StringBuilder var10 = new StringBuilder("\"author\":{\"name\":" + this.escapeJson(this.author.name));
         if (this.author.url != null && !this.author.url.isEmpty()) {
            var10.append(",\"url\":").append(this.escapeJson(this.author.url));
         }

         if (this.author.iconUrl != null && !this.author.iconUrl.isEmpty()) {
            var10.append(",\"icon_url\":").append(this.escapeJson(this.author.iconUrl));
         }

         var10.append("}");
         var4.add(var10.toString());
      }

      if (this.thumbnail != null && this.thumbnail.url != null && !this.thumbnail.url.isEmpty()) {
         var4.add("\"thumbnail\":{\"url\":" + this.escapeJson(this.thumbnail.url) + "}");
      }

      if (this.image != null && this.image.url != null && !this.image.url.isEmpty()) {
         var4.add("\"image\":{\"url\":" + this.escapeJson(this.image.url) + "}");
      }

      if (this.timestamp != null && !this.timestamp.isEmpty()) {
         var4.add("\"timestamp\":" + this.escapeJson(this.timestamp));
      }

      var3.append(String.join(",", var4));
      var3.append("}]");
      var2.add(var3.toString());
      var1.append(String.join(",", var2));
      var1.append("}");
      return var1.toString();
   }

   private String escapeJson(String var1) {
      if (var1 == null) {
         return "null";
      } else {
         StringBuilder var2 = new StringBuilder("\"");

         for(char var6 : var1.toCharArray()) {
            switch (var6) {
               case '\b':
                  var2.append("\\b");
                  break;
               case '\t':
                  var2.append("\\t");
                  break;
               case '\n':
                  var2.append("\\n");
                  break;
               case '\f':
                  var2.append("\\f");
                  break;
               case '\r':
                  var2.append("\\r");
                  break;
               case '"':
                  var2.append("\\\"");
                  break;
               case '\\':
                  var2.append("\\\\");
                  break;
               default:
                  if (var6 < ' ') {
                     var2.append(String.format("\\u%04x", var6));
                  } else {
                     var2.append(var6);
                  }
            }
         }

         var2.append("\"");
         return var2.toString();
      }
   }

   public static class Builder {
      private final DiscordEmbed embed = new DiscordEmbed();

      public Builder title(String var1) {
         this.embed.title = var1;
         return this;
      }

      public Builder description(String var1) {
         this.embed.description = var1;
         return this;
      }

      public Builder color(String var1) {
         this.embed.color = this.parseColor(var1);
         return this;
      }

      public Builder color(int var1) {
         this.embed.color = var1;
         return this;
      }

      public Builder addField(String var1, String var2, boolean var3) {
         this.embed.fields.add(new Field(var1, var2, var3));
         return this;
      }

      public Builder footer(String var1, String var2) {
         this.embed.footer = new Footer(var1, var2);
         return this;
      }

      public Builder author(String var1, String var2, String var3) {
         this.embed.author = new Author(var1, var2, var3);
         return this;
      }

      public Builder thumbnail(String var1) {
         this.embed.thumbnail = new Thumbnail(var1);
         return this;
      }

      public Builder image(String var1) {
         this.embed.image = new Image(var1);
         return this;
      }

      public Builder timestamp(boolean var1) {
         if (var1) {
            this.embed.timestamp = Instant.now().toString();
         }

         return this;
      }

      public Builder username(String var1) {
         this.embed.username = var1;
         return this;
      }

      public Builder avatarUrl(String var1) {
         this.embed.avatarUrl = var1;
         return this;
      }

      public Builder content(String var1) {
         this.embed.content = var1;
         return this;
      }

      public DiscordEmbed build() {
         return this.embed;
      }

      private int parseColor(String var1) {
         if (var1 != null && !var1.isEmpty()) {
            String var2 = var1.startsWith("#") ? var1.substring(1) : var1;

            try {
               return Integer.parseInt(var2, 16);
            } catch (NumberFormatException var4) {
               return 16777215;
            }
         } else {
            return 16777215;
         }
      }
   }

   private static record Field(String name, String value, boolean inline) {
   }

   private static record Footer(String text, String iconUrl) {
   }

   private static record Author(String name, String url, String iconUrl) {
   }

   private static record Thumbnail(String url) {
   }

   private static record Image(String url) {
   }
}
