package fr.maxlego08.zauctionhouse.libs.sarah.database;

public enum SchemaType {
   CREATE,
   MODIFY,
   DROP,
   UPSERT,
   UPDATE,
   INSERT,
   SELECT,
   SELECT_COUNT,
   DELETE,
   ALTER,
   RENAME,
   CREATE_INDEX;

   // $FF: synthetic method
   private static SchemaType[] $values() {
      return new SchemaType[]{CREATE, MODIFY, DROP, UPSERT, UPDATE, INSERT, SELECT, SELECT_COUNT, DELETE, ALTER, RENAME, CREATE_INDEX};
   }
}
