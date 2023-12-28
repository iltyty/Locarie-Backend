package com.locarie.backend.controllers;

import com.locarie.backend.domain.dto.*;
import com.locarie.backend.exceptions.UserAlreadyExistsException;
import com.locarie.backend.global.ResultCode;
import com.locarie.backend.services.user.UserService;
import jakarta.validation.Valid;
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

  @PostMapping("/register")
  public ResponseEntity<UserDto> register(@Valid @RequestBody UserRegistrationDto dto)
      throws UserAlreadyExistsException {
    UserDto savedUser = service.register(dto);
    if (savedUser == null) {
      throw new UserAlreadyExistsException("user already exists");
    }
    return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
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
