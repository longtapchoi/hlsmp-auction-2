package fr.maxlego08.zauctionhouse.api.utils;

public interface IntList extends Iterable<Integer> {
   void add(int var1);

   boolean rem(int var1);

   int getInt(int var1);

   int size();

   boolean isEmpty();

   void clear();

   boolean contains(int var1);

   IntList clone();
}
