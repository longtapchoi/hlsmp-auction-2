package fr.maxlego08.zauctionhouse.command;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.command.CommandManager;
import fr.maxlego08.zauctionhouse.api.command.CommandType;
import fr.maxlego08.zauctionhouse.api.command.VCommand;
import fr.maxlego08.zauctionhouse.api.configuration.records.CooldownConfiguration;
import fr.maxlego08.zauctionhouse.api.messages.Message;
import fr.maxlego08.zauctionhouse.utils.ZUtils;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

@SuppressWarnings({"unchecked", "rawtypes"})
public class ZCommandManager extends ZUtils implements CommandManager, CommandExecutor, TabCompleter {
   private static CommandMap commandMap;
   private static Constructor<? extends PluginCommand> constructor;
   private final AuctionPlugin plugin;
   private final List<VCommand> commands = new ArrayList();
   private final Map<UUID, Map<String, Long>> cooldowns = new HashMap();

   public ZCommandManager(AuctionPlugin var1) {
      this.plugin = var1;
   }

   public VCommand registerCommand(VCommand var1) {
      this.commands.add(var1);
      return var1;
   }

   public void clearCooldowns(UUID var1) {
      this.cooldowns.remove(var1);
   }

   public boolean onCommand(CommandSender var1, Command var2, String var3, String[] var4) {
      for(VCommand var6 : this.commands) {
         if (var6.getSubCommands().contains(var2.getName().toLowerCase())) {
            if ((var4.length == 0 || var6.isIgnoreParent()) && var6.getParent() == null) {
               CommandType var7 = this.processRequirements(var6, var1, var4);
               if (!var7.equals(CommandType.CONTINUE)) {
                  return true;
               }
            }
         } else if (var4.length >= 1 && var6.getParent() != null && this.canExecute(var4, var2.getName().toLowerCase(), var6)) {
            CommandType var8 = this.processRequirements(var6, var1, var4);
            if (!var8.equals(CommandType.CONTINUE)) {
               return true;
            }
         }
      }

      this.message(this.plugin, var1, Message.COMMAND_NO_ARG, new Object[0]);
      return true;
   }

   private boolean canExecute(String[] var1, String var2, VCommand var3) {
      for(int var4 = var1.length - 1; var4 > -1; --var4) {
         if (var3.getSubCommands().contains(var1[var4].toLowerCase())) {
            if (!var3.isIgnoreArgs() || var3.getParent() != null && !this.canExecute(var1, var2, var3.getParent(), var4 - 1)) {
               if (var4 < var1.length - 1) {
                  return false;
               }

               return this.canExecute(var1, var2, var3.getParent(), var4 - 1);
            }

            return true;
         }
      }

      return false;
   }

   private boolean canExecute(String[] var1, String var2, VCommand var3, int var4) {
      if (var4 < 0 && var3.getSubCommands().contains(var2.toLowerCase())) {
         return true;
      } else if (var4 < 0) {
         return false;
      } else {
         return var3.getSubCommands().contains(var1[var4].toLowerCase()) ? this.canExecute(var1, var2, var3.getParent(), var4 - 1) : false;
      }
   }

   private CommandType processRequirements(VCommand var1, CommandSender var2, String[] var3) {
      if (!(var2 instanceof Player) && !var1.isConsoleCanUse()) {
         this.message(this.plugin, var2, Message.COMMAND_NO_CONSOLE, new Object[0]);
         return CommandType.DEFAULT;
      } else if (var1.getPermission() != null && !this.hasPermission(var2, var1.getPermission())) {
         this.message(this.plugin, var2, Message.COMMAND_NO_PERMISSION, new Object[0]);
         return CommandType.DEFAULT;
      } else {
         if (var2 instanceof Player) {
            Player var4 = (Player)var2;
            CooldownConfiguration var5 = this.plugin.getConfiguration().getCooldown();
            String var6 = var1.getFirst();
            long var7 = var5.getCooldownForCommand(var6);
            if (var7 > 0L && !this.hasPermission(var2, var5.bypassPermission())) {
               UUID var9 = var4.getUniqueId();
               Map var10 = (Map)this.cooldowns.computeIfAbsent(var9, (var0) -> new HashMap());
               long var11 = System.currentTimeMillis();
               Long var13 = (Long)var10.get(var6);
               if (var13 != null && var11 - var13 < var7) {
                  this.message(this.plugin, var2, Message.COMMAND_COOLDOWN, new Object[0]);
                  return CommandType.DEFAULT;
               }

               var10.put(var6, var11);
            }
         }

         if (var1.isRunAsync()) {
            this.plugin.getScheduler().runAsync((var4x) -> {
               CommandType var5 = var1.prePerform(this.plugin, var2, var3);
               if (var5 == CommandType.SYNTAX_ERROR) {
                  this.message(this.plugin, var2, Message.COMMAND_SYNTAX_ERROR, new Object[]{"%syntax%", var1.getSyntax()});
               }

            });
            return CommandType.DEFAULT;
         } else {
            CommandType var14 = var1.prePerform(this.plugin, var2, var3);
            if (var14 == CommandType.SYNTAX_ERROR) {
               this.message(this.plugin, var2, Message.COMMAND_SYNTAX_ERROR, new Object[]{"%syntax%", var1.getSyntax()});
            }

            return var14;
         }
      }
   }

   public List<VCommand> getCommands() {
      return this.commands;
   }

   private int getUniqueCommand() {
      return (int)this.commands.stream().filter((var0) -> var0.getParent() == null).count();
   }

   private void commandChecking() {
      this.commands.forEach((var1) -> {
         if (var1.sameSubCommands()) {
            this.plugin.getLogger().info(String.valueOf(var1) + " command to an argument similar to its parent command !");
            Bukkit.getPluginManager().disablePlugin(this.plugin);
         }

      });
   }

   public List<String> onTabComplete(CommandSender var1, Command var2, String var3, String[] var4) {
      for(VCommand var6 : this.commands) {
         if (var6.getSubCommands().contains(var2.getName().toLowerCase())) {
            if (var4.length == 1 && var6.getParent() == null) {
               return this.proccessTab(var1, var6, var4);
            }
         } else {
            String[] var7 = (String[])Arrays.copyOf(var4, var4.length - 1);
            if (var7.length >= 1 && var6.getParent() != null && this.canExecute(var7, var2.getName().toLowerCase(), var6)) {
               return this.proccessTab(var1, var6, var4);
            }
         }
      }

      return null;
   }

   private List<String> proccessTab(CommandSender var1, VCommand var2, String[] var3) {
      CommandType var4 = var2.getTabCompleter();
      if (!var4.equals(CommandType.DEFAULT)) {
         return var4.equals(CommandType.SUCCESS) ? var2.toTab(this.plugin, var1, var3) : null;
      } else {
         String var5 = var3[var3.length - 1];
         ArrayList var6 = new ArrayList();

         for(VCommand var8 : this.commands) {
            if (var8.getParent() != null && var8.getParent() == var2) {
               String var9 = (String)var8.getSubCommands().get(0);
               if ((var8.getPermission() == null || var1.hasPermission(var8.getPermission())) && (var5.isEmpty() || var9.startsWith(var5))) {
                  var6.add(var9);
               }
            }
         }

         return var6.isEmpty() ? null : var6;
      }
   }

   public void registerCommand(Plugin var1, String var2, VCommand var3, List<String> var4) {
      try {
         PluginCommand var5 = (PluginCommand)constructor.newInstance(var2, this.plugin);
         var5.setExecutor(this);
         var5.setTabCompleter(this);
         var5.setAliases(var4);
         if (var3.getPermission() != null) {
            var5.setPermission(var3.getPermission());
         }

         this.commands.add(var3.addSubCommand(var2));
         var3.addSubCommand(var4);
         if (!commandMap.register(var5.getName(), var1.getDescription().getName(), var5)) {
            var1.getLogger().info("Unable to add the command " + var3.getSyntax());
         }
      } catch (Exception var6) {
         this.plugin.getLogger().severe("Failed to register command " + var2 + ": " + var6.getMessage());
      }

   }

   static {
      try {
         Field var0 = Bukkit.getServer().getClass().getDeclaredField("commandMap");
         var0.setAccessible(true);
         commandMap = (CommandMap)var0.get(Bukkit.getServer());
         constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
         constructor.setAccessible(true);
      } catch (Exception var1) {
      }

   }
}
