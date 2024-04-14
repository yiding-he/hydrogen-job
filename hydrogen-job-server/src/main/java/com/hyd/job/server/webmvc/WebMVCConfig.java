package com.hyd.job.server.webmvc;

import com.hyd.job.server.i18n.I18nService;
import com.hyd.job.server.utilities.Jackson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

  @Autowired
  private I18nService i18nService;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new HandlerInterceptor() {
      @Override
      public void postHandle(
        HttpServletRequest request, HttpServletResponse response,
        Object handler, ModelAndView modelAndView
      ) throws Exception {
        if (modelAndView != null) {
          // for javascript
          modelAndView.addObject("I18nStr", Jackson.serializeStandardJson(i18nService.allMessages()));
          // for freemarker
          modelAndView.addObject("bundle", i18nService.createResourceBundleModel());
        }
      }
    });
  }
}
