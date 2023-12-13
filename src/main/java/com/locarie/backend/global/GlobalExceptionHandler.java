package com.locarie.backend.global;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.locarie.backend.domain.dto.ResponseDto;
import com.locarie.backend.exceptions.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final ObjectMapper objectMapper;

    public GlobalExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDto<String> handleValidationException(MethodArgumentNotValidException e)
            throws JsonProcessingException {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult()
                .getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseDto.fail(ResultCode.RC101, objectMapper.writeValueAsString(errors));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseDto<String> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        return ResponseDto.fail(ResultCode.RC201);
    }

    @ExceptionHandler(Exception.class)
    public ResponseDto<String> handleException(Exception e) {
        return ResponseDto.fail(ResultCode.FAIL, e.getMessage());
    }
}
