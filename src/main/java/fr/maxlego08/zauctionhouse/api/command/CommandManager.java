package fr.maxlego08.zauctionhouse.api.command;

import java.util.List;
import java.util.UUID;

public interface CommandManager {
   VCommand registerCommand(VCommand var1);

   List<VCommand> getCommands();

   void clearCooldowns(UUID var1);
}
