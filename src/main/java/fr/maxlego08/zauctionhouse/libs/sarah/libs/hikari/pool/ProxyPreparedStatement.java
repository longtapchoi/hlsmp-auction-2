package fr.maxlego08.zauctionhouse.libs.sarah.libs.hikari.pool;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public abstract class ProxyPreparedStatement extends ProxyStatement implements PreparedStatement {
   ProxyPreparedStatement(ProxyConnection var1, PreparedStatement var2) {
      super(var1, var2);
   }

   public boolean execute() {
      this.connection.markCommitStateDirty();
      return ((PreparedStatement)this.delegate).execute();
   }

   public ResultSet executeQuery() {
      this.connection.markCommitStateDirty();
      ResultSet var1 = ((PreparedStatement)this.delegate).executeQuery();
      return ProxyFactory.getProxyResultSet(this.connection, this, var1);
   }

   public int executeUpdate() {
      this.connection.markCommitStateDirty();
      return ((PreparedStatement)this.delegate).executeUpdate();
   }

   public long executeLargeUpdate() {
      this.connection.markCommitStateDirty();
      return ((PreparedStatement)this.delegate).executeLargeUpdate();
   }
}
