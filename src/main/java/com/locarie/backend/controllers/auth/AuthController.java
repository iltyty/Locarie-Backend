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
  public ResponseEntity<Boolean> forgotPassword(@RequestParam("email") String email) {
    return new ResponseEntity<>(service.forgotPassword(email), HttpStatus.CREATED);
  }

  @PostMapping("/forgot-password/validate")
  public ResponseEntity<Boolean> validateForgotPassword(
      @RequestParam("email") String email, @RequestParam("code") String code) {
    return new ResponseEntity<>(
        service.validateForgotPasswordCode(email, code), HttpStatus.CREATED);
  }

  @PostMapping("/reset-password")
  public ResponseEntity<Boolean> resetPassword(
      @RequestParam("email") String email, @RequestParam("password") String password) {
    return new ResponseEntity<>(service.resetPassword(email, password), HttpStatus.CREATED);
  }

  @PostMapping("/reset-password/validate")
  public ResponseEntity<String> validatePassword(
      @RequestParam("email") String email, @RequestParam("password") String password) {
    return new ResponseEntity<>(service.validatePassword(email, password), HttpStatus.CREATED);
  }
}
