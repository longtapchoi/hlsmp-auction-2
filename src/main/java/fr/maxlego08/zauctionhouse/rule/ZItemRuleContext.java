package fr.maxlego08.zauctionhouse.rule;

import fr.maxlego08.zauctionhouse.api.component.ComponentMessage;
import fr.maxlego08.zauctionhouse.api.rules.ItemRuleContext;
import fr.maxlego08.zauctionhouse.utils.component.ComponentMessageHelper;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ZItemRuleContext implements ItemRuleContext {
   private final ItemStack itemStack;
   private final Material material;
   private final String displayName;
   private final boolean hasDisplayName;
   private final List<String> lore;
   private final boolean hasLore;
   private final int customModelData;
   private final boolean hasCustomModelData;

   public ZItemRuleContext(ItemStack var1) {
      this.itemStack = var1;
      this.material = var1.getType();
      ComponentMessage var2 = ComponentMessageHelper.componentMessage;
      this.hasDisplayName = var2.hasDisplayName(var1);
      this.displayName = this.hasDisplayName ? var2.getItemStackName(var1) : null;
      this.lore = var2.getItemStackLore(var1);
      this.hasLore = this.lore != null && !this.lore.isEmpty();
      ItemMeta var3 = var1.getItemMeta();
      if (var3 != null && var3.hasCustomModelData()) {
         this.hasCustomModelData = true;
         this.customModelData = var3.getCustomModelData();
      } else {
         this.hasCustomModelData = false;
         this.customModelData = 0;
      }

   }

   public ItemStack getItemStack() {
      return this.itemStack;
   }

   public Material getMaterial() {
      return this.material;
   }

   public String getDisplayName() {
      return this.displayName;
   }

   public boolean hasDisplayName() {
      return this.hasDisplayName;
   }

   public List<String> getLore() {
      return this.lore;
   }

   public boolean hasLore() {
      return this.hasLore;
   }

   public int getCustomModelData() {
      return this.customModelData;
   }

   public boolean hasCustomModelData() {
      return this.hasCustomModelData;
   }
}
