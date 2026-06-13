package fr.maxlego08.zauctionhouse.loader;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.configuration.ConfigurationFile;
import fr.maxlego08.zauctionhouse.api.messages.AuctionMessage;
import fr.maxlego08.zauctionhouse.api.messages.Message;
import fr.maxlego08.zauctionhouse.api.messages.MessageType;
import fr.maxlego08.zauctionhouse.api.messages.messages.BossBarMessage;
import fr.maxlego08.zauctionhouse.api.messages.messages.ClassicMessage;
import fr.maxlego08.zauctionhouse.api.messages.messages.TitleMessage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.stream.Collectors;
import org.bukkit.configuration.file.YamlConfiguration;

@SuppressWarnings({"unchecked", "rawtypes"})
public class MessageLoader implements ConfigurationFile {
   private final Locale locale = Locale.getDefault();
   private final AuctionPlugin plugin;
   private final List<Message> loadedMessages = new ArrayList();

   public MessageLoader(AuctionPlugin var1) {
      this.plugin = var1;
   }

   public void load() {
      File var1 = new File(this.plugin.getDataFolder(), "messages.yml");
      this.plugin.saveFile("messages.yml", true);
      this.loadMessages(YamlConfiguration.loadConfiguration(var1));
      if (this.loadedMessages.size() != Message.values().length) {
         this.plugin.getLogger().log(Level.SEVERE, "Messages were not loaded correctly.");

         for(Message var5 : Message.values()) {
            if (!this.loadedMessages.contains(var5)) {
               var5.setPlugin(this.plugin);
               String var6 = var5.name().replace("_", "-").toLowerCase();
               this.plugin.getLogger().log(Level.SEVERE, var6 + " has not been found, use of the default value.");
               ArrayList var7 = new ArrayList();

               for(AuctionMessage var9 : var5.getMessages()) {
                  if (var9 instanceof ClassicMessage) {
                     ClassicMessage var10 = (ClassicMessage)var9;
                     ClassicMessage var10000 = var10;

                     try {
                        var57 = var10000.messageType();
                     } catch (Throwable var44) {
                        throw new MatchException(var44.toString(), var44);
                     }

                     MessageType var30 = var57;
                     var10000 = var10;

                     try {
                        var59 = var10000.messages();
                     } catch (Throwable var43) {
                        throw new MatchException(var43.toString(), var43);
                     }

                     List var45 = var59;
                     var7.add(new ClassicMessage(var30, var45.stream().map(this::replaceMessagesColors).toList()));
                  } else if (var9 instanceof BossBarMessage) {
                     BossBarMessage var13 = (BossBarMessage)var9;
                     BossBarMessage var60 = var13;

                     try {
                        var61 = var60.text();
                     } catch (Throwable var42) {
                        throw new MatchException(var42.toString(), var42);
                     }

                     String var46 = var61;
                     var60 = var13;

                     try {
                        var63 = var60.color();
                     } catch (Throwable var41) {
                        throw new MatchException(var41.toString(), var41);
                     }

                     var46 = var63;
                     var60 = var13;

                     try {
                        var65 = var60.overlay();
                     } catch (Throwable var40) {
                        throw new MatchException(var40.toString(), var40);
                     }

                     var46 = var65;
                     var60 = var13;

                     try {
                        var67 = var60.flags();
                     } catch (Throwable var39) {
                        throw new MatchException(var39.toString(), var39);
                     }

                     List var49 = var67;
                     var60 = var13;

                     try {
                        var69 = var60.duration();
                     } catch (Throwable var38) {
                        throw new MatchException(var38.toString(), var38);
                     }

                     long var50 = var69;
                     var60 = var13;

                     try {
                        var71 = var60.isStatic();
                     } catch (Throwable var37) {
                        throw new MatchException(var37.toString(), var37);
                     }

                     boolean var51 = var71;
                     var7.add(new BossBarMessage(this.replaceMessagesColors(var46), var46, var46, var49, var50, var51));
                  } else if (var9 instanceof TitleMessage) {
                     TitleMessage var21 = (TitleMessage)var9;
                     TitleMessage var72 = var21;

                     try {
                        var73 = var72.title();
                     } catch (Throwable var36) {
                        throw new MatchException(var36.toString(), var36);
                     }

                     String var52 = var73;
                     var72 = var21;

                     try {
                        var75 = var72.subtitle();
                     } catch (Throwable var35) {
                        throw new MatchException(var35.toString(), var35);
                     }

                     var52 = var75;
                     var72 = var21;

                     try {
                        var77 = var72.start();
                     } catch (Throwable var34) {
                        throw new MatchException(var34.toString(), var34);
                     }

                     long var54 = var77;
                     var72 = var21;

                     try {
                        var79 = var72.time();
                     } catch (Throwable var33) {
                        throw new MatchException(var33.toString(), var33);
                     }

                     var54 = var79;
                     var72 = var21;

                     try {
                        var81 = var72.end();
                     } catch (Throwable var32) {
                        throw new MatchException(var32.toString(), var32);
                     }

                     long var56 = var81;
                     var7.add(new TitleMessage(this.replaceMessagesColors(var52), this.replaceMessagesColors(var52), var54, var54, var56));
                  }
               }

               var5.setMessages(var7);
            }
         }
      }

   }

   private void loadMessages(YamlConfiguration var1) {
      this.loadedMessages.clear();

      for(String var3 : var1.getKeys(false)) {
         String var4 = var3.replace("-", "_").toUpperCase();

         try {
            Message var5 = Message.fromString(var4);
            if (var5 != null) {
               var5.setPlugin(this.plugin);
               ArrayList var6 = new ArrayList();
               List var7 = var1.getMapList(var3);
               if (!var7.isEmpty()) {
                  for(int var8 = 0; var8 != var7.size(); ++var8) {
                     String var9 = var3 + " and index " + (var8 + 1);
                     Map var10 = (Map)var7.get(var8);
                     MessageType var11 = var10.containsKey("type") ? MessageType.fromString((String)var10.get("type")) : MessageType.TCHAT;
                     if (var11 == null) {
                        var11 = MessageType.TCHAT;
                        this.plugin.getLogger().severe("Message type was not found for " + var9 + ", use TCHAT by default.");
                     }

                     if (var11 == MessageType.BOSSBAR) {
                        String var12 = this.replaceMessagesColors((String)this.getValue(var10, "text", var9, String.class, "Default text", true));
                        String var13 = (String)this.getValue(var10, "color", var9, String.class, "WHITE", false);
                        String var14 = (String)this.getValue(var10, "overlay", var9, String.class, "PROGRESS", false);
                        List var15 = (List)this.getValue(var10, "flags", var9, List.class, new ArrayList(), false);
                        long var16 = (Long)this.getValue(var10, "duration", var9, Long.class, 60L, false);
                        boolean var18 = (Boolean)this.getValue(var10, "static", var9, Boolean.class, false, false);
                        BossBarMessage var19 = new BossBarMessage(var12, var13, var14, var15, var16, var18);
                        if (var19.isValid(this.plugin)) {
                           var6.add(var19);
                        }
                     } else if (var11 == MessageType.TITLE) {
                        String var33 = this.replaceMessagesColors((String)this.getValue(var10, "title", var9, String.class, "Default title", true));
                        String var36 = this.replaceMessagesColors((String)this.getValue(var10, "subtitle", var9, String.class, "Default subtitle", true));
                        long var40 = (Long)this.getValue(var10, "start", var9, Long.class, 100L, false);
                        long var43 = (Long)this.getValue(var10, "time", var9, Long.class, 2800L, false);
                        long var45 = (Long)this.getValue(var10, "end", var9, Long.class, 100L, false);
                        TitleMessage var20 = new TitleMessage(var33, var36, var40, var43, var45);
                        var6.add(var20);
                     } else {
                        List var34 = this.getMessage(var10);
                        var34.removeIf(Objects::isNull);
                        if (var34.isEmpty()) {
                           this.plugin.getLogger().severe("Message is empty for " + var3 + " and index " + var8 + ", use default configuration.");
                        } else {
                           ClassicMessage var37 = new ClassicMessage(var11, (List)var34.stream().map(this::replaceMessagesColors).collect(Collectors.toList()));
                           var6.add(var37);
                        }
                     }
                  }
               } else if (var1.contains(var3 + ".type")) {
                  MessageType var22 = MessageType.fromString(var1.getString(var3 + ".type", "TCHAT"));
                  if (var22 == null) {
                     var22 = MessageType.TCHAT;
                     this.plugin.getLogger().severe("Message type was not found for " + var3 + ", use TCHAT by default.");
                  }

                  if (var22 == MessageType.TITLE) {
                     String var24 = this.replaceMessagesColors(var1.getString(var3 + ".title", "Default title"));
                     String var28 = this.replaceMessagesColors(var1.getString(var3 + ".subtitle", "Default subtitle"));
                     long var31 = var1.getLong(var3 + ".start", 100L);
                     long var38 = var1.getLong(var3 + ".time", 2800L);
                     long var41 = var1.getLong(var3 + ".end", 100L);
                     TitleMessage var17 = new TitleMessage(var24, var28, var31, var38, var41);
                     var6.add(var17);
                  } else if (var22 == MessageType.BOSSBAR) {
                     String var25 = this.replaceMessagesColors(var1.getString(var3 + ".text", "Default Text"));
                     String var29 = var1.getString("color", "WHITE");
                     String var32 = var1.getString("overlay", "PROGRESS");
                     List var35 = var1.getStringList("flags");
                     long var39 = var1.getLong("duration", 60L);
                     boolean var42 = var1.getBoolean("static", false);
                     BossBarMessage var44 = new BossBarMessage(var25, var29, var32, var35, var39, var42);
                     if (var44.isValid(this.plugin)) {
                        var6.add(var44);
                     }
                  } else {
                     List var26 = var1.getStringList(var3 + ".messages");
                     if (var26.isEmpty()) {
                        var26.add(this.replaceMessagesColors(var1.getString(var3 + ".message")));
                     } else {
                        var26 = (List)var26.stream().map(this::replaceMessagesColors).collect(Collectors.toList());
                     }

                     var26.removeIf(Objects::isNull);
                     if (var26.isEmpty()) {
                        this.plugin.getLogger().severe("Message is empty for " + var3 + ", use default configuration.");
                     } else {
                        ClassicMessage var30 = new ClassicMessage(var22, var26);
                        var6.add(var30);
                     }
                  }
               } else {
                  List var23 = var1.getStringList(var3);
                  if (var23.isEmpty()) {
                     var23.add(this.replaceMessagesColors(var1.getString(var3)));
                  } else {
                     var23 = (List)var23.stream().map(this::replaceMessagesColors).collect(Collectors.toList());
                  }

                  var23.removeIf(Objects::isNull);
                  if (var23.isEmpty()) {
                     this.plugin.getLogger().severe("Message is empty for " + var3 + ", use default configuration.");
                  } else {
                     ClassicMessage var27 = new ClassicMessage(MessageType.TCHAT, var23);
                     var6.add(var27);
                  }
               }

               var5.setMessages(var6);
               this.loadedMessages.add(var5);
            }
         } catch (Exception var21) {
            this.plugin.getLogger().log(Level.SEVERE, "Failed to load message " + var4 + ": " + var21.getMessage());
         }
      }

   }

   private String replaceMessagesColors(String var1) {
      return (String)this.plugin.getConfiguration().getMessageColors().stream().reduce(var1, (var0, var1x) -> var0.replace(var1x.key(), var1x.color()), (var0, var1x) -> var0);
   }

   private List<String> getMessage(Map<?, ?> var1) {
      ArrayList var2 = new ArrayList();

      for(String var6 : new String[]{"messages", "message"}) {
         Object var7 = var1.get(var6);
         if (var7 instanceof List) {
            for(Object var9 : (List)var7) {
               if (var9 != null) {
                  var2.add(var9.toString());
               }
            }
         } else if (var7 != null) {
            var2.add(var7.toString());
         }
      }

      return var2;
   }

   private <T> T getValue(Map<?, ?> var1, String var2, String var3, Class<T> var4, T var5, boolean var6) {
      if (var1.containsKey(var2)) {
         Object var7 = var1.get(var2);
         if (var7 == null) {
            return var5;
         }

         if (Number.class.isAssignableFrom(var4) && var7 instanceof Number) {
            Number var8 = (Number)var7;
            Object var9;
            if (var4 == Integer.class) {
               var9 = var8.intValue();
            } else if (var4 == Long.class) {
               var9 = var8.longValue();
            } else if (var4 == Double.class) {
               var9 = var8.doubleValue();
            } else {
               if (var4 != Float.class) {
                  this.plugin.getLogger().severe("Unsupported numeric type for the key " + var2 + " for the message " + var3);
                  return var5;
               }

               var9 = var8.floatValue();
            }

            return (T)var4.cast(var9);
         }

         if (var4 == String.class) {
            if (var7 instanceof String) {
               return (T)var4.cast(var7);
            }
         } else {
            if (var4 == Boolean.class && var7 instanceof Boolean) {
               return (T)var4.cast(var7);
            }

            if (var4.isInstance(var7)) {
               return (T)var4.cast(var7);
            }
         }

         this.plugin.getLogger().severe("Type mismatch for the key " + var2 + " for the message " + var3 + " (expected " + var4.getSimpleName() + ", got " + var7.getClass().getSimpleName() + ")");
      } else if (var6) {
         this.plugin.getLogger().severe("Unable to find the key " + var2 + " for the message " + var3);
      }

      return var5;
   }
}
