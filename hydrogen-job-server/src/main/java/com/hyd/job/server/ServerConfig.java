package com.hyd.job.server;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Locale;

@Data
@ConfigurationProperties(prefix = "hydrogen-job")
public class ServerConfig {

  private Locale locale = Locale.getDefault();

}
