package com.hyd.job.server.webmvc.module;

import com.hyd.job.server.domain.User;
import com.hyd.job.server.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrivilegeService {

  @Autowired
  private UserMapper userMapper;

  public boolean checkPrivilege(long userId, String product, String line) {
    User user = userMapper.findByUserId(userId);
    // TODO implement privilege check logic
    return true;
  }
}
