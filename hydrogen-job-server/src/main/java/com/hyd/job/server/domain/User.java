package com.hyd.job.server.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Date;

@Table("users")
@Data
public class User {

  public static final String ANONYMOUS_USER_NAME = "anonymous";

  @Id
  private long userId;

  @Column("user_name")
  private String userName;

  private String password;

  private String email;

  private String product;

  private String line;

  @Column("created_at")
  private Date createdAt;

  @Column("updated_at")
  private Date updatedAt;

}
