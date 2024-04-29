package com.hyd.job.server.sql;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class SqlCommand {

  private String statement;

  private List<Object> params;

  public SqlCommand() {
  }

  public SqlCommand(String statement, List<Object> params) {
    this.statement = statement;
    this.params = params;
  }

  public SqlCommand(String statement) {
    this.statement = statement;
  }

  @Override
  public String toString() {
    return "Command{" +
      "statement='" + statement + '\'' +
      ", params=" + params +
      '}';
  }
}
