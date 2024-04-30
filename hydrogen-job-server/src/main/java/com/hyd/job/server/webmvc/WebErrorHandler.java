package com.hyd.job.server.webmvc;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
@Slf4j
public class WebErrorHandler {

  @ExceptionHandler
  @ResponseBody
  public ModelAndView handleException(final Exception ex, final HttpServletRequest request) {
    log.error(ex.getMessage(), ex);
    return new ModelAndView("/admin/error")
      .addObject("message", ex.getMessage())
      .addObject("trace", ex.getStackTrace());
  }
}
