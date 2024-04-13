package com.hyd.job.server.utilities;

import java.util.Locale;
import java.util.ResourceBundle;

class I18nUtilTest {

  public static void main(String[] args) {
    var bundleEn = ResourceBundle.getBundle("i18n/message", Locale.ENGLISH);
    var bundleZhCn = ResourceBundle.getBundle("i18n/message", Locale.CHINA);
    var bundleZhTc = ResourceBundle.getBundle("i18n/message", Locale.TRADITIONAL_CHINESE);
    bundleEn.keySet().forEach(key -> System.out.println(
      "\"" + key + "\",\"" +
      bundleEn.getString(key) + "\",\"" +
      bundleZhCn.getString(key) + "\",\"" +
      bundleZhTc.getString(key) + "\""
    ));
  }
}
