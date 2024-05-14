package com.hyd.job.server.mapper;

import com.hyd.job.server.domain.Line;
import com.hyd.job.server.sql.Sql;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LineMapper extends SqlMapper {

  String TABLE = "t_line";

  default List<Line> listAll() {
    return query(Sql.Select("*").From(TABLE))
      .stream().map(row -> row.injectTo(new Line()))
      .toList();
  }
}
