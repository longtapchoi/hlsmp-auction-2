package fr.maxlego08.zauctionhouse.libs.sarah.libs.hikari;

import fr.maxlego08.zauctionhouse.libs.sarah.libs.hikari.util.PropertyElf;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Set;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;
import javax.sql.DataSource;

public class HikariJNDIFactory implements ObjectFactory {
   public synchronized Object getObjectInstance(Object var1, Name var2, Context var3, Hashtable<?, ?> var4) {
      if (var1 instanceof Reference && "javax.sql.DataSource".equals(((Reference)var1).getClassName())) {
         Reference var5 = (Reference)var1;
         Set var6 = PropertyElf.getPropertyNames(HikariConfig.class);
         Properties var7 = new Properties();
         Enumeration var8 = var5.getAll();

         while(var8.hasMoreElements()) {
            RefAddr var9 = (RefAddr)var8.nextElement();
            String var10 = var9.getType();
            if (var10.startsWith("dataSource.") || var6.contains(var10)) {
               var7.setProperty(var10, var9.getContent().toString());
            }
         }

         return this.createDataSource(var7, var3);
      } else {
         return null;
      }
   }

   private DataSource createDataSource(Properties var1, Context var2) {
      String var3 = var1.getProperty("dataSourceJNDI");
      return (DataSource)(var3 != null ? this.lookupJndiDataSource(var1, var2, var3) : new HikariDataSource(new HikariConfig(var1)));
   }

   private DataSource lookupJndiDataSource(Properties var1, Context var2, String var3) throws java.sql.SQLException {
      if (var2 == null) {
         throw new RuntimeException("JNDI context does not found for dataSourceJNDI : " + var3);
      } else {
         DataSource var4 = (DataSource)var2.lookup(var3);
         if (var4 == null) {
            InitialContext var5 = new InitialContext();
            var4 = (DataSource)var5.lookup(var3);
            var5.close();
         }

         if (var4 != null) {
            HikariConfig var6 = new HikariConfig(var1);
            var6.setDataSource(var4);
            return new HikariDataSource(var6);
         } else {
            return null;
         }
      }
   }
}
