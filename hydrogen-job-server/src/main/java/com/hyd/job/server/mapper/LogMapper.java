package com.hyd.job.server.mapper;

import com.hyd.job.server.domain.User;
import com.hyd.job.server.webmvc.module.ModuleOperation;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper
public interface LogMapper extends SqlMapper {

  default void addLoginLog(User sessionUser) {
    var log = Map.of(
      "user_id", sessionUser.getUserId(),
      "user_name", sessionUser.getUserName(),
      "req_path", "/login"
    );

    // TODO save login log to database
  }

  default void addOperationLog(
    User sessionUser, ModuleOperation moduleOperation, HttpServletRequest request
  ) {

    String parameters = request.getParameterMap().entrySet().stream()
      .map(entry -> entry.getKey() + "=" + Arrays.asList(entry.getValue()))
      .collect(Collectors.joining("\n"));

    var log = Map.of(
      "user_id", sessionUser.getUserId(),
      "user_name", sessionUser.getUserName(),
      "req_path", "/" + moduleOperation.getModulePath() + "/" + moduleOperation.getOperation(),
      "req_params", parameters
    );

    // TODO save login log to database
  }
}
