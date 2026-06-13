package fr.maxlego08.zauctionhouse.libs.sarah.security;

import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.util.HashSet;
import java.util.Set;

public class SecureObjectInputStream extends ObjectInputStream {
   private final Set<String> allowedClasses;
   private final Set<String> allowedPackagePrefixes;

   public SecureObjectInputStream(InputStream var1, Class<?>... var2) {
      super(var1);
      this.allowedClasses = new HashSet();
      this.allowedPackagePrefixes = new HashSet();
      this.addSafeDefaults();

      for(Class var6 : var2) {
         this.allowedClasses.add(var6.getName());
      }

   }

   public SecureObjectInputStream(InputStream var1, Set<String> var2, Set<String> var3) {
      super(var1);
      this.allowedClasses = new HashSet(var2);
      this.allowedPackagePrefixes = new HashSet(var3);
      this.addSafeDefaults();
   }

   private void addSafeDefaults() {
      this.allowedClasses.add("java.lang.String");
      this.allowedClasses.add("java.lang.Integer");
      this.allowedClasses.add("java.lang.Long");
      this.allowedClasses.add("java.lang.Double");
      this.allowedClasses.add("java.lang.Float");
      this.allowedClasses.add("java.lang.Boolean");
      this.allowedClasses.add("java.lang.Byte");
      this.allowedClasses.add("java.lang.Short");
      this.allowedClasses.add("java.lang.Character");
      this.allowedClasses.add("java.lang.Number");
      this.allowedClasses.add("java.util.Date");
      this.allowedClasses.add("java.sql.Date");
      this.allowedClasses.add("java.sql.Time");
      this.allowedClasses.add("java.sql.Timestamp");
      this.allowedClasses.add("[B");
      this.allowedClasses.add("[C");
      this.allowedClasses.add("[I");
      this.allowedClasses.add("[J");
      this.allowedClasses.add("[F");
      this.allowedClasses.add("[D");
      this.allowedClasses.add("[Z");
      this.allowedClasses.add("[S");
      this.allowedClasses.add("java.util.ArrayList");
      this.allowedClasses.add("java.util.HashMap");
      this.allowedClasses.add("java.util.HashSet");
      this.allowedClasses.add("java.util.LinkedList");
      this.allowedClasses.add("java.util.TreeMap");
      this.allowedClasses.add("java.util.TreeSet");
      this.allowedClasses.add("java.util.UUID");
   }

   protected Class<?> resolveClass(ObjectStreamClass var1) {
      String var2 = var1.getName();
      if (this.allowedClasses.contains(var2)) {
         return super.resolveClass(var1);
      } else {
         for(String var4 : this.allowedPackagePrefixes) {
            if (var2.startsWith(var4)) {
               return super.resolveClass(var1);
            }
         }

         throw new InvalidClassException("Unauthorized deserialization attempt", "Class " + var2 + " is not in the whitelist. This is a security measure to prevent deserialization attacks.");
      }
   }

   public void allowClass(Class<?> var1) {
      this.allowedClasses.add(var1.getName());
   }

   public void allowPackagePrefix(String var1) {
      this.allowedPackagePrefixes.add(var1);
   }
}
