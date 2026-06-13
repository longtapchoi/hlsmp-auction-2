package fr.maxlego08.zauctionhouse.buttons.admin;

import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.engine.ItemButton;
import fr.maxlego08.menu.api.utils.LoreType;
import fr.maxlego08.menu.api.utils.MetaUpdater;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.button.LoadingButton;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCache;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCacheKey;
import fr.maxlego08.zauctionhouse.api.configuration.Configuration;
import fr.maxlego08.zauctionhouse.api.configuration.records.ItemLoreConfiguration;
import fr.maxlego08.zauctionhouse.api.filter.DateFilter;
import fr.maxlego08.zauctionhouse.api.inventories.Inventories;
import fr.maxlego08.zauctionhouse.api.log.AdminLogItem;
import fr.maxlego08.zauctionhouse.api.log.LogType;
import fr.maxlego08.zauctionhouse.api.messages.Message;
import fr.maxlego08.zauctionhouse.api.storage.dto.LogDTO;
import fr.maxlego08.zauctionhouse.api.utils.Base64ItemStack;
import fr.maxlego08.zauctionhouse.storage.repository.repositories.LogRepository;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NonNull;

public class AdminLogsButton extends LoadingButton {
   public AdminLogsButton(Plugin var1, int var2) {
      super((AuctionPlugin)var1, var2);
   }

   public void onRender(Player var1, InventoryEngine var2) {
      Optional var3 = this.getTarget(var1);
      if (var3.isEmpty()) {
         this.plugin.getAuctionManager().message(var1, Message.ADMIN_TARGET_REQUIRED);
      } else {
         PlayerCache var4 = this.plugin.getAuctionManager().getCache(var1);
         Boolean var5 = (Boolean)var4.get(PlayerCacheKey.ADMIN_LOGS_LOADING, false);
         if (var5) {
            this.showLoadingItem(var2, var1);
         } else {
            Object var6 = (List)var4.get(PlayerCacheKey.ADMIN_LOGS_DATA);
            if ((var6 == null || ((List)var6).isEmpty()) && !var4.has(PlayerCacheKey.ADMIN_LOGS_DATA)) {
               this.loadLogsAsync(var1, (UUID)var3.get(), var2);
            } else {
               if (var6 == null) {
                  var6 = new ArrayList();
               }

               List var7 = this.applyFilters(var4, (List)var6);
               if (var7.isEmpty()) {
                  var2.buildButton(this.getElseButton(), new Placeholders());
               } else {
                  Configuration var8 = this.plugin.getConfiguration();
                  SimpleDateFormat var9 = var8.getDateFormat();
                  List var10 = var8.getItemLore().adminLogLore();
                  List var11 = var8.getItemLore().adminLogMultipleLore();
                  this.paginate(var7, var2, (var6x, var7x) -> {
                     AdminLogItem var8 = this.createAdminLogItem(var7x);
                     boolean var9x = var8.hasMultipleItems();
                     List var10x = var9x ? var11 : var10;
                     ItemStack var11x = this.createDisplayItem(var8, var9, var10x);
                     ItemButton var12 = var2.addItem(var6x, var11x);
                     if (var12 != null) {
                        var12.setClick((var3) -> this.handleClick(var1, var3.getClick(), var8));
                     }

                  });
               }
            }
         }
      }
   }

   public int getPaginationSize(@NonNull Player var1) {
      PlayerCache var2 = this.plugin.getAuctionManager().getCache(var1);
      List var3 = (List)var2.get(PlayerCacheKey.ADMIN_LOGS_DATA);
      return var3 == null ? 0 : this.applyFilters(var2, var3).size();
   }

   private Optional<UUID> getTarget(Player var1) {
      return Optional.ofNullable((UUID)this.plugin.getAuctionManager().getCache(var1).get(PlayerCacheKey.ADMIN_TARGET_UUID));
   }

   private void loadLogsAsync(Player var1, UUID var2, InventoryEngine var3) {
      PlayerCache var4 = this.plugin.getAuctionManager().getCache(var1);
      var4.set(PlayerCacheKey.ADMIN_LOGS_LOADING, true);
      this.showLoadingItem(var3, var1);
      CompletableFuture.supplyAsync(() -> ((LogRepository)this.plugin.getStorageManager().with(LogRepository.class)).selectByPlayerOrTarget(var2), this.plugin.getExecutorService()).thenAccept((var3x) -> {
         var4.set(PlayerCacheKey.ADMIN_LOGS_DATA, var3x);
         var4.set(PlayerCacheKey.ADMIN_LOGS_LOADING, false);
         this.plugin.getScheduler().runAtEntity(var1, (var2) -> {
            if (var1.isOnline()) {
               this.plugin.getInventoriesLoader().getInventoryManager().updateInventory(var1);
            }

         });
      }).exceptionally((var2x) -> {
         var4.set(PlayerCacheKey.ADMIN_LOGS_LOADING, false);
         this.plugin.getLogger().severe("Failed to load logs: " + var2x.getMessage());
         return null;
      });
   }

   private void showLoadingItem(InventoryEngine var1, Player var2) {
      var1.addItem(this.loadingSlot, this.getCustomItemStack(var2, false, new Placeholders()));
   }

   private List<LogDTO> applyFilters(PlayerCache var1, List<LogDTO> var2) {
      LogType var3 = (LogType)var1.get(PlayerCacheKey.ADMIN_LOGS_TYPE_FILTER);
      DateFilter var4 = (DateFilter)var1.get(PlayerCacheKey.ADMIN_LOGS_DATE_FILTER, DateFilter.ALL);
      return var2.stream().filter((var1x) -> var3 == null || var1x.log_type() == var3).filter((var1x) -> var4.matches(var1x.created_at())).toList();
   }

   private AdminLogItem createAdminLogItem(LogDTO var1) {
      ArrayList var2 = new ArrayList();
      if (var1.itemstack() != null && !var1.itemstack().isEmpty()) {
         try {
            ItemStack var3 = Base64ItemStack.decode(var1.itemstack());
            if (var3 != null) {
               var2.add(var3);
            }
         } catch (Exception var4) {
            Logger var10000 = this.plugin.getLogger();
            int var10001 = var1.id();
            var10000.warning("Failed to decode itemstack for log " + var10001 + ": " + var4.getMessage());
         }
      }

      return new AdminLogItem(var1, var2);
   }

   private ItemStack createDisplayItem(AdminLogItem var1, SimpleDateFormat var2, List<String> var3) {
      LogDTO var4 = var1.log();
      ItemStack var6 = var1.getFirstItem();
      ItemStack var5;
      if (var6 != null) {
         var5 = var6.clone();
      } else {
         var5 = new ItemStack(this.getMaterialForLogType(var4.log_type()));
      }

      ItemMeta var7 = var5.getItemMeta();
      if (var7 == null) {
         return var5;
      } else {
         ItemLoreConfiguration var8 = this.plugin.getConfiguration().getItemLore();
         Placeholders var9 = new Placeholders();
         var9.register("type", var8.getLogTypeName(var4.log_type()));
         var9.register("player", this.getPlayerName(var4.player_unique_id()));
         var9.register("target", var4.target_unique_id() != null ? this.getPlayerName(var4.target_unique_id()) : "N/A");
         var9.register("price", this.formatPrice(var4));
         var9.register("date", var2.format(var4.created_at()));
         var9.register("item_id", String.valueOf(var4.item_id()));
         MetaUpdater var10 = this.plugin.getInventoriesLoader().getInventoryManager().getMeta();
         Stream var10002 = var3.stream();
         Objects.requireNonNull(var9);
         var10.updateLore(var7, var10002.map(var9::parse).toList(), LoreType.APPEND);
         var5.setItemMeta(var7);
         return var5;
      }
   }

   private void handleClick(Player var1, ClickType var2, AdminLogItem var3) {
      PlayerCache var4 = this.plugin.getAuctionManager().getCache(var1);
      if (var2 != ClickType.LEFT && var2 != ClickType.SHIFT_LEFT) {
         if (var2 == ClickType.RIGHT || var2 == ClickType.SHIFT_RIGHT) {
            if (var3.hasMultipleItems()) {
               var4.set(PlayerCacheKey.ADMIN_LOG_SELECTED, var3);
               var4.set(PlayerCacheKey.CURRENT_PAGE, this.plugin.getInventoriesLoader().getInventoryManager().getPage(var1));
               this.plugin.getInventoriesLoader().openInventory(var1, Inventories.PURCHASE_INVENTORY_CONFIRM);
            } else {
               this.giveItemsToPlayer(var1, var3);
            }
         }
      } else {
         this.giveItemsToPlayer(var1, var3);
      }

   }

   private void giveItemsToPlayer(Player var1, AdminLogItem var2) {
      List var3 = var2.itemStacks();
      if (var3 != null && !var3.isEmpty()) {
         for(ItemStack var5 : var3) {
            if (var5 != null) {
               var1.getInventory().addItem(new ItemStack[]{var5.clone()}).forEach((var1x, var2x) -> var1.getWorld().dropItem(var1.getLocation(), var2x));
            }
         }

         this.plugin.getAuctionManager().message(var1, Message.ADMIN_ITEM_RETRIEVED);
      } else {
         this.plugin.getAuctionManager().message(var1, Message.ADMIN_NO_ITEM_TO_RETRIEVE);
      }
   }

   private String formatPrice(LogDTO var1) {
      String var2 = var1.price() != null ? var1.price().toPlainString() : "0";
      return var1.economy_name() != null && !var1.economy_name().isEmpty() ? var2 + " " + var1.economy_name() : var2;
   }

   private Material getMaterialForLogType(LogType var1) {
      Material var10000;
      switch (var1) {
         case SALE -> var10000 = Material.GOLD_INGOT;
         case PURCHASE -> var10000 = Material.EMERALD;
         case REMOVE_LISTED -> var10000 = Material.BARRIER;
         case REMOVE_SELLING -> var10000 = Material.CHEST;
         case REMOVE_EXPIRED -> var10000 = Material.CLOCK;
         case REMOVE_PURCHASED -> var10000 = Material.HOPPER;
         default -> throw new MatchException((String)null, (Throwable)null);
      }

      return var10000;
   }

   private String getPlayerName(UUID var1) {
      if (var1 == null) {
         return "N/A";
      } else {
         OfflinePlayer var2 = this.plugin.getServer().getOfflinePlayer(var1);
         return var2.getName() != null ? var2.getName() : var1.toString().substring(0, 8);
      }
   }
}
