package fr.maxlego08.zauctionhouse.placeholder;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.placeholders.Placeholder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.bukkit.entity.Player;

@SuppressWarnings({"unchecked", "rawtypes"})
public class LocalPlaceholder implements Placeholder {
   private final Pattern pattern = Pattern.compile("[%]([^%]+)[%]");
   private final List<AutoPlaceholder> autoPlaceholders = new ArrayList();
   private final AuctionPlugin plugin;
   private final String prefix = "zauctionhouse";

   public LocalPlaceholder(AuctionPlugin var1) {
      this.plugin = var1;
   }

   public String setPlaceholders(Player var1, String var2) {
      if (var2 != null && var2.contains("%")) {
         Objects.requireNonNull(this);
         String var3 = "zauctionhouse" + "_";
         Matcher var4 = this.pattern.matcher(var2);

         while(var4.find()) {
            String var5 = var4.group(0);
            String var6 = var4.group(1).replace(var3, "");
            String var7 = this.onRequest(var1, var6);
            if (var7 != null) {
               var2 = var2.replace(var5, var7);
            }
         }

         return var2;
      } else {
         return var2;
      }
   }

   public List<String> setPlaceholders(Player var1, List<String> var2) {
      return var2 == null ? null : (List)var2.stream().map((var2x) -> this.setPlaceholders(var1, var2x)).collect(Collectors.toList());
   }

   public String onRequest(Player var1, String var2) {
      if (var2 != null && var1 != null) {
         Optional var3 = this.autoPlaceholders.stream().filter((var1x) -> var1x.startsWith(var2)).findFirst();
         if (var3.isPresent()) {
            AutoPlaceholder var4 = (AutoPlaceholder)var3.get();
            String var5 = var2.replace(var4.getStartWith(), "");
            return var4.accept(var1, var5);
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public void register(String var1, BiFunction<Player, String, String> var2, String var3, String... var4) {
      this.autoPlaceholders.add(new AutoPlaceholder(var1, var2, var3, Arrays.asList(var4)));
   }

   public void register(String var1, Function<Player, String> var2, String var3) {
      this.autoPlaceholders.add(new AutoPlaceholder(var1, var2, var3));
   }

   public String getPrefix() {
      return "zauctionhouse";
   }

   public AuctionPlugin getPlugin() {
      return this.plugin;
   }

   public List<AutoPlaceholder> getAutoPlaceholders() {
      return this.autoPlaceholders;
   }
}
