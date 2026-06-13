package fr.maxlego08.zauctionhouse.api.placeholders;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.bukkit.entity.Player;

public interface Placeholder {
   void register(String var1, BiFunction<Player, String, String> var2, String var3, String... var4);

   void register(String var1, Function<Player, String> var2, String var3);

   String getPrefix();

   String onRequest(Player var1, String var2);
}
