package fr.maxlego08.zauctionhouse.api.utils;

public enum Plugins {
   VAULT("Vault"),
   ESSENTIALS("Essentials"),
   EXECUTABLE_ITEMS("ExecutableItems"),
   EXECUTABLE_BLOCKS("ExecutableBlocks"),
   HEADDATABASE("HeadDatabase"),
   ZHEAD("zHead"),
   PLACEHOLDER("PlaceholderAPI"),
   CITIZENS("Citizens"),
   TRANSLATIONAPI("TranslationAPI"),
   ZTRANSLATOR("zTranslator"),
   ORAXEN("Oraxen"),
   ITEMSADDER("ItemsAdder"),
   SLIMEFUN("Slimefun"),
   NOVA("Nova"),
   ECO("eco"),
   ZITEMS("zItems"),
   HMCCOSMETICS("HMCCosmetics"),
   JOBS("Jobs"),
   LUCKPERMS("LuckPerms"),
   CRAFTENGINE("CraftEngine"),
   NEXO("Nexo"),
   MAGICCOSMETICS("MagicCosmetics"),
   NEXTGENS("NextGens"),
   MYTHICMOBS("MythicMobs"),
   ZMENUPLUS("zMenuPlus"),
   BREWERYX("BreweryX"),
   MMOITEMS("MMOItems"),
   CRUCIBLE("MythicCrucible"),
   DENIZEN("Denizen"),
   ADVANCEDITEMS("AdvancedItems"),
   CUSTOMCRAFTING("CustomCrafting"),
   ECOITEMS("EcoItems"),
   ZELAUCTION("ZelAuction"),
   AXSHULKERS("AxShulkers");

   private final String name;

   private Plugins(String var3) {
      this.name = var3;
   }

   public String getName() {
      return this.name;
   }

   // $FF: synthetic method
   private static Plugins[] $values() {
      return new Plugins[]{VAULT, ESSENTIALS, EXECUTABLE_ITEMS, EXECUTABLE_BLOCKS, HEADDATABASE, ZHEAD, PLACEHOLDER, CITIZENS, TRANSLATIONAPI, ZTRANSLATOR, ORAXEN, ITEMSADDER, SLIMEFUN, NOVA, ECO, ZITEMS, HMCCOSMETICS, JOBS, LUCKPERMS, CRAFTENGINE, NEXO, MAGICCOSMETICS, NEXTGENS, MYTHICMOBS, ZMENUPLUS, BREWERYX, MMOITEMS, CRUCIBLE, DENIZEN, ADVANCEDITEMS, CUSTOMCRAFTING, ECOITEMS, ZELAUCTION, AXSHULKERS};
   }
}
