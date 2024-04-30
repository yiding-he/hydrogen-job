package com.hyd.job.server.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

import static java.util.Collections.emptyList;

@Mapper
public interface ModuleMapper {

  default List<Module> listModules(long userId) {
    return emptyList();
  }
}
