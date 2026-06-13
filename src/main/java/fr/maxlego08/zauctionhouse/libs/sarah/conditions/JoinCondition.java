package fr.maxlego08.zauctionhouse.libs.sarah.conditions;

public class JoinCondition {
   private final String primaryTable;
   private final String primaryTableAlias;
   private final String primaryColumn;
   private final String foreignTable;
   private final String foreignColumn;
   private final JoinType joinType;
   private final JoinCondition additionalCondition;

   public JoinCondition(JoinType var1, String var2, String var3, String var4, String var5, String var6, JoinCondition var7) {
      this.primaryTable = var2;
      this.primaryTableAlias = var3;
      this.primaryColumn = var4;
      this.foreignTable = var5;
      this.foreignColumn = var6;
      this.joinType = var1;
      this.additionalCondition = var7;
   }

   public static JoinCondition and(String var0, String var1, String var2) {
      return new JoinCondition((JoinType)null, (String)null, var0, var1, (String)null, var2, (JoinCondition)null);
   }

   public String getJoinClause() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.joinType.getSql()).append(" ").append(this.primaryTable).append(" AS ").append(this.primaryTableAlias).append(" ON ").append(this.primaryTableAlias).append(".").append(this.primaryColumn).append(" = ").append(this.foreignTable).append(".").append(this.foreignColumn);
      if (this.additionalCondition != null) {
         var1.append(" AND ").append(this.additionalCondition.getCondition());
      }

      return var1.toString();
   }

   private String getCondition() {
      return this.primaryTableAlias + "." + this.primaryColumn + " = '" + this.foreignColumn + "'";
   }

   public static enum JoinType {
      INNER("INNER JOIN"),
      LEFT("LEFT JOIN"),
      RIGHT("RIGHT JOIN"),
      FULL("FULL OUTER JOIN");

      private final String sql;

      private JoinType(String var3) {
         this.sql = var3;
      }

      public String getSql() {
         return this.sql;
      }

      // $FF: synthetic method
      private static JoinType[] $values() {
         return new JoinType[]{INNER, LEFT, RIGHT, FULL};
      }
   }
}
