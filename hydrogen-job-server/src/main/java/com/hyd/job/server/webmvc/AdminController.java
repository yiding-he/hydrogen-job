package com.hyd.job.server.webmvc;

import com.hyd.job.server.domain.ResponseData;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

  @InitBinder
  public void initBinder(WebDataBinder binder) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    dateFormat.setLenient(false);
    binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
  }

  ////////////////////////////////////////

  @GetMapping("/login")
  public ModelAndView login() {
    return new ModelAndView("/admin/login");
  }

  @RequestMapping({"/index", "/"})
  public ModelAndView index() {
    return new ModelAndView("/admin/index")
      .addObject("jobInfoCount", 100)
      .addObject("jobLogCount", 100)
      .addObject("executorCount", 100)
      ;
  }

  @RequestMapping("/chartInfo")
  @ResponseBody
  public ResponseData<Map<String, Object>> chartInfo(
    @RequestParam("startDate") Date startDate, @RequestParam("endDate") Date endDate
  ) {
    return ResponseData.success();
  }

}
