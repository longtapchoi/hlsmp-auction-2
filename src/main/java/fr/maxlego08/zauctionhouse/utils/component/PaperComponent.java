package fr.maxlego08.zauctionhouse.utils.component;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.component.ComponentMessage;
import fr.maxlego08.zauctionhouse.api.messages.messages.BossBarMessage;
import fr.maxlego08.zauctionhouse.api.messages.messages.TitleMessage;
import fr.maxlego08.zauctionhouse.utils.BossBarAnimation;
import fr.maxlego08.zauctionhouse.utils.MessageUtils;
import fr.maxlego08.zauctionhouse.utils.cache.SimpleCache;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.format.TextDecoration.State;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.Title.Times;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PaperComponent implements ComponentMessage {
   private final MiniMessage MINI_MESSAGE = MiniMessage.builder().tags(TagResolver.builder().resolver(StandardTags.defaults()).build()).build();
   private final Map<String, String> COLORS_MAPPINGS = new HashMap();
   private final SimpleCache<String, Component> cache = new SimpleCache<String, Component>();

   public PaperComponent() {
      this.COLORS_MAPPINGS.put("0", "black");
      this.COLORS_MAPPINGS.put("1", "dark_blue");
      this.COLORS_MAPPINGS.put("2", "dark_green");
      this.COLORS_MAPPINGS.put("3", "dark_aqua");
      this.COLORS_MAPPINGS.put("4", "dark_red");
      this.COLORS_MAPPINGS.put("5", "dark_purple");
      this.COLORS_MAPPINGS.put("6", "gold");
      this.COLORS_MAPPINGS.put("7", "gray");
      this.COLORS_MAPPINGS.put("8", "dark_gray");
      this.COLORS_MAPPINGS.put("9", "blue");
      this.COLORS_MAPPINGS.put("a", "green");
      this.COLORS_MAPPINGS.put("b", "aqua");
      this.COLORS_MAPPINGS.put("c", "red");
      this.COLORS_MAPPINGS.put("d", "light_purple");
      this.COLORS_MAPPINGS.put("e", "yellow");
      this.COLORS_MAPPINGS.put("f", "white");
      this.COLORS_MAPPINGS.put("k", "obfuscated");
      this.COLORS_MAPPINGS.put("l", "bold");
      this.COLORS_MAPPINGS.put("m", "strikethrough");
      this.COLORS_MAPPINGS.put("n", "underlined");
      this.COLORS_MAPPINGS.put("o", "italic");
      this.COLORS_MAPPINGS.put("r", "reset");
   }

   private TextDecoration.State getState(String var1) {
      return !var1.contains("&o") && !var1.contains("<i>") && !var1.contains("<em>") && !var1.contains("<italic>") ? State.FALSE : State.TRUE;
   }

   private String colorMiniMessage(String var1) {
      StringBuilder var2 = new StringBuilder();
      Pattern var3 = Pattern.compile("(?<!<)(?<!:)(?<!</)#([a-fA-F0-9]{6})");
      Matcher var4 = var3.matcher(var1);

      while(var4.find()) {
         var4.appendReplacement(var2, "<$0>");
      }

      var4.appendTail(var2);
      String var5 = var2.toString();

      for(Map.Entry var7 : this.COLORS_MAPPINGS.entrySet()) {
         String var8 = (String)var7.getKey();
         String var9 = (String)var7.getValue();
         var5 = var5.replace("&" + var8, "<" + var9 + ">");
         var5 = var5.replace("§" + var8, "<" + var9 + ">");
         var5 = var5.replace("&" + var8.toUpperCase(), "<" + var9 + ">");
         var5 = var5.replace("§" + var8.toUpperCase(), "<" + var9 + ">");
      }

      return var5;
   }

   public Component getComponent(String var1) {
      return this.cache.get(var1, () -> this.MINI_MESSAGE.deserialize(this.colorMiniMessage(var1)));
   }

   public void sendMessage(CommandSender var1, String var2) {
      Component var3 = this.cache.get(var2, () -> this.MINI_MESSAGE.deserialize(this.colorMiniMessage(var2)));
      var1.sendMessage(var3);
   }

   public void sendActionBar(Player var1, String var2) {
      Component var3 = this.cache.get(var2, () -> this.MINI_MESSAGE.deserialize(this.colorMiniMessage(var2)));
      var1.sendActionBar(var3);
   }

   public void sendTitle(Player var1, TitleMessage var2, Object... var3) {
      Component var4 = this.getComponent(MessageUtils.getString(var2.title(), var3));
      Component var5 = this.getComponent(MessageUtils.getString(var2.subtitle(), var3));
      var1.showTitle(Title.title(var4, var5, Times.times(Duration.ofMillis(var2.start()), Duration.ofMillis(var2.time()), Duration.ofMillis(var2.end()))));
   }

   public void sendBossBar(AuctionPlugin var1, Player var2, BossBarMessage var3) {
      BossBar var4 = BossBar.bossBar(this.getComponent(var3.text()), 1.0F, var3.getColor(), var3.getOverlay(), var3.getFlags());
      var2.showBossBar(var4);
      new BossBarAnimation(var1, var2, var4, var3.duration());
   }

   public String getItemStackName(ItemStack var1) {
      if (!var1.hasItemMeta()) {
         return "";
      } else {
         ItemMeta var2 = var1.getItemMeta();
         return !var2.hasDisplayName() ? "" : PlainTextComponentSerializer.plainText().serialize((Component)Objects.requireNonNull(var2.displayName()));
      }
   }

   public List<String> getItemStackLore(ItemStack var1) {
      if (!var1.hasItemMeta()) {
         return List.of();
      } else {
         ItemMeta var2 = var1.getItemMeta();
         if (!var2.hasLore()) {
            return List.of();
         } else {
            Stream var10000 = ((List)Objects.requireNonNull(var2.lore())).stream();
            PlainTextComponentSerializer var10001 = PlainTextComponentSerializer.plainText();
            Objects.requireNonNull(var10001);
            return var10000.map(var10001::serialize).toList();
         }
      }
   }

   public boolean hasDisplayName(ItemStack var1) {
      return var1.hasItemMeta() && var1.getItemMeta().hasDisplayName();
   }

   public String getItemStackDisplayName(ItemStack var1) {
      return (String)MiniMessage.miniMessage().serialize((Component)Objects.requireNonNull(var1.getItemMeta().displayName()));
   }

   public String stripColor(String var1) {
      return PlainTextComponentSerializer.plainText().serialize(this.MINI_MESSAGE.deserialize(this.colorMiniMessage(var1)));
   }
}
