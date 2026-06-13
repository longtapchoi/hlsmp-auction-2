package fr.maxlego08.zauctionhouse.command.commands.admin;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.cache.PlayerCacheKey;
import fr.maxlego08.zauctionhouse.api.category.Category;
import fr.maxlego08.zauctionhouse.api.category.CategoryManager;
import fr.maxlego08.zauctionhouse.api.command.CommandType;
import fr.maxlego08.zauctionhouse.api.command.VCommand;
import fr.maxlego08.zauctionhouse.api.economy.AuctionEconomy;
import fr.maxlego08.zauctionhouse.api.economy.EconomyManager;
import fr.maxlego08.zauctionhouse.api.item.ItemType;
import fr.maxlego08.zauctionhouse.api.item.StorageType;
import fr.maxlego08.zauctionhouse.api.item.items.AuctionItem;
import fr.maxlego08.zauctionhouse.api.messages.Message;
import fr.maxlego08.zauctionhouse.api.storage.StorageManager;
import fr.maxlego08.zauctionhouse.api.utils.Permission;
import fr.maxlego08.zauctionhouse.storage.repository.repositories.AuctionItemRepository;
import fr.maxlego08.zauctionhouse.storage.repository.repositories.ItemRepository;
import fr.maxlego08.zauctionhouse.storage.repository.repositories.PlayerRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

public class CommandAuctionAdminGenerate extends VCommand {
   private static final String[] FIRST_NAMES = new String[]{"Alex", "Steve", "Luna", "Max", "Aria", "Leo", "Nova", "Felix", "Maya", "Oscar", "Zara", "Kai", "Iris", "Theo", "Cleo", "Jax", "Sage", "Finn", "Ruby", "Milo", "Nyx", "Axel", "Jade", "Raven", "Storm", "Phoenix", "Blaze", "Shadow", "Crystal", "Frost", "Ember", "Vex", "Onyx", "Dawn", "Dusk", "Echo", "Flare", "Glitch", "Hex", "Ivy", "Jet", "Knox", "Lyric", "Maze", "Neon", "Orion", "Pulse", "Quest", "Rex", "Spark"};
   private static final String[] LAST_SUFFIXES = new String[]{"Player", "Gamer", "Master", "Pro", "King", "Queen", "Lord", "Boss", "Chief", "Hero", "Legend", "Star", "Wolf", "Dragon", "Phoenix", "Ninja", "Knight", "Wizard", "Mage", "Hunter", "Crafter", "Builder", "Miner", "Archer", "Warrior", "Slayer", "Ranger", "Scout", "Seeker", "Rider", "123", "456", "789", "007", "42", "99", "77", "666", "888", "1337", "HD", "TV", "YT", "TTV", "MC", "PVP", "PVE", "OP", "GG", "XD"};
   private final Map<CommandSender, Long> confirmationMap = new HashMap();
   private final Map<CommandSender, Integer> confirmationAmountMap = new HashMap();

   public CommandAuctionAdminGenerate(AuctionPlugin var1) {
      super(var1);
      this.addSubCommand("generate");
      this.setPermission(Permission.ZAUCTIONHOUSE_ADMIN);
      this.setDescription(Message.ADMIN_GENERATE_WARNING);
      this.addRequireArg("amount", (var0, var1x) -> List.of("100", "500", "1000", "5000", "10000", "50000"));
      this.setConsoleCanUse(true);
   }

   protected CommandType perform(AuctionPlugin var1) {
      String var2 = this.argAsString(0);
      if (var2 == null) {
         return CommandType.SYNTAX_ERROR;
      } else {
         int var3;
         try {
            var3 = Integer.parseInt(var2);
         } catch (NumberFormatException var9) {
            this.message(this.plugin, this.sender, Message.ADMIN_GENERATE_INVALID_AMOUNT, new Object[0]);
            return CommandType.DEFAULT;
         }

         if (var3 >= 1 && var3 <= 100000) {
            List var4 = this.getValidMaterials();
            if (var4.isEmpty()) {
               this.message(this.plugin, this.sender, Message.ADMIN_GENERATE_NO_MATERIALS, new Object[0]);
               return CommandType.DEFAULT;
            } else {
               long var5 = System.currentTimeMillis();
               Long var7 = (Long)this.confirmationMap.get(this.sender);
               Integer var8 = (Integer)this.confirmationAmountMap.get(this.sender);
               if (var7 != null && var8 != null && var5 - var7 < 30000L && var8.equals(var3)) {
                  this.confirmationMap.remove(this.sender);
                  this.confirmationAmountMap.remove(this.sender);
                  this.message(this.plugin, this.sender, Message.ADMIN_GENERATE_CONFIRMED, new Object[]{"%amount%", String.valueOf(var3)});
                  this.generateAuctionItems(var3, var4);
                  return CommandType.SUCCESS;
               } else {
                  this.confirmationMap.put(this.sender, var5);
                  this.confirmationAmountMap.put(this.sender, var3);
                  this.message(this.plugin, this.sender, Message.ADMIN_GENERATE_WARNING, new Object[]{"%amount%", String.valueOf(var3)});
                  return CommandType.SUCCESS;
               }
            }
         } else {
            this.message(this.plugin, this.sender, Message.ADMIN_GENERATE_INVALID_AMOUNT, new Object[0]);
            return CommandType.DEFAULT;
         }
      }
   }

   private List<Material> getValidMaterials() {
      ArrayList var1 = new ArrayList();
      CategoryManager var2 = this.plugin.getCategoryManager();

      for(Material var6 : Material.values()) {
         if (!var6.isAir() && var6.isItem()) {
            ItemStack var7 = new ItemStack(var6);
            Category var8 = var2.getCategoryFor(var7);
            if (!var8.isMiscellaneous()) {
               var1.add(var6);
            }
         }
      }

      return var1;
   }

   private void generateAuctionItems(int var1, List<Material> var2) {
      long var3 = System.currentTimeMillis();
      CommandSender var5 = this.sender;
      ArrayList var6 = new ArrayList(var1);
      HashMap var7 = new HashMap();
      PlayerRepository var8 = (PlayerRepository)this.plugin.getStorageManager().with(PlayerRepository.class);

      for(int var9 = 0; var9 < var1; ++var9) {
         ThreadLocalRandom var10 = ThreadLocalRandom.current();
         String var11 = this.generateRandomName(var10);
         UUID var12 = (UUID)var7.get(var11);
         if (var12 == null) {
            var12 = var8.selectByName(var11);
            if (var12 == null) {
               var12 = UUID.randomUUID();
            }

            var7.put(var11, var12);
         }

         Material var13 = (Material)var2.get(var10.nextInt(var2.size()));
         int var14 = var13.getMaxStackSize() == 1 ? 1 : var10.nextInt(var13.getMaxStackSize()) + 1;
         BigDecimal var15 = BigDecimal.valueOf((long)(var10.nextInt(99990) + 10));
         long var16 = System.currentTimeMillis() + 86400000L;
         var6.add(new GenerationData(var12, var11, var13, var14, var15, var16));
      }

      StorageManager var18 = this.plugin.getStorageManager();
      EconomyManager var19 = this.plugin.getEconomyManager();
      AuctionEconomy var20 = var19.getDefaultEconomy(ItemType.AUCTION);
      if (var20 == null) {
         this.plugin.getLogger().severe("No default economy configured for AUCTION items, cannot generate items.");
      } else {
         AtomicInteger var21 = new AtomicInteger(0);
         AtomicInteger var22 = new AtomicInteger(0);
         this.plugin.getScheduler().runAsync((var11x) -> {
            try {
               ItemRepository var12 = (ItemRepository)var18.with(ItemRepository.class);
               AuctionItemRepository var13 = (AuctionItemRepository)var18.with(AuctionItemRepository.class);
               HashSet var14 = new HashSet();

               for(GenerationData var16 : var6) {
                  var14.add(var16.sellerName);
               }

               int var28 = var14.size();
               this.plugin.getLogger().info("[Generate] Starting registration of " + var28 + " unique players...");
               this.plugin.getScheduler().runNextTick((var3x) -> this.message(this.plugin, var5, Message.ADMIN_GENERATE_PLAYERS_START, new Object[]{"%total%", String.valueOf(var28)}));
               HashMap var29 = new HashMap();
               AtomicInteger var17 = new AtomicInteger(0);
               AtomicInteger var18x = new AtomicInteger(0);

               for(GenerationData var20x : var6) {
                  if (!var29.containsKey(var20x.sellerName)) {
                     var8.upsertPlayer(var20x.sellerUUID, var20x.sellerName);
                     var29.put(var20x.sellerName, var20x.sellerUUID);
                     int var21x = var17.incrementAndGet();
                     if (var21x - var18x.get() >= 100) {
                        var18x.set(var21x);
                        this.plugin.getLogger().info("[Generate] Players registered: " + var21x + "/" + var28);
                        this.plugin.getScheduler().runNextTick((var4) -> this.message(this.plugin, var5, Message.ADMIN_GENERATE_PLAYERS_PROGRESS, new Object[]{"%current%", String.valueOf(var21x), "%total%", String.valueOf(var28)}));
                     }
                  }
               }

               int var30 = var17.get();
               this.plugin.getLogger().info("[Generate] Completed registration of " + var30 + " unique players.");
               this.plugin.getScheduler().runNextTick((var3x) -> this.message(this.plugin, var5, Message.ADMIN_GENERATE_PLAYERS_COMPLETE, new Object[]{"%amount%", String.valueOf(var30)}));
               this.plugin.getLogger().info("[Generate] Starting creation of " + var1 + " auction items...");
               this.plugin.getScheduler().runNextTick((var3x) -> this.message(this.plugin, var5, Message.ADMIN_GENERATE_ITEMS_START, new Object[]{"%total%", String.valueOf(var1)}));

               for(GenerationData var33 : var6) {
                  try {
                     ItemStack var22x = new ItemStack(var33.material, var33.itemAmount);
                     int var23 = var12.create(var33.sellerUUID, ItemType.AUCTION, var33.price, var33.expiredAt, var20);
                     AuctionItem var24 = var13.create(var33.sellerUUID, var33.sellerName, var23, var33.price, var33.expiredAt, List.of(var22x), var20);
                     this.plugin.getCategoryManager().applyCategories(var24);
                     this.auctionManager.addItem(StorageType.LISTED, var24);
                     int var25 = var21.incrementAndGet();
                     if (var25 - var22.get() >= 500) {
                        var22.set(var25);
                        this.plugin.getScheduler().runNextTick((var4) -> this.message(this.plugin, var5, Message.ADMIN_GENERATE_PROGRESS, new Object[]{"%current%", String.valueOf(var25), "%total%", String.valueOf(var1)}));
                     }
                  } catch (Exception var26) {
                     this.plugin.getLogger().warning("Failed to generate auction item: " + var26.getMessage());
                  }
               }

               long var32 = System.currentTimeMillis();
               long var34 = var32 - var3;
               this.auctionManager.clearPlayersCache(PlayerCacheKey.ITEMS_LISTED, PlayerCacheKey.ITEMS_SEARCH);
               this.plugin.getCategoryManager().invalidateCategoryCountCache();
               this.plugin.getScheduler().runNextTick((var5x) -> this.message(this.plugin, var5, Message.ADMIN_GENERATE_COMPLETE, new Object[]{"%amount%", String.valueOf(var21.get()), "%time%", String.valueOf(var34)}));
            } catch (Exception var27) {
               this.plugin.getLogger().severe("Failed to generate auction items: " + var27.getMessage());
               this.plugin.getScheduler().runNextTick((var3x) -> this.message(this.plugin, var5, Message.ADMIN_GENERATE_COMPLETE, new Object[]{"%amount%", String.valueOf(var21.get()), "%time%", "ERROR"}));
            }

         });
      }
   }

   private String generateRandomName(ThreadLocalRandom var1) {
      String var2 = FIRST_NAMES[var1.nextInt(FIRST_NAMES.length)];
      String var3 = LAST_SUFFIXES[var1.nextInt(LAST_SUFFIXES.length)];
      int var4 = var1.nextInt(4);
      String var10000;
      switch (var4) {
         case 0 -> var10000 = var2 + var3;
         case 1 -> var10000 = var2 + "_" + var3;
         case 2 -> var10000 = var3 + var2;
         default -> var10000 = var2 + var1.nextInt(1000);
      }

      return var10000;
   }

   private static record GenerationData(UUID sellerUUID, String sellerName, Material material, int itemAmount, BigDecimal price, long expiredAt) {
   }
}
