package fr.maxlego08.zauctionhouse.libs.sarah.conditions;

import fr.maxlego08.zauctionhouse.libs.sarah.DatabaseConfiguration;
import fr.maxlego08.zauctionhouse.libs.sarah.database.DatabaseType;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings({"unchecked", "rawtypes"})
public class ColumnDefinition {
   private String name;
   private String type;
   private int length;
   private int decimal;
   private boolean nullable = false;
   private String defaultValue;
   private boolean isPrimaryKey = false;
   private String referenceTable;
   private Object object;
   private boolean isAutoIncrement;
   private boolean unique = false;
   private List<String> enumValues;

   public ColumnDefinition(String var1, String var2) {
      this.name = var1;
      this.type = var2;
   }

   public ColumnDefinition(String var1) {
      this.name = var1;
   }

   public String build(DatabaseConfiguration var1) {
      String var2 = this.type;
      if (this.isAutoIncrement && var1.getDatabaseType() == DatabaseType.SQLITE && (this.type.equalsIgnoreCase("BIGINT") || this.type.equalsIgnoreCase("INT") || this.type.equalsIgnoreCase("INTEGER"))) {
         var2 = "INTEGER";
      }

      StringBuilder var3 = new StringBuilder("`" + this.name + "` " + var2);
      if (this.enumValues != null && !this.enumValues.isEmpty()) {
         if (var1.getDatabaseType() == DatabaseType.SQLITE) {
            var3 = new StringBuilder("`" + this.name + "` TEXT");
         } else {
            String var4 = (String)this.enumValues.stream().map((var0) -> "'" + var0.replace("'", "''") + "'").collect(Collectors.joining(", "));
            var3 = new StringBuilder("`" + this.name + "` ENUM(" + var4 + ")");
         }
      } else if (this.length != 0 && this.decimal != 0) {
         var3.append("(").append(this.length).append(",").append(this.decimal).append(")");
      } else if (this.length != 0) {
         var3.append("(").append(this.length).append(")");
      }

      if (this.isAutoIncrement && this.isPrimaryKey) {
         if (var1.getDatabaseType() == DatabaseType.SQLITE) {
            var3.append(" PRIMARY KEY AUTOINCREMENT");
            if (this.unique) {
               var3.append(" UNIQUE");
            }

            return var3.toString();
         }

         var3.append(" AUTO_INCREMENT");
      }

      if (this.nullable) {
         var3.append(" NULL");
      } else {
         var3.append(" NOT NULL");
      }

      if (this.defaultValue != null) {
         var3.append(" DEFAULT ").append(this.defaultValue);
      }

      if (this.unique) {
         var3.append(" UNIQUE");
      }

      return var3.toString();
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public String getSafeName() {
      return String.format("`%s`", this.name);
   }

   public String getType() {
      return this.type;
   }

   public void setType(String var1) {
      this.type = var1;
   }

   public Integer getLength() {
      return this.length;
   }

   public ColumnDefinition setLength(Integer var1) {
      this.length = var1;
      return this;
   }

   public ColumnDefinition setLength(int var1) {
      this.length = var1;
      return this;
   }

   public ColumnDefinition setDecimal(Integer var1) {
      this.decimal = var1;
      return this;
   }

   public Boolean getNullable() {
      return this.nullable;
   }

   public String getDefaultValue() {
      return this.defaultValue;
   }

   public void setDefaultValue(String var1) {
      this.defaultValue = var1;
   }

   public boolean isPrimaryKey() {
      return this.isPrimaryKey;
   }

   public void setPrimaryKey(boolean var1) {
      this.isPrimaryKey = var1;
   }

   public String getReferenceTable() {
      return this.referenceTable;
   }

   public void setReferenceTable(String var1) {
      this.referenceTable = var1;
   }

   public boolean isNullable() {
      return this.nullable;
   }

   public void setNullable(Boolean var1) {
      this.nullable = var1;
   }

   public void setNullable(boolean var1) {
      this.nullable = var1;
   }

   public void setUnique(boolean var1) {
      this.unique = var1;
   }

   public boolean isUnique() {
      return this.unique;
   }

   public Object getObject() {
      return this.object;
   }

   public ColumnDefinition setObject(Object var1) {
      this.object = var1;
      return this;
   }

   public boolean isAutoIncrement() {
      return this.isAutoIncrement;
   }

   public ColumnDefinition setAutoIncrement(boolean var1) {
      this.isAutoIncrement = var1;
      return this;
   }

   public List<String> getEnumValues() {
      return this.enumValues;
   }

   public ColumnDefinition setEnumValues(List<String> var1) {
      this.enumValues = var1;
      return this;
   }

   public ColumnDefinition setEnumValues(String... var1) {
      this.enumValues = Arrays.asList(var1);
      return this;
   }

   public <E extends Enum<E>> ColumnDefinition setEnumValues(Class<E> var1) {
      this.enumValues = (List)Arrays.stream((Enum[])var1.getEnumConstants()).map(Enum::name).collect(Collectors.toList());
      return this;
   }
}
