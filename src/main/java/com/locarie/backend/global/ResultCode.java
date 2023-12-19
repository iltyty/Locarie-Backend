package com.locarie.backend.global;

import lombok.Getter;

@Getter
public enum ResultCode {
  SUCCESS(0, "success"),
  FAIL(1, "fail"),

  // parameters-related codes
  RC101(101, "invalid parameters"),

  // authentication-related codes
  RC201(201, "email already in use"),
  RC202(202, "incorrect email or password");

  private final int code;

  private final String message;

  ResultCode(int code, String message) {
    this.code = code;
    this.message = message;
  }
}
