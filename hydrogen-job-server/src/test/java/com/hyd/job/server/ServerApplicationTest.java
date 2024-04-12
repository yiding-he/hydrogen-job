package com.hyd.job.server;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("unit")
public class ServerApplicationTest {

  @Autowired
  private ApplicationContext applicationContext;

  @Test
  public void contextLoads() {
    assertNotNull(applicationContext);
  }
}
