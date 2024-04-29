package com.hyd.job.server.mapper;

import com.hyd.job.server.ServerApplicationTest;
import com.hyd.job.server.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
class UserMapperTest extends ServerApplicationTest {

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Test
  public void testInsertUser() throws Exception {
    User user = new User();
    user.setUserName("admin");
    user.setPassword(passwordEncoder.encode("admin"));
    user.setProductId(User.ALL_PRODUCT);
    user.setLineId(User.ALL_LINE);
    user.setEmail("admin@localhost");
    log.info("Inserting user: {}", user);
    userMapper.insertUser(user);
  }


  @Test
  public void testSelectUser() throws Exception {
    User user = userMapper.findByUsername("admin");
    assertNotNull(user);
    System.out.println(user);
  }
}
