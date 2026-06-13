package fr.maxlego08.zauctionhouse.migration.v3.reader;

import fr.maxlego08.zauctionhouse.migration.v3.items.V3AuctionItem;
import fr.maxlego08.zauctionhouse.migration.v3.items.V3Transaction;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface V3DataReader {
   CompletableFuture<Boolean> testConnection();

   CompletableFuture<List<V3AuctionItem>> readItems();

   CompletableFuture<List<V3Transaction>> readTransactions();

   CompletableFuture<Integer> getItemCount();

   CompletableFuture<Integer> getTransactionCount();

   void close();
}
