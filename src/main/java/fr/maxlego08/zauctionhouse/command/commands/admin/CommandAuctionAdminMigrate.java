package fr.maxlego08.zauctionhouse.command.commands.admin;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.command.CommandType;
import fr.maxlego08.zauctionhouse.api.command.VCommand;
import fr.maxlego08.zauctionhouse.api.messages.Message;
import fr.maxlego08.zauctionhouse.api.migration.MigrationCallback;
import fr.maxlego08.zauctionhouse.api.migration.MigrationProvider;
import fr.maxlego08.zauctionhouse.api.utils.Permission;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.bukkit.configuration.ConfigurationSection;

public class CommandAuctionAdminMigrate extends VCommand {
   private static final String CONFIRM_ARG = "confirm";

   public CommandAuctionAdminMigrate(AuctionPlugin var1) {
      super(var1);
      this.addSubCommand("migrate");
      this.setPermission(Permission.ZAUCTIONHOUSE_ADMIN);
      this.setDescription(Message.COMMAND_DESCRIPTION_AUCTION_MIGRATE);
      this.addRequireArg("source", (var1x, var2) -> new ArrayList(var1.getMigrationRegistry().getProviderIds()));
      this.addOptionalArg("confirm", (var0, var1x) -> List.of("confirm"));
   }

   protected CommandType perform(AuctionPlugin var1) {
      String var2 = this.argAsString(0, "");
      String var3 = this.argAsString(1, "");
      Optional var4 = var1.getMigrationRegistry().getProvider(var2);
      if (var4.isEmpty()) {
         this.message(var1, this.sender, Message.MIGRATION_INVALID_SOURCE, new Object[]{"%source%", var2});
         this.message(var1, this.sender, Message.MIGRATION_AVAILABLE_SOURCES, new Object[]{"%sources%", var1.getMigrationRegistry().getProviderIds().stream().map((var0) -> "&f" + var0).collect(Collectors.joining("&7, "))});
         return CommandType.SUCCESS;
      } else {
         MigrationProvider var5 = (MigrationProvider)var4.get();
         ConfigurationSection var6 = var5.getConfigSection() != null ? var1.getConfig().getConfigurationSection("migration." + var5.getConfigSection()) : null;
         if (var6 == null && var5.getConfigSection() != null) {
            this.message(var1, this.sender, Message.MIGRATION_NOT_CONFIGURED, new Object[]{"%source%", var5.getDisplayName()});
            return CommandType.SUCCESS;
         } else {
            String var7 = var5.validateConfig(var6);
            if (var7 != null) {
               this.message(var1, this.sender, Message.MIGRATION_FAILED, new Object[]{"%error%", var7});
               return CommandType.SUCCESS;
            } else if (!var3.equalsIgnoreCase("confirm")) {
               this.message(var1, this.sender, Message.MIGRATION_INFO, new Object[]{"%source%", var5.getDisplayName(), "%details%", var5.getDescription()});
               this.message(var1, this.sender, Message.MIGRATION_CONFIRM, new Object[]{"%source%", var5.getId()});
               return CommandType.SUCCESS;
            } else {
               this.message(var1, this.sender, Message.MIGRATION_STARTED, new Object[]{"%source%", var5.getDisplayName()});
               MigrationCallback var8 = (var2x) -> {
                  if (this.sender != null) {
                     this.message(var1, this.sender, Message.MIGRATION_PROGRESS, new Object[]{"%progress%", var2x});
                  }

               };
               CompletableFuture var9 = var5.migrate(var1, var6, var8);
               var9.thenAccept((var3x) -> var1.getScheduler().runNextTick((var4) -> {
                     if (var3x.isSuccess()) {
                        this.message(var1, this.sender, Message.MIGRATION_SUCCESS, new Object[]{"%source%", var5.getDisplayName(), "%players%", String.valueOf(var3x.getPlayersImported()), "%items%", String.valueOf(var3x.getItemsImported()), "%transactions%", String.valueOf(var3x.getTransactionsImported()), "%errors%", String.valueOf(var3x.getErrors()), "%duration%", String.valueOf(var3x.getDurationMs())});
                        var1.getStorageManager().loadItems();
                     } else {
                        this.message(var1, this.sender, Message.MIGRATION_FAILED, new Object[]{"%error%", var3x.getErrorMessage()});
                     }

                  })).exceptionally((var2x) -> {
                  var1.getScheduler().runNextTick((var3) -> this.message(var1, this.sender, Message.MIGRATION_FAILED, new Object[]{"%error%", var2x.getMessage()}));
                  return null;
               });
               return CommandType.SUCCESS;
            }
         }
      }
   }
}
