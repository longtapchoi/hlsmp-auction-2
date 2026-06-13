package fr.maxlego08.zauctionhouse.api.utils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.jspecify.annotations.NonNull;

public class IntArrayList implements IntList {
   private static final int DEFAULT_CAPACITY = 10;
   private int[] data;
   private int size;

   public IntArrayList() {
      this(10);
   }

   public IntArrayList(int var1) {
      if (var1 < 0) {
         throw new IllegalArgumentException("Initial capacity cannot be negative: " + var1);
      } else {
         this.data = new int[var1];
         this.size = 0;
      }
   }

   public void add(int var1) {
      this.ensureCapacity(this.size + 1);
      this.data[this.size++] = var1;
   }

   public boolean rem(int var1) {
      for(int var2 = 0; var2 < this.size; ++var2) {
         if (this.data[var2] == var1) {
            this.removeAtIndex(var2);
            return true;
         }
      }

      return false;
   }

   public int getInt(int var1) {
      if (var1 >= 0 && var1 < this.size) {
         return this.data[var1];
      } else {
         throw new IndexOutOfBoundsException("Index: " + var1 + ", Size: " + this.size);
      }
   }

   public int size() {
      return this.size;
   }

   public boolean isEmpty() {
      return this.size == 0;
   }

   public void clear() {
      this.size = 0;
   }

   public boolean contains(int var1) {
      for(int var2 = 0; var2 < this.size; ++var2) {
         if (this.data[var2] == var1) {
            return true;
         }
      }

      return false;
   }

   public IntList clone() {
      IntArrayList var1 = new IntArrayList(this.size);
      var1.size = this.size;
      System.arraycopy(this.data, 0, var1.data, 0, this.size);
      return var1;
   }

   public @NonNull Iterator<Integer> iterator() {
      return new IntIterator();
   }

   private void ensureCapacity(int var1) {
      if (var1 > this.data.length) {
         int var2 = Math.max(this.data.length + (this.data.length >> 1), var1);
         this.data = Arrays.copyOf(this.data, var2);
      }

   }

   private void removeAtIndex(int var1) {
      int var2 = this.size - var1 - 1;
      if (var2 > 0) {
         System.arraycopy(this.data, var1 + 1, this.data, var1, var2);
      }

      --this.size;
   }

   public String toString() {
      if (this.size == 0) {
         return "[]";
      } else {
         StringBuilder var1 = new StringBuilder("[");

         for(int var2 = 0; var2 < this.size; ++var2) {
            if (var2 > 0) {
               var1.append(", ");
            }

            var1.append(this.data[var2]);
         }

         return var1.append("]").toString();
      }
   }

   private class IntIterator implements Iterator<Integer> {
      private int cursor = 0;

      public boolean hasNext() {
         return this.cursor < IntArrayList.this.size;
      }

      public Integer next() {
         if (!this.hasNext()) {
            throw new NoSuchElementException();
         } else {
            return IntArrayList.this.data[this.cursor++];
         }
      }
   }
}
