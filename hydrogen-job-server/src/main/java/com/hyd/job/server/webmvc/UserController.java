package com.hyd.job.server.webmvc;

import com.hyd.job.server.model.CrudModelAndView;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/user")
public class UserController {

  @RequestMapping("/index")
  public CrudModelAndView index() {
    return new CrudModelAndView("/admin/user");
  }
}
