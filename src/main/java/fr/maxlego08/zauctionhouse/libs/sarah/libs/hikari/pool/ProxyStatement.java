package fr.maxlego08.zauctionhouse.libs.sarah.libs.hikari.pool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class ProxyStatement implements Statement {
   protected final ProxyConnection connection;
   final Statement delegate;
   private boolean isClosed;
   private ResultSet proxyResultSet;

   ProxyStatement(ProxyConnection var1, Statement var2) {
      this.connection = var1;
      this.delegate = var2;
   }

   final SQLException checkException(SQLException var1) {
      return this.connection.checkException(var1);
   }

   public final String toString(){
      String var1 = this.delegate.toString();
      return this.getClass().getSimpleName() + '@' + System.identityHashCode(this) + " wrapping " + var1;
   }

   public final void close() throws java.sql.SQLException {
      synchronized(this) {
         if (this.isClosed) {
            return;
         }

         this.isClosed = true;
      }

      this.connection.untrackStatement(this.delegate);

      try {
         this.delegate.close();
      } catch (SQLException var3) {
         throw this.connection.checkException(var3);
      }
   }

   public Connection getConnection() {
      return this.connection;
   }

   public boolean execute(String var1) {
      this.connection.markCommitStateDirty();
      return this.delegate.execute(var1);
   }

   public boolean execute(String var1, int var2) {
      this.connection.markCommitStateDirty();
      return this.delegate.execute(var1, var2);
   }

   public ResultSet executeQuery(String var1) {
      this.connection.markCommitStateDirty();
      ResultSet var2 = this.delegate.executeQuery(var1);
      return ProxyFactory.getProxyResultSet(this.connection, this, var2);
   }

   public int executeUpdate(String var1) {
      this.connection.markCommitStateDirty();
      return this.delegate.executeUpdate(var1);
   }

   public int[] executeBatch() throws java.sql.SQLException {
      this.connection.markCommitStateDirty();
      return this.delegate.executeBatch();
   }

   public int executeUpdate(String var1, int var2) {
      this.connection.markCommitStateDirty();
      return this.delegate.executeUpdate(var1, var2);
   }

   public int executeUpdate(String var1, int[] var2) {
      this.connection.markCommitStateDirty();
      return this.delegate.executeUpdate(var1, var2);
   }

   public int executeUpdate(String var1, String[] var2) {
      this.connection.markCommitStateDirty();
      return this.delegate.executeUpdate(var1, var2);
   }

   public boolean execute(String var1, int[] var2) {
      this.connection.markCommitStateDirty();
      return this.delegate.execute(var1, var2);
   }

   public boolean execute(String var1, String[] var2) {
      this.connection.markCommitStateDirty();
      return this.delegate.execute(var1, var2);
   }

   public long[] executeLargeBatch() {
      this.connection.markCommitStateDirty();
      return this.delegate.executeLargeBatch();
   }

   public long executeLargeUpdate(String var1) {
      this.connection.markCommitStateDirty();
      return this.delegate.executeLargeUpdate(var1);
   }

   public long executeLargeUpdate(String var1, int var2) {
      this.connection.markCommitStateDirty();
      return this.delegate.executeLargeUpdate(var1, var2);
   }

   public long executeLargeUpdate(String var1, int[] var2) {
      this.connection.markCommitStateDirty();
      return this.delegate.executeLargeUpdate(var1, var2);
   }

   public long executeLargeUpdate(String var1, String[] var2) {
      this.connection.markCommitStateDirty();
      return this.delegate.executeLargeUpdate(var1, var2);
   }

   public ResultSet getResultSet() {
      ResultSet var1 = this.delegate.getResultSet();
      if (var1 != null) {
         if (this.proxyResultSet == null || ((ProxyResultSet)this.proxyResultSet).delegate != var1) {
            this.proxyResultSet = ProxyFactory.getProxyResultSet(this.connection, this, var1);
         }
      } else {
         this.proxyResultSet = null;
      }

      return this.proxyResultSet;
   }

   public ResultSet getGeneratedKeys() throws java.sql.SQLException {
      ResultSet var1 = this.delegate.getGeneratedKeys();
      if (this.proxyResultSet == null || ((ProxyResultSet)this.proxyResultSet).delegate != var1) {
         this.proxyResultSet = ProxyFactory.getProxyResultSet(this.connection, this, var1);
      }

      return this.proxyResultSet;
   }

   public final <T> T unwrap(Class<T> var1) throws java.sql.SQLException {
      if (var1.isInstance(this.delegate)) {
         return (T)this.delegate;
      } else if (this.delegate != null) {
         return (T)this.delegate.unwrap(var1);
      } else {
         throw new SQLException("Wrapped statement is not an instance of " + var1);
      }
   }
}
