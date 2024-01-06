package com.locarie.backend.exceptions;

public class RequestArgumentNotValidException extends RuntimeException {
  public RequestArgumentNotValidException(String message) {
    super(message);
  }

  public RequestArgumentNotValidException(String message, Throwable cause) {
    super(message, cause);
  }
}
