package com.hyd.job.server.i18n;

import com.hyd.job.server.ServerConfig;
import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.ext.beans.ResourceBundleModel;
import freemarker.template.Configuration;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
public class I18nService {

  public static final String RESOURCE_BUNDLE_PATH =
    System.getProperty("resource.bundle.path", "/i18n/messages.csv");

  public static final BeansWrapper BEANS_WRAPPER =
    new BeansWrapperBuilder(Configuration.VERSION_2_3_32).build();

  @Autowired
  private ServerConfig serverConfig;

  private CSVMixedResourceBundle resourceBundle;

  @PostConstruct
  public void init() {
    resourceBundle = CSVMixedResourceBundle.fromResourcePath(RESOURCE_BUNDLE_PATH);
    resourceBundle.setLocale(serverConfig.getLocale());
  }

  /**
   * Create a FreeMarker resource bundle model.
   */
  public ResourceBundleModel createResourceBundleModel() {
    return new ResourceBundleModel(resourceBundle, BEANS_WRAPPER);
  }

  /**
   * Get all messages in the resource bundle corresponding to the configured locale.
   */
  public Map<String, String> allMessages() {
    return resourceBundle.keySet().stream().collect(
      Collectors.toMap(key -> key, resourceBundle::getString)
    );
  }

}
