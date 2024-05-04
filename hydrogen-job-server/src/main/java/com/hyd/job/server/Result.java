package com.hyd.job.server;


public record Result<T>(int code, String message, T data) {

  public static final int CODE_SUCCESS = 0;

  public static final int CODE_UNCLASSIFIED_ERROR = -1;

  public static Result<?> success() {
    return new Result<>(CODE_SUCCESS, "", null);
  }

  public static Result<?> success(String message) {
    return new Result<>(CODE_SUCCESS, message, null);
  }

  public static <T> Result<T> success(T data) {
    return new Result<>(CODE_SUCCESS, "", data);
  }

  public static Result<?> error(int code, String message) {
    return new Result<>(code, message, null);
  }

  public static Result<?> error(String message) {
    return new Result<>(CODE_UNCLASSIFIED_ERROR, message, null);
  }
}
