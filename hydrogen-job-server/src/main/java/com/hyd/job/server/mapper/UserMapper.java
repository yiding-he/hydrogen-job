package com.hyd.job.server.mapper;

import com.hyd.job.server.domain.User;
import com.hyd.job.server.sql.Sql;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends SqlMapper {

  default User findByUsername(String username) {
    var row = queryOne(Sql
      .Select("*").From("users")
      .Where("user_name=?", username));

    return row == null ? null : row.injectTo(new User());
  }
}
