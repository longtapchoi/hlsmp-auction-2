package fr.maxlego08.zauctionhouse.utils.yaml;

import java.util.ArrayList;
import java.util.List;

public class YamlKey {
   private final String fullPath;
   private final String key;
   private final int indentLevel;
   private final List<String> precedingComments;
   private final String inlineComment;
   private String rawLine;
   private Object value;
   private boolean isList;

   public YamlKey(String var1, String var2, int var3) {
      this.fullPath = var1;
      this.key = var2;
      this.indentLevel = var3;
      this.precedingComments = new ArrayList();
      this.inlineComment = null;
      this.isList = false;
   }

   public YamlKey(String var1, String var2, int var3, String var4) {
      this.fullPath = var1;
      this.key = var2;
      this.indentLevel = var3;
      this.precedingComments = new ArrayList();
      this.inlineComment = var4;
      this.isList = false;
   }

   public String getFullPath() {
      return this.fullPath;
   }

   public String getKey() {
      return this.key;
   }

   public int getIndentLevel() {
      return this.indentLevel;
   }

   public List<String> getPrecedingComments() {
      return this.precedingComments;
   }

   public void addPrecedingComment(String var1) {
      this.precedingComments.add(var1);
   }

   public String getInlineComment() {
      return this.inlineComment;
   }

   public String getRawLine() {
      return this.rawLine;
   }

   public void setRawLine(String var1) {
      this.rawLine = var1;
   }

   public Object getValue() {
      return this.value;
   }

   public void setValue(Object var1) {
      this.value = var1;
   }

   public boolean isList() {
      return this.isList;
   }

   public void setList(boolean var1) {
      this.isList = var1;
   }

   public boolean isSection() {
      return this.value == null && !this.isList;
   }

   public String toString() {
      return "YamlKey{fullPath='" + this.fullPath + "', key='" + this.key + "', indent=" + this.indentLevel + "}";
   }
}
