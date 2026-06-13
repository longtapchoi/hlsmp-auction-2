package fr.maxlego08.zauctionhouse.migration.v3.reader;

import fr.maxlego08.zauctionhouse.migration.v3.items.V3AuctionItem;
import fr.maxlego08.zauctionhouse.migration.v3.items.V3Transaction;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface V3DataReader {
   CompletableFuture<Boolean> testConnection() throws java.sql.SQLException;

   CompletableFuture<List<V3AuctionItem>> readItems() throws java.sql.SQLException;

   CompletableFuture<List<V3Transaction>> readTransactions() throws java.sql.SQLException;

   CompletableFuture<Integer> getItemCount() throws java.sql.SQLException;

   CompletableFuture<Integer> getTransactionCount() throws java.sql.SQLException;

   void close() throws java.sql.SQLException;
}
