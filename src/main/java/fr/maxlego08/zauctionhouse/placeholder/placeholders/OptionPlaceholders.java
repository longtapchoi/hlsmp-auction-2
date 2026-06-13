package fr.maxlego08.zauctionhouse.placeholder.placeholders;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.option.PlayerOption;
import fr.maxlego08.zauctionhouse.api.placeholders.Placeholder;
import fr.maxlego08.zauctionhouse.api.placeholders.PlaceholderRegister;

public class OptionPlaceholders implements PlaceholderRegister {
   public void register(Placeholder var1, AuctionPlugin var2) {
      var1.register("option_", (var1x, var2x) -> {
         if (var2x != null && !var2x.isEmpty()) {
            PlayerOption var3 = PlayerOption.fromKey(var2x);
            return var3 == null ? "false" : String.valueOf(var2.getAuctionManager().getOptionService().getOption(var1x, var3));
         } else {
            return "false";
         }
      }, "Returns the value of a player option (true/false)", "<option_key>");
   }
}
