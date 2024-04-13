package com.hyd.job.server.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseData<T> {

  public static final int SUCCESS_CODE = 0;

  public static <D> ResponseData<D> success() {
    return new ResponseData<>(0, "success", null);
  }

  public static <D> ResponseData<D> success(String msg) {
    return new ResponseData<>(0, msg, null);
  }

  ////////////////////////////////////////

  private int code;

  private String msg;

  private T content;

}
