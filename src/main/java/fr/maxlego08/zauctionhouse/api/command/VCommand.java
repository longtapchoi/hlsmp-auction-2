package fr.maxlego08.zauctionhouse.api.command;

import fr.maxlego08.zauctionhouse.api.AuctionManager;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.messages.Message;
import fr.maxlego08.zauctionhouse.api.utils.Permission;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;

public abstract class VCommand extends Arguments {
   protected final AuctionPlugin plugin;
   protected final AuctionManager auctionManager;
   private final List<String> subCommands = new ArrayList();
   private final List<String> requireArgs = new ArrayList();
   private final List<String> optionalArgs = new ArrayList();
   protected VCommand parent;
   protected List<VCommand> subVCommands = new ArrayList();
   private boolean runAsync = false;
   protected CommandSender sender;
   protected Player player;
   protected Map<Integer, CollectionBiConsumer> tabCompletions = new HashMap<>();
   private String permission;
   private boolean consoleCanUse = true;
   private boolean ignoreParent = false;
   private boolean ignoreArgs = false;
   private boolean extendedArgs = false;
   private CommandType tabCompleter;
   private String syntax;
   private String description;
   private int argsMinLength;
   private int argsMaxLength;

   public VCommand(AuctionPlugin var1) {
      this.tabCompleter = CommandType.DEFAULT;
      this.plugin = var1;
      this.auctionManager = var1.getAuctionManager();
   }

   protected void message(AuctionPlugin var1, CommandSender var2, Message var3, Object... var4) {
      var1.sendMessage(var2, var3, var4);
   }

   protected boolean hasPermission(Permissible var1, String var2) {
      return var1.hasPermission(var2);
   }

   public Optional<CollectionBiConsumer> getCompletionAt(int var1) {
      return Optional.ofNullable(this.tabCompletions.getOrDefault(var1, null));
   }

   public String getPermission() {
      return this.permission;
   }

   protected VCommand setPermission(String var1) {
      this.permission = var1;
      return this;
   }

   protected VCommand setPermission(Permission var1) {
      this.permission = var1.asPermission();
      return this;
   }

   public VCommand getParent() {
      return this.parent;
   }

   public VCommand getMainParent() {
      if (this.parent == null) {
         return null;
      } else {
         return this.parent.getParent() == null ? this.parent : this.parent.getMainParent();
      }
   }

   protected VCommand setParent(VCommand var1) {
      this.parent = var1;
      return this;
   }

   public List<String> getSubCommands() {
      return this.subCommands;
   }

   public boolean isConsoleCanUse() {
      return this.consoleCanUse;
   }

   protected VCommand setConsoleCanUse(boolean var1) {
      this.consoleCanUse = var1;
      return this;
   }

   public boolean isIgnoreParent() {
      return this.ignoreParent;
   }

   public void setIgnoreParent(boolean var1) {
      this.ignoreParent = var1;
   }

   public CommandSender getSender() {
      return this.sender;
   }

   public int getArgsMinLength() {
      return this.argsMinLength;
   }

   public int getArgsMaxLength() {
      return this.argsMaxLength;
   }

   public Player getPlayer() {
      return this.player;
   }

   public String getSyntax() {
      if (this.syntax == null) {
         this.syntax = this.generateDefaultSyntax("");
      }

      return this.syntax;
   }

   protected VCommand setSyntax(String var1) {
      this.syntax = var1;
      return this;
   }

   public boolean isIgnoreArgs() {
      return this.ignoreArgs;
   }

   public void setIgnoreArgs(boolean var1) {
      this.ignoreArgs = var1;
   }

   public String getDescription() {
      return this.description == null ? "no description" : this.description;
   }

   protected VCommand setDescription(String var1) {
      this.description = var1;
      return this;
   }

   protected VCommand setDescription(Message var1) {
      this.description = var1.getMessageAsString();
      return this;
   }

   public CommandType getTabCompleter() {
      return this.tabCompleter;
   }

   protected void setTabCompleter() {
      this.tabCompleter = CommandType.SUCCESS;
   }

   public void setExtendedArgs(boolean var1) {
      this.extendedArgs = var1;
   }

   public boolean isRunAsync() {
      return this.runAsync;
   }

   protected VCommand setRunAsync(boolean var1) {
      this.runAsync = var1;
      return this;
   }

   protected VCommand onlyPlayers() {
      this.consoleCanUse = false;
      return this;
   }

   protected void addRequireArg(String var1) {
      this.requireArgs.add(var1);
      this.ignoreParent = this.parent == null;
      this.ignoreArgs = true;
   }

   protected void addRequireArg(String var1, CollectionBiConsumer var2) {
      this.addRequireArg(var1);
      int var3 = this.requireArgs.size();
      this.addCompletion(var3 - 1, var2);
   }

   protected void addOptionalArg(String var1, CollectionBiConsumer var2) {
      this.addOptionalArg(var1);
      int var3 = this.requireArgs.size() + this.optionalArgs.size();
      this.addCompletion(var3 - 1, var2);
   }

   protected void addOptionalArg(String var1) {
      this.optionalArgs.add(var1);
      this.ignoreParent = this.parent == null;
      this.ignoreArgs = true;
   }

   public String getFirst() {
      return (String)this.subCommands.getFirst();
   }

   public VCommand addSubCommand(String var1) {
      this.subCommands.add(var1);
      return this;
   }

   public VCommand addSubCommand(VCommand var1) {
      var1.setParent(this);
      this.plugin.getCommandManager().registerCommand(var1);
      this.subVCommands.add(var1);
      return this;
   }

   public VCommand addSubCommand(String... var1) {
      this.subCommands.addAll(Arrays.asList(var1));
      return this;
   }

   public void addCompletion(int var1, CollectionBiConsumer var2) {
      this.tabCompletions.put(var1, var2);
      this.setTabCompleter();
   }

   private String generateDefaultSyntax(String var1) {
      String var2 = (String)this.subCommands.get(0);
      boolean var3 = var1.equals("");
      if (this.requireArgs.size() != 0 && var3) {
         for(String var5 : this.requireArgs) {
            var5 = "<" + var5 + ">";
            var1 = var1 + " " + var5;
         }
      }

      if (this.optionalArgs.size() != 0 && var3) {
         StringBuilder var8 = new StringBuilder(var1);

         for(String var6 : this.optionalArgs) {
            var6 = "[<" + var6 + ">]";
            var8.append(" ").append(var6);
         }

         var1 = var8.toString();
      }

      var2 = var2 + var1;
      return this.parent == null ? "/" + var2 : this.parent.generateDefaultSyntax(" " + var2);
   }

   private int parentCount(int var1) {
      return this.parent == null ? var1 : this.parent.parentCount(var1 + 1);
   }

   public CommandType prePerform(AuctionPlugin var1, CommandSender var2, String[] var3) {
      this.parentCount = this.parentCount(0);
      this.argsMaxLength = this.requireArgs.size() + this.optionalArgs.size() + this.parentCount;
      this.argsMinLength = this.requireArgs.size() + this.parentCount;
      if (this.syntax == null) {
         this.syntax = this.generateDefaultSyntax("");
      }

      this.args = var3;
      String var4 = super.argAsString(0);
      if (var4 != null) {
         for(VCommand var6 : this.subVCommands) {
            if (var6.getSubCommands().contains(var4.toLowerCase())) {
               return CommandType.CONTINUE;
            }
         }
      }

      if ((this.argsMinLength == 0 || var3.length >= this.argsMinLength) && (this.argsMaxLength == 0 || var3.length <= this.argsMaxLength || this.extendedArgs)) {
         this.sender = var2;
         if (this.sender instanceof Player) {
            this.player = (Player)var2;
         }

         try {
            return this.perform(var1);
         } catch (Exception var7) {
            var7.printStackTrace();
            return CommandType.SYNTAX_ERROR;
         }
      } else {
         return CommandType.SYNTAX_ERROR;
      }
   }

   protected abstract CommandType perform(AuctionPlugin var1);

   public boolean sameSubCommands() {
      if (this.parent == null) {
         return false;
      } else {
         for(String var2 : this.subCommands) {
            if (this.parent.getSubCommands().contains(var2)) {
               return true;
            }
         }

         return false;
      }
   }

   public String toString() {
      String var10000 = this.permission;
      return "VCommand [permission=" + var10000 + ", subCommands=" + String.valueOf(this.subCommands) + ", consoleCanUse=" + this.consoleCanUse + ", description=" + this.description + "]";
   }

   public List<String> toTab(AuctionPlugin var1, CommandSender var2, String[] var3) {
      this.parentCount = this.parentCount(0);
      int var4 = var3.length - this.parentCount - 1;
      Optional<CollectionBiConsumer> var5 = this.getCompletionAt(var4);
      if (var5.isPresent()) {
         CollectionBiConsumer var6 = var5.get();
         String var7 = var3[var3.length - 1];
         return this.generateList(var6.accept(var2, var3), var7);
      } else {
         return null;
      }
   }

   protected List<String> generateList(String var1, String... var2) {
      return this.generateList(Arrays.asList(var2), var1);
   }

   protected List<String> generateList(Tab var1, String var2, String... var3) {
      return this.generateList(Arrays.asList(var3), var2, var1);
   }

   protected List<String> generateList(List<String> var1, String var2) {
      return this.generateList(var1, var2, Tab.CONTAINS);
   }

   protected List<String> generateList(List<String> var1, String var2, Tab var3) {
      ArrayList var4 = new ArrayList();

      for(String var6 : var1) {
         if (!var2.isEmpty()) {
            if (var3.equals(Tab.START)) {
               if (!var6.toLowerCase().startsWith(var2.toLowerCase())) {
                  continue;
               }
            } else if (!var6.toLowerCase().contains(var2.toLowerCase())) {
               continue;
            }
         }

         var4.add(var6);
      }

      return var4.isEmpty() ? null : var4;
   }

   public void addSubCommand(List<String> var1) {
      this.subCommands.addAll(var1);
   }

   public void syntaxMessage(CommandSender var1) {
      this.subVCommands.forEach((var2) -> {
         if (var2.getPermission() == null || this.hasPermission(var1, var2.getPermission())) {
            this.message(this.plugin, var1, Message.COMMAND_SYNTAX_HELP, "%syntax%", var2.getSyntax(), "%description%", var2.getDescription());
         }

      });
   }
}
