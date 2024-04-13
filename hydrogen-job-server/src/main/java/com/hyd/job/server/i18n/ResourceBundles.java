package com.hyd.job.server.i18n;

import com.hyd.job.server.utilities.CSVReader;

import java.util.HashMap;
import java.util.ListResourceBundle;
import java.util.Map;
import java.util.ResourceBundle;

public class ResourceBundles {

  private static final Map<String, ResourceBundle> bundles = new HashMap<>();

  static {
    var csvData = CSVReader.read("/i18n/messages.csv");
    var localeDataMap = new HashMap<String, String[][]>();
    var rowSize = csvData.getRows().size();
    csvData.getHeaders().stream().filter(h -> !h.equals("key")).forEach(locale -> {
      var localeData = localeDataMap.computeIfAbsent(locale, l -> new String[rowSize][2]);
      for (int i = 0; i < csvData.getRows().size(); i++) {
        Map<String, String> row = csvData.getRows().get(i);
        var key = row.get("key");
        var value = row.get(locale);
        localeData[i][0] = key;
        localeData[i][1] = value;
      }
    });
    for (var locale : localeDataMap.keySet()) {
      var data = localeDataMap.get(locale);
      var bundle = new ListResourceBundle() {
        @Override
        protected Object[][] getContents() {
          return data;
        }
      };
      bundles.put(locale, bundle);
    }
  }

  public static ResourceBundle getBundle(String locale) {
    return bundles.getOrDefault(locale, bundles.get("en"));
  }
}
