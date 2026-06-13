package fr.maxlego08.zauctionhouse.services;

import fr.maxlego08.zauctionhouse.api.AuctionManager;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCacheKey;
import fr.maxlego08.zauctionhouse.api.configuration.Configuration;
import fr.maxlego08.zauctionhouse.api.event.events.AuctionExpireEvent;
import fr.maxlego08.zauctionhouse.api.item.Item;
import fr.maxlego08.zauctionhouse.api.item.ItemStatus;
import fr.maxlego08.zauctionhouse.api.item.StorageType;
import fr.maxlego08.zauctionhouse.api.services.AuctionExpireService;
import fr.maxlego08.zauctionhouse.api.storage.StorageManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@SuppressWarnings({"unchecked", "rawtypes"})
public class ExpireService implements AuctionExpireService {
   private final AuctionPlugin plugin;
   private final AuctionManager auctionManager;

   public ExpireService(AuctionPlugin var1, AuctionManager var2) {
      this.plugin = var1;
      this.auctionManager = var2;
   }

   public void processExpiredItem(Item var1, StorageType var2) {
      if (var1.getStatus() == ItemStatus.DELETED) {
         this.auctionManager.removeItem(var2, var1);
      } else {
         this.plugin.getScheduler().runNextTick((var2x) -> {
            AuctionExpireEvent var3 = new AuctionExpireEvent(List.of(var1), var2);
            var3.callEvent();
         });
         Configuration var3 = this.plugin.getConfiguration();
         StorageManager var4 = this.plugin.getStorageManager();
         this.auctionManager.clearPlayersCache(PlayerCacheKey.ITEMS_LISTED, PlayerCacheKey.ITEMS_SEARCH);
         OfflinePlayer var5 = var1.getSeller();
         if (var5.isOnline()) {
            Player var6 = var5.getPlayer();
            if (var6 != null) {
               this.auctionManager.clearPlayerCache(var6, PlayerCacheKey.ITEMS_SELLING, PlayerCacheKey.ITEMS_EXPIRED);
            }
         }

         if (var2 == StorageType.LISTED) {
            var1.setStatus(ItemStatus.REMOVED);
            this.auctionManager.removeItem(StorageType.LISTED, var1);
            Consumer var10 = (var3x) -> this.plugin.getScheduler().runNextTick((var4x) -> {
                  long var5 = var3x > 0L ? System.currentTimeMillis() + var3x * 1000L : 0L;
                  var1.setExpiredAt(new Date(var5));
                  this.auctionManager.addItem(StorageType.EXPIRED, var1);
                  var4.updateItem(var1, StorageType.EXPIRED);
               });
            Player var7 = var5.isOnline() ? var5.getPlayer() : null;
            if (var7 != null) {
               long var8 = var3.getExpireExpiration().getExpiration(var7);
               var10.accept(var8);
            } else {
               var3.getExpireExpiration().getExpiration(this.plugin.getOfflinePermission(), var5).whenComplete((var4x, var5x) -> {
                  long var6 = var4x != null ? var4x : var3.getExpireExpiration().defaultExpiration();
                  if (var5x != null) {
                     this.plugin.getLogger().log(Level.WARNING, "Cannot compute expiration for offline player " + var5.getName(), var5x);
                  }

                  var10.accept(var6);
               });
            }
         } else {
            var1.setStatus(ItemStatus.DELETED);
            var4.updateItem(var1, StorageType.DELETED);
         }

         if (this.plugin.getConfiguration().isEnableDebug()) {
            Logger var10000 = this.plugin.getLogger();
            int var10001 = var1.getId();
            var10000.info("Item " + var10001 + " expired from " + String.valueOf(var2) + " (seller: " + var1.getSellerName() + ")");
         }

      }
   }

   public void processExpiredItems(List<Item> var1, StorageType var2) {
      if (!var1.isEmpty()) {
         ArrayList var3 = new ArrayList();

         for(Item var5 : var1) {
            if (var5.getStatus() == ItemStatus.DELETED) {
               this.auctionManager.removeItem(var2, var5);
            } else {
               var3.add(var5);
            }
         }

         if (!var3.isEmpty()) {
            Configuration var17 = this.plugin.getConfiguration();
            StorageManager var18 = this.plugin.getStorageManager();
            this.plugin.getScheduler().runNextTick((var2x) -> {
               AuctionExpireEvent var3x = new AuctionExpireEvent(var3, var2);
               var3x.callEvent();
            });
            this.auctionManager.clearPlayersCache(PlayerCacheKey.ITEMS_LISTED, PlayerCacheKey.ITEMS_SEARCH);
            HashSet var7 = new HashSet();

            for(Item var9 : var3) {
               OfflinePlayer var10 = var9.getSeller();
               if (var10.isOnline() && !var7.contains(var10)) {
                  var7.add(var10);
                  Player var11 = var10.getPlayer();
                  if (var11 != null) {
                     this.auctionManager.clearPlayerCache(var11, PlayerCacheKey.ITEMS_SELLING, PlayerCacheKey.ITEMS_EXPIRED);
                  }
               }
            }

            if (var2 == StorageType.LISTED) {
               ArrayList var19 = new ArrayList();
               ArrayList var22 = new ArrayList();

               for(Item var28 : var3) {
                  var28.setStatus(ItemStatus.REMOVED);
                  this.auctionManager.removeItem(StorageType.LISTED, var28);
                  if (var28.getSeller().isOnline()) {
                     var19.add(var28);
                  } else {
                     var22.add(var28);
                  }
               }

               if (!var19.isEmpty()) {
                  for(Item var29 : var19) {
                     Player var12 = var29.getSeller().getPlayer();
                     long var13 = var12 != null ? var17.getExpireExpiration().getExpiration(var12) : var17.getExpireExpiration().defaultExpiration();
                     long var15 = var13 > 0L ? System.currentTimeMillis() + var13 * 1000L : 0L;
                     var29.setExpiredAt(new Date(var15));
                     this.auctionManager.addItem(StorageType.EXPIRED, var29);
                  }

                  EnumMap var26 = new EnumMap(StorageType.class);
                  var26.put(StorageType.EXPIRED, var19);
                  var18.updateItems(var26);
               }

               if (!var22.isEmpty()) {
                  AtomicInteger var27 = new AtomicInteger(var22.size());
                  CopyOnWriteArrayList var30 = new CopyOnWriteArrayList();

                  for(Item var32 : var22) {
                     var17.getExpireExpiration().getExpiration(this.plugin.getOfflinePermission(), var32.getSeller()).whenComplete((var6, var7x) -> {
                        long var8 = var6 != null ? var6 : var17.getExpireExpiration().defaultExpiration();
                        if (var7x != null) {
                           this.plugin.getLogger().log(Level.WARNING, "Cannot compute expiration for offline player " + var32.getSeller().getName(), var7x);
                        }

                        long var10 = var8 > 0L ? System.currentTimeMillis() + var8 * 1000L : 0L;
                        var32.setExpiredAt(new Date(var10));
                        var30.add(var32);
                        this.auctionManager.addItem(StorageType.EXPIRED, var32);
                        if (var27.decrementAndGet() == 0) {
                           this.plugin.getScheduler().runNextTick((var2) -> {
                              EnumMap var3 = new EnumMap(StorageType.class);
                              var3.put(StorageType.EXPIRED, new ArrayList(var30));
                              var18.updateItems(var3);
                           });
                        }

                     });
                  }
               }
            } else {
               for(Item var23 : var3) {
                  var23.setStatus(ItemStatus.DELETED);
               }

               EnumMap var21 = new EnumMap(StorageType.class);
               var21.put(StorageType.DELETED, var3);
               var18.updateItems(var21);
            }

         }
      }
   }
}
