package com.hyd.job.server.webmvc.module.product;

import com.hyd.job.server.domain.Product;
import com.hyd.job.server.mapper.ProductMapper;
import com.hyd.job.server.webmvc.RequestContext;
import com.hyd.job.server.webmvc.module.ModuleOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductIndex extends ModuleOperation {

  @Autowired
  private ProductMapper productMapper;

  @Override
  public String getModulePath() {
    return "product";
  }

  @Override
  public String getOperation() {
    return "index";
  }

  @Override
  public void execute(RequestContext requestContext) throws Exception {
    var dataTable = requestContext.addTable()
      .addColumn("productId", "产品ID")
      .addColumn("productName", "产品名称")
      .addOperation("编辑", "edit")
      .addOperation("删除", "delete", "确定要删除吗？");

    var products = productMapper.listAllProducts();
    for (Product product : products) {
      dataTable.addRow(product.getProductId())
        .setAttribute("productName", product.getProductName());
    }
  }
}
