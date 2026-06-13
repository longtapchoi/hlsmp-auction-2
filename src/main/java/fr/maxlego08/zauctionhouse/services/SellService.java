package fr.maxlego08.zauctionhouse.services;

import fr.maxlego08.zauctionhouse.ZAuctionPlugin;
import fr.maxlego08.zauctionhouse.api.AuctionManager;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCache;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCacheKey;
import fr.maxlego08.zauctionhouse.api.configuration.Configuration;
import fr.maxlego08.zauctionhouse.api.economy.AuctionEconomy;
import fr.maxlego08.zauctionhouse.api.economy.EconomyManager;
import fr.maxlego08.zauctionhouse.api.inventories.Inventories;
import fr.maxlego08.zauctionhouse.api.item.ItemType;
import fr.maxlego08.zauctionhouse.api.item.StorageType;
import fr.maxlego08.zauctionhouse.api.item.items.AuctionItem;
import fr.maxlego08.zauctionhouse.api.log.LogType;
import fr.maxlego08.zauctionhouse.api.messages.Message;
import fr.maxlego08.zauctionhouse.api.rules.ItemRuleManager;
import fr.maxlego08.zauctionhouse.api.services.AuctionSellService;
import fr.maxlego08.zauctionhouse.api.services.result.SellFailReason;
import fr.maxlego08.zauctionhouse.api.services.result.SellResult;
import fr.maxlego08.zauctionhouse.api.storage.StorageManager;
import fr.maxlego08.zauctionhouse.api.tax.TaxConfiguration;
import fr.maxlego08.zauctionhouse.api.tax.TaxResult;
import fr.maxlego08.zauctionhouse.api.tax.TaxType;
import fr.maxlego08.zauctionhouse.api.utils.Base64ItemStack;
import fr.maxlego08.zauctionhouse.discord.DiscordWebhookService;
import fr.maxlego08.zauctionhouse.utils.ZUtils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

@SuppressWarnings({"unchecked", "rawtypes"})
public class SellService extends ZUtils implements AuctionSellService {
   private final AuctionPlugin plugin;
   private final AuctionManager manager;

   public SellService(AuctionPlugin var1, AuctionManager var2) {
      this.plugin = var1;
      this.manager = var2;
   }

   public CompletableFuture<SellResult> sellAuctionItems(Player var1, BigDecimal var2, long var3, Map<Integer, ItemStack> var5, AuctionEconomy var6) {
      Map var7 = (Map)var5.entrySet().stream().filter((var0) -> var0.getValue() != null && !((ItemStack)var0.getValue()).getType().isAir()).collect(Collectors.toMap(Map.Entry::getKey, (var0) -> ((ItemStack)var0.getValue()).clone()));
      if (var7.isEmpty()) {
         this.message(this.plugin, var1, Message.SELL_ERROR_AIR, new Object[0]);
         return CompletableFuture.completedFuture(SellResult.failure("No valid items to sell", SellFailReason.INVALID_ITEM));
      } else {
         ArrayList var8 = new ArrayList(var7.values());
         SellFailReason var9 = this.validateItems(var1, var2, var6, var8);
         if (var9 != SellFailReason.NONE) {
            return CompletableFuture.completedFuture(SellResult.failure("Validation failed", var9));
         } else if (!this.verifyItemsInSlots(var1, var7)) {
            this.message(this.plugin, var1, Message.SELL_ERROR_CHANGE, new Object[0]);
            return CompletableFuture.completedFuture(SellResult.failure("Items changed", SellFailReason.ITEMS_CHANGED));
         } else {
            CompletableFuture var10 = new CompletableFuture();
            var10.completeOnTimeout(SellResult.failure("Operation timed out", SellFailReason.DATABASE_ERROR), 30L, TimeUnit.SECONDS);
            this.applySellTaxAsync(var1, var2, var8, var6).thenAccept((var9x) -> {
               if (var9x == null) {
                  var10.complete(SellResult.failure("Insufficient funds for tax", SellFailReason.INSUFFICIENT_FUNDS));
               } else {
                  this.plugin.getScheduler().runAtEntity(var1, (var10x) -> {
                     if (!var1.isOnline()) {
                        if (var9x.hasTax()) {
                           var6.deposit(var1.getUniqueId(), var9x.taxAmount(), "Refund sell tax (items changed)");
                        }

                        var10.complete(SellResult.failure("Player disconnected", SellFailReason.PLAYER_DISCONNECTED));
                     } else if (!this.verifyItemsInSlots(var1, var7)) {
                        this.message(this.plugin, var1, Message.SELL_ERROR_CHANGE, new Object[0]);
                        if (var9x.hasTax()) {
                           var6.deposit(var1.getUniqueId(), var9x.taxAmount(), "Refund sell tax (items changed)");
                        }

                        var10.complete(SellResult.failure("Items changed", SellFailReason.ITEMS_CHANGED));
                     } else {
                        this.removeItemsFromSlots(var1, var7);
                        StorageManager var11 = this.plugin.getStorageManager();
                        var11.createAuctionItem(var1, var2, var3, var8, var6).thenAccept((var5) -> {
                           this.postSell(var1, var5, var6, var9x);
                           var10.complete(SellResult.success("Item listed successfully", var5));
                        }).exceptionally((var6x) -> {
                           this.plugin.getLogger().severe("Unable to sell item: " + var6x.getMessage());
                           if (var6x.getCause() != null) {
                              this.plugin.getLogger().severe("Caused by: " + var6x.getCause().getMessage());
                           }

                           if (var1.isOnline() && var8 != null) {
                              var8.forEach((var1x) -> {
                                 if (var1x != null) {
                                    var1.getInventory().addItem(new ItemStack[]{var1x});
                                 }

                              });
                           }

                           if (var9x.hasTax()) {
                              var6.deposit(var1.getUniqueId(), var9x.taxAmount(), "Refund sell tax (sale failed)");
                           }

                           var10.complete(SellResult.failure("Database error", SellFailReason.DATABASE_ERROR));
                           return null;
                        });
                     }
                  });
               }
            }).exceptionally((var2x) -> {
               this.plugin.getLogger().severe("Unable to check tax for sell: " + var2x.getMessage());
               var10.complete(SellResult.failure("Tax calculation error", SellFailReason.TAX_ERROR));
               return null;
            });
            return var10;
         }
      }
   }

   public void openSellCommandInventory(Player var1, BigDecimal var2, AuctionEconomy var3) {
      PlayerCache var4 = this.manager.getCache(var1);
      Configuration var5 = this.plugin.getConfiguration();
      long var6 = var5.getSellExpiration().getExpiration(var1);
      long var8 = var6 > 0L ? System.currentTimeMillis() + var6 * 1000L : 0L;
      var4.set(PlayerCacheKey.SELL_PRICE, var2);
      var4.set(PlayerCacheKey.SELL_ECONOMY, var3);
      var4.set(PlayerCacheKey.SELL_EXPIRED_AT, var8);
      var4.set(PlayerCacheKey.SELL_AMOUNT, 1);
      var4.remove(PlayerCacheKey.SELL_ITEMS);
      this.plugin.getInventoriesLoader().openInventory(var1, Inventories.SELL_INVENTORY);
   }

   private boolean verifyItemsInSlots(Player var1, Map<Integer, ItemStack> var2) {
      PlayerInventory var3 = var1.getInventory();

      for(Map.Entry var5 : var2.entrySet()) {
         int var6 = (Integer)var5.getKey();
         ItemStack var7 = (ItemStack)var5.getValue();
         ItemStack var8;
         if (var6 == -1) {
            var8 = var3.getItemInMainHand();
         } else {
            var8 = var3.getItem(var6);
         }

         if (var8 == null || !var8.isSimilar(var7) || var8.getAmount() < var7.getAmount()) {
            return false;
         }
      }

      return true;
   }

   private void removeItemsFromSlots(Player var1, Map<Integer, ItemStack> var2) {
      PlayerInventory var3 = var1.getInventory();

      for(Map.Entry var5 : var2.entrySet()) {
         int var6 = (Integer)var5.getKey();
         ItemStack var7 = (ItemStack)var5.getValue();
         int var8 = var7.getAmount();
         if (var6 == -1) {
            ItemStack var9 = var3.getItemInMainHand();
            if (var9.getAmount() > var8) {
               var9.setAmount(var9.getAmount() - var8);
            } else {
               var3.setItemInMainHand((ItemStack)null);
            }
         } else {
            ItemStack var10 = var3.getItem(var6);
            if (var10 != null) {
               if (var10.getAmount() > var8) {
                  var10.setAmount(var10.getAmount() - var8);
               } else {
                  var3.setItem(var6, (ItemStack)null);
               }
            }
         }
      }

   }

   private SellFailReason validateItems(Player var1, BigDecimal var2, AuctionEconomy var3, List<ItemStack> var4) {
      EconomyManager var5 = this.plugin.getEconomyManager();
      Configuration var6 = this.plugin.getConfiguration();
      ItemRuleManager var7 = this.plugin.getItemRuleManager();
      BigDecimal var8 = var3.getMaxPrice(ItemType.AUCTION);
      BigDecimal var9 = var3.getMinPrice(ItemType.AUCTION);
      if (var2.compareTo(var8) > 0) {
         this.message(this.plugin, var1, Message.PRICE_TOO_HIGH, new Object[]{"%max-price%", var5.format((AuctionEconomy)var3, var8)});
         return SellFailReason.PRICE_TOO_HIGH;
      } else if (var2.compareTo(var9) < 0) {
         this.message(this.plugin, var1, Message.PRICE_TOO_LOW, new Object[]{"%min-price%", var5.format((AuctionEconomy)var3, var9)});
         return SellFailReason.PRICE_TOO_LOW;
      } else {
         long var10 = (long)this.manager.getPlayerSellingItems(var1).size();
         long var12 = (long)var6.getPermission().getLimit(ItemType.AUCTION, var1);
         if (var10 >= var12) {
            this.message(this.plugin, var1, Message.LISTED_ITEMS_LIMIT, new Object[]{"%max-items%", String.valueOf(var12)});
            return SellFailReason.LISTING_LIMIT_REACHED;
         } else if (var6.getWorld().isWorldBanned(ItemType.AUCTION, var1.getWorld().getName())) {
            this.message(this.plugin, var1, Message.WORLD_BANNED, new Object[0]);
            return SellFailReason.WORLD_RESTRICTED;
         } else {
            for(ItemStack var15 : var4) {
               if (var15.getType().isAir()) {
                  this.message(this.plugin, var1, Message.SELL_ERROR_AIR, new Object[0]);
                  return SellFailReason.INVALID_ITEM;
               }

               if (var7.isBlacklistEnabled() && var7.isBlacklisted(var15)) {
                  this.message(this.plugin, var1, Message.ITEM_BLACKLISTED, new Object[0]);
                  return SellFailReason.BLACKLISTED;
               }

               if (var7.isWhitelistEnabled() && !var7.isWhitelisted(var15)) {
                  this.message(this.plugin, var1, Message.ITEM_WHITELISTED, new Object[0]);
                  return SellFailReason.NOT_WHITELISTED;
               }
            }

            return SellFailReason.NONE;
         }
      }
   }

   private CompletableFuture<TaxResult> applySellTaxAsync(Player var1, BigDecimal var2, List<ItemStack> var3, AuctionEconomy var4) {
      TaxConfiguration var5 = var4.getTaxConfiguration();
      EconomyManager var6 = this.plugin.getEconomyManager();
      TaxType var7 = var5.getTaxType();
      if (var5.isEnabled() && (var7 == TaxType.SELL || var7 == TaxType.BOTH)) {
         TaxResult var8 = null;

         for(ItemStack var10 : var3) {
            TaxResult var11 = var4.calculateSellTax(var1, var2, var10);
            if (var8 == null || var11.taxAmount().compareTo(var8.taxAmount()) > 0) {
               var8 = var11;
            }
         }

         if (var8 == null) {
            return CompletableFuture.completedFuture(TaxResult.disabled(var2));
         } else if (var8.isBypassed()) {
            this.message(this.plugin, var1, Message.TAX_EXEMPT, new Object[0]);
            return CompletableFuture.completedFuture(var8);
         } else if (!var8.hasTax()) {
            return CompletableFuture.completedFuture(var8);
         } else {
            return var4.has(var1.getUniqueId(), var8.taxAmount()).thenApply((var5x) -> {
               if (!var5x) {
                  this.plugin.getScheduler().runAtEntity(var1, (var5) -> this.message(this.plugin, var1, Message.TAX_INSUFFICIENT_FUNDS, new Object[]{"%tax%", var6.format((AuctionEconomy)var4, var8.taxAmount())}));
                  return null;
               } else {
                  var4.withdraw(var1.getUniqueId(), var8.taxAmount(), "Sell tax (zAuctionHouse)");
                  this.plugin.getScheduler().runAtEntity(var1, (var5) -> {
                     if (var8.isReduced()) {
                        this.message(this.plugin, var1, Message.TAX_REDUCED, new Object[]{"%percentage%", String.format("%.1f", (double)100.0F - var8.reductionPercentage())});
                     }

                     this.message(this.plugin, var1, Message.TAX_SELL_APPLIED, new Object[]{"%tax%", var6.format((AuctionEconomy)var4, var8.taxAmount()), "%percentage%", String.format("%.1f", var8.taxPercentage())});
                  });
                  return var8;
               }
            });
         }
      } else {
         return CompletableFuture.completedFuture(TaxResult.disabled(var2));
      }
   }

   private void postSell(Player var1, AuctionItem var2, AuctionEconomy var3, TaxResult var4) {
      this.plugin.getCategoryManager().applyCategories(var2);
      this.manager.addItem(StorageType.LISTED, var2);
      this.manager.clearPlayersCache(PlayerCacheKey.ITEMS_LISTED, PlayerCacheKey.ITEMS_SEARCH);
      this.manager.clearPlayerCache(var1, PlayerCacheKey.ITEMS_SELLING);
      this.manager.updateListedItems(var2, true, var1);
      this.message(this.plugin, var1, Message.ITEM_SOLD, new Object[]{"%price%", var2.getFormattedPrice(), "%items%", var2.getItemDisplay()});
      String var5 = (String)var2.getItemStacks().stream().map(Base64ItemStack::encode).collect(Collectors.joining(";"));
      String var6 = "added_auction_item_to_listed";
      if (var4.hasTax()) {
         var6 = var6 + ";sell_tax=" + String.valueOf(var4.taxAmount());
      }

      this.plugin.getStorageManager().log(LogType.SALE, var2.getId(), var1, (UUID)null, var5, var2.getPrice(), var3.getName(), var6, (Date)null);
      this.plugin.getAuctionClusterBridge().notifyItemListed(var2).thenAccept((var1x) -> this.plugin.getLogger().info("Cluster notify item sold")).exceptionally((var1x) -> {
         this.plugin.getLogger().severe("Unable to notify item sold: " + var1x.getMessage());
         return null;
      });
      AuctionPlugin var8 = this.plugin;
      if (var8 instanceof ZAuctionPlugin var7) {
         DiscordWebhookService var9 = var7.getDiscordWebhookService();
         if (var9 != null && var9.isEnabled()) {
            var9.notifyItemSold(var1, var2);
         }

         var7.getBroadcastService().broadcastSell(var1, var2);
      }

   }
}
