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
  RC202(202, "incorrect email or password"),

  // user-related codes
  RC301(301, "user not found"),

  // post-related codes
  RC401(401, "post not found");

  private final int code;

  private final String message;

  ResultCode(int code, String message) {
    this.code = code;
    this.message = message;
  }
}
