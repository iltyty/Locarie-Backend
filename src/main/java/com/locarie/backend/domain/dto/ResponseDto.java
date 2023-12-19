package com.locarie.backend.domain.dto;

import com.locarie.backend.global.ResultCode;
import lombok.Data;

@Data
public class ResponseDto<T> {

  private int status;

  private T data;

  private String message;

  public static <T> ResponseDto<T> success(T data) {
    ResponseDto<T> response = new ResponseDto<>();
    response.setStatus(ResultCode.SUCCESS.getCode());
    response.setData(data);
    response.setMessage(ResultCode.SUCCESS.getMessage());
    return response;
  }

  private static <T> ResponseDto<T> fail(int code, String message) {
    ResponseDto<T> response = new ResponseDto<>();
    response.setStatus(code);
    response.setMessage(message);
    return response;
  }

  public static <T> ResponseDto<T> fail(ResultCode code) {
    return fail(code.getCode(), code.getMessage());
  }

  public static <T> ResponseDto<T> fail(ResultCode code, String message) {
    return fail(code.getCode(), message);
  }
}
