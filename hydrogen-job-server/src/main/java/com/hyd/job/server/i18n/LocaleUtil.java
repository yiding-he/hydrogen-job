package com.hyd.job.server.i18n;

import java.util.Locale;

public class LocaleUtil {

  public static final Locale[] AVAILABLE_LOCALES = Locale.getAvailableLocales();

  /**
   * 根据语言编号查询系统预定义的 Locale 常量值，忽略大小写
   */
  public static Locale find(String language) {
    var finalLanguage = language.toLowerCase();
    Locale candidate = null;
    for (Locale locale : AVAILABLE_LOCALES) {
      String name = locale.toString().toLowerCase();
      if (name.equals(finalLanguage)) {
        return locale;
      }
      if (name.startsWith(finalLanguage)) {
        candidate = locale;
      }
    }
    return candidate;
  }
}
