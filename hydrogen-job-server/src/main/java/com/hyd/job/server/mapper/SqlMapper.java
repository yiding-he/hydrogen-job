package com.hyd.job.server.mapper;

import com.hyd.job.server.sql.Row;
import com.hyd.job.server.sql.Sql;
import com.hyd.job.server.sql.SqlCommand;
import com.hyd.job.server.utilities.Snowflake;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import java.util.List;

@Mapper
public interface SqlMapper {

  Snowflake snowflake = new Snowflake();

  ThreadLocal<String> statementThreadLocal = new ThreadLocal<>();

  class SqlProvider {

    public static String getStatement() {
      return statementThreadLocal.get();
    }
  }

  /////////////////////// 对 MyBatis 框架的封装

  private static void fixAndSetupSql(String sql) {
    int count = 0;
    while (sql.contains("?")) {
      sql = sql.replaceFirst("\\?", "#{list[" + count + "]}");
      count++;
    }
    statementThreadLocal.set(sql);
  }

  default int execute(SqlCommand command) {
    var sql = command.getStatement();
    var params = command.getParams();
    fixAndSetupSql(sql);
    return this.doExecute(params);
  }

  default int execute(Sql.Update update) {
    return execute(update.toCommand());
  }

  default int execute(Sql.Delete delete) {
    return execute(delete.toCommand());
  }

  default int execute(Sql.Insert insert) {
    return execute(insert.toCommand());
  }

  default List<Row> query(SqlCommand command) {
    var sql = command.getStatement();
    var params = command.getParams();
    fixAndSetupSql(sql);
    return this.doQuery(params);
  }

  default Row queryOne(SqlCommand command) {
    List<Row> rows = query(command);
    return rows.isEmpty() ? null : rows.get(0);
  }

  default List<Row> query(Sql.Select select) {
    return query(select.toCommand());
  }

  default Row queryOne(Sql.Select select) {
    List<Row> rows = query(select);
    return rows.isEmpty() ? null : rows.get(0);
  }

  /////////////////////// 交给 MyBatis 框架执行的部分

  @UpdateProvider(type = SqlProvider.class, method = "getStatement")
  int doExecute(List<?> list);


  @SelectProvider(type = SqlProvider.class, method = "getStatement")
  List<Row> doQuery(List<?> list);
}
