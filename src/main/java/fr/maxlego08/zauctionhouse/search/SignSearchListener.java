package fr.maxlego08.zauctionhouse.search;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.libs.folialib.wrapper.task.WrappedTask;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class SignSearchListener implements Listener {
   private static final String INPUT_PREFIX = ">>";
   private static final long TIMEOUT_TICKS = 1200L;
   private static final long OPEN_DELAY_TICKS = 10L;
   private static final int SIGN_Y = -5;
   private static final int MAX_Y_ATTEMPTS = 5;

   private final AuctionPlugin plugin;
   private final Set<UUID> waitingForInput = ConcurrentHashMap.newKeySet();
   private final Map<UUID, Location> signLocations = new ConcurrentHashMap<>();
   private final Map<UUID, BlockState> savedBlocks = new ConcurrentHashMap<>();
   private final Map<UUID, WrappedTask> timeoutTasks = new ConcurrentHashMap<>();
   private final Map<UUID, WrappedTask> openTasks = new ConcurrentHashMap<>();
   private final Map<UUID, String> occupiedKeys = new ConcurrentHashMap<>();
   private final Set<String> occupiedLocations = ConcurrentHashMap.newKeySet();

   public SignSearchListener(AuctionPlugin var1) {
      this.plugin = var1;
   }

   public void startSearch(Player var1) {
      UUID var2 = var1.getUniqueId();
      if (this.waitingForInput.contains(var2)) {
         this.cleanup(var2);
      }

      {
         var1.closeInventory();
         World var3 = var1.getWorld();
         Location var4 = var1.getLocation().clone();
         var4.setPitch(0.0F);
         var4.setYaw(0.0F);
         int var10 = var3.getMinHeight();
         String var11 = null;

         for(int var12 = 0; var12 < MAX_Y_ATTEMPTS; ++var12) {
            int var13 = SIGN_Y - var12;
            if (var13 < var10) {
               var13 = var10;
            }

            var4.setY(var13);
            String var14 = this.locationKey(var4);
            if (this.occupiedLocations.add(var14)) {
               var11 = var14;
               break;
            }

            if (var13 <= var10) {
               break;
            }
         }

         if (var11 == null) {
            return;
         }

         this.occupiedKeys.put(var2, var11);
         BlockState var5 = var4.getBlock().getState();
         this.savedBlocks.put(var2, var5);
         this.signLocations.put(var2, var4);
         var4.getBlock().setType(Material.PALE_OAK_SIGN, false);
         BlockState var6 = var4.getBlock().getState();
         if (var6 instanceof Sign) {
            Sign var7 = (Sign)var6;
            var7.getSide(Side.FRONT).line(0, net.kyori.adventure.text.Component.text("> TÌM KIẾM <"));
            var7.getSide(Side.FRONT).line(1, net.kyori.adventure.text.Component.text(INPUT_PREFIX));
            var7.update(true, false);
         }

         this.waitingForInput.add(var2);
         WrappedTask var9 = this.plugin.getScheduler().runAtEntityLater(var1, () -> {
            this.openTasks.remove(var2);
            if (var1.isOnline() && this.waitingForInput.contains(var2)) {
               BlockState var2x = var4.getBlock().getState(false);
               if (var2x instanceof Sign) {
                  var1.openSign((Sign)var2x, Side.FRONT);
               } else {
                  this.cleanup(var2);
               }
            }
         }, OPEN_DELAY_TICKS);
         this.openTasks.put(var2, var9);
         WrappedTask var8 = this.plugin.getScheduler().runAtEntityLater(var1, () -> {
            if (this.waitingForInput.remove(var2)) {
               this.timeoutTasks.remove(var2);
               this.restoreBlock(var2);
            }
         }, TIMEOUT_TICKS);
         this.timeoutTasks.put(var2, var8);
      }
   }

   @EventHandler(
      priority = EventPriority.LOWEST
   )
   public void onSignChange(SignChangeEvent var1) {
      Player var2 = var1.getPlayer();
      UUID var3 = var2.getUniqueId();
      if (this.waitingForInput.contains(var3)) {
         Location var4 = this.signLocations.get(var3);
         if (var4 != null && var4.getBlock().getLocation().equals(var1.getBlock().getLocation())) {
            var1.setCancelled(true);
            this.waitingForInput.remove(var3);
            WrappedTask var5 = this.timeoutTasks.remove(var3);
            if (var5 != null) {
               var5.cancel();
            }

            WrappedTask var10 = this.openTasks.remove(var3);
            if (var10 != null) {
               var10.cancel();
            }

            String var6 = var1.getLine(1);
            String var7 = var6 == null ? "" : var6.trim();
            if (var7.startsWith(INPUT_PREFIX)) {
               var7 = var7.substring(INPUT_PREFIX.length()).trim();
            }

            String var8 = var7;
            this.restoreBlock(var3);
            this.plugin.getScheduler().runAtEntityLater(var2, () -> {
               if (var2.isOnline()) {
                  this.plugin.getAuctionManager().startSearch(var2, var8);
               }

            }, 1L);
         }
      }
   }

   @EventHandler
   public void onQuit(PlayerQuitEvent var1) {
      UUID var2 = var1.getPlayer().getUniqueId();
      if (this.waitingForInput.contains(var2)) {
         this.cleanup(var2);
      }
   }

   private void cleanup(UUID var1) {
      this.waitingForInput.remove(var1);
      WrappedTask var2 = this.timeoutTasks.remove(var1);
      if (var2 != null) {
         var2.cancel();
      }

      WrappedTask var3 = this.openTasks.remove(var1);
      if (var3 != null) {
         var3.cancel();
      }

      this.restoreBlock(var1);
   }

   private void restoreBlock(UUID var1) {
      BlockState var2 = this.savedBlocks.remove(var1);
      this.signLocations.remove(var1);
      String var3 = this.occupiedKeys.remove(var1);
      if (var3 != null) {
         this.occupiedLocations.remove(var3);
      }

      if (var2 != null) {
         var2.update(true, false);
      }

   }

   private String locationKey(Location var1) {
      return var1.getWorld().getUID() + ":" + var1.getBlockX() + ":" + var1.getBlockY() + ":" + var1.getBlockZ();
   }
}
