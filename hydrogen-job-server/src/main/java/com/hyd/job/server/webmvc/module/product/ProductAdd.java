package com.hyd.job.server.webmvc.module.product;

import com.hyd.job.server.domain.Product;
import com.hyd.job.server.mapper.ProductMapper;
import com.hyd.job.server.webmvc.RequestContext;
import com.hyd.job.server.webmvc.module.ModuleOperation;
import com.hyd.job.server.webmvc.ui.form.FieldType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
public class ProductAdd extends ModuleOperation {

  @Autowired
  private ProductMapper productMapper;

  @Override
  public String getModulePath() {
    return "product";
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
      doAddProduct(requestContext);
    }
  }

  private void showForm(RequestContext requestContext) {
    requestContext.addForm(getOperation(), HttpMethod.POST, "product_add_product")
      .addField("product_form_name", "productName", FieldType.TextField)
    ;
  }

  private void doAddProduct(RequestContext requestContext) {
    var productName = requestContext.getParameter("productName");
    var product = new Product();
    product.setProductName(productName);
    productMapper.insertProduct(product);
  }
}
