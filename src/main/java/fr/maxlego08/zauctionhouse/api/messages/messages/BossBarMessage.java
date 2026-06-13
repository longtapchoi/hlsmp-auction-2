package fr.maxlego08.zauctionhouse.api.messages.messages;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.messages.AuctionMessage;
import fr.maxlego08.zauctionhouse.api.messages.MessageType;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.bossbar.BossBar.Color;
import net.kyori.adventure.bossbar.BossBar.Flag;
import net.kyori.adventure.bossbar.BossBar.Overlay;

public record BossBarMessage(String text, String color, String overlay, List<String> flags, long duration, boolean isStatic) implements AuctionMessage {
   public MessageType messageType() {
      return MessageType.BOSSBAR;
   }

   public boolean isValid(AuctionPlugin var1) {
      try {
         Color.valueOf(this.color);
      } catch (Exception var7) {
         var1.getLogger().severe("BossBar Color " + this.color + " doesn't exit !");
         return false;
      }

      try {
         Overlay.valueOf(this.overlay);
      } catch (Exception var6) {
         var1.getLogger().severe("BossBar Overlay " + this.overlay + " doesn't exit !");
         return false;
      }

      for(String var3 : this.flags) {
         try {
            Flag.valueOf(var3);
         } catch (Exception var5) {
            var1.getLogger().severe("BossBar Flag " + var3 + " doesn't exit !");
            return false;
         }
      }

      return true;
   }

   public BossBar.Overlay getOverlay() {
      return Overlay.valueOf(this.overlay);
   }

   public BossBar.Color getColor() {
      return Color.valueOf(this.color);
   }

   public Set<BossBar.Flag> getFlags() {
      return (Set)this.flags.stream().map(BossBar.Flag::valueOf).collect(Collectors.toSet());
   }
}
