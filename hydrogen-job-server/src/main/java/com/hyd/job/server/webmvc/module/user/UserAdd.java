package com.hyd.job.server.webmvc.module.user;

import com.hyd.job.server.mapper.ProductMapper;
import com.hyd.job.server.mapper.UserMapper;
import com.hyd.job.server.webmvc.RequestContext;
import com.hyd.job.server.webmvc.module.ModuleOperation;
import com.hyd.job.server.webmvc.ui.form.FieldType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
public class UserAdd extends ModuleOperation {

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private ProductMapper productMapper;

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
    if (requestContext.isGetMethod()) {
      showForm(requestContext);
    } else {
      doAddUser(requestContext);
    }
  }

  private void showForm(RequestContext requestContext) {
    requestContext
      .addForm(getOperation(), HttpMethod.POST, "添加用户")
      .addField("用户名", "username", FieldType.TextField)
      .addField("密码", "password", FieldType.TextField)
      .addField("产品", "product", productMapper.listAsKeyValue())
    ;
  }

  private void doAddUser(RequestContext requestContext) {
    String username = requestContext.getParameter("username");
    String password = requestContext.getParameter("password");
    Long roleId = requestContext.getParameterLong("roleId");

    // TODO: 保存用户信息到数据库
  }
}
