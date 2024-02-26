package com.locarie.backend.services.auth;

public interface AuthService {
  boolean forgotPassword(Long userId);

  boolean validateForgotPassword(Long userId, String code);
}
