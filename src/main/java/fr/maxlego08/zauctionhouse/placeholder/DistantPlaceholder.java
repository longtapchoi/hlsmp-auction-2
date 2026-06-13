package fr.maxlego08.zauctionhouse.placeholder;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.placeholders.Placeholder;
import java.util.List;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class DistantPlaceholder extends PlaceholderExpansion {
   private final AuctionPlugin plugin;
   private final Placeholder placeholder;

   public DistantPlaceholder(AuctionPlugin var1, Placeholder var2) {
      this.plugin = var1;
      this.placeholder = var2;
   }

   public String getAuthor() {
      List var1 = this.plugin.getDescription().getAuthors();
      return var1.isEmpty() ? "Unknown" : (String)var1.get(0);
   }

   public String getIdentifier() {
      return this.placeholder.getPrefix();
   }

   public String getVersion() {
      return this.plugin.getDescription().getVersion();
   }

   public boolean persist() {
      return true;
   }

   public String onPlaceholderRequest(Player var1, String var2) {
      return this.placeholder.onRequest(var1, var2);
   }
}
