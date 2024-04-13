package com.hyd.job.server.domain;

import com.hyd.job.server.utilities.I18nUtil;

public enum ErrorCode {

  SUCCESS(0, ""),

  ;

  private final int code;

  private final String messageKey;

  public int getCode() {
    return code;
  }

  public String getMessage() {
    return I18nUtil.getString(messageKey);
  }

  public String getMessageKey() {
    return messageKey;
  }

  ErrorCode(int code, String messageKey) {
    this.code = code;
    this.messageKey = messageKey;
  }
}
