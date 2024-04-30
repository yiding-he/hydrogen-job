package com.hyd.job.server.webmvc;

import com.hyd.job.server.domain.User;
import com.hyd.job.server.mapper.ModuleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin")
@SessionAttributes("user")
public class AdminController {

  @Autowired
  private ModuleMapper moduleMapper;

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
  public ModelAndView index(@SessionAttribute("user") User user) {
    List<Module> modules = moduleMapper.listModules(user.getUserId());
    return new ModelAndView("/admin/main").addObject("modules", modules);
  }


}
