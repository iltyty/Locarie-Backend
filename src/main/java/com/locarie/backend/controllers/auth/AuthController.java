package com.locarie.backend.controllers.auth;

import com.locarie.backend.services.auth.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
  private final AuthService service;

  public AuthController(AuthService service) {
    this.service = service;
  }

  @PostMapping("/forgot-password")
  public ResponseEntity<Boolean> forgotPassword(@RequestParam("userId") Long userId) {
    return new ResponseEntity<>(service.forgotPassword(userId), HttpStatus.CREATED);
  }
}
