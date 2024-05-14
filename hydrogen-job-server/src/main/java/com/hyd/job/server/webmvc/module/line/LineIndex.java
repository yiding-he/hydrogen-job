package com.hyd.job.server.webmvc.module.line;

import com.hyd.job.server.mapper.LineMapper;
import com.hyd.job.server.webmvc.RequestContext;
import com.hyd.job.server.webmvc.module.ModuleOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LineIndex extends ModuleOperation {

  @Autowired
  private LineMapper lineMapper;

  @Override
  public String getModulePath() {
    return "line";
  }

  @Override
  public String getOperation() {
    return "index";
  }

  @Override
  public void execute(RequestContext requestContext) throws Exception {
    var dataTable = requestContext.addTable()
      .addColumn("lineId", "Line ID")
      .addColumn("lineName", "Line Name")
      .addOperation("编辑", "edit")
      .addOperation("删除", "delete");

    lineMapper.listAll().forEach(line -> {
      dataTable.addRow(line.getLineId())
        .setAttribute("lineId", line.getLineId())
        .setAttribute("lineName", line.getLineName());
    });
  }
}
