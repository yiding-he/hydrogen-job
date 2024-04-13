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
    return new ResponseData<>(0, "", null);
  }

  public static <D> ResponseData<D> success(D content) {
    return new ResponseData<>(0, "", content);
  }

  public static <D> ResponseData<D> success(String msg, D content) {
    return new ResponseData<>(0, msg, content);
  }

  ////////////////////////////////////////

  private int code;

  private String msg;

  private T content;

}
