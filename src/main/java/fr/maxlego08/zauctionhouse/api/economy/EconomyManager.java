package fr.maxlego08.zauctionhouse.api.economy;

import fr.maxlego08.zauctionhouse.api.item.ItemType;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface EconomyManager {
   Collection<AuctionEconomy> getEconomies();

   boolean registerEconomy(AuctionEconomy var1);

   boolean removeEconomy(AuctionEconomy var1);

   Optional<AuctionEconomy> getEconomy(String var1);

   void loadEconomies();

   AuctionEconomy getDefaultEconomy(ItemType var1);

   DecimalFormat getPriceDecimalFormat();

   List<NumberFormatReduction> getPriceReductions();

   String format(PriceFormat var1, Number var2);

   String format(AuctionEconomy var1, Number var2);
}
