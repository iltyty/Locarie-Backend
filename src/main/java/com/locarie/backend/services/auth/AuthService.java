package com.locarie.backend.services.auth;

public interface AuthService {
  boolean forgotPassword(Long userId);

  boolean validateForgotPasswordCode(Long userId, String code);
}
