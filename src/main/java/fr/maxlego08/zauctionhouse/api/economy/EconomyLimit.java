package fr.maxlego08.zauctionhouse.api.economy;

import java.math.BigDecimal;

public interface EconomyLimit {
   String getEconomyName();

   BigDecimal getMax();

   BigDecimal getMin();
}
