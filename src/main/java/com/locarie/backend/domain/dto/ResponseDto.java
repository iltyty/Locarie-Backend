package com.locarie.backend.domain.dto;

import lombok.Data;

@Data
public class ResponseDto<T> {

    private int status;

    private T data;

    private String message;

    public static <T> ResponseDto<T> success(T data) {
        ResponseDto<T> response = new ResponseDto<>();
        response.setStatus(StatusCode.SUCCESS.code);
        response.setData(data);
        response.setMessage(StatusCode.SUCCESS.message);
        return response;
    }

    public static <T> ResponseDto<T> fail(String message) {
        ResponseDto<T> response = new ResponseDto<>();
        response.setStatus(StatusCode.FAIL.code);
        response.setMessage(message);
        return response;
    }

    public enum StatusCode {
        SUCCESS(0, "success"), FAIL(1, "fail");

        private final int code;

        private final String message;

        StatusCode(int code, String message) {
            this.code = code;
            this.message = message;
        }
    }
}
