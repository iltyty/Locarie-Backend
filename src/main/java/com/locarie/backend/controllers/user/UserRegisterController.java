package com.locarie.backend.controllers.user;

import com.locarie.backend.domain.dto.user.UserDto;
import com.locarie.backend.domain.dto.user.UserRegistrationDto;
import com.locarie.backend.exceptions.UserAlreadyExistsException;
import com.locarie.backend.services.user.UserRegisterService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users/register")
public class UserRegisterController {
  private final UserRegisterService service;

  public UserRegisterController(UserRegisterService service) {
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<UserDto> register(@Valid @RequestBody UserRegistrationDto dto)
      throws UserAlreadyExistsException {
    UserDto savedUser = service.register(dto);
    if (savedUser == null) {
      throw new UserAlreadyExistsException("user already exists");
    }
    return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
  }
}
