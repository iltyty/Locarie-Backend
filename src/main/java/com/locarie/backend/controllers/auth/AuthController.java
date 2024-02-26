package com.locarie.backend.controllers.auth;

import com.locarie.backend.services.auth.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

  @GetMapping("/forgot-password/validate")
  public boolean validateForgotPassword(
      @RequestParam("userId") Long userId, @RequestParam("code") String code) {
    return service.validateForgotPasswordCode(userId, code);
  }
}
