package fr.maxlego08.zauctionhouse.libs.folialib.type;

public class Ref<T> {
   private T value;

   public Ref() {
      this.value = null;
   }

   public Ref(T var1) {
      this.value = var1;
   }

   public T get() {
      return this.value;
   }

   public void set(T var1) {
      this.value = var1;
   }
}
