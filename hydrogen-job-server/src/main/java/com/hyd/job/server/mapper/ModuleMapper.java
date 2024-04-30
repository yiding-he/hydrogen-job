package com.hyd.job.server.mapper;

import com.hyd.job.server.webmvc.module.ModuleObj;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ModuleMapper {

  List<ModuleObj> MODULES = List.of(
    new ModuleObj(1L, "module_name_user", "user", "ok", "fas fa-baby")
  );

  default List<ModuleObj> listModules(long userId) {
    return MODULES;
  }
}
