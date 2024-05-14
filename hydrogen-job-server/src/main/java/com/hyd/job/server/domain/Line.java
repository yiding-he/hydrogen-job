package com.hyd.job.server.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Line {

  private long lineId;

  private String lineName;

  private Date createdAt;

  private Date updatedAt;
}
