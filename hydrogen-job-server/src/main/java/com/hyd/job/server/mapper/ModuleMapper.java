package com.hyd.job.server.mapper;

import com.hyd.job.server.webmvc.module.ModuleObj;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface ModuleMapper {

  List<ModuleObj> ADMIN_MODULES = List.of(
    new ModuleObj(1L, "module_name_product", "product", "ok", "fas fa-baby"),
    new ModuleObj(2L, "module_name_line", "line", "ok", "fas fa-baby"),
    new ModuleObj(3L, "module_name_user", "user", "ok", "fas fa-baby")
  );

  List<ModuleObj> TASK_MODULES = List.of(
    new ModuleObj(3L, "module_name_job", "job", "ok", "fas fa-baby")
  );

  default List<ModuleObj> listModules(String userName) {
    var modules = new ArrayList<ModuleObj>();
    if (userName.equals("admin")) {
      modules.addAll(ADMIN_MODULES);
    }
    modules.addAll(TASK_MODULES);
    return modules;
  }
}
