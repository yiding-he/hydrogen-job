package com.hyd.job.server.model;

import com.hyd.job.server.utilities.Jackson;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

public class CrudModelAndView extends ModelAndView {

  public CrudModelAndView(String viewName) {
    super(viewName);
  }

  @Override
  public CrudModelAndView addObject(String attributeName, Object attributeValue) {
    super.addObject(attributeName, attributeValue);
    return this;
  }

  @Override
  public CrudModelAndView addAllObjects(Map<String, ?> modelMap) {
     super.addAllObjects(modelMap);
     return this;
  }

  public CrudModelAndView setColumns(TableColumn... columns) {
    return addObject("columns", Jackson.serializeStandardJson(columns, true));
  }
}
