package com.hyd.job.server.webmvc.ui.op;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Operation {

  private String name;

  private String code;

  private String confirm;

  public Operation(String name, String code) {
    this.name = name;
    this.code = code;
  }

  public Operation(String name, String code, String confirm) {
    this.name = name;
    this.code = code;
    this.confirm = confirm;
  }
}
