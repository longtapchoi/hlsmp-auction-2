package fr.maxlego08.zauctionhouse.libs.sarah.libs.hikari.pool;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Wrapper;
import java.util.Calendar;

public final class HikariProxyPreparedStatement extends ProxyPreparedStatement implements Wrapper, AutoCloseable, Statement, PreparedStatement {
   public boolean isWrapperFor(Class var1) {
      try {
         return ((PreparedStatement)super.delegate).isWrapperFor(var1);
      } catch (SQLException var3) {
         throw ((ProxyStatement)this).checkException(var3);
      }
   }

   public ResultSet executeQuery(String var1) {
      try {
         return super.executeQuery(var1);
      } catch (SQLException var3) {
         throw ((ProxyStatement)this).checkException(var3);
      }
   }

   public int executeUpdate(String var1) {
      try {
         return super.executeUpdate(var1);
      } catch (SQLException var3) {
         throw ((ProxyStatement)this).checkException(var3);
      }
   }

   public int getMaxFieldSize() {
      try {
         return ((PreparedStatement)super.delegate).getMaxFieldSize();
      } catch (SQLException var2) {
         throw ((ProxyStatement)this).checkException(var2);
      }
   }

   public void setMaxFieldSize(int var1) {
      try {
         ((PreparedStatement)super.delegate).setMaxFieldSize(var1);
      } catch (SQLException var3) {
         throw ((ProxyStatement)this).checkException(var3);
      }
   }

   public int getMaxRows() {
      try {
         return ((PreparedStatement)super.delegate).getMaxRows();
      } catch (SQLException var2) {
         throw ((ProxyStatement)this).checkException(var2);
      }
   }

   public void setMaxRows(int var1) {
      try {
         ((PreparedStatement)super.delegate).setMaxRows(var1);
      } catch (SQLException var3) {
         throw ((ProxyStatement)this).checkException(var3);
      }
   }

   public void setEscapeProcessing(boolean var1) {
      try {
         ((PreparedStatement)super.delegate).setEscapeProcessing(var1);
      } catch (SQLException var3) {
         throw ((ProxyStatement)this).checkException(var3);
      }
   }

   public int getQueryTimeout() {
      try {
         return ((PreparedStatement)super.delegate).getQueryTimeout();
      } catch (SQLException var2) {
         throw ((ProxyStatement)this).checkException(var2);
      }
   }

   public void setQueryTimeout(int var1) {
      try {
         ((PreparedStatement)super.delegate).setQueryTimeout(var1);
      } catch (SQLException var3) {
         throw ((ProxyStatement)this).checkException(var3);
      }
   }

   public void cancel() {
      try {
         ((PreparedStatement)super.delegate).cancel();
      } catch (SQLException var2) {
         throw ((ProxyStatement)this).checkException(var2);
      }
   }

   public SQLWarning getWarnings() {
      try {
         return ((PreparedStatement)super.delegate).getWarnings();
      } catch (SQLException var2) {
         throw ((ProxyStatement)this).checkException(var2);
      }
   }

   public void clearWarnings() {
      try {
         ((PreparedStatement)super.delegate).clearWarnings();
      } catch (SQLException var2) {
         throw ((ProxyStatement)this).checkException(var2);
      }
   }

   public void setCursorName(String var1) {
      try {
         ((PreparedStatement)super.delegate).setCursorName(var1);
      } catch (SQLException var3) {
         throw ((ProxyStatement)this).checkException(var3);
      }
   }

   public boolean execute(String var1) {
      try {
         return super.execute(var1);
      } catch (SQLException var3) {
         throw ((ProxyStatement)this).checkException(var3);
      }
   }

   public ResultSet getResultSet() {
      try {
         return super.getResultSet();
      } catch (SQLException var2) {
         throw ((ProxyStatement)this).checkException(var2);
      }
   }

   public int getUpdateCount() {
      try {
         return ((PreparedStatement)super.delegate).getUpdateCount();
      } catch (SQLException var2) {
         throw ((ProxyStatement)this).checkException(var2);
      }
   }

   public boolean getMoreResults() {
      try {
         return ((PreparedStatement)super.delegate).getMoreResults();
      } catch (SQLException var2) {
         throw ((ProxyStatement)this).checkException(var2);
      }
   }

   public void setFetchDirection(int var1) {
      try {
         ((PreparedStatement)super.delegate).setFetchDirection(var1);
      } catch (SQLException var3) {
         throw ((ProxyStatement)this).checkException(var3);
      }
   }

   public int getFetchDirection() {
      try {
         return ((PreparedStatement)super.delegate).getFetchDirection();
      } catch (SQLException var2) {
         throw ((ProxyStatement)this).checkException(var2);
      }
   }

   public void setFetchSize(int var1) {
      try {
         ((PreparedStatement)super.delegate).setFetchSize(var1);
      } catch (SQLException var3) {
         throw ((ProxyStatement)this).checkException(var3);
      }
   }

   public int getFetchSize() {
      try {
         return ((PreparedStatement)super.delegate).getFetchSize();
      } catch (SQLException var2) {
         throw ((ProxyStatement)this).checkException(var2);
      }
   }

   public int getResultSetConcurrency() {
      try {
         return ((PreparedStatement)super.delegate).getResultSetConcurrency();
      } catch (SQLException var2) {
         throw ((ProxyStatement)this).checkException(var2);
      }
   }

   public int getResultSetType() {
      try {
         return ((PreparedStatement)super.delegate).getResultSetType();
      } catch (SQLException var2) {
         throw ((ProxyStatement)this).checkException(var2);
      }
   }

   public void addBatch(String var1) {
      try {
         ((PreparedStatement)super.delegate).addBatch(var1);
      } catch (SQLException var3) {
         throw ((ProxyStatement)this).checkException(var3);
      }
   }

   public void clearBatch() {
      try {
         ((PreparedStatement)super.delegate).clearBatch();
      } catch (SQLException var2) {
         throw ((ProxyStatement)this).checkException(var2);
      }
   }

   public int[] executeBatch() throws java.sql.SQLException {
      try {
         return super.executeBatch();
      } catch (SQLException var2) {
         throw ((ProxyStatement)this).checkException(var2);
      }
   }

   public Connection getConnection() {
      try {
         return super.getConnection();
      } catch (SQLException var2) {
         throw ((ProxyStatement)this).checkException(var2);
      }
   }

   public boolean getMoreResults(int var1) {
      try {
         return ((PreparedStatement)super.delegate).getMoreResults(var1);
      } catch (SQLException var3) {
         throw ((ProxyStatement)this).checkException(var3);
      }
   }

   public ResultSet getGeneratedKeys() {
      try {
         return super.getGeneratedKeys();
      } catch (SQLException var2) {
         throw ((ProxyStatement)this).checkException(var2);
      }
   }

   public int executeUpdate(String var1, int var2) {
      try {
         return super.executeUpdate(var1, var2);
      } catch (SQLException var4) {
         throw ((ProxyStatement)this).checkException(var4);
      }
   }

   public int executeUpdate(String var1, int[] var2) {
      try {
         return super.executeUpdate(var1, var2);
      } catch (SQLException var4) {
         throw ((ProxyStatement)this).checkException(var4);
      }
   }

   public int executeUpdate(String var1, String[] var2) {
      try {
         return super.executeUpdate(var1, var2);
      } catch (SQLException var4) {
         throw ((ProxyStatement)this).checkException(var4);
      }
   }

   public boolean execute(String var1, int var2) {
      try {
         return super.execute(var1, var2);
      } catch (SQLException var4) {
         throw ((ProxyStatement)this).checkException(var4);
      }
   }

   public boolean execute(String var1, int[] var2) {
      try {
         return super.execute(var1, var2);
      } catch (SQLException var4) {
         throw ((ProxyStatement)this).checkException(var4);
      }
   }

   public boolean execute(String var1, String[] var2) {
      try {
         return super.execute(var1, var2);
      } catch (SQLException var4) {
         throw ((ProxyStatement)this).checkException(var4);
      }
   }

   public int getResultSetHoldability() {
      try {
         return ((PreparedStatement)super.delegate).getResultSetHoldability();
      } catch (SQLException var2) {
         throw ((ProxyStatement)this).checkException(var2);
      }
   }

   public boolean isClosed() {
      try {
         return ((PreparedStatement)super.delegate).isClosed();
      } catch (SQLException var2) {
         throw ((ProxyStatement)this).checkException(var2);
      }
   }

   public void setPoolable(boolean var1) {
      try {
         ((PreparedStatement)super.delegate).setPoolable(var1);
      } catch (SQLException var3) {
         throw ((ProxyStatement)this).checkException(var3);
      }
   }

   public boolean isPoolable() {
      try {
         return ((PreparedStatement)super.delegate).isPoolable();
      } catch (SQLException var2) {
         throw ((ProxyStatement)this).checkException(var2);
      }
   }

   public void closeOnCompletion() {
      try {
         ((PreparedStatement)super.delegate).closeOnCompletion();
      } catch (SQLException var2) {
         throw ((ProxyStatement)this).checkException(var2);
      }
   }

   public boolean isCloseOnCompletion() {
      try {
         return ((PreparedStatement)super.delegate).isCloseOnCompletion();
      } catch (SQLException var2) {
         throw ((ProxyStatement)this).checkException(var2);
      }
   }

   public long getLargeUpdateCount() {
      try {
         return ((PreparedStatement)super.delegate).getLargeUpdateCount();
      } catch (SQLException var2) {
         throw ((ProxyStatement)this).checkException(var2);
      }
   }

   public void setLargeMaxRows(long var1) {
      try {
         ((PreparedStatement)super.delegate).setLargeMaxRows(var1);
      } catch (SQLException var4) {
         throw ((ProxyStatement)this).checkException(var4);
      }
   }

   public long getLargeMaxRows() {
      try {
         return ((PreparedStatement)super.delegate).getLargeMaxRows();
      } catch (SQLException var2) {
         throw ((ProxyStatement)this).checkException(var2);
      }
   }

   public long[] executeLargeBatch() {
      try {
         return ((PreparedStatement)super.delegate).executeLargeBatch();
      } catch (SQLException var2) {
         throw ((ProxyStatement)this).checkException(var2);
      }
   }

   public long executeLargeUpdate(String var1) {
      try {
         return ((PreparedStatement)super.delegate).executeLargeUpdate(var1);
      } catch (SQLException var3) {
         throw ((ProxyStatement)this).checkException(var3);
      }
   }

   public long executeLargeUpdate(String var1, int var2) {
      try {
         return ((PreparedStatement)super.delegate).executeLargeUpdate(var1, var2);
      } catch (SQLException var4) {
         throw ((ProxyStatement)this).checkException(var4);
      }
   }

   public long executeLargeUpdate(String var1, int[] var2) {
      try {
         return ((PreparedStatement)super.delegate).executeLargeUpdate(var1, var2);
      } catch (SQLException var4) {
         throw ((ProxyStatement)this).checkException(var4);
      }
   }

   public long executeLargeUpdate(String var1, String[] var2) {
      try {
         return ((PreparedStatement)super.delegate).executeLargeUpdate(var1, var2);
      } catch (SQLException var4) {
         throw ((ProxyStatement)this).checkException(var4);
      }
   }

   public ResultSet executeQuery() {
      try {
         return super.executeQuery();
      } catch (SQLException var2) {
         throw ((ProxyStatement)this).checkException(var2);
      }
   }

   public int executeUpdate() {
      try {
         return super.executeUpdate();
      } catch (SQLException var2) {
         throw ((ProxyStatement)this).checkException(var2);
      }
   }

   public void setNull(int var1, int var2) {
      try {
         ((PreparedStatement)super.delegate).setNull(var1, var2);
      } catch (SQLException var4) {
         throw ((ProxyStatement)this).checkException(var4);
      }
   }

   public void setBoolean(int var1, boolean var2) {
      try {
         ((PreparedStatement)super.delegate).setBoolean(var1, var2);
      } catch (SQLException var4) {
         throw ((ProxyStatement)this).checkException(var4);
      }
   }

   public void setByte(int var1, byte var2) {
      try {
         ((PreparedStatement)super.delegate).setByte(var1, var2);
      } catch (SQLException var4) {
         throw ((ProxyStatement)this).checkException(var4);
      }
   }

   public void setShort(int var1, short var2) {
      try {
         ((PreparedStatement)super.delegate).setShort(var1, var2);
      } catch (SQLException var4) {
         throw ((ProxyStatement)this).checkException(var4);
      }
   }

   public void setInt(int var1, int var2) {
      try {
         ((PreparedStatement)super.delegate).setInt(var1, var2);
      } catch (SQLException var4) {
         throw ((ProxyStatement)this).checkException(var4);
      }
   }

   public void setLong(int var1, long var2) {
      try {
         ((PreparedStatement)super.delegate).setLong(var1, var2);
      } catch (SQLException var5) {
         throw ((ProxyStatement)this).checkException(var5);
      }
   }

   public void setFloat(int var1, float var2) {
      try {
         ((PreparedStatement)super.delegate).setFloat(var1, var2);
      } catch (SQLException var4) {
         throw ((ProxyStatement)this).checkException(var4);
      }
   }

   public void setDouble(int var1, double var2) {
      try {
         ((PreparedStatement)super.delegate).setDouble(var1, var2);
      } catch (SQLException var5) {
         throw ((ProxyStatement)this).checkException(var5);
      }
   }

   public void setBigDecimal(int var1, BigDecimal var2) {
      try {
         ((PreparedStatement)super.delegate).setBigDecimal(var1, var2);
      } catch (SQLException var4) {
         throw ((ProxyStatement)this).checkException(var4);
      }
   }

   public void setString(int var1, String var2) {
      try {
         ((PreparedStatement)super.delegate).setString(var1, var2);
      } catch (SQLException var4) {
         throw ((ProxyStatement)this).checkException(var4);
      }
   }

   public void setBytes(int var1, byte[] var2) {
      try {
         ((PreparedStatement)super.delegate).setBytes(var1, var2);
      } catch (SQLException var4) {
         throw ((ProxyStatement)this).checkException(var4);
      }
   }

   public void setDate(int var1, Date var2) {
      try {
         ((PreparedStatement)super.delegate).setDate(var1, var2);
      } catch (SQLException var4) {
         throw ((ProxyStatement)this).checkException(var4);
      }
   }

   public void setTime(int var1, Time var2) {
      try {
         ((PreparedStatement)super.delegate).setTime(var1, var2);
      } catch (SQLException var4) {
         throw ((ProxyStatement)this).checkException(var4);
      }
   }

   public void setTimestamp(int var1, Timestamp var2) {
      try {
         ((PreparedStatement)super.delegate).setTimestamp(var1, var2);
      } catch (SQLException var4) {
         throw ((ProxyStatement)this).checkException(var4);
      }
   }

   public void setAsciiStream(int var1, InputStream var2, int var3) {
      try {
         ((PreparedStatement)super.delegate).setAsciiStream(var1, var2, var3);
      } catch (SQLException var5) {
         throw ((ProxyStatement)this).checkException(var5);
      }
   }

   public void setUnicodeStream(int var1, InputStream var2, int var3) {
      try {
         ((PreparedStatement)super.delegate).setUnicodeStream(var1, var2, var3);
      } catch (SQLException var5) {
         throw ((ProxyStatement)this).checkException(var5);
      }
   }

   public void setBinaryStream(int var1, InputStream var2, int var3) {
      try {
         ((PreparedStatement)super.delegate).setBinaryStream(var1, var2, var3);
      } catch (SQLException var5) {
         throw ((ProxyStatement)this).checkException(var5);
      }
   }

   public void clearParameters() {
      try {
         ((PreparedStatement)super.delegate).clearParameters();
      } catch (SQLException var2) {
         throw ((ProxyStatement)this).checkException(var2);
      }
   }

   public void setObject(int var1, Object var2, int var3) {
      try {
         ((PreparedStatement)super.delegate).setObject(var1, var2, var3);
      } catch (SQLException var5) {
         throw ((ProxyStatement)this).checkException(var5);
      }
   }

   public void setObject(int var1, Object var2) {
      try {
         ((PreparedStatement)super.delegate).setObject(var1, var2);
      } catch (SQLException var4) {
         throw ((ProxyStatement)this).checkException(var4);
      }
   }

   public boolean execute() {
      try {
         return super.execute();
      } catch (SQLException var2) {
         throw ((ProxyStatement)this).checkException(var2);
      }
   }

   public void addBatch() {
      try {
         ((PreparedStatement)super.delegate).addBatch();
      } catch (SQLException var2) {
         throw ((ProxyStatement)this).checkException(var2);
      }
   }

   public void setCharacterStream(int var1, Reader var2, int var3) {
      try {
         ((PreparedStatement)super.delegate).setCharacterStream(var1, var2, var3);
      } catch (SQLException var5) {
         throw ((ProxyStatement)this).checkException(var5);
      }
   }

   public void setRef(int var1, Ref var2) {
      try {
         ((PreparedStatement)super.delegate).setRef(var1, var2);
      } catch (SQLException var4) {
         throw ((ProxyStatement)this).checkException(var4);
      }
   }

   public void setBlob(int var1, Blob var2) {
      try {
         ((PreparedStatement)super.delegate).setBlob(var1, var2);
      } catch (SQLException var4) {
         throw ((ProxyStatement)this).checkException(var4);
      }
   }

   public void setClob(int var1, Clob var2) {
      try {
         ((PreparedStatement)super.delegate).setClob(var1, var2);
      } catch (SQLException var4) {
         throw ((ProxyStatement)this).checkException(var4);
      }
   }

   public void setArray(int var1, Array var2) {
      try {
         ((PreparedStatement)super.delegate).setArray(var1, var2);
      } catch (SQLException var4) {
         throw ((ProxyStatement)this).checkException(var4);
      }
   }

   public ResultSetMetaData getMetaData() {
      try {
         return ((PreparedStatement)super.delegate).getMetaData();
      } catch (SQLException var2) {
         throw ((ProxyStatement)this).checkException(var2);
      }
   }

   public void setDate(int var1, Date var2, Calendar var3) {
      try {
         ((PreparedStatement)super.delegate).setDate(var1, var2, var3);
      } catch (SQLException var5) {
         throw ((ProxyStatement)this).checkException(var5);
      }
   }

   public void setTime(int var1, Time var2, Calendar var3) {
      try {
         ((PreparedStatement)super.delegate).setTime(var1, var2, var3);
      } catch (SQLException var5) {
         throw ((ProxyStatement)this).checkException(var5);
      }
   }

   public void setTimestamp(int var1, Timestamp var2, Calendar var3) {
      try {
         ((PreparedStatement)super.delegate).setTimestamp(var1, var2, var3);
      } catch (SQLException var5) {
         throw ((ProxyStatement)this).checkException(var5);
      }
   }

   public void setNull(int var1, int var2, String var3) {
      try {
         ((PreparedStatement)super.delegate).setNull(var1, var2, var3);
      } catch (SQLException var5) {
         throw ((ProxyStatement)this).checkException(var5);
      }
   }

   public void setURL(int var1, URL var2) {
      try {
         ((PreparedStatement)super.delegate).setURL(var1, var2);
      } catch (SQLException var4) {
         throw ((ProxyStatement)this).checkException(var4);
      }
   }

   public ParameterMetaData getParameterMetaData() {
      try {
         return ((PreparedStatement)super.delegate).getParameterMetaData();
      } catch (SQLException var2) {
         throw ((ProxyStatement)this).checkException(var2);
      }
   }

   public void setRowId(int var1, RowId var2) {
      try {
         ((PreparedStatement)super.delegate).setRowId(var1, var2);
      } catch (SQLException var4) {
         throw ((ProxyStatement)this).checkException(var4);
      }
   }

   public void setNString(int var1, String var2) {
      try {
         ((PreparedStatement)super.delegate).setNString(var1, var2);
      } catch (SQLException var4) {
         throw ((ProxyStatement)this).checkException(var4);
      }
   }

   public void setNCharacterStream(int var1, Reader var2, long var3) {
      try {
         ((PreparedStatement)super.delegate).setNCharacterStream(var1, var2, var3);
      } catch (SQLException var6) {
         throw ((ProxyStatement)this).checkException(var6);
      }
   }

   public void setNClob(int var1, NClob var2) {
      try {
         ((PreparedStatement)super.delegate).setNClob(var1, var2);
      } catch (SQLException var4) {
         throw ((ProxyStatement)this).checkException(var4);
      }
   }

   public void setClob(int var1, Reader var2, long var3) {
      try {
         ((PreparedStatement)super.delegate).setClob(var1, var2, var3);
      } catch (SQLException var6) {
         throw ((ProxyStatement)this).checkException(var6);
      }
   }

   public void setBlob(int var1, InputStream var2, long var3) {
      try {
         ((PreparedStatement)super.delegate).setBlob(var1, var2, var3);
      } catch (SQLException var6) {
         throw ((ProxyStatement)this).checkException(var6);
      }
   }

   public void setNClob(int var1, Reader var2, long var3) {
      try {
         ((PreparedStatement)super.delegate).setNClob(var1, var2, var3);
      } catch (SQLException var6) {
         throw ((ProxyStatement)this).checkException(var6);
      }
   }

   public void setSQLXML(int var1, SQLXML var2) {
      try {
         ((PreparedStatement)super.delegate).setSQLXML(var1, var2);
      } catch (SQLException var4) {
         throw ((ProxyStatement)this).checkException(var4);
      }
   }

   public void setObject(int var1, Object var2, int var3, int var4) {
      try {
         ((PreparedStatement)super.delegate).setObject(var1, var2, var3, var4);
      } catch (SQLException var6) {
         throw ((ProxyStatement)this).checkException(var6);
      }
   }

   public void setAsciiStream(int var1, InputStream var2, long var3) {
      try {
         ((PreparedStatement)super.delegate).setAsciiStream(var1, var2, var3);
      } catch (SQLException var6) {
         throw ((ProxyStatement)this).checkException(var6);
      }
   }

   public void setBinaryStream(int var1, InputStream var2, long var3) {
      try {
         ((PreparedStatement)super.delegate).setBinaryStream(var1, var2, var3);
      } catch (SQLException var6) {
         throw ((ProxyStatement)this).checkException(var6);
      }
   }

   public void setCharacterStream(int var1, Reader var2, long var3) {
      try {
         ((PreparedStatement)super.delegate).setCharacterStream(var1, var2, var3);
      } catch (SQLException var6) {
         throw ((ProxyStatement)this).checkException(var6);
      }
   }

   public void setAsciiStream(int var1, InputStream var2) {
      try {
         ((PreparedStatement)super.delegate).setAsciiStream(var1, var2);
      } catch (SQLException var4) {
         throw ((ProxyStatement)this).checkException(var4);
      }
   }

   public void setBinaryStream(int var1, InputStream var2) {
      try {
         ((PreparedStatement)super.delegate).setBinaryStream(var1, var2);
      } catch (SQLException var4) {
         throw ((ProxyStatement)this).checkException(var4);
      }
   }

   public void setCharacterStream(int var1, Reader var2) {
      try {
         ((PreparedStatement)super.delegate).setCharacterStream(var1, var2);
      } catch (SQLException var4) {
         throw ((ProxyStatement)this).checkException(var4);
      }
   }

   public void setNCharacterStream(int var1, Reader var2) {
      try {
         ((PreparedStatement)super.delegate).setNCharacterStream(var1, var2);
      } catch (SQLException var4) {
         throw ((ProxyStatement)this).checkException(var4);
      }
   }

   public void setClob(int var1, Reader var2) {
      try {
         ((PreparedStatement)super.delegate).setClob(var1, var2);
      } catch (SQLException var4) {
         throw ((ProxyStatement)this).checkException(var4);
      }
   }

   public void setBlob(int var1, InputStream var2) {
      try {
         ((PreparedStatement)super.delegate).setBlob(var1, var2);
      } catch (SQLException var4) {
         throw ((ProxyStatement)this).checkException(var4);
      }
   }

   public void setNClob(int var1, Reader var2) {
      try {
         ((PreparedStatement)super.delegate).setNClob(var1, var2);
      } catch (SQLException var4) {
         throw ((ProxyStatement)this).checkException(var4);
      }
   }

   public void setObject(int var1, Object var2, SQLType var3, int var4) {
      try {
         ((PreparedStatement)super.delegate).setObject(var1, var2, var3, var4);
      } catch (SQLException var6) {
         throw ((ProxyStatement)this).checkException(var6);
      }
   }

   public void setObject(int var1, Object var2, SQLType var3) {
      try {
         ((PreparedStatement)super.delegate).setObject(var1, var2, var3);
      } catch (SQLException var5) {
         throw ((ProxyStatement)this).checkException(var5);
      }
   }

   public long executeLargeUpdate() {
      try {
         return ((PreparedStatement)super.delegate).executeLargeUpdate();
      } catch (SQLException var2) {
         throw ((ProxyStatement)this).checkException(var2);
      }
   }

   HikariProxyPreparedStatement(ProxyConnection var1, PreparedStatement var2) {
      super(var1, var2);
   }
}
