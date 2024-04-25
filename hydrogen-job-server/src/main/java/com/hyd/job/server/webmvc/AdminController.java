package com.hyd.job.server.webmvc;

import com.hyd.job.server.domain.MenuItem;
import com.hyd.job.server.domain.PageData;
import com.hyd.job.server.domain.ResponseData;
import com.hyd.job.server.domain.User;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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

  //////////////////////////////////////// Login

  @GetMapping("/login")
  public ModelAndView login() {
    return new ModelAndView("/admin/login");
  }

  //////////////////////////////////////// Index

  @RequestMapping({"/index", "/"})
  public ModelAndView index() {
    return new ModelAndView("/admin/index")
      .addObject("menuItems", List.of(
        new MenuItem("1", false, List.of(
          new MenuItem("1.1", false, null),
          new MenuItem("1.2", false, null),
          new MenuItem("1.3", false, List.of(
            new MenuItem("1.3.1", false, null),
            new MenuItem("1.3.2", false, null)
          ))
        )),
        new MenuItem("2", false, List.of(
          new MenuItem("2.1", false, null),
          new MenuItem("2.2", false, null)
        ))
      ))
      ;
  }

  @RequestMapping("/chartInfo")
  @ResponseBody
  public ResponseData<Map<String, Object>> chartInfo(
    @RequestParam("startDate") Date startDate, @RequestParam("endDate") Date endDate
  ) {
    return ResponseData.success();
  }

  //////////////////////////////////////// User

  @RequestMapping("/user")
  public ModelAndView user() {
    return new ModelAndView("/admin/user");
  }

  @RequestMapping("/user/pageList")
  @ResponseBody
  public PageData<User> pageList(
    @RequestParam(name = "start", required = false, defaultValue = "0") int start,
    @RequestParam(name = "length", required = false, defaultValue = "10") int length,
    @RequestParam(name = "username", required = false, defaultValue = "") String username,
    @RequestParam(name = "role", required = false, defaultValue = "0") int role
  ) {
    return new PageData<>(1, 1, List.of(
      new User(0, "admin", "admin", "admin@admin.com", "admin", "admin", null, null)
    ));
  }
}
