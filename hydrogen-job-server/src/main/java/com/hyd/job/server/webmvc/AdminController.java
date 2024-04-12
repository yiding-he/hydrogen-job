package com.hyd.job.server.webmvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin")
public class AdminController {

  @RequestMapping({"/index", "/"})
  public ModelAndView index() {
    return new ModelAndView("/admin/index");
  }

  @GetMapping("/login")
  public ModelAndView login() {
    return new ModelAndView("/admin/login");
  }
}
