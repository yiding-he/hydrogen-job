package com.hyd.job.server.webmvc;

import com.hyd.job.server.i18n.LocaleUtil;
import com.hyd.job.server.utilities.CSVReader;
import com.hyd.job.server.utilities.Coll;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.StaticMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.util.Map;

@Configuration
@Slf4j
public class WebMVCConfig implements WebMvcConfigurer {

  @Bean("messageSource")
  public MessageSource messageSource() throws IOException {
    var messageSource = new StaticMessageSource();
    var messageData = CSVReader.read(new ClassPathResource("/i18n/messages.csv"));
    var locales = Coll.subList(messageData.getHeaders(), 1, -1);
    for (var locale : locales) {
      for (Map<String, String> row : messageData.getRows()) {
        var key = row.get("key");
        var value = row.get(locale);
        messageSource.addMessage(key, LocaleUtil.find(locale), value);
      }
    }
    return messageSource;
  }

  @Bean
  public ServletContextInitializer servletContextInitializer() {
    return ctx -> {
      log.info("Web application context initialized");
    };
  }
}
