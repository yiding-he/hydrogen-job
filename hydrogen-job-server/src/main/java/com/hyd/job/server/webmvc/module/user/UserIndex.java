package com.hyd.job.server.webmvc.module.user;

import com.hyd.job.server.mapper.UserMapper;
import com.hyd.job.server.webmvc.RequestContext;
import com.hyd.job.server.webmvc.module.ModuleOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserIndex extends ModuleOperation {

  @Autowired
  private UserMapper userMapper;

  @Override
  public String getModulePath() {
    return "user";
  }

  @Override
  public String getOperation() {
    return "index";
  }

  @Override
  public void execute(RequestContext requestContext) throws Exception {

    var dataTable = requestContext.addTable()
      .addColumn("id", "用户ID")
      .addColumn("username", "用户名")
      .addOperation("edit", "编辑");

    for (var user : userMapper.listAll()) {
      dataTable.addRow(user.getUserId())
        .setAttribute("username", user.getUserName())
      ;
    }
  }
}
