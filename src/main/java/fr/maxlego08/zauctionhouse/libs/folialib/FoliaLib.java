package fr.maxlego08.zauctionhouse.libs.folialib;

import fr.maxlego08.zauctionhouse.libs.folialib.enums.ImplementationType;
import fr.maxlego08.zauctionhouse.libs.folialib.impl.PlatformScheduler;
import fr.maxlego08.zauctionhouse.libs.folialib.util.FoliaLibOptions;
import fr.maxlego08.zauctionhouse.libs.folialib.util.InvalidTickDelayNotifier;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;
import org.bukkit.plugin.Plugin;

public class FoliaLib {
   private final Plugin plugin;
   private final ImplementationType implementationType;
   private final PlatformScheduler scheduler;
   private FoliaLibOptions options;
   private InvalidTickDelayNotifier invalidTickDelayNotifier;

   public FoliaLib(Plugin var1) {
      this(var1, new FoliaLibOptions());
   }

   public FoliaLib(Plugin var1, FoliaLibOptions var2) {
      this.plugin = var1;
      this.options = var2;
      this.invalidTickDelayNotifier = new InvalidTickDelayNotifier(var1.getLogger(), this.options);
      ImplementationType var3 = ImplementationType.UNKNOWN;

      for(ImplementationType var7 : ImplementationType.values()) {
         if (var7.selfCheck()) {
            var3 = var7;
            break;
         }
      }

      this.implementationType = var3;
      this.scheduler = this.createServerImpl(this.implementationType.getImplementationClassName());
      if (this.scheduler == null) {
         throw new IllegalStateException("Failed to create server implementation. Please report this to the FoliaLib GitHub issues page. Forks of server software may not all be supported. If you are using an unofficial fork, please report this to the fork's developers first.");
      } else {
         String var8 = "com,tcoded,folialib,".replace(",", ".");
         if (this.getClass().getName().startsWith(var8)) {
            Logger var9 = this.plugin.getLogger();
            var9.severe("****************************************************************");
            var9.severe("FoliaLib is not relocated correctly! This will cause conflicts");
            var9.severe("with other plugins using FoliaLib. Please contact the developers");
            var9.severe(String.format("of '%s' and inform them of this issue immediately!", this.plugin.getDescription().getName()));
            var9.severe("****************************************************************");
         }

      }
   }

   public ImplementationType getImplType() {
      return this.implementationType;
   }

   /** @deprecated */
   @Deprecated
   public PlatformScheduler getImpl() {
      return this.getScheduler();
   }

   public PlatformScheduler getScheduler() {
      return this.scheduler;
   }

   public boolean isFolia() {
      return this.implementationType == ImplementationType.FOLIA;
   }

   public boolean isPaper() {
      return this.implementationType == ImplementationType.PAPER || this.implementationType == ImplementationType.LEGACY_PAPER;
   }

   public boolean isSpigot() {
      return this.implementationType == ImplementationType.SPIGOT || this.implementationType == ImplementationType.LEGACY_SPIGOT;
   }

   public boolean isUnsupported() {
      return this.implementationType == ImplementationType.UNKNOWN;
   }

   public Plugin getPlugin() {
      return this.plugin;
   }

   /** @deprecated */
   @Deprecated
   public void disableInvalidTickValueWarning() {
      this.getOptions().disableNotifications();
   }

   /** @deprecated */
   @Deprecated
   public void enableInvalidTickValueDebug() {
      this.getOptions().enableInvalidTickDebugMode();
   }

   public FoliaLibOptions getOptions() {
      return this.options;
   }

   /** @deprecated */
   @Deprecated
   public InvalidTickDelayNotifier getInvalidTickDelayNotifier() {
      return this.invalidTickDelayNotifier;
   }

   private PlatformScheduler createServerImpl(String var1) {
      String var2 = this.getClass().getPackage().getName() + ".impl.";

      try {
         return (PlatformScheduler)Class.forName(var2 + var1).getConstructor(this.getClass()).newInstance(this);
      } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException var4) {
         ((ReflectiveOperationException)var4).printStackTrace();
         return null;
      }
   }
}
