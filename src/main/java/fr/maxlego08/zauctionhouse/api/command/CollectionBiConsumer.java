package fr.maxlego08.zauctionhouse.api.command;

import java.util.List;
import org.bukkit.command.CommandSender;

@FunctionalInterface
public interface CollectionBiConsumer {
   List<String> accept(CommandSender var1, String[] var2);
}
