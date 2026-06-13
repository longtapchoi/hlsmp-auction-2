package fr.maxlego08.zauctionhouse.libs.currencies.providers;

import fr.maxlego08.zauctionhouse.libs.currencies.CurrencyProvider;
import java.math.BigDecimal;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;

public class ItemProvider implements CurrencyProvider {
   protected final Plugin plugin;
   private final ItemStack itemStack;

   public ItemProvider(Plugin var1, ItemStack var2) {
      this.plugin = var1;
      this.itemStack = var2;
   }

   public void deposit(UUID var1, BigDecimal var2, String var3) {
      Player var4 = Bukkit.getPlayer(var1);
      if (var4 != null) {
         this.giveItem(var4, (long)var2.intValue(), this.itemStack);
      } else {
         this.plugin.getLogger().severe("Deposit items to " + var1 + " but is offline");
      }

   }

   public void withdraw(UUID var1, BigDecimal var2, String var3) {
      Player var4 = Bukkit.getPlayer(var1);
      if (var4 != null) {
         this.removeItems(var4, this.itemStack, (long)var2.intValue());
      } else {
         this.plugin.getLogger().severe("Withdraw items from " + var1 + " but is offline");
      }

   }

   public BigDecimal getBalance(UUID var1) {
      Player var2 = Bukkit.getPlayer(var1);
      return var2 != null ? BigDecimal.valueOf((long)this.getAmount(var2, this.itemStack)) : BigDecimal.ZERO;
   }

   protected int getAmount(Player var1, ItemStack var2) {
      int var3 = 0;

      for(int var4 = 0; var4 != 36; ++var4) {
         ItemStack var5 = var1.getInventory().getItem(var4);
         if (var5 != null && var5.isSimilar(var2)) {
            var3 += var5.getAmount();
         }
      }

      return var3;
   }

   protected void removeItems(Player var1, ItemStack var2, long var3) {
      PlayerInventory var5 = var1.getInventory();
      int var6 = (int)var3;
      int var7 = 0;

      for(ItemStack var11 : var5.getContents()) {
         if (var11 != null && var11.isSimilar(var2) && var6 > 0) {
            int var12 = var11.getAmount() - var6;
            var6 -= var11.getAmount();
            if (var12 <= 0) {
               if (var7 == 40) {
                  var5.setItemInOffHand((ItemStack)null);
               } else {
                  var5.removeItem(new ItemStack[]{var11});
               }
            } else {
               var11.setAmount(var12);
            }
         }

         ++var7;
      }

   }

   protected void giveItem(Player var1, long var2, ItemStack var4) {
      var4 = var4.clone();
      if (var2 > 64L) {
         var2 -= 64L;
         var4.setAmount(64);
         this.give(var1, var4);
         this.giveItem(var1, var2, var4);
      } else {
         var4.setAmount((int)var2);
         this.give(var1, var4);
      }

   }

   public ItemStack getItemStack(Player var1) {
      return this.itemStack.clone();
   }

   private void give(Player var1, ItemStack var2) {
      if (this.hasInventoryFull(var1)) {
         var1.getWorld().dropItem(var1.getLocation(), var2);
      } else {
         var1.getInventory().addItem(new ItemStack[]{var2});
      }

   }

   private boolean hasInventoryFull(Player var1) {
      int var2 = 0;
      PlayerInventory var3 = var1.getInventory();

      for(int var4 = 0; var4 != 36; ++var4) {
         ItemStack var5 = var3.getContents()[var4];
         if (var5 == null) {
            ++var2;
         }
      }

      return var2 == 0;
   }
}
