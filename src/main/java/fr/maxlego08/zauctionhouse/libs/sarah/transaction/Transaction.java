package fr.maxlego08.zauctionhouse.libs.sarah.transaction;

import fr.maxlego08.zauctionhouse.libs.sarah.exceptions.DatabaseException;
import java.sql.Connection;
import java.sql.SQLException;

public class Transaction implements AutoCloseable {
   private final Connection connection;
   private boolean committed = false;
   private boolean rolledBack = false;

   public Transaction(Connection var1) throws java.sql.SQLException {
      this.connection = var1;
      this.connection.setAutoCommit(false);
   }

   public Connection getConnection() {
      return this.connection;
   }

   public void commit() {
      if (!this.committed && !this.rolledBack) {
         try {
            this.connection.commit();
            this.committed = true;
         } catch (SQLException var2) {
            throw new DatabaseException("commit", var2);
         }
      } else {
         throw new IllegalStateException("Transaction already " + (this.committed ? "committed" : "rolled back"));
      }
   }

   public void rollback() {
      if (!this.committed && !this.rolledBack) {
         try {
            this.connection.rollback();
            this.rolledBack = true;
         } catch (SQLException var2) {
            throw new DatabaseException("rollback", var2);
         }
      } else {
         throw new IllegalStateException("Transaction already " + (this.committed ? "committed" : "rolled back"));
      }
   }

   public void close() {
      try {
         if (!this.committed && !this.rolledBack) {
            this.connection.rollback();
         }

         this.connection.setAutoCommit(true);
      } catch (SQLException var2) {
         throw new DatabaseException("close-transaction", var2);
      }
   }

   public boolean isCommitted() {
      return this.committed;
   }

   public boolean isRolledBack() {
      return this.rolledBack;
   }
}
