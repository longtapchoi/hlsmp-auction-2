package fr.maxlego08.zauctionhouse.services;

import fr.maxlego08.zauctionhouse.utils.ZUtils;
import java.util.concurrent.CompletableFuture;

public abstract class AuctionService extends ZUtils {
   protected <T> CompletableFuture<T> failedFuture(Throwable var1) {
      CompletableFuture var2 = new CompletableFuture();
      var2.completeExceptionally(var1);
      return var2;
   }
}
