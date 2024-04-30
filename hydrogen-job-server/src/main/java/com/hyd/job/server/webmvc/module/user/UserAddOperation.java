package com.hyd.job.server.webmvc.module.user;

import com.hyd.job.server.webmvc.RequestContext;
import com.hyd.job.server.webmvc.module.ModuleOperation;
import org.springframework.stereotype.Component;

@Component
public class UserAddOperation extends ModuleOperation {

  @Override
  public String getModulePath() {
    return "user";
  }

  @Override
  public String getOperation() {
    return "add";
  }

  @Override
  public void execute(RequestContext requestContext) throws Exception {

  }
}
