package fr.maxlego08.zauctionhouse.libs.sarah.conditions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WhereCondition {
   private final String column;
   private final Object value;
   private final String operator;
   private final WhereAction whereAction;
   private final List<String> values = new ArrayList();

   public WhereCondition(String var1, String var2, String var3, Object var4) {
      this.column = (var1 == null ? "" : var1 + ".") + "`" + var2 + "`";
      this.operator = var3;
      this.value = var4;
      this.whereAction = WhereCondition.WhereAction.NORMAL;
   }

   public WhereCondition(String var1, String var2, List<String> var3) {
      this.column = (var1 == null ? "" : var1 + ".") + "`" + var2 + "`";
      this.value = null;
      this.operator = null;
      this.values.addAll(var3);
      this.whereAction = WhereCondition.WhereAction.IN;
   }

   public WhereCondition(String var1, WhereAction var2) {
      this.column = var1;
      this.value = null;
      this.operator = null;
      this.whereAction = var2;
   }

   public String getCondition() {
      if (this.whereAction == WhereCondition.WhereAction.IS_NOT_NULL) {
         return this.column + " IS NOT NULL";
      } else if (this.whereAction == WhereCondition.WhereAction.IS_NULL) {
         return this.column + " IS NULL";
      } else {
         return this.whereAction == WhereCondition.WhereAction.IN ? this.column + " IN (" + (String)this.values.stream().map((var0) -> "?").collect(Collectors.joining(",")) + ")" : this.column + " " + this.operator + " ?";
      }
   }

   public String getOperator() {
      return this.operator;
   }

   public Object getValue() {
      return this.value;
   }

   public String getColumn() {
      return this.column;
   }

   public WhereAction getWhereAction() {
      return this.whereAction;
   }

   public List<String> getValues() {
      return this.values;
   }

   public static enum WhereAction {
      IS_NOT_NULL,
      IS_NULL,
      NORMAL,
      IN;

      // $FF: synthetic method
      private static WhereAction[] $values() {
         return new WhereAction[]{IS_NOT_NULL, IS_NULL, NORMAL, IN};
      }
   }
}
