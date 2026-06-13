package fr.maxlego08.zauctionhouse.libs.sarah.conditions;

import java.util.Objects;

public class SelectCondition {
   private final String tablePrefix;
   private final String column;
   private final String aliases;
   private final boolean isCoalesce;
   private final Object defaultValue;

   public SelectCondition(String var1, String var2, String var3, boolean var4, Object var5) {
      this.tablePrefix = var1;
      this.column = var2;
      this.aliases = var3;
      this.isCoalesce = var4;
      this.defaultValue = var5;
   }

   public String getTablePrefix() {
      return this.tablePrefix;
   }

   public String getColumn() {
      return this.column;
   }

   public boolean isCoalesce() {
      return this.isCoalesce;
   }

   public Object getDefaultValue() {
      return this.defaultValue;
   }

   public String getSelectColumn() {
      String var1 = this.tablePrefix == null ? this.getColumnAndAliases() : this.tablePrefix + "." + this.getColumnAndAliases();
      if (this.isCoalesce) {
         String var2 = this.tablePrefix == null ? "`" + this.column + "`" : this.tablePrefix + ".`" + this.column + "`";
         return "COALESCE(" + var2 + ", " + this.defaultValue + ")" + this.getAliases();
      } else {
         return var1;
      }
   }

   private String getColumnAndAliases() {
      return "`" + this.column + "`" + this.getAliases();
   }

   private String getAliases() {
      return this.aliases == null ? "" : " as " + this.aliases;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         SelectCondition var2 = (SelectCondition)var1;
         return this.isCoalesce == var2.isCoalesce && Objects.equals(this.tablePrefix, var2.tablePrefix) && Objects.equals(this.column, var2.column) && Objects.equals(this.aliases, var2.aliases) && Objects.equals(this.defaultValue, var2.defaultValue);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.tablePrefix, this.column, this.aliases, this.isCoalesce, this.defaultValue});
   }

   public String toString() {
      return "SelectCondition{tablePrefix='" + this.tablePrefix + '\'' + ", column='" + this.column + '\'' + ", aliases='" + this.aliases + '\'' + ", isCoalesce=" + this.isCoalesce + ", defaultValue=" + this.defaultValue + '}';
   }
}
