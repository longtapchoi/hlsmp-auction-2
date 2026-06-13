package fr.maxlego08.zauctionhouse.libs.currencies.providers;

import fr.maxlego08.menu.api.InventoryManager;
import fr.maxlego08.menu.api.MenuItemStack;
import java.io.File;
import java.math.BigDecimal;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class ZMenuItemProvider extends ItemProvider {
   private final MenuItemStack menuItemStack;

   public ZMenuItemProvider(Plugin var1, File var2, String var3) {
      super(var1, (ItemStack)null);
      InventoryManager var4 = (InventoryManager)var1.getServer().getServicesManager().getRegistration(InventoryManager.class).getProvider();
      this.menuItemStack = var4.loadItemStack(YamlConfiguration.loadConfiguration(var2), var3, var2);
   }

   public ZMenuItemProvider(Plugin var1, MenuItemStack var2) {
      super(var1, (ItemStack)null);
      this.menuItemStack = var2;
   }

   public BigDecimal getBalance(UUID var1) {
      Player var2 = Bukkit.getPlayer(var1);
      return var2 != null ? BigDecimal.valueOf((long)this.getAmount(var2, this.menuItemStack.build(var2))) : BigDecimal.ZERO;
   }

   public void deposit(UUID var1, BigDecimal var2, String var3) {
      Player var4 = Bukkit.getPlayer(var1);
      if (var4 != null) {
         this.giveItem(var4, (long)var2.intValue(), this.menuItemStack.build(var4));
      } else {
         this.plugin.getLogger().severe("Deposit items to " + var1 + " but is offline");
      }

   }

   public void withdraw(UUID var1, BigDecimal var2, String var3) {
      Player var4 = Bukkit.getPlayer(var1);
      if (var4 != null) {
         this.removeItems(var4, this.menuItemStack.build(var4), (long)var2.intValue());
      } else {
         this.plugin.getLogger().severe("Withdraw items from " + var1 + " but is offline");
      }

   }

   public ItemStack getItemStack(Player var1) {
      return this.menuItemStack.build(var1);
   }
}
