package fr.maxlego08.zauctionhouse.api.utils;

public final class Base64 {
   private static final int BASELENGTH = 128;
   private static final int LOOKUPLENGTH = 64;
   private static final int TWENTYFOURBITGROUP = 24;
   private static final int EIGHTBIT = 8;
   private static final int SIXTEENBIT = 16;
   private static final int FOURBYTE = 4;
   private static final int SIGN = -128;
   private static final char PAD = '=';
   private static final boolean fDebug = false;
   private static final byte[] base64Alphabet = new byte[128];
   private static final char[] lookUpBase64Alphabet = new char[64];

   protected static boolean isWhiteSpace(char var0) {
      return var0 == ' ' || var0 == '\r' || var0 == '\n' || var0 == '\t';
   }

   protected static boolean isPad(char var0) {
      return var0 == '=';
   }

   protected static boolean isData(char var0) {
      return var0 < 128 && base64Alphabet[var0] != -1;
   }

   protected static boolean isBase64(char var0) {
      return isWhiteSpace(var0) || isPad(var0) || isData(var0);
   }

   public static String encode(byte[] var0) {
      if (var0 == null) {
         return null;
      } else {
         int var1 = var0.length * 8;
         if (var1 == 0) {
            return "";
         } else {
            int var2 = var1 % 24;
            int var3 = var1 / 24;
            int var4 = var2 != 0 ? var3 + 1 : var3;
            Object var5 = null;
            char[] var17 = new char[var4 * 4];
            byte var6 = 0;
            byte var7 = 0;
            byte var8 = 0;
            byte var9 = 0;
            byte var10 = 0;
            int var11 = 0;
            int var12 = 0;

            for(int var13 = 0; var13 < var3; ++var13) {
               var8 = var0[var12++];
               var9 = var0[var12++];
               var10 = var0[var12++];
               var7 = (byte)(var9 & 15);
               var6 = (byte)(var8 & 3);
               byte var14 = (var8 & -128) == 0 ? (byte)(var8 >> 2) : (byte)(var8 >> 2 ^ 192);
               byte var15 = (var9 & -128) == 0 ? (byte)(var9 >> 4) : (byte)(var9 >> 4 ^ 240);
               byte var16 = (var10 & -128) == 0 ? (byte)(var10 >> 6) : (byte)(var10 >> 6 ^ 252);
               var17[var11++] = lookUpBase64Alphabet[var14];
               var17[var11++] = lookUpBase64Alphabet[var15 | var6 << 4];
               var17[var11++] = lookUpBase64Alphabet[var7 << 2 | var16];
               var17[var11++] = lookUpBase64Alphabet[var10 & 63];
            }

            if (var2 == 8) {
               var8 = var0[var12];
               var6 = (byte)(var8 & 3);
               byte var42 = (var8 & -128) == 0 ? (byte)(var8 >> 2) : (byte)(var8 >> 2 ^ 192);
               var17[var11++] = lookUpBase64Alphabet[var42];
               var17[var11++] = lookUpBase64Alphabet[var6 << 4];
               var17[var11++] = '=';
               var17[var11++] = '=';
            } else if (var2 == 16) {
               var8 = var0[var12];
               var9 = var0[var12 + 1];
               var7 = (byte)(var9 & 15);
               var6 = (byte)(var8 & 3);
               byte var43 = (var8 & -128) == 0 ? (byte)(var8 >> 2) : (byte)(var8 >> 2 ^ 192);
               byte var44 = (var9 & -128) == 0 ? (byte)(var9 >> 4) : (byte)(var9 >> 4 ^ 240);
               var17[var11++] = lookUpBase64Alphabet[var43];
               var17[var11++] = lookUpBase64Alphabet[var44 | var6 << 4];
               var17[var11++] = lookUpBase64Alphabet[var7 << 2];
               var17[var11++] = '=';
            }

            return new String(var17);
         }
      }
   }

   public static byte[] decode(String var0) {
      if (var0 == null) {
         return null;
      } else {
         char[] var1 = var0.toCharArray();
         int var2 = removeWhiteSpace(var1);
         if (var2 % 4 != 0) {
            return null;
         } else {
            int var3 = var2 / 4;
            if (var3 == 0) {
               return new byte[0];
            } else {
               Object var4 = null;
               byte var5 = 0;
               byte var6 = 0;
               byte var7 = 0;
               byte var8 = 0;
               char var9 = '\u0000';
               char var10 = '\u0000';
               char var11 = '\u0000';
               char var12 = '\u0000';
               int var13 = 0;
               int var14 = 0;
               int var15 = 0;

               byte[] var17;
               for(var17 = new byte[var3 * 3]; var13 < var3 - 1; ++var13) {
                  if (!isData(var9 = var1[var15++]) || !isData(var10 = var1[var15++]) || !isData(var11 = var1[var15++]) || !isData(var12 = var1[var15++])) {
                     return null;
                  }

                  var5 = base64Alphabet[var9];
                  var6 = base64Alphabet[var10];
                  var7 = base64Alphabet[var11];
                  var8 = base64Alphabet[var12];
                  var17[var14++] = (byte)(var5 << 2 | var6 >> 4);
                  var17[var14++] = (byte)((var6 & 15) << 4 | var7 >> 2 & 15);
                  var17[var14++] = (byte)(var7 << 6 | var8);
               }

               if (isData(var9 = var1[var15++]) && isData(var10 = var1[var15++])) {
                  var5 = base64Alphabet[var9];
                  var6 = base64Alphabet[var10];
                  var11 = var1[var15++];
                  var12 = var1[var15++];
                  if (isData(var11) && isData(var12)) {
                     var7 = base64Alphabet[var11];
                     var8 = base64Alphabet[var12];
                     var17[var14++] = (byte)(var5 << 2 | var6 >> 4);
                     var17[var14++] = (byte)((var6 & 15) << 4 | var7 >> 2 & 15);
                     var17[var14++] = (byte)(var7 << 6 | var8);
                     return var17;
                  } else if (isPad(var11) && isPad(var12)) {
                     if ((var6 & 15) != 0) {
                        return null;
                     } else {
                        byte[] var48 = new byte[var13 * 3 + 1];
                        System.arraycopy(var17, 0, var48, 0, var13 * 3);
                        var48[var14] = (byte)(var5 << 2 | var6 >> 4);
                        return var48;
                     }
                  } else if (!isPad(var11) && isPad(var12)) {
                     var7 = base64Alphabet[var11];
                     if ((var7 & 3) != 0) {
                        return null;
                     } else {
                        byte[] var16 = new byte[var13 * 3 + 2];
                        System.arraycopy(var17, 0, var16, 0, var13 * 3);
                        var16[var14++] = (byte)(var5 << 2 | var6 >> 4);
                        var16[var14] = (byte)((var6 & 15) << 4 | var7 >> 2 & 15);
                        return var16;
                     }
                  } else {
                     return null;
                  }
               } else {
                  return null;
               }
            }
         }
      }
   }

   protected static int removeWhiteSpace(char[] var0) {
      if (var0 == null) {
         return 0;
      } else {
         int var1 = 0;
         int var2 = var0.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            if (!isWhiteSpace(var0[var3])) {
               var0[var1++] = var0[var3];
            }
         }

         return var1;
      }
   }

   static {
      for(int var0 = 0; var0 < 128; ++var0) {
         base64Alphabet[var0] = -1;
      }

      for(int var2 = 90; var2 >= 65; --var2) {
         base64Alphabet[var2] = (byte)(var2 - 65);
      }

      for(int var3 = 122; var3 >= 97; --var3) {
         base64Alphabet[var3] = (byte)(var3 - 97 + 26);
      }

      for(int var4 = 57; var4 >= 48; --var4) {
         base64Alphabet[var4] = (byte)(var4 - 48 + 52);
      }

      base64Alphabet[43] = 62;
      base64Alphabet[47] = 63;

      for(int var5 = 0; var5 <= 25; ++var5) {
         lookUpBase64Alphabet[var5] = (char)(65 + var5);
      }

      int var6 = 26;

      for(int var1 = 0; var6 <= 51; ++var1) {
         lookUpBase64Alphabet[var6] = (char)(97 + var1);
         ++var6;
      }

      var6 = 52;

      for(int var8 = 0; var6 <= 61; ++var8) {
         lookUpBase64Alphabet[var6] = (char)(48 + var8);
         ++var6;
      }

      lookUpBase64Alphabet[62] = '+';
      lookUpBase64Alphabet[63] = '/';
   }
}
