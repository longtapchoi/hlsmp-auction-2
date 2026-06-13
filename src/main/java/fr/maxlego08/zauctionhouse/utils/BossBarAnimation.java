package fr.maxlego08.zauctionhouse.utils;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.libs.folialib.wrapper.task.WrappedTask;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.entity.Player;

public class BossBarAnimation implements Runnable {
   private final Player player;
   private final BossBar bossBar;
   private final WrappedTask wrappedTask;
   private final long duration;
   private long remainingTicks;

   public BossBarAnimation(AuctionPlugin var1, Player var2, BossBar var3, long var4) {
      this.player = var2;
      this.bossBar = var3;
      this.remainingTicks = var4;
      this.duration = var4;
      this.wrappedTask = var1.getScheduler().runTimer(this, 0L, 1L);
   }

   public void run() {
      double var1 = (double)this.remainingTicks / (double)this.duration;
      this.bossBar.progress((float)var1);
      if (this.remainingTicks <= 0L) {
         this.player.hideBossBar(this.bossBar);
         this.wrappedTask.cancel();
      }

      --this.remainingTicks;
   }
}
