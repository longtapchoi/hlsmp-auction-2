package fr.maxlego08.zauctionhouse.utils.tags;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Material;
import org.bukkit.Tag;

public class TagRegistry {
   private static final Logger LOGGER = Logger.getLogger(TagRegistry.class.getName());
   private static final Map<String, Tag<Material>> tagMap = new HashMap();

   public static void register(String var0, Tag<Material> var1) {
      tagMap.put(var0, var1);
   }

   public static Tag<Material> getTag(String var0) {
      return (Tag)tagMap.get(var0);
   }

   static {
      for(Field var3 : Tag.class.getDeclaredFields()) {
         if (Tag.class.isAssignableFrom(var3.getType())) {
            try {
               Class var4 = (Class)((ParameterizedType)var3.getGenericType()).getActualTypeArguments()[0];
               if (Material.class.isAssignableFrom(var4)) {
                  register(var3.getName(), (Tag)var3.get((Object)null));
               }
            } catch (Exception var5) {
               LOGGER.log(Level.WARNING, "Failed to register tag " + var3.getName(), var5);
            }
         }
      }

      register("blocks", new BlocksTag());
   }
}
