package fr.maxlego08.zauctionhouse.discord;

import fr.maxlego08.zauctionhouse.api.category.Category;
import fr.maxlego08.zauctionhouse.api.economy.AuctionEconomy;
import fr.maxlego08.zauctionhouse.api.item.items.AuctionItem;
import fr.maxlego08.zauctionhouse.utils.component.ComponentMessageHelper;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DiscordPlaceholderResolver {
   private final Map<String, Supplier<String>> placeholders = new HashMap<>();
   private String cachedImageUrl;
   private String cachedDominantColor;

   public DiscordPlaceholderResolver(String var1, AuctionItem var2, Player var3, Player var4, String var5, boolean var6, String var7, ColorExtractor var8) {
      this.registerItemPlaceholders(var2);
      this.registerSellerPlaceholders(var2, var3);
      this.registerBuyerPlaceholders(var2, var4);
      this.registerPricePlaceholders(var2);
      this.registerTimePlaceholders(var2);
      this.registerServerPlaceholders(var1);
      this.registerCategoryPlaceholders(var2);
      this.registerImagePlaceholders(var2, var5, var6, var7, var8);
   }

   private void registerItemPlaceholders(AuctionItem var1) {
      this.placeholders.put("%item_id%", () -> String.valueOf(var1.getId()));
      ItemStack var2 = var1.getItemStack();
      if (var2 != null) {
         this.placeholders.put("%item_material%", () -> var2.getType().name());
         this.placeholders.put("%item_amount%", () -> String.valueOf(var1.getAmount()));
         this.placeholders.put("%item_display%", () -> ComponentMessageHelper.componentMessage.stripColor(var1.getItemDisplay()));
         ItemMeta var3 = var2.getItemMeta();
         if (var3 != null) {
            this.placeholders.put("%item_lore%", () -> {
               List<String> loreList = ComponentMessageHelper.componentMessage.getItemStackLore(var2);
               return loreList != null ? String.join("\n", loreList) : "";
            });
            this.placeholders.put("%item_enchantments%", () -> var3.hasEnchants() ? var3.getEnchants().entrySet().stream().map((enchEntry) -> {
                  String var10000 = this.formatEnchantment(enchEntry.getKey());
                  return var10000 + " " + String.valueOf(enchEntry.getValue());
               }).collect(Collectors.joining(", ")) : "None");
            this.placeholders.put("%item_custom_model_data%", () -> var3.hasCustomModelData() ? String.valueOf(var3.getCustomModelData()) : "0");
         } else {
            this.placeholders.put("%item_lore%", () -> "");
            this.placeholders.put("%item_enchantments%", () -> "None");
            this.placeholders.put("%item_custom_model_data%", () -> "0");
         }
      } else {
         this.placeholders.put("%item_material%", () -> "UNKNOWN");
         this.placeholders.put("%item_amount%", () -> "0");
         this.placeholders.put("%item_display%", () -> "Unknown Item");
         this.placeholders.put("%item_lore%", () -> "");
         this.placeholders.put("%item_enchantments%", () -> "None");
         this.placeholders.put("%item_custom_model_data%", () -> "0");
      }

   }

   private void registerSellerPlaceholders(AuctionItem var1, Player var2) {
      if (var2 != null) {
         Map<String, Supplier<String>> var10000 = this.placeholders;
         Objects.requireNonNull(var2);
         var10000.put("%seller_name%", var2::getName);
         this.placeholders.put("%seller_uuid%", () -> var2.getUniqueId().toString());
      } else {
         Map<String, Supplier<String>> var3 = this.placeholders;
         Objects.requireNonNull(var1);
         var3.put("%seller_name%", var1::getSellerName);
         this.placeholders.put("%seller_uuid%", () -> {
            UUID var1x = var1.getSellerUniqueId();
            return var1x != null ? var1x.toString() : "";
         });
      }

   }

   private void registerBuyerPlaceholders(AuctionItem var1, Player var2) {
      if (var2 != null) {
         Map<String, Supplier<String>> var10000 = this.placeholders;
         Objects.requireNonNull(var2);
         var10000.put("%buyer_name%", var2::getName);
         this.placeholders.put("%buyer_uuid%", () -> var2.getUniqueId().toString());
      } else {
         String var3 = var1.getBuyerName();
         UUID var4 = var1.getBuyerUniqueId();
         this.placeholders.put("%buyer_name%", () -> var3 != null ? var3 : "Unknown");
         this.placeholders.put("%buyer_uuid%", () -> var4 != null ? var4.toString() : "");
      }

   }

   private void registerPricePlaceholders(AuctionItem var1) {
      this.placeholders.put("%price%", () -> var1.getPrice().toPlainString());
      Map<String, Supplier<String>> var10000 = this.placeholders;
      Objects.requireNonNull(var1);
      var10000.put("%formatted_price%", var1::getFormattedPrice);
      AuctionEconomy var2 = var1.getAuctionEconomy();
      if (var2 != null) {
         var10000 = this.placeholders;
         Objects.requireNonNull(var2);
         var10000.put("%economy_name%", var2::getName);
         var10000 = this.placeholders;
         Objects.requireNonNull(var2);
         var10000.put("%economy_display_name%", var2::getDisplayName);
      } else {
         this.placeholders.put("%economy_name%", () -> "default");
         this.placeholders.put("%economy_display_name%", () -> "Default");
      }

   }

   private void registerTimePlaceholders(AuctionItem var1) {
      this.placeholders.put("%created_at%", () -> {
         Date var1x = var1.getCreatedAt();
         return var1x != null ? var1x.toString() : "";
      });
      Map<String, Supplier<String>> var10000 = this.placeholders;
      Objects.requireNonNull(var1);
      var10000.put("%expires_at%", var1::getFormattedExpireDate);
      var10000 = this.placeholders;
      Objects.requireNonNull(var1);
      var10000.put("%remaining_time%", var1::getRemainingTime);
      this.placeholders.put("%timestamp%", () -> Instant.now().toString());
   }

   private void registerServerPlaceholders(String var1) {
      this.placeholders.put("%server_name%", () -> var1);
   }

   private void registerCategoryPlaceholders(AuctionItem var1) {
      this.placeholders.put("%category_names%", () -> {
         Set<Category> var1x = var1.getCategories();
         return var1x != null && !var1x.isEmpty() ? var1x.stream().map(Category::getDisplayName).collect(Collectors.joining(", ")) : "None";
      });
      this.placeholders.put("%category_count%", () -> {
         Set<Category> var1x = var1.getCategories();
         return String.valueOf(var1x != null ? var1x.size() : 0);
      });
   }

   private void registerImagePlaceholders(AuctionItem var1, String var2, boolean var3, String var4, ColorExtractor var5) {
      ItemStack var6 = var1.getItemStack();
      String var7 = var6 != null ? var6.getType().name() : "UNKNOWN";
      if (var2 != null && !var2.isEmpty()) {
         this.cachedImageUrl = var2.replace("%item_material%", var7).replace("%ITEM_MATERIAL%", var7);
      } else {
         this.cachedImageUrl = "";
      }

      this.placeholders.put("%item_image_url%", () -> this.cachedImageUrl);
      if (var3 && var5 != null && !this.cachedImageUrl.isEmpty()) {
         this.cachedDominantColor = var5.getColorForMaterial(var7, this.cachedImageUrl);
      } else {
         this.cachedDominantColor = var4;
      }

      this.placeholders.put("%item_dominant_color%", () -> this.cachedDominantColor);
   }

   public String resolve(String var1) {
      if (var1 != null && !var1.isEmpty()) {
         String var2 = var1;

         for(Map.Entry<String, Supplier<String>> var4 : this.placeholders.entrySet()) {
            if (var2.contains(var4.getKey())) {
               String var5 = var4.getValue().get();
               var2 = var2.replace(var4.getKey(), var5 != null ? var5 : "");
            }
         }

         return var2;
      } else {
         return var1;
      }
   }

   private String formatEnchantment(Enchantment var1) {
      String var2 = var1.getKey().getKey();
      String[] var3 = var2.split("_");
      StringBuilder var4 = new StringBuilder();

      for(String var8 : var3) {
         if (!var8.isEmpty()) {
            var4.append(Character.toUpperCase(var8.charAt(0))).append(var8.substring(1).toLowerCase()).append(" ");
         }
      }

      return var4.toString().trim();
   }
}
