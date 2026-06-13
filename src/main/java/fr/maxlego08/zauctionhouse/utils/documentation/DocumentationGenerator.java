package fr.maxlego08.zauctionhouse.utils.documentation;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.command.VCommand;
import fr.maxlego08.zauctionhouse.placeholder.AutoPlaceholder;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings({"unchecked", "rawtypes"})
public class DocumentationGenerator {
   private final AuctionPlugin plugin;

   public DocumentationGenerator(AuctionPlugin var1) {
      this.plugin = var1;
   }

   public void generate(List<VCommand> var1, List<AutoPlaceholder> var2) {
      File var3 = new File(this.plugin.getDataFolder(), "docs");
      if (!var3.exists()) {
         var3.mkdirs();
      }

      try {
         this.generateCommands(var3, var1);
         this.generatePlaceholders(var3, var2);
      } catch (Exception var5) {
         this.plugin.getLogger().severe("Error while generating documentation: " + var5.getMessage());
      }

   }

   private void generateCommands(File var1, List<VCommand> var2) {
      ArrayList var3 = new ArrayList();
      var2.stream().filter((var0) -> var0.getParent() == null).sorted(Comparator.comparing(VCommand::getFirst)).forEach((var2x) -> {
         var3.add(var2x);
         var3.addAll(var2.stream().filter((var1) -> var1.getMainParent() == var2x).sorted(Comparator.comparing(VCommand::getFirst)).toList());
      });
      StringBuilder var4 = new StringBuilder();
      var4.append("| Command | Aliases | Permission | Description |\n");
      var4.append("|---------|---------|------------|-------------|\n");

      for(VCommand var6 : var3) {
         String var7 = var6.getSyntax();
         ArrayList var8 = new ArrayList(var6.getSubCommands());
         if (!var8.isEmpty()) {
            var8.removeFirst();
         }

         String var9 = (String)var8.stream().map((var0) -> "/" + var0).collect(Collectors.joining(", "));
         String var10 = var6.getPermission();
         String var11 = var6.getDescription();
         var11 = var11 == null ? "" : var11.replace("|", "\\|");
         var10 = var10 == null ? "" : var10;
         var4.append(String.format("| `%s` | %s | %s | %s |\n", var7, var9, var10, var11));
      }

      Path var12 = (new File(var1, "commands.md")).toPath();
      Files.writeString(var12, var4.toString());
   }

   public void generatePlaceholders(File var1, List<AutoPlaceholder> var2) {
      StringBuilder var3 = new StringBuilder();
      var3.append("| Placeholder | Description |\n");
      var3.append("|--------------|-------------|\n");
      var2.sort(Comparator.comparing(AutoPlaceholder::getStartWith));

      for(AutoPlaceholder var5 : var2) {
         String var6 = "%zauctionhouse_" + var5.getStartWith();
         if (!var5.getArgs().isEmpty()) {
            String var7 = (String)var5.getArgs().stream().map((var0) -> "<" + var0 + ">").collect(Collectors.joining("_"));
            var6 = var6 + var7;
         }

         var6 = var6 + "%";
         String var10 = var5.getDescription().replace("|", "\\|");
         var3.append(String.format("| `%s` | %s |\n", var6, var10));
      }

      File var8 = new File(var1, "placeholders.md");
      Files.writeString(var8.toPath(), var3.toString());
   }
}
