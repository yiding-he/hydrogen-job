package com.hyd.job.server.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

  public static final String ANONYMOUS_USER_NAME = "anonymous";

  private long userId;

  private String userName;

  private String password;

  private String email;

  private String product;

  private String line;

  private Date createdAt;

  private Date updatedAt;

}
