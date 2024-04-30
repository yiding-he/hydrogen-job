package com.hyd.job.server.mapper;

import com.hyd.job.server.domain.Product;
import com.hyd.job.server.sql.Row;
import com.hyd.job.server.sql.Sql;
import org.apache.commons.collections4.KeyValue;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Mapper
public interface ProductMapper extends SqlMapper {

  String TABLE_NAME = "t_product";

  /**
   * List all products as key-value pairs, where the key is the product ID and the value is the product name.
   */
  default List<KeyValue<String, String>> listAsKeyValue() {

    Function<Row, KeyValue<String, String>> converter = row ->
      new DefaultKeyValue<>(row.getString("product_id"), row.getString("product_name"));

    return listAllRows().stream().map(converter).toList();
  }

  default List<Row> listAllRows() {
    return query(
      Sql.Select("product_id", "product_name").From(TABLE_NAME)
    );
  }

  default List<Product> listAllProducts() {
    return listAllRows().stream().map(row -> row.injectTo(new Product())).toList();
  }

  default void insertProduct(Product product) {
    product.setProductId(snowflake.nextId());

    execute(Sql.Insert(TABLE_NAME).Values(Map.of(
      "product_id", product.getProductId(),
      "product_name", product.getProductName()
    )));
  }
}
