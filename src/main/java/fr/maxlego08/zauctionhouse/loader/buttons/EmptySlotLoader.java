package fr.maxlego08.zauctionhouse.loader.buttons;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.button.DefaultButtonValue;
import fr.maxlego08.menu.api.loader.ButtonLoader;
import java.util.logging.Logger;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

public class EmptySlotLoader extends ButtonLoader {
   private final Class<? extends Button> clazz;

   public EmptySlotLoader(@NotNull Plugin var1, @NotNull Class<? extends Button> var2, String var3) {
      super(var1, var3);
      this.clazz = var2;
   }

   public @Nullable Button load(@NonNull YamlConfiguration var1, @NonNull String var2, @NonNull DefaultButtonValue var3) {
      try {
         int var4 = var1.getInt(var2 + "empty-slot", 22);
         return (Button)this.clazz.getConstructor(Plugin.class, Integer.TYPE).newInstance(this.plugin, var4);
      } catch (Exception var7) {
         try {
            return (Button)this.clazz.getDeclaredConstructor().newInstance();
         } catch (Exception var6) {
            Logger var10000 = this.plugin.getLogger();
            String var10001 = this.clazz.getSimpleName();
            var10000.severe("Failed to load button " + var10001 + ": " + var6.getMessage());
            return null;
         }
      }
   }
}
