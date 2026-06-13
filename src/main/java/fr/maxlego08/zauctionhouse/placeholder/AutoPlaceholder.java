package fr.maxlego08.zauctionhouse.placeholder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.bukkit.entity.Player;

@SuppressWarnings({"unchecked", "rawtypes"})
public class AutoPlaceholder {
   private final String startWith;
   private final BiFunction<Player, String, String> biConsumer;
   private final Function<Player, String> consumer;
   private final String description;
   private final List<String> args;

   public AutoPlaceholder(String var1, BiFunction<Player, String, String> var2, String var3, List<String> var4) {
      this.startWith = var1;
      this.biConsumer = var2;
      this.description = var3;
      this.args = var4;
      this.consumer = null;
   }

   public AutoPlaceholder(String var1, Function<Player, String> var2, String var3) {
      this.startWith = var1;
      this.description = var3;
      this.biConsumer = null;
      this.consumer = var2;
      this.args = new ArrayList();
   }

   public List<String> getArgs() {
      return this.args;
   }

   public String getDescription() {
      return this.description;
   }

   public String getStartWith() {
      return this.startWith;
   }

   public BiFunction<Player, String, String> getBiConsumer() {
      return this.biConsumer;
   }

   public Function<Player, String> getConsumer() {
      return this.consumer;
   }

   public String accept(Player var1, String var2) {
      if (this.consumer != null) {
         return (String)this.consumer.apply(var1);
      } else {
         return this.biConsumer != null ? (String)this.biConsumer.apply(var1, var2) : "Error with consumer !";
      }
   }

   public boolean startsWith(String var1) {
      return this.consumer != null ? this.startWith.equalsIgnoreCase(var1) : var1.startsWith(this.startWith);
   }
}
