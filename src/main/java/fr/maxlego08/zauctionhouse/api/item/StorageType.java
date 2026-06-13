package fr.maxlego08.zauctionhouse.api.item;

import fr.maxlego08.zauctionhouse.api.cache.PlayerCacheKey;

public enum StorageType {
   LISTED(PlayerCacheKey.ITEMS_LISTED),
   PURCHASED(PlayerCacheKey.ITEMS_PURCHASED),
   EXPIRED(PlayerCacheKey.ITEMS_EXPIRED),
   DELETED((PlayerCacheKey)null);

   private final PlayerCacheKey playerCacheKey;

   private StorageType(PlayerCacheKey var3) {
      this.playerCacheKey = var3;
   }

   public PlayerCacheKey getPlayerCacheKey() {
      return this.playerCacheKey;
   }

   // $FF: synthetic method
   private static StorageType[] $values() {
      return new StorageType[]{LISTED, PURCHASED, EXPIRED, DELETED};
   }
}
