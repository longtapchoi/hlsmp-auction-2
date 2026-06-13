package fr.maxlego08.zauctionhouse.api.messages.messages;

import fr.maxlego08.zauctionhouse.api.messages.AuctionMessage;
import fr.maxlego08.zauctionhouse.api.messages.MessageType;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public record ClassicMessage(MessageType messageType, List<String> messages) implements AuctionMessage {
   public static AuctionMessage tchat(String... var0) {
      return new ClassicMessage(MessageType.TCHAT, Arrays.asList(var0));
   }

   public static AuctionMessage action(String var0) {
      return new ClassicMessage(MessageType.ACTION, Collections.singletonList(var0));
   }
}
