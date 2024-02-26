package com.locarie.backend.exceptions;

public class NotAuthorizedOperationException extends RuntimeException {
  public NotAuthorizedOperationException(String message) {
    super(message);
  }

  public NotAuthorizedOperationException(String message, Throwable cause) {
    super(message, cause);
  }
}
