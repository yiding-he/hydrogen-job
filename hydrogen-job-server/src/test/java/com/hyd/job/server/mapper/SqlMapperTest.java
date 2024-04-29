package com.hyd.job.server.mapper;

import com.hyd.job.server.ServerApplicationTest;
import com.hyd.job.server.sql.SqlCommand;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class SqlMapperTest extends ServerApplicationTest {

  @Autowired
  private SqlMapper sqlMapper;

  @Test
  void testShowTables() {
    System.out.println(sqlMapper.query(new SqlCommand("show tables")));
  }
}
