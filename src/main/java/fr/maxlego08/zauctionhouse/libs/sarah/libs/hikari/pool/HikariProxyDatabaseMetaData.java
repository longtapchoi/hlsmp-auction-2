package fr.maxlego08.zauctionhouse.libs.sarah.libs.hikari.pool;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.RowIdLifetime;
import java.sql.SQLException;
import java.sql.Wrapper;

public final class HikariProxyDatabaseMetaData extends ProxyDatabaseMetaData implements Wrapper, DatabaseMetaData {
   public boolean isWrapperFor(Class var1) {
      try {
         return super.delegate.isWrapperFor(var1);
      } catch (SQLException var3) {
         throw ((ProxyDatabaseMetaData)this).checkException(var3);
      }
   }

   public boolean allProceduresAreCallable() {
      try {
         return super.delegate.allProceduresAreCallable();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean allTablesAreSelectable() {
      try {
         return super.delegate.allTablesAreSelectable();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public String getURL() {
      try {
         return super.delegate.getURL();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public String getUserName() {
      try {
         return super.delegate.getUserName();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean isReadOnly() {
      try {
         return super.delegate.isReadOnly();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean nullsAreSortedHigh() {
      try {
         return super.delegate.nullsAreSortedHigh();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean nullsAreSortedLow() {
      try {
         return super.delegate.nullsAreSortedLow();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean nullsAreSortedAtStart() {
      try {
         return super.delegate.nullsAreSortedAtStart();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean nullsAreSortedAtEnd() {
      try {
         return super.delegate.nullsAreSortedAtEnd();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public String getDatabaseProductName() {
      try {
         return super.delegate.getDatabaseProductName();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public String getDatabaseProductVersion() {
      try {
         return super.delegate.getDatabaseProductVersion();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public String getDriverName() {
      try {
         return super.delegate.getDriverName();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public String getDriverVersion() {
      try {
         return super.delegate.getDriverVersion();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public int getDriverMajorVersion() {
      return ((DatabaseMetaData)super.delegate).getDriverMajorVersion();
   }

   public int getDriverMinorVersion() {
      return ((DatabaseMetaData)super.delegate).getDriverMinorVersion();
   }

   public boolean usesLocalFiles() {
      try {
         return super.delegate.usesLocalFiles();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean usesLocalFilePerTable() {
      try {
         return super.delegate.usesLocalFilePerTable();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsMixedCaseIdentifiers() {
      try {
         return super.delegate.supportsMixedCaseIdentifiers();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean storesUpperCaseIdentifiers() {
      try {
         return super.delegate.storesUpperCaseIdentifiers();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean storesLowerCaseIdentifiers() {
      try {
         return super.delegate.storesLowerCaseIdentifiers();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean storesMixedCaseIdentifiers() {
      try {
         return super.delegate.storesMixedCaseIdentifiers();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsMixedCaseQuotedIdentifiers() {
      try {
         return super.delegate.supportsMixedCaseQuotedIdentifiers();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean storesUpperCaseQuotedIdentifiers() {
      try {
         return super.delegate.storesUpperCaseQuotedIdentifiers();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean storesLowerCaseQuotedIdentifiers() {
      try {
         return super.delegate.storesLowerCaseQuotedIdentifiers();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean storesMixedCaseQuotedIdentifiers() {
      try {
         return super.delegate.storesMixedCaseQuotedIdentifiers();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public String getIdentifierQuoteString() {
      try {
         return super.delegate.getIdentifierQuoteString();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public String getSQLKeywords() {
      try {
         return super.delegate.getSQLKeywords();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public String getNumericFunctions() {
      try {
         return super.delegate.getNumericFunctions();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public String getStringFunctions() {
      try {
         return super.delegate.getStringFunctions();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public String getSystemFunctions() {
      try {
         return super.delegate.getSystemFunctions();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public String getTimeDateFunctions() {
      try {
         return super.delegate.getTimeDateFunctions();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public String getSearchStringEscape() {
      try {
         return super.delegate.getSearchStringEscape();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public String getExtraNameCharacters() {
      try {
         return super.delegate.getExtraNameCharacters();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsAlterTableWithAddColumn() {
      try {
         return super.delegate.supportsAlterTableWithAddColumn();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsAlterTableWithDropColumn() {
      try {
         return super.delegate.supportsAlterTableWithDropColumn();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsColumnAliasing() {
      try {
         return super.delegate.supportsColumnAliasing();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean nullPlusNonNullIsNull() {
      try {
         return super.delegate.nullPlusNonNullIsNull();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsConvert() {
      try {
         return super.delegate.supportsConvert();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsConvert(int var1, int var2) {
      try {
         return super.delegate.supportsConvert(var1, var2);
      } catch (SQLException var4) {
         throw ((ProxyDatabaseMetaData)this).checkException(var4);
      }
   }

   public boolean supportsTableCorrelationNames() {
      try {
         return super.delegate.supportsTableCorrelationNames();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsDifferentTableCorrelationNames() {
      try {
         return super.delegate.supportsDifferentTableCorrelationNames();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsExpressionsInOrderBy() {
      try {
         return super.delegate.supportsExpressionsInOrderBy();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsOrderByUnrelated() {
      try {
         return super.delegate.supportsOrderByUnrelated();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsGroupBy() {
      try {
         return super.delegate.supportsGroupBy();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsGroupByUnrelated() {
      try {
         return super.delegate.supportsGroupByUnrelated();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsGroupByBeyondSelect() {
      try {
         return super.delegate.supportsGroupByBeyondSelect();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsLikeEscapeClause() {
      try {
         return super.delegate.supportsLikeEscapeClause();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsMultipleResultSets() {
      try {
         return super.delegate.supportsMultipleResultSets();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsMultipleTransactions() {
      try {
         return super.delegate.supportsMultipleTransactions();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsNonNullableColumns() {
      try {
         return super.delegate.supportsNonNullableColumns();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsMinimumSQLGrammar() {
      try {
         return super.delegate.supportsMinimumSQLGrammar();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsCoreSQLGrammar() {
      try {
         return super.delegate.supportsCoreSQLGrammar();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsExtendedSQLGrammar() {
      try {
         return super.delegate.supportsExtendedSQLGrammar();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsANSI92EntryLevelSQL() {
      try {
         return super.delegate.supportsANSI92EntryLevelSQL();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsANSI92IntermediateSQL() {
      try {
         return super.delegate.supportsANSI92IntermediateSQL();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsANSI92FullSQL() {
      try {
         return super.delegate.supportsANSI92FullSQL();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsIntegrityEnhancementFacility() {
      try {
         return super.delegate.supportsIntegrityEnhancementFacility();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsOuterJoins() {
      try {
         return super.delegate.supportsOuterJoins();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsFullOuterJoins() {
      try {
         return super.delegate.supportsFullOuterJoins();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsLimitedOuterJoins() {
      try {
         return super.delegate.supportsLimitedOuterJoins();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public String getSchemaTerm() {
      try {
         return super.delegate.getSchemaTerm();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public String getProcedureTerm() {
      try {
         return super.delegate.getProcedureTerm();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public String getCatalogTerm() {
      try {
         return super.delegate.getCatalogTerm();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean isCatalogAtStart() {
      try {
         return super.delegate.isCatalogAtStart();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public String getCatalogSeparator() {
      try {
         return super.delegate.getCatalogSeparator();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsSchemasInDataManipulation() {
      try {
         return super.delegate.supportsSchemasInDataManipulation();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsSchemasInProcedureCalls() {
      try {
         return super.delegate.supportsSchemasInProcedureCalls();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsSchemasInTableDefinitions() {
      try {
         return super.delegate.supportsSchemasInTableDefinitions();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsSchemasInIndexDefinitions() {
      try {
         return super.delegate.supportsSchemasInIndexDefinitions();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsSchemasInPrivilegeDefinitions() {
      try {
         return super.delegate.supportsSchemasInPrivilegeDefinitions();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsCatalogsInDataManipulation() {
      try {
         return super.delegate.supportsCatalogsInDataManipulation();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsCatalogsInProcedureCalls() {
      try {
         return super.delegate.supportsCatalogsInProcedureCalls();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsCatalogsInTableDefinitions() {
      try {
         return super.delegate.supportsCatalogsInTableDefinitions();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsCatalogsInIndexDefinitions() {
      try {
         return super.delegate.supportsCatalogsInIndexDefinitions();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsCatalogsInPrivilegeDefinitions() {
      try {
         return super.delegate.supportsCatalogsInPrivilegeDefinitions();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsPositionedDelete() {
      try {
         return super.delegate.supportsPositionedDelete();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsPositionedUpdate() {
      try {
         return super.delegate.supportsPositionedUpdate();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsSelectForUpdate() {
      try {
         return super.delegate.supportsSelectForUpdate();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsStoredProcedures() {
      try {
         return super.delegate.supportsStoredProcedures();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsSubqueriesInComparisons() {
      try {
         return super.delegate.supportsSubqueriesInComparisons();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsSubqueriesInExists() {
      try {
         return super.delegate.supportsSubqueriesInExists();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsSubqueriesInIns() {
      try {
         return super.delegate.supportsSubqueriesInIns();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsSubqueriesInQuantifieds() {
      try {
         return super.delegate.supportsSubqueriesInQuantifieds();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsCorrelatedSubqueries() {
      try {
         return super.delegate.supportsCorrelatedSubqueries();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsUnion() {
      try {
         return super.delegate.supportsUnion();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsUnionAll() {
      try {
         return super.delegate.supportsUnionAll();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsOpenCursorsAcrossCommit() {
      try {
         return super.delegate.supportsOpenCursorsAcrossCommit();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsOpenCursorsAcrossRollback() {
      try {
         return super.delegate.supportsOpenCursorsAcrossRollback();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsOpenStatementsAcrossCommit() {
      try {
         return super.delegate.supportsOpenStatementsAcrossCommit();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsOpenStatementsAcrossRollback() {
      try {
         return super.delegate.supportsOpenStatementsAcrossRollback();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public int getMaxBinaryLiteralLength() {
      try {
         return super.delegate.getMaxBinaryLiteralLength();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public int getMaxCharLiteralLength() {
      try {
         return super.delegate.getMaxCharLiteralLength();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public int getMaxColumnNameLength() {
      try {
         return super.delegate.getMaxColumnNameLength();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public int getMaxColumnsInGroupBy() {
      try {
         return super.delegate.getMaxColumnsInGroupBy();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public int getMaxColumnsInIndex() {
      try {
         return super.delegate.getMaxColumnsInIndex();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public int getMaxColumnsInOrderBy() {
      try {
         return super.delegate.getMaxColumnsInOrderBy();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public int getMaxColumnsInSelect() {
      try {
         return super.delegate.getMaxColumnsInSelect();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public int getMaxColumnsInTable() {
      try {
         return super.delegate.getMaxColumnsInTable();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public int getMaxConnections() {
      try {
         return super.delegate.getMaxConnections();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public int getMaxCursorNameLength() {
      try {
         return super.delegate.getMaxCursorNameLength();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public int getMaxIndexLength() {
      try {
         return super.delegate.getMaxIndexLength();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public int getMaxSchemaNameLength() {
      try {
         return super.delegate.getMaxSchemaNameLength();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public int getMaxProcedureNameLength() {
      try {
         return super.delegate.getMaxProcedureNameLength();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public int getMaxCatalogNameLength() {
      try {
         return super.delegate.getMaxCatalogNameLength();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public int getMaxRowSize() {
      try {
         return super.delegate.getMaxRowSize();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean doesMaxRowSizeIncludeBlobs() {
      try {
         return super.delegate.doesMaxRowSizeIncludeBlobs();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public int getMaxStatementLength() {
      try {
         return super.delegate.getMaxStatementLength();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public int getMaxStatements() {
      try {
         return super.delegate.getMaxStatements();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public int getMaxTableNameLength() {
      try {
         return super.delegate.getMaxTableNameLength();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public int getMaxTablesInSelect() {
      try {
         return super.delegate.getMaxTablesInSelect();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public int getMaxUserNameLength() {
      try {
         return super.delegate.getMaxUserNameLength();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public int getDefaultTransactionIsolation() {
      try {
         return super.delegate.getDefaultTransactionIsolation();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsTransactions() {
      try {
         return super.delegate.supportsTransactions();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsTransactionIsolationLevel(int var1) {
      try {
         return super.delegate.supportsTransactionIsolationLevel(var1);
      } catch (SQLException var3) {
         throw ((ProxyDatabaseMetaData)this).checkException(var3);
      }
   }

   public boolean supportsDataDefinitionAndDataManipulationTransactions() {
      try {
         return super.delegate.supportsDataDefinitionAndDataManipulationTransactions();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsDataManipulationTransactionsOnly() {
      try {
         return super.delegate.supportsDataManipulationTransactionsOnly();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean dataDefinitionCausesTransactionCommit() {
      try {
         return super.delegate.dataDefinitionCausesTransactionCommit();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean dataDefinitionIgnoredInTransactions() {
      try {
         return super.delegate.dataDefinitionIgnoredInTransactions();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public ResultSet getProcedures(String var1, String var2, String var3) {
      try {
         return super.getProcedures(var1, var2, var3);
      } catch (SQLException var5) {
         throw ((ProxyDatabaseMetaData)this).checkException(var5);
      }
   }

   public ResultSet getProcedureColumns(String var1, String var2, String var3, String var4) {
      try {
         return super.getProcedureColumns(var1, var2, var3, var4);
      } catch (SQLException var6) {
         throw ((ProxyDatabaseMetaData)this).checkException(var6);
      }
   }

   public ResultSet getTables(String var1, String var2, String var3, String[] var4) {
      try {
         return super.getTables(var1, var2, var3, var4);
      } catch (SQLException var6) {
         throw ((ProxyDatabaseMetaData)this).checkException(var6);
      }
   }

   public ResultSet getSchemas() {
      try {
         return super.getSchemas();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public ResultSet getCatalogs() {
      try {
         return super.getCatalogs();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public ResultSet getTableTypes() {
      try {
         return super.getTableTypes();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public ResultSet getColumns(String var1, String var2, String var3, String var4) {
      try {
         return super.getColumns(var1, var2, var3, var4);
      } catch (SQLException var6) {
         throw ((ProxyDatabaseMetaData)this).checkException(var6);
      }
   }

   public ResultSet getColumnPrivileges(String var1, String var2, String var3, String var4) {
      try {
         return super.getColumnPrivileges(var1, var2, var3, var4);
      } catch (SQLException var6) {
         throw ((ProxyDatabaseMetaData)this).checkException(var6);
      }
   }

   public ResultSet getTablePrivileges(String var1, String var2, String var3) {
      try {
         return super.getTablePrivileges(var1, var2, var3);
      } catch (SQLException var5) {
         throw ((ProxyDatabaseMetaData)this).checkException(var5);
      }
   }

   public ResultSet getBestRowIdentifier(String var1, String var2, String var3, int var4, boolean var5) {
      try {
         return super.getBestRowIdentifier(var1, var2, var3, var4, var5);
      } catch (SQLException var7) {
         throw ((ProxyDatabaseMetaData)this).checkException(var7);
      }
   }

   public ResultSet getVersionColumns(String var1, String var2, String var3) {
      try {
         return super.getVersionColumns(var1, var2, var3);
      } catch (SQLException var5) {
         throw ((ProxyDatabaseMetaData)this).checkException(var5);
      }
   }

   public ResultSet getPrimaryKeys(String var1, String var2, String var3) {
      try {
         return super.getPrimaryKeys(var1, var2, var3);
      } catch (SQLException var5) {
         throw ((ProxyDatabaseMetaData)this).checkException(var5);
      }
   }

   public ResultSet getImportedKeys(String var1, String var2, String var3) {
      try {
         return super.getImportedKeys(var1, var2, var3);
      } catch (SQLException var5) {
         throw ((ProxyDatabaseMetaData)this).checkException(var5);
      }
   }

   public ResultSet getExportedKeys(String var1, String var2, String var3) {
      try {
         return super.getExportedKeys(var1, var2, var3);
      } catch (SQLException var5) {
         throw ((ProxyDatabaseMetaData)this).checkException(var5);
      }
   }

   public ResultSet getCrossReference(String var1, String var2, String var3, String var4, String var5, String var6) {
      try {
         return super.getCrossReference(var1, var2, var3, var4, var5, var6);
      } catch (SQLException var8) {
         throw ((ProxyDatabaseMetaData)this).checkException(var8);
      }
   }

   public ResultSet getTypeInfo() {
      try {
         return super.getTypeInfo();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public ResultSet getIndexInfo(String var1, String var2, String var3, boolean var4, boolean var5) {
      try {
         return super.getIndexInfo(var1, var2, var3, var4, var5);
      } catch (SQLException var7) {
         throw ((ProxyDatabaseMetaData)this).checkException(var7);
      }
   }

   public boolean supportsResultSetType(int var1) {
      try {
         return super.delegate.supportsResultSetType(var1);
      } catch (SQLException var3) {
         throw ((ProxyDatabaseMetaData)this).checkException(var3);
      }
   }

   public boolean supportsResultSetConcurrency(int var1, int var2) {
      try {
         return super.delegate.supportsResultSetConcurrency(var1, var2);
      } catch (SQLException var4) {
         throw ((ProxyDatabaseMetaData)this).checkException(var4);
      }
   }

   public boolean ownUpdatesAreVisible(int var1) {
      try {
         return super.delegate.ownUpdatesAreVisible(var1);
      } catch (SQLException var3) {
         throw ((ProxyDatabaseMetaData)this).checkException(var3);
      }
   }

   public boolean ownDeletesAreVisible(int var1) {
      try {
         return super.delegate.ownDeletesAreVisible(var1);
      } catch (SQLException var3) {
         throw ((ProxyDatabaseMetaData)this).checkException(var3);
      }
   }

   public boolean ownInsertsAreVisible(int var1) {
      try {
         return super.delegate.ownInsertsAreVisible(var1);
      } catch (SQLException var3) {
         throw ((ProxyDatabaseMetaData)this).checkException(var3);
      }
   }

   public boolean othersUpdatesAreVisible(int var1) {
      try {
         return super.delegate.othersUpdatesAreVisible(var1);
      } catch (SQLException var3) {
         throw ((ProxyDatabaseMetaData)this).checkException(var3);
      }
   }

   public boolean othersDeletesAreVisible(int var1) {
      try {
         return super.delegate.othersDeletesAreVisible(var1);
      } catch (SQLException var3) {
         throw ((ProxyDatabaseMetaData)this).checkException(var3);
      }
   }

   public boolean othersInsertsAreVisible(int var1) {
      try {
         return super.delegate.othersInsertsAreVisible(var1);
      } catch (SQLException var3) {
         throw ((ProxyDatabaseMetaData)this).checkException(var3);
      }
   }

   public boolean updatesAreDetected(int var1) {
      try {
         return super.delegate.updatesAreDetected(var1);
      } catch (SQLException var3) {
         throw ((ProxyDatabaseMetaData)this).checkException(var3);
      }
   }

   public boolean deletesAreDetected(int var1) {
      try {
         return super.delegate.deletesAreDetected(var1);
      } catch (SQLException var3) {
         throw ((ProxyDatabaseMetaData)this).checkException(var3);
      }
   }

   public boolean insertsAreDetected(int var1) {
      try {
         return super.delegate.insertsAreDetected(var1);
      } catch (SQLException var3) {
         throw ((ProxyDatabaseMetaData)this).checkException(var3);
      }
   }

   public boolean supportsBatchUpdates() {
      try {
         return super.delegate.supportsBatchUpdates();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public ResultSet getUDTs(String var1, String var2, String var3, int[] var4) {
      try {
         return super.getUDTs(var1, var2, var3, var4);
      } catch (SQLException var6) {
         throw ((ProxyDatabaseMetaData)this).checkException(var6);
      }
   }

   public boolean supportsSavepoints() {
      try {
         return super.delegate.supportsSavepoints();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsNamedParameters() {
      try {
         return super.delegate.supportsNamedParameters();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsMultipleOpenResults() {
      try {
         return super.delegate.supportsMultipleOpenResults();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsGetGeneratedKeys() {
      try {
         return super.delegate.supportsGetGeneratedKeys();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public ResultSet getSuperTypes(String var1, String var2, String var3) {
      try {
         return super.getSuperTypes(var1, var2, var3);
      } catch (SQLException var5) {
         throw ((ProxyDatabaseMetaData)this).checkException(var5);
      }
   }

   public ResultSet getSuperTables(String var1, String var2, String var3) {
      try {
         return super.getSuperTables(var1, var2, var3);
      } catch (SQLException var5) {
         throw ((ProxyDatabaseMetaData)this).checkException(var5);
      }
   }

   public ResultSet getAttributes(String var1, String var2, String var3, String var4) {
      try {
         return super.getAttributes(var1, var2, var3, var4);
      } catch (SQLException var6) {
         throw ((ProxyDatabaseMetaData)this).checkException(var6);
      }
   }

   public boolean supportsResultSetHoldability(int var1) {
      try {
         return super.delegate.supportsResultSetHoldability(var1);
      } catch (SQLException var3) {
         throw ((ProxyDatabaseMetaData)this).checkException(var3);
      }
   }

   public int getResultSetHoldability() {
      try {
         return super.delegate.getResultSetHoldability();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public int getDatabaseMajorVersion() {
      try {
         return super.delegate.getDatabaseMajorVersion();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public int getDatabaseMinorVersion() {
      try {
         return super.delegate.getDatabaseMinorVersion();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public int getJDBCMajorVersion() {
      try {
         return super.delegate.getJDBCMajorVersion();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public int getJDBCMinorVersion() {
      try {
         return super.delegate.getJDBCMinorVersion();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public int getSQLStateType() {
      try {
         return super.delegate.getSQLStateType();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean locatorsUpdateCopy() {
      try {
         return super.delegate.locatorsUpdateCopy();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsStatementPooling() {
      try {
         return super.delegate.supportsStatementPooling();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public RowIdLifetime getRowIdLifetime() {
      try {
         return super.delegate.getRowIdLifetime();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public ResultSet getSchemas(String var1, String var2) {
      try {
         return super.getSchemas(var1, var2);
      } catch (SQLException var4) {
         throw ((ProxyDatabaseMetaData)this).checkException(var4);
      }
   }

   public boolean supportsStoredFunctionsUsingCallSyntax() {
      try {
         return super.delegate.supportsStoredFunctionsUsingCallSyntax();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean autoCommitFailureClosesAllResultSets() {
      try {
         return super.delegate.autoCommitFailureClosesAllResultSets();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public ResultSet getClientInfoProperties() {
      try {
         return super.getClientInfoProperties();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public ResultSet getFunctions(String var1, String var2, String var3) {
      try {
         return super.getFunctions(var1, var2, var3);
      } catch (SQLException var5) {
         throw ((ProxyDatabaseMetaData)this).checkException(var5);
      }
   }

   public ResultSet getFunctionColumns(String var1, String var2, String var3, String var4) {
      try {
         return super.getFunctionColumns(var1, var2, var3, var4);
      } catch (SQLException var6) {
         throw ((ProxyDatabaseMetaData)this).checkException(var6);
      }
   }

   public ResultSet getPseudoColumns(String var1, String var2, String var3, String var4) {
      try {
         return super.getPseudoColumns(var1, var2, var3, var4);
      } catch (SQLException var6) {
         throw ((ProxyDatabaseMetaData)this).checkException(var6);
      }
   }

   public boolean generatedKeyAlwaysReturned() {
      try {
         return super.delegate.generatedKeyAlwaysReturned();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public long getMaxLogicalLobSize() {
      try {
         return super.delegate.getMaxLogicalLobSize();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   public boolean supportsRefCursors() {
      try {
         return super.delegate.supportsRefCursors();
      } catch (SQLException var2) {
         throw ((ProxyDatabaseMetaData)this).checkException(var2);
      }
   }

   HikariProxyDatabaseMetaData(ProxyConnection var1, DatabaseMetaData var2) {
      super(var1, var2);
   }
}
