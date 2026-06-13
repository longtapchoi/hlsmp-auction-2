package fr.maxlego08.zauctionhouse.libs.sarah.libs.hikari.pool;

import java.sql.CallableStatement;

public abstract class ProxyCallableStatement extends ProxyPreparedStatement implements CallableStatement {
   protected ProxyCallableStatement(ProxyConnection var1, CallableStatement var2) {
      super(var1, var2);
   }
}
