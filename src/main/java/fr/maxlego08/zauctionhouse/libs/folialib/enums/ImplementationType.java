package fr.maxlego08.zauctionhouse.libs.folialib.enums;

import fr.maxlego08.zauctionhouse.libs.folialib.util.ImplementationTestsUtil;
import java.util.function.Supplier;

public enum ImplementationType {
   FOLIA("FoliaImplementation", new Supplier[0], new String[]{"io.papermc.paper.threadedregions.RegionizedServer"}),
   PAPER("PaperImplementation", new Supplier[]{ImplementationTestsUtil::isTaskConsumersSupported}, new String[]{"com.destroystokyo.paper.PaperConfig", "io.papermc.paper.configuration.Configuration"}),
   LEGACY_PAPER("LegacyPaperImplementation", new Supplier[0], new String[]{"com.destroystokyo.paper.PaperConfig", "io.papermc.paper.configuration.Configuration"}),
   SPIGOT("SpigotImplementation", new Supplier[]{ImplementationTestsUtil::isTaskConsumersSupported}, new String[]{"org.spigotmc.SpigotConfig"}),
   LEGACY_SPIGOT("LegacySpigotImplementation", new Supplier[0], new String[]{"org.spigotmc.SpigotConfig"}),
   UNKNOWN("UnsupportedImplementation", new Supplier[0], new String[0]);

   private final String implementationClassName;
   private final Supplier<Boolean>[] tests;
   private final String[] classNames;

   private ImplementationType(String var3, Supplier<Boolean>[] var4, String... var5) {
      this.implementationClassName = var3;
      this.tests = var4;
      this.classNames = var5;
   }

   public String getImplementationClassName() {
      return this.implementationClassName;
   }

   public Supplier<Boolean>[] getTests() {
      return this.tests;
   }

   public String[] getClassNames() {
      return this.classNames;
   }

   public boolean selfCheck() {
      for(Supplier var4 : this.getTests()) {
         if (!(Boolean)var4.get()) {
            return false;
         }
      }

      String[] var8 = this.getClassNames();

      for(String var5 : var8) {
         try {
            Class.forName(var5);
            return true;
         } catch (ClassNotFoundException var7) {
         }
      }

      return false;
   }

   // $FF: synthetic method
   private static ImplementationType[] $values() {
      return new ImplementationType[]{FOLIA, PAPER, LEGACY_PAPER, SPIGOT, LEGACY_SPIGOT, UNKNOWN};
   }
}
