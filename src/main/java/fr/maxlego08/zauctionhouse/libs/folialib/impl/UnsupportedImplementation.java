package fr.maxlego08.zauctionhouse.libs.folialib.impl;

import fr.maxlego08.zauctionhouse.libs.folialib.FoliaLib;
import java.util.logging.Logger;
import org.bukkit.plugin.Plugin;

public class UnsupportedImplementation extends LegacySpigotImplementation {
   public UnsupportedImplementation(FoliaLib var1) {
      super(var1);
      Plugin var2 = var1.getPlugin();
      Logger var3 = var2.getLogger();
      var3.warning(String.format("\n---------------------------------------------------------------------\nFoliaLib does not support this server software! (%s)\nFoliaLib will attempt to use the legacy spigot implementation.\n---------------------------------------------------------------------\n", var2.getServer().getVersion()));
   }
}
