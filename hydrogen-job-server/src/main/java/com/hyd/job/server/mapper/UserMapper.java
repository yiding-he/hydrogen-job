package com.hyd.job.server.mapper;

import com.hyd.job.server.domain.User;
import com.hyd.job.server.sql.Sql;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper extends SqlMapper {

  String TABLE_NAME = "t_user";

  default User findByUsername(String username) {
    var row = queryOne(Sql
      .Select("*").From(TABLE_NAME)
      .Where("user_name=?", username));

    return row == null ? null : row.injectTo(new User());
  }

  default User findByUserId(long userId) {
    var row = queryOne(Sql
      .Select("*").From(TABLE_NAME)
      .Where("user_id=?", userId));

    return row == null ? null : row.injectTo(new User());
  }

  default User insertUser(User user) {
    user.setUserId(snowflake.nextId());
    execute(Sql.Insert(TABLE_NAME)
      .Values(Map.of(
        "user_id", user.getUserId(),
        "user_name", user.getUserName(),
        "password", user.getPassword(),
        "email", user.getEmail(),
        "product_id", user.getProductId(),
        "line_id", user.getLineId(),
        "type", user.getType()
      )));
    return user;
  }

  default List<User> listAll() {
    return this.query(Sql.Select("*").From(TABLE_NAME))
      .stream().map(row -> row.injectTo(new User()))
      .toList();
  }
}
