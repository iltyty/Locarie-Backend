package com.locarie.backend.controllers;

import com.locarie.backend.domain.dto.*;
import com.locarie.backend.domain.dto.user.UserDto;
import com.locarie.backend.domain.dto.user.UserLoginRequestDto;
import com.locarie.backend.domain.dto.user.UserLoginResponseDto;
import com.locarie.backend.global.ResultCode;
import com.locarie.backend.services.user.UserService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

  private final UserService service;

  public UserController(UserService service) {
    this.service = service;
  }

  @PostMapping("/login")
  public ResponseDto<UserLoginResponseDto> login(UserLoginRequestDto dto) {
    UserLoginResponseDto result = service.login(dto);
    return result == null ? ResponseDto.fail(ResultCode.RC202) : ResponseDto.success(result);
  }

  @GetMapping
  public List<UserDto> listUsers() {
    return service.list();
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserDto> getUser(@PathVariable("id") Long id) {
    return service
        .get(id)
        .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }
}
