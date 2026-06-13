package fr.maxlego08.zauctionhouse.libs.sarah.database;

import fr.maxlego08.zauctionhouse.libs.sarah.DatabaseConnection;
import fr.maxlego08.zauctionhouse.libs.sarah.conditions.ColumnDefinition;
import fr.maxlego08.zauctionhouse.libs.sarah.conditions.JoinCondition;
import fr.maxlego08.zauctionhouse.libs.sarah.conditions.SelectCondition;
import fr.maxlego08.zauctionhouse.libs.sarah.conditions.WhereCondition;
import fr.maxlego08.zauctionhouse.libs.sarah.logger.Logger;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface Schema {
   Schema uuid(String var1);

   Schema uuid(String var1, UUID var2);

   Schema string(String var1, int var2);

   Schema text(String var1);

   Schema longText(String var1);

   Schema decimal(String var1);

   Schema decimal(String var1, int var2, int var3);

   Schema string(String var1, String var2);

   Schema decimal(String var1, Number var2);

   Schema date(String var1, Date var2);

   Schema bigInt(String var1);

   Schema integer(String var1);

   Schema bigInt(String var1, long var2);

   Schema object(String var1, Object var2);

   Schema bool(String var1);

   Schema bool(String var1, boolean var2);

   Schema json(String var1);

   Schema blob(String var1);

   Schema enumValue(String var1);

   Schema enumValue(String var1, Enum<?> var2);

   Schema enumType(String var1, String... var2);

   <E extends Enum<E>> Schema enumType(String var1, Class<E> var2);

   Schema blob(String var1, byte[] var2);

   Schema blob(String var1, Object var2);

   Schema primary();

   Schema foreignKey(String var1);

   Schema foreignKey(String var1, String var2, boolean var3);

   Schema createdAt();

   Schema updatedAt();

   Schema timestamps();

   Schema timestamp(String var1);

   Schema autoIncrement(String var1);

   Schema autoIncrementBigInt(String var1);

   Schema nullable();

   Schema unique();

   Schema unique(boolean var1);

   Schema defaultValue(Object var1);

   Schema defaultCurrentTimestamp();

   Schema where(String var1, Object var2);

   Schema where(String var1, UUID var2);

   Schema where(String var1, String var2, Object var3);

   Schema where(String var1, String var2, String var3, Object var4);

   Schema whereNotNull(String var1);

   Schema whereNull(String var1);

   Schema whereIn(String var1, Object... var2);

   Schema whereIn(String var1, List<String> var2);

   Schema whereIn(String var1, String var2, List<String> var3);

   Schema leftJoin(String var1, String var2, String var3, String var4, String var5);

   Schema leftJoin(String var1, String var2, String var3, String var4, String var5, JoinCondition var6);

   Schema rightJoin(String var1, String var2, String var3, String var4, String var5);

   Schema innerJoin(String var1, String var2, String var3, String var4, String var5);

   Schema fullJoin(String var1, String var2, String var3, String var4, String var5);

   int execute(DatabaseConnection var1, Logger var2) throws SQLException;

   List<Map<String, Object>> executeSelect(DatabaseConnection var1, Logger var2) throws SQLException;

   long executeSelectCount(DatabaseConnection var1, Logger var2) throws SQLException;

   <T> List<T> executeSelect(Class<T> var1, DatabaseConnection var2, Logger var3) throws Exception;

   Migration getMigration();

   void setMigration(Migration var1);

   String getTableName();

   void whereConditions(StringBuilder var1);

   void applyWhereConditions(PreparedStatement var1, int var2) throws SQLException;

   List<ColumnDefinition> getColumns();

   List<String> getPrimaryKeys();

   List<String> getForeignKeys();

   List<JoinCondition> getJoinConditions();

   void orderBy(String var1);

   void orderByDesc(String var1);

   String getOrderBy();

   void distinct();

   boolean isDistinct();

   void addSelect(String var1);

   void addSelect(String var1, String var2);

   void addSelect(String var1, String var2, String var3);

   void addSelect(String var1, String var2, String var3, Object var4);

   SchemaType getSchemaType();

   Schema addColumn(ColumnDefinition var1);

   List<WhereCondition> getWhereConditions();

   List<SelectCondition> getSelectColumns();

   String getNewTableName();
}
