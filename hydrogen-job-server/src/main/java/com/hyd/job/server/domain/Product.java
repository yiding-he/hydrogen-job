package com.hyd.job.server.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Product {

  private long productId;

  private String productName;

  private Date createdAt;

  private Date updatedAt;
}
