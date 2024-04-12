package com.hyd.job.server.webmvc;

import com.hyd.job.server.utilities.I18nUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new HandlerInterceptor() {
      @Override
      public void postHandle(
        HttpServletRequest request, HttpServletResponse response,
        Object handler, ModelAndView modelAndView
      ) throws Exception {
        if (modelAndView != null) {
          modelAndView.addObject("I18n", I18nUtil.getMultMap());
          modelAndView.addObject("I18nStr", I18nUtil.getMultString());
        }
      }
    });
  }
}
