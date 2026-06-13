package fr.maxlego08.zauctionhouse.buttons.option;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.option.PlayerOption;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public class OptionToggleButton extends Button {
   private final AuctionPlugin plugin;
   private final PlayerOption option;
   private final String enableText;
   private final String disableText;

   public OptionToggleButton(AuctionPlugin var1, PlayerOption var2, String var3, String var4) {
      this.plugin = var1;
      this.option = var2;
      this.enableText = var3;
      this.disableText = var4;
   }

   public boolean isPermanent() {
      return true;
   }

   public ItemStack getCustomItemStack(@NotNull Player var1, boolean var2, @NotNull Placeholders var3) {
      boolean var4 = this.plugin.getAuctionManager().getOptionService().getOption(var1, this.option);
      var3.register("option_status", var4 ? this.enableText : this.disableText);
      var3.register("option_value", String.valueOf(var4));
      var3.register("option_name", this.option.getKey());
      return this.getItemStack().build(var1, false, var3);
   }

   public void onClick(@NonNull Player var1, @NonNull InventoryClickEvent var2, @NonNull InventoryEngine var3, int var4, @NonNull Placeholders var5) {
      super.onClick(var1, var2, var3, var4, var5);
      boolean var6 = this.plugin.getAuctionManager().getOptionService().getOption(var1, this.option);
      boolean var7 = !var6;
      this.plugin.getAuctionManager().getOptionService().setOption(var1.getUniqueId(), this.option, var7);
      this.plugin.sendMessage(var1, var7 ? this.option.getEnabledMessage() : this.option.getDisabledMessage());
      this.plugin.getInventoriesLoader().getInventoryManager().updateInventory(var1);
   }
}
