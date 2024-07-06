package com.locarie.backend.global;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.locarie.backend.domain.dto.ResponseDto;
import com.locarie.backend.exceptions.*;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

  private final ObjectMapper objectMapper;

  public GlobalExceptionHandler(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseDto<String> handleValidationException(MethodArgumentNotValidException e)
      throws JsonProcessingException {
    log.error(e.getMessage());
    Map<String, String> errors = new HashMap<>();
    e.getBindingResult()
        .getFieldErrors()
        .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
    return ResponseDto.fail(ResultCode.RC101, objectMapper.writeValueAsString(errors));
  }

  @ExceptionHandler(RequestArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseDto<String> handleRequestArgumentNotValidException(
      RequestArgumentNotValidException e) {
    log.error(e.getMessage());
    return ResponseDto.fail(ResultCode.RC101, e.getMessage());
  }

  @ExceptionHandler(NotAuthorizedOperationException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ResponseDto<String> handleNotAuthorizedOperationException(
      NotAuthorizedOperationException e) {
    log.error(e.getMessage());
    return ResponseDto.fail(ResultCode.RC203, e.getMessage());
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseDto<String> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
    log.error(e.getMessage());
    return ResponseDto.fail(ResultCode.RC201);
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseDto<String> handleUserNotFoundException(UserNotFoundException e) {
    log.error(e.getMessage());
    return ResponseDto.fail(ResultCode.RC301);
  }

  @ExceptionHandler(PostNotFoundException.class)
  public ResponseDto<String> handlePostNotFoundException(PostNotFoundException e) {
    log.error(e.getMessage());
    return ResponseDto.fail(ResultCode.RC401);
  }

  @ExceptionHandler(Exception.class)
  public ResponseDto<String> handleException(Exception e) {
    log.error(e.getMessage());
    return ResponseDto.fail(ResultCode.FAIL, e.getMessage());
  }
}
