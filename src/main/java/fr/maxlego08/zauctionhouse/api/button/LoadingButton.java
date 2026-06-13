package fr.maxlego08.zauctionhouse.api.button;

import fr.maxlego08.menu.api.button.PaginateButton;
import fr.maxlego08.zauctionhouse.api.AuctionPlugin;

public abstract class LoadingButton extends PaginateButton {
   protected final AuctionPlugin plugin;
   protected final int loadingSlot;

   public LoadingButton(AuctionPlugin var1, int var2) {
      this.plugin = var1;
      this.loadingSlot = var2;
   }

   public AuctionPlugin getPlugin() {
      return this.plugin;
   }

   public int getLoadingSlot() {
      return this.loadingSlot;
   }
}
