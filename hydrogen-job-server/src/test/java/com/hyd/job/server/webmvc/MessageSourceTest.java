package com.hyd.job.server.webmvc;

import com.hyd.job.server.ServerApplicationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.util.Locale;

public class MessageSourceTest extends ServerApplicationTest {

  @Autowired
  private MessageSource messageSource;

  @Test
  public void test() {
    System.out.println(messageSource.getMessage("login", null, Locale.ENGLISH));
    System.out.println(messageSource.getMessage("login", null, Locale.SIMPLIFIED_CHINESE));
    System.out.println(messageSource.getMessage("login", null, Locale.TRADITIONAL_CHINESE));
  }
}
