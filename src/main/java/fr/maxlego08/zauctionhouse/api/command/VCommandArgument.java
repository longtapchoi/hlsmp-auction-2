package fr.maxlego08.zauctionhouse.api.command;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.configuration.commands.CommandArgumentConfiguration;
import fr.maxlego08.zauctionhouse.api.configuration.commands.CommandConfiguration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public abstract class VCommandArgument<T extends Enum<T>> extends VCommand {
   protected final Map<T, Integer> argumentIndexes = new HashMap<>();
   protected final Map<T, String> argumentDefaultValues = new HashMap<>();
   protected final Class<T> enumClass;

   public VCommandArgument(AuctionPlugin var1, Class<T> var2) {
      super(var1);
      this.enumClass = var2;
      this.createCommandArguments(var1, var2);
      this.populateIndexes();
   }

   public abstract void createCommandArguments(AuctionPlugin var1, Class<T> var2);

   protected void populateIndexes() {
      for(T var4 : this.enumClass.getEnumConstants()) {
         this.argumentIndexes.putIfAbsent(var4, -1);
      }

   }

   protected void forEachArgument(String var1, Function<CommandArgumentConfiguration<T>, CollectionBiConsumer> var2) {
      CommandConfiguration var3 = this.plugin.getConfiguration().loadCommandConfiguration(var1, this.enumClass);
      this.addSubCommand(var3.aliases());
      List<CommandArgumentConfiguration<T>> var4 = var3.arguments();

      for(int var5 = 0; var5 != var4.size(); ++var5) {
         CommandArgumentConfiguration<T> var6 = var4.get(var5);
         this.argumentIndexes.put(var6.name(), var5);
         this.argumentDefaultValues.put(var6.name(), var6.defaultValue());
         CollectionBiConsumer var7 = var2.apply(var6);
         if (var6.required()) {
            this.addRequireArg(var6.displayName(), var7);
         } else {
            this.addOptionalArg(var6.displayName(), var7);
         }
      }

   }

   protected String argAsString(T var1) {
      return this.argAsString(var1, this.argumentDefaultValues.getOrDefault(var1, null));
   }

   protected String argAsString(T var1, String var2) {
      return this.argAsString(this.argumentIndexes.getOrDefault(var1, -1), var2);
   }

   protected boolean argAsBoolean(T var1) {
      boolean var2 = false;

      try {
         var2 = Boolean.parseBoolean(this.argumentDefaultValues.getOrDefault(var1, "false"));
      } catch (Exception var4) {
      }

      return this.argAsBoolean(var1, var2);
   }

   protected boolean argAsBoolean(T var1, boolean var2) {
      return this.argAsBoolean(this.argumentIndexes.getOrDefault(var1, -1), var2);
   }

   protected int argAsInteger(T var1) {
      int var2 = 0;

      try {
         var2 = Integer.parseInt(this.argumentDefaultValues.getOrDefault(var1, "0"));
      } catch (Exception var4) {
      }

      return this.argAsInteger(var1, var2);
   }

   protected int argAsInteger(T var1, int var2) {
      return this.argAsInteger(this.argumentIndexes.getOrDefault(var1, -1), var2);
   }

   protected double argAsDouble(T var1) {
      double var2 = (double)0.0F;

      try {
         var2 = Double.parseDouble(this.argumentDefaultValues.getOrDefault(var1, "0.0"));
      } catch (Exception var5) {
      }

      return this.argAsDouble(var1, var2);
   }

   protected double argAsDouble(T var1, double var2) {
      return this.argAsDouble(this.argumentIndexes.getOrDefault(var1, -1), var2);
   }

   protected long argAsLong(T var1) {
      long var2 = 0L;

      try {
         var2 = Long.parseLong(this.argumentDefaultValues.getOrDefault(var1, "0"));
      } catch (Exception var5) {
      }

      return this.argAsLong(var1, var2);
   }

   protected long argAsLong(T var1, long var2) {
      return this.argAsLong(this.argumentIndexes.getOrDefault(var1, -1), var2);
   }

   protected Player argAsPlayer(T var1) {
      return this.argAsPlayer(this.argumentIndexes.getOrDefault(var1, -1));
   }

   protected Player argAsPlayer(T var1, Player var2) {
      return this.argAsPlayer(this.argumentIndexes.getOrDefault(var1, -1), var2);
   }

   protected OfflinePlayer argAsOfflinePlayer(T var1) {
      return this.argAsOfflinePlayer(this.argumentIndexes.getOrDefault(var1, -1));
   }

   protected OfflinePlayer argAsOfflinePlayer(T var1, OfflinePlayer var2) {
      return this.argAsOfflinePlayer(this.argumentIndexes.getOrDefault(var1, -1), var2);
   }
}
