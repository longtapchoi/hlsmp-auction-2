package fr.maxlego08.zauctionhouse.api.event.events;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.economy.EconomyManager;
import fr.maxlego08.zauctionhouse.api.event.AuctionEvent;

public class AuctionLoadEconomyEvent extends AuctionEvent {
   private final AuctionPlugin plugin;
   private final EconomyManager economyManager;

   public AuctionLoadEconomyEvent(AuctionPlugin var1, EconomyManager var2) {
      this.plugin = var1;
      this.economyManager = var2;
   }

   public AuctionPlugin getPlugin() {
      return this.plugin;
   }

   public EconomyManager getEconomyManager() {
      return this.economyManager;
   }
}
