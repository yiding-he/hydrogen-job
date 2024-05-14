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

  public static final long ALL_PRODUCT = 0;

  public static final long ALL_LINE = 0;

  public static final long TYPE_UI = 1;

  public static final long TYPE_API = 2;

  private long userId;

  private String userName;

  private String password;

  private String email;

  private long productId;

  private long lineId;

  private long type;

  private Date createdAt;

  private Date updatedAt;

}
