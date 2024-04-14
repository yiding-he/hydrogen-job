package com.hyd.job.server.i18n;

import java.util.Locale;
import java.util.function.Consumer;

class CSVMixedResourceBundleTest {

  public static void main(String[] args) {
    var bundle = CSVMixedResourceBundle.fromResourcePath("/i18n/messages.csv");
    Consumer<String> outputKey = key -> System.out.println(key + " = " + bundle.getString(key));

    bundle.setLocale(Locale.SIMPLIFIED_CHINESE);
    System.out.println("bundle.getLocale() = " + bundle.getLocale());
    outputKey.accept("test");
    outputKey.accept("user_username");

    bundle.setLocale(Locale.TRADITIONAL_CHINESE);
    System.out.println("bundle.getLocale() = " + bundle.getLocale());
    outputKey.accept("test");
    outputKey.accept("user_username");
  }
}
