package fr.maxlego08.zauctionhouse.utils.yaml;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

@SuppressWarnings({"unchecked", "rawtypes"})
public class YamlUpdater {
   private static final Pattern KEY_PATTERN = Pattern.compile("^(\\s*)([^:#\\s][^:]*?):\\s*(.*)$");
   private static final Pattern LIST_ITEM_PATTERN = Pattern.compile("^(\\s*)-\\s*(.*)$");
   private final Plugin plugin;

   public YamlUpdater(Plugin var1) {
      this.plugin = var1;
   }

   public boolean update(String var1, String var2) {
      File var3 = new File(this.plugin.getDataFolder(), var2);
      if (!var3.exists()) {
         this.plugin.saveResource(var1, false);
         return true;
      } else {
         try {
            InputStream var4 = this.plugin.getResource(var1);
            if (var4 == null) {
               this.plugin.getLogger().severe("Cannot find resource: " + var1);
               return false;
            } else {
               List var5 = this.readLines(var4);
               List var6 = this.readLines(var3);
               YamlConfiguration var7 = this.loadConfigFromLines(var5);
               YamlConfiguration var8 = new YamlConfiguration();

               try {
                  var8.load(var3);
               } catch (InvalidConfigurationException var11) {
                  this.plugin.getLogger().warning(var2 + " is corrupted and cannot be parsed. Backing up and replacing with defaults.");
                  this.backupAndReplace(var3, var5);
                  return true;
               }

               if (var8.getKeys(false).isEmpty() && var3.length() > 0L) {
                  this.plugin.getLogger().warning(var2 + " has content but no valid YAML keys. Backing up and replacing with defaults.");
                  this.backupAndReplace(var3, var5);
                  return true;
               } else {
                  Set var9 = this.findMissingKeys(var7, var8);
                  if (var9.isEmpty()) {
                     return false;
                  } else {
                     Logger var10000 = this.plugin.getLogger();
                     int var10001 = var9.size();
                     var10000.info("Found " + var10001 + " missing key(s) in " + var2);
                     List var10 = this.mergeConfigurations(var5, var6, var9, var7);
                     this.writeLines(var3, var10);
                     this.plugin.getLogger().info("Successfully updated " + var2 + " with new keys while preserving comments.");
                     return true;
                  }
               }
            }
         } catch (Exception var12) {
            this.plugin.getLogger().log(Level.SEVERE, "Error updating " + var2, var12);
            return false;
         }
      }
   }

   private void backupAndReplace(File var1, List<String> var2) {
      File var3 = new File(var1.getParentFile(), var1.getName() + ".backup");
      if (var3.exists()) {
         var3.delete();
      }

      Files.copy(var1.toPath(), var3.toPath());
      this.writeLines(var1, var2);
      this.plugin.getLogger().info("Corrupted file backed up as " + var3.getName() + " and replaced with defaults.");
   }

   private Set<String> findMissingKeys(YamlConfiguration var1, YamlConfiguration var2) {
      LinkedHashSet var3 = new LinkedHashSet();
      this.collectMissingKeys(var1, var2, "", var3);
      return var3;
   }

   private void collectMissingKeys(YamlConfiguration var1, YamlConfiguration var2, String var3, Set<String> var4) {
      for(String var7 : var3.isEmpty() ? var1.getKeys(false) : (var1.getConfigurationSection(var3) != null ? var1.getConfigurationSection(var3).getKeys(false) : Collections.emptySet())) {
         String var8 = var3.isEmpty() ? var7 : var3 + "." + var7;
         if (!var2.contains(var8)) {
            var4.add(var8);
         } else if (var1.isConfigurationSection(var8)) {
            this.collectMissingKeys(var1, var2, var8, var4);
         }
      }

   }

   private List<String> mergeConfigurations(List<String> var1, List<String> var2, Set<String> var3, YamlConfiguration var4) {
      Map var5 = this.parseKeyBlocks(var1);
      ArrayList var6 = new ArrayList(var2);
      Map var7 = this.groupKeysByParent(var3);

      for(Map.Entry var9 : var7.entrySet()) {
         String var10 = (String)var9.getKey();

         for(String var13 : (Set)var9.getValue()) {
            KeyBlock var14 = (KeyBlock)var5.get(var13);
            if (var14 == null) {
               var14 = this.createKeyBlock(var13, var4, var1);
            }

            if (var14 != null) {
               int var15 = this.findInsertPosition(var6, var10, var13, var4);
               var6.addAll(var15, var14.lines);
            }
         }
      }

      return var6;
   }

   private Map<String, Set<String>> groupKeysByParent(Set<String> var1) {
      LinkedHashMap var2 = new LinkedHashMap();

      for(String var4 : var1) {
         String var5 = var4.contains(".") ? var4.substring(0, var4.lastIndexOf(46)) : "";
         ((Set)var2.computeIfAbsent(var5, (var0) -> new LinkedHashSet())).add(var4);
      }

      return var2;
   }

   private int findInsertPosition(List<String> var1, String var2, String var3, YamlConfiguration var4) {
      String var5 = var3.contains(".") ? var3.substring(var3.lastIndexOf(46) + 1) : var3;
      int var6 = (int)var3.chars().filter((var0) -> var0 == 46).count() * 2;
      if (var2.isEmpty()) {
         return this.findPositionAtRootLevel(var1, var5, var4);
      } else {
         int var7 = this.findKeyLine(var1, var2);
         if (var7 == -1) {
            return var1.size();
         } else {
            int var8 = this.findSectionEnd(var1, var7, var6);
            return var8;
         }
      }
   }

   private int findPositionAtRootLevel(List<String> var1, String var2, YamlConfiguration var3) {
      ArrayList var4 = new ArrayList(var3.getKeys(false));
      int var5 = var4.indexOf(var2);
      if (var5 > 0) {
         String var6 = (String)var4.get(var5 - 1);
         int var7 = this.findKeyLine(var1, var6);
         if (var7 != -1) {
            return this.findSectionEnd(var1, var7, 0);
         }
      }

      return var1.size();
   }

   private int findKeyLine(List<String> var1, String var2) {
      String[] var3 = var2.split("\\.");
      int var4 = 0;
      int var5 = 0;

      for(String var9 : var3) {
         boolean var10 = false;

         for(int var11 = var4; var11 < var1.size(); ++var11) {
            String var12 = (String)var1.get(var11);
            Matcher var13 = KEY_PATTERN.matcher(var12);
            if (var13.matches()) {
               int var14 = var13.group(1).length();
               String var15 = var13.group(2).trim();
               if (var14 == var5 && var15.equals(var9)) {
                  var4 = var11;
                  var5 += 2;
                  var10 = true;
                  break;
               }
            }
         }

         if (!var10) {
            return -1;
         }
      }

      return var4;
   }

   private int findSectionEnd(List<String> var1, int var2, int var3) {
      for(int var4 = var2 + 1; var4 < var1.size(); ++var4) {
         String var5 = (String)var1.get(var4);
         if (!var5.trim().isEmpty() && !var5.trim().startsWith("#")) {
            Matcher var6 = KEY_PATTERN.matcher(var5);
            Matcher var7 = LIST_ITEM_PATTERN.matcher(var5);
            int var8;
            if (var6.matches()) {
               var8 = var6.group(1).length();
            } else {
               if (!var7.matches()) {
                  continue;
               }

               var8 = var7.group(1).length();
            }

            if (var8 <= var3) {
               return var4;
            }
         }
      }

      return var1.size();
   }

   private Map<String, KeyBlock> parseKeyBlocks(List<String> var1) {
      LinkedHashMap var2 = new LinkedHashMap();
      ArrayList var3 = new ArrayList();
      ArrayDeque var4 = new ArrayDeque();
      ArrayDeque var5 = new ArrayDeque();

      for(int var6 = 0; var6 < var1.size(); ++var6) {
         String var7 = (String)var1.get(var6);
         if (!var7.trim().isEmpty() && !var7.trim().startsWith("#")) {
            Matcher var8 = KEY_PATTERN.matcher(var7);
            if (var8.matches()) {
               int var9 = var8.group(1).length();
               String var10 = var8.group(2).trim();

               while(!var5.isEmpty() && (Integer)var5.peek() >= var9) {
                  var5.pop();
                  var4.pop();
               }

               var4.push(var10);
               var5.push(var9);
               String var11 = this.buildPath(var4);
               KeyBlock var12 = new KeyBlock();
               var12.lines.addAll(var3);
               var12.lines.add(var7);
               var12.startLine = var6 - var3.size();
               int var13 = this.findKeyBlockEnd(var1, var6, var9);

               for(int var14 = var6 + 1; var14 < var13; ++var14) {
                  var12.lines.add((String)var1.get(var14));
               }

               while(var12.lines.size() > 1) {
                  String var15 = (String)var12.lines.get(var12.lines.size() - 1);
                  if (!var15.trim().isEmpty() && !var15.trim().startsWith("#")) {
                     break;
                  }

                  var12.lines.remove(var12.lines.size() - 1);
               }

               var2.put(var11, var12);
               var3.clear();
            }
         } else {
            var3.add(var7);
         }
      }

      return var2;
   }

   private String buildPath(Deque<String> var1) {
      ArrayList var2 = new ArrayList(var1);
      Collections.reverse(var2);
      return String.join(".", var2);
   }

   private int findKeyBlockEnd(List<String> var1, int var2, int var3) {
      for(int var4 = var2 + 1; var4 < var1.size(); ++var4) {
         String var5 = (String)var1.get(var4);
         if (!var5.trim().isEmpty() && !var5.trim().startsWith("#")) {
            Matcher var6 = KEY_PATTERN.matcher(var5);
            if (var6.matches()) {
               int var7 = var6.group(1).length();
               if (var7 <= var3) {
                  return var4;
               }
            }
         }
      }

      return var1.size();
   }

   private KeyBlock createKeyBlock(String var1, YamlConfiguration var2, List<String> var3) {
      KeyBlock var4 = new KeyBlock();
      int var5 = (int)var1.chars().filter((var0) -> var0 == 46).count() * 2;
      String var6 = " ".repeat(var5);
      String var7 = var1.contains(".") ? var1.substring(var1.lastIndexOf(46) + 1) : var1;
      int var8 = this.findKeyLineInDefault(var3, var1);
      if (var8 != -1) {
         for(String var11 : this.extractPrecedingComments(var3, var8)) {
            if (var5 > 0 && !var11.trim().isEmpty()) {
               var4.lines.add(var6 + var11.trim());
            } else {
               var4.lines.add(var11);
            }
         }
      }

      Object var12 = var2.get(var1);
      if (var2.isConfigurationSection(var1)) {
         var4.lines.add(var6 + var7 + ":");
      } else if (var12 instanceof List) {
         var4.lines.add(var6 + var7 + ":");

         for(Object var14 : (List)var12) {
            var4.lines.add(var6 + "  - " + this.formatValue(var14));
         }
      } else {
         var4.lines.add(var6 + var7 + ": " + this.formatValue(var12));
      }

      if (!var4.lines.isEmpty() && !((String)var4.lines.get(0)).trim().isEmpty()) {
         var4.lines.add(0, "");
      }

      return var4;
   }

   private int findKeyLineInDefault(List<String> var1, String var2) {
      String[] var3 = var2.split("\\.");
      int var4 = 0;
      int var5 = 0;

      for(String var9 : var3) {
         for(int var10 = var4; var10 < var1.size(); ++var10) {
            String var11 = (String)var1.get(var10);
            Matcher var12 = KEY_PATTERN.matcher(var11);
            if (var12.matches()) {
               int var13 = var12.group(1).length();
               String var14 = var12.group(2).trim();
               if (var13 == var5 && var14.equals(var9)) {
                  var4 = var10;
                  var5 += 2;
                  break;
               }
            }
         }
      }

      return var4;
   }

   private List<String> extractPrecedingComments(List<String> var1, int var2) {
      ArrayList var3 = new ArrayList();

      for(int var4 = var2 - 1; var4 >= 0; --var4) {
         String var5 = (String)var1.get(var4);
         if (!var5.trim().startsWith("#") && !var5.trim().isEmpty()) {
            break;
         }

         var3.add(0, var5);
      }

      while(!var3.isEmpty() && ((String)var3.get(0)).trim().isEmpty()) {
         var3.remove(0);
      }

      return var3;
   }

   private String formatValue(Object var1) {
      if (var1 == null) {
         return "null";
      } else if (var1 instanceof String) {
         String var2 = (String)var1;
         if (var2.isEmpty()) {
            return "''";
         } else {
            return this.needsQuoting(var2) ? "'" + var2.replace("'", "''") + "'" : var2;
         }
      } else if (!(var1 instanceof Boolean) && !(var1 instanceof Number)) {
         String var10000 = var1.toString();
         return "'" + var10000.replace("'", "''") + "'";
      } else {
         return var1.toString();
      }
   }

   private boolean needsQuoting(String var1) {
      if (!var1.startsWith(" ") && !var1.endsWith(" ")) {
         if (!var1.contains(":") && !var1.contains("#") && !var1.contains("'") && !var1.contains("\"")) {
            if (!var1.contains("\n") && !var1.contains("\r")) {
               String var2 = var1.toLowerCase();
               if (!var2.equals("true") && !var2.equals("false") && !var2.equals("null") && !var2.equals("yes") && !var2.equals("no") && !var2.equals("on") && !var2.equals("off")) {
                  try {
                     Double.parseDouble(var1);
                     return true;
                  } catch (NumberFormatException var4) {
                     return false;
                  }
               } else {
                  return true;
               }
            } else {
               return true;
            }
         } else {
            return true;
         }
      } else {
         return true;
      }
   }

   private List<String> readLines(InputStream var1) {
      ArrayList var2 = new ArrayList();
      BufferedReader var3 = new BufferedReader(new InputStreamReader(var1, StandardCharsets.UTF_8));

      String var4;
      try {
         while((var4 = var3.readLine()) != null) {
            var2.add(var4);
         }
      } catch (Throwable var7) {
         try {
            var3.close();
         } catch (Throwable var6) {
            var7.addSuppressed(var6);
         }

         throw var7;
      }

      var3.close();
      return var2;
   }

   private List<String> readLines(File var1) {
      return Files.readAllLines(var1.toPath(), StandardCharsets.UTF_8);
   }

   private void writeLines(File var1, List<String> var2) {
      Files.write(var1.toPath(), var2, StandardCharsets.UTF_8);
   }

   private YamlConfiguration loadConfigFromLines(List<String> var1) {
      String var2 = String.join("\n", var1);
      return YamlConfiguration.loadConfiguration(new StringReader(var2));
   }

   private static class KeyBlock {
      List<String> lines = new ArrayList();
      int startLine;
   }
}
