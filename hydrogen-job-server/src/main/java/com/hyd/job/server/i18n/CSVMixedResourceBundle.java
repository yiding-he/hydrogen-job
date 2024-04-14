package com.hyd.job.server.i18n;

import lombok.*;
import org.springframework.lang.NonNull;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

/**
 * A multi-locale resource bundle that reads data from a CSV file.
 * The CSV file should have the following format:
 * <pre>
 *   "key","en_US","zh_CN"
 *   "hello","Hello","你好"
 *   "world","World","世界"
 * </pre>
 * The first row is the header row, which contains the keys for each locale.
 * The remaining rows are data rows, which contain the translations for each key in each locale.
 * The CSV file should be encoded in UTF-8.
 * For double quotes in value, use two consecutive double quotes.
 * <p>
 * The class is designed to be used like a regular ResourceBundle, but it also supports
 * multiple locales and fallback to the default locale if a translation is not found for the current locale.
 * <p>
 * You can create a new instance by calling the static methods {@link #fromResourcePath} or {@link #fromFile},
 * or by calling the constructor with an input stream.
 * <p>
 * For example:
 * <pre>
 *   var bundle = CSVMixedResourceBundle.fromResourcePath("/i18n/messages.csv");
 *   bundle.setDefaultLocale(Locale.ENGLISH);  // optional
 *   bundle.setLocale(Locale.SIMPLIFIED_CHINESE);
 *   System.out.println(bundle.getString("hello"));
 * </pre>
 * The above code will load the default locale (in this case, English) from the CSV file,
 * and then use the default locale as a fallback if a translation is not found for the current locale.
 */
public class CSVMixedResourceBundle extends ResourceBundle {

  public static CSVMixedResourceBundle fromResourcePath(String resourcePath) {
    var inputStream = CSVMixedResourceBundle.class.getResourceAsStream(resourcePath);
    if (inputStream == null) {
      throw new IllegalArgumentException("Resource not found: " + resourcePath);
    }
    return new CSVMixedResourceBundle(inputStream);
  }

  public static CSVMixedResourceBundle fromFile(String filePath) {
    try {
      var inputStream = new FileInputStream(filePath);
      return new CSVMixedResourceBundle(inputStream);
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("File not found: " + filePath, e);
    }
  }

  ////////////////////////////////////////

  private final Map<String, ResourceBundle> bundles = new HashMap<>();

  @Getter
  private Locale defaultLocale = Locale.ENGLISH;

  @Getter
  @Setter
  private Locale locale = defaultLocale;

  public CSVMixedResourceBundle(InputStream is) {
    var csvData = CSVReader.read(is);
    var localeDataMap = new HashMap<String, String[][]>();
    var rowSize = csvData.getRows().size();
    csvData.getHeaders().stream().filter(h -> !h.equals("key")).forEach(locale -> {
      var localeData = localeDataMap.computeIfAbsent(locale, l -> new String[rowSize][2]);
      for (int i = 0; i < csvData.getRows().size(); i++) {
        Map<String, String> row = csvData.getRows().get(i);
        var key = row.get("key");
        var value = row.get(locale);
        if (value != null) {
          localeData[i][0] = key;
          localeData[i][1] = value;
        }
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

  public void setDefaultLocale(Locale defaultLocale) {
    if (defaultLocale == null) {
      throw new IllegalArgumentException("defaultLocale cannot be null");
    }
    this.defaultLocale = defaultLocale;
  }

  @Override
  protected Object handleGetObject(@NonNull String key) {
    var locale = this.locale;
    if (locale == null) {
      locale = defaultLocale;
    } else if (!bundles.containsKey(locale.toString())) {
      locale = defaultLocale;
    } else if (!bundles.get(locale.toString()).containsKey(key)) {
      locale = defaultLocale;
    } else if (bundles.get(locale.toString()).getString(key).isEmpty()) {
      locale = defaultLocale;
    }
    return bundles.get(locale.toString()).getString(key);
  }

  @Override
  @NonNull
  public Enumeration<String> getKeys() {
    var keys = new HashSet<String>();
    this.bundles.values().forEach(b -> keys.addAll(b.keySet()));
    return Collections.enumeration(keys);
  }
}

class CSVReader {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class CSVData {

    private List<String> headers;

    private List<Map<String, String>> rows;
  }

  public static CSVData read(InputStream inputStream) {
    try (Scanner scanner = new Scanner(inputStream)) {
      List<Map<String, String>> rows = new ArrayList<>();
      List<String> headers = parseQuotedString(scanner.nextLine());
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        if (line.trim().isEmpty()) {
          continue;
        }
        List<String> values = parseQuotedString(line);
        if (values.size() != headers.size()) {
          throw new IllegalArgumentException("Invalid CSV format: " + line);
        }
        Map<String, String> row = new HashMap<>();
        for (int i = 0; i < headers.size(); i++) {
          row.put(headers.get(i), values.get(i));
        }
        rows.add(row);
      }
      return new CSVData(headers, rows);
    }
  }

  // "1","""2""and3","4" -> ["1", "\"2\"and3", "4"]
  // ,,, -> ["", "", "", ""]
  public static List<String> parseQuotedString(String input) {
    List<String> result = new ArrayList<>();
    StringBuilder sb = new StringBuilder();
    boolean insideQuotes = false;
    for (int i = 0; i < input.length(); i++) {
      char currentChar = input.charAt(i);
      if (currentChar == '"') {
        if (insideQuotes) {
          if (i + 1 < input.length() && input.charAt(i + 1) == '"') {
            sb.append('"');
            i++;
          } else {
            insideQuotes = false;
          }
        } else {
          insideQuotes = true;
        }
      } else if (currentChar == ',') {
        if (insideQuotes) {
          sb.append(currentChar);
        } else {
          result.add(sb.toString());
          sb.setLength(0);
        }
      } else {
        sb.append(currentChar);
      }
    }
    result.add(sb.toString());
    return result;
  }
}
