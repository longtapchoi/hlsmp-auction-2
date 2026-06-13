package fr.maxlego08.zauctionhouse.utils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

@SuppressWarnings({"unchecked", "rawtypes"})
public class LocaleHelper {
   private static final Map<String, String> COUNTRY_TO_LANGUAGE = new HashMap();
   private final Logger logger;
   private final String configuredLanguage;
   private final String detectedLanguage;

   public LocaleHelper(Logger var1, String var2) {
      this.logger = var1;
      this.configuredLanguage = var2;
      this.detectedLanguage = this.detectLanguage();
   }

   private String detectLanguage() {
      Locale var1 = Locale.getDefault();
      String var2 = var1.getLanguage();
      String var3 = var1.getCountry();
      this.logger.info("System locale detected: " + var1.toLanguageTag());
      this.logger.info("  - Language code: " + var2);
      this.logger.info("  - Country code: " + var3);
      if (this.configuredLanguage != null && !this.configuredLanguage.isEmpty() && !this.configuredLanguage.equalsIgnoreCase("auto")) {
         this.logger.info("Using manually configured language: " + this.configuredLanguage);
         return this.configuredLanguage.toLowerCase();
      } else {
         if (var3 != null && !var3.isEmpty()) {
            String var4 = (String)COUNTRY_TO_LANGUAGE.get(var3.toUpperCase());
            if (var4 != null) {
               this.logger.info("Language mapped from country " + var3 + " -> " + var4);
               return var4;
            }
         }

         this.logger.info("Using system language: " + var2);
         return var2;
      }
   }

   public String getLanguage() {
      return this.detectedLanguage;
   }

   public static String getLanguageForCountry(String var0) {
      return (String)COUNTRY_TO_LANGUAGE.get(var0.toUpperCase());
   }

   public static boolean hasMapping(String var0) {
      return COUNTRY_TO_LANGUAGE.containsKey(var0.toUpperCase());
   }

   public static Map<String, String> getAllMappings() {
      return new HashMap(COUNTRY_TO_LANGUAGE);
   }

   static {
      COUNTRY_TO_LANGUAGE.put("ES", "es");
      COUNTRY_TO_LANGUAGE.put("MX", "es");
      COUNTRY_TO_LANGUAGE.put("AR", "es");
      COUNTRY_TO_LANGUAGE.put("CL", "es");
      COUNTRY_TO_LANGUAGE.put("CO", "es");
      COUNTRY_TO_LANGUAGE.put("PE", "es");
      COUNTRY_TO_LANGUAGE.put("VE", "es");
      COUNTRY_TO_LANGUAGE.put("EC", "es");
      COUNTRY_TO_LANGUAGE.put("GT", "es");
      COUNTRY_TO_LANGUAGE.put("CU", "es");
      COUNTRY_TO_LANGUAGE.put("BO", "es");
      COUNTRY_TO_LANGUAGE.put("DO", "es");
      COUNTRY_TO_LANGUAGE.put("HN", "es");
      COUNTRY_TO_LANGUAGE.put("PY", "es");
      COUNTRY_TO_LANGUAGE.put("SV", "es");
      COUNTRY_TO_LANGUAGE.put("NI", "es");
      COUNTRY_TO_LANGUAGE.put("CR", "es");
      COUNTRY_TO_LANGUAGE.put("PA", "es");
      COUNTRY_TO_LANGUAGE.put("UY", "es");
      COUNTRY_TO_LANGUAGE.put("PR", "es");
      COUNTRY_TO_LANGUAGE.put("GQ", "es");
      COUNTRY_TO_LANGUAGE.put("FR", "fr");
      COUNTRY_TO_LANGUAGE.put("BE", "fr");
      COUNTRY_TO_LANGUAGE.put("CH", "fr");
      COUNTRY_TO_LANGUAGE.put("CA", "fr");
      COUNTRY_TO_LANGUAGE.put("LU", "fr");
      COUNTRY_TO_LANGUAGE.put("MC", "fr");
      COUNTRY_TO_LANGUAGE.put("SN", "fr");
      COUNTRY_TO_LANGUAGE.put("CI", "fr");
      COUNTRY_TO_LANGUAGE.put("ML", "fr");
      COUNTRY_TO_LANGUAGE.put("BF", "fr");
      COUNTRY_TO_LANGUAGE.put("NE", "fr");
      COUNTRY_TO_LANGUAGE.put("TG", "fr");
      COUNTRY_TO_LANGUAGE.put("BJ", "fr");
      COUNTRY_TO_LANGUAGE.put("GN", "fr");
      COUNTRY_TO_LANGUAGE.put("CD", "fr");
      COUNTRY_TO_LANGUAGE.put("CG", "fr");
      COUNTRY_TO_LANGUAGE.put("GA", "fr");
      COUNTRY_TO_LANGUAGE.put("CM", "fr");
      COUNTRY_TO_LANGUAGE.put("MG", "fr");
      COUNTRY_TO_LANGUAGE.put("HT", "fr");
      COUNTRY_TO_LANGUAGE.put("RW", "fr");
      COUNTRY_TO_LANGUAGE.put("BI", "fr");
      COUNTRY_TO_LANGUAGE.put("DJ", "fr");
      COUNTRY_TO_LANGUAGE.put("KM", "fr");
      COUNTRY_TO_LANGUAGE.put("MU", "fr");
      COUNTRY_TO_LANGUAGE.put("SC", "fr");
      COUNTRY_TO_LANGUAGE.put("RE", "fr");
      COUNTRY_TO_LANGUAGE.put("GP", "fr");
      COUNTRY_TO_LANGUAGE.put("MQ", "fr");
      COUNTRY_TO_LANGUAGE.put("GF", "fr");
      COUNTRY_TO_LANGUAGE.put("PF", "fr");
      COUNTRY_TO_LANGUAGE.put("NC", "fr");
      COUNTRY_TO_LANGUAGE.put("IT", "it");
      COUNTRY_TO_LANGUAGE.put("SM", "it");
      COUNTRY_TO_LANGUAGE.put("VA", "it");
      COUNTRY_TO_LANGUAGE.put("PT", "pt");
      COUNTRY_TO_LANGUAGE.put("BR", "pt");
      COUNTRY_TO_LANGUAGE.put("AO", "pt");
      COUNTRY_TO_LANGUAGE.put("MZ", "pt");
      COUNTRY_TO_LANGUAGE.put("GW", "pt");
      COUNTRY_TO_LANGUAGE.put("CV", "pt");
      COUNTRY_TO_LANGUAGE.put("ST", "pt");
      COUNTRY_TO_LANGUAGE.put("TL", "pt");
      COUNTRY_TO_LANGUAGE.put("DE", "de");
      COUNTRY_TO_LANGUAGE.put("AT", "de");
      COUNTRY_TO_LANGUAGE.put("LI", "de");
      COUNTRY_TO_LANGUAGE.put("CN", "zh");
      COUNTRY_TO_LANGUAGE.put("TW", "zh");
      COUNTRY_TO_LANGUAGE.put("HK", "zh");
      COUNTRY_TO_LANGUAGE.put("MO", "zh");
      COUNTRY_TO_LANGUAGE.put("SG", "zh");
      COUNTRY_TO_LANGUAGE.put("RU", "ru");
      COUNTRY_TO_LANGUAGE.put("BY", "ru");
      COUNTRY_TO_LANGUAGE.put("KZ", "ru");
      COUNTRY_TO_LANGUAGE.put("KG", "ru");
      COUNTRY_TO_LANGUAGE.put("JP", "ja");
      COUNTRY_TO_LANGUAGE.put("KR", "ko");
      COUNTRY_TO_LANGUAGE.put("KP", "ko");
      COUNTRY_TO_LANGUAGE.put("PL", "pl");
      COUNTRY_TO_LANGUAGE.put("TR", "tr");
      COUNTRY_TO_LANGUAGE.put("NL", "nl");
      COUNTRY_TO_LANGUAGE.put("SR", "nl");
      COUNTRY_TO_LANGUAGE.put("TH", "th");
   }
}
