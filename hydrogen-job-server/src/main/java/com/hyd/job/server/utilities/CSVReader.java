package com.hyd.job.server.utilities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class CSVReader {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class CSVData {

    private List<String> headers;

    private List<Map<String, String>> rows;
  }

  public static CSVReader.CSVData read(Resource resource) throws IOException {
     return read(resource.getInputStream());
  }

  public static CSVReader.CSVData read(InputStream inputStream) {
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
      return new CSVReader.CSVData(headers, rows);
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
