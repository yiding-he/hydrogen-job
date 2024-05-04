package com.hyd.job.server.webmvc.module.product;

import com.hyd.job.server.mapper.ProductMapper;
import com.hyd.job.server.webmvc.RequestContext;
import com.hyd.job.server.webmvc.module.ModuleOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductDelete extends ModuleOperation {

  @Autowired
  private ProductMapper productMapper;

  @Override
  public String getModulePath() {
    return "product";
  }

  @Override
  public String getOperation() {
    return "delete";
  }

  @Override
  public void execute(RequestContext requestContext) throws Exception {
    var id = requestContext.getParameterLong("id");
    productMapper.deleteProduct(id);
  }
}
