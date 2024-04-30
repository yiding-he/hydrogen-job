package com.hyd.job.server.webmvc.module.product;

import com.hyd.job.server.webmvc.module.ModuleOperation;
import org.springframework.stereotype.Component;

@Component
public class ProductIndex extends ModuleOperation {

  @Override
  public String getModulePath() {
    return "product";
  }

  @Override
  public String getOperation() {
    return "index";
  }

}
