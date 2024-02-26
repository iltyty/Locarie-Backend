package com.locarie.backend.services.auth;

public interface AuthService {
  boolean forgotPassword(Long userId);

  boolean validateForgotPasswordCode(Long userId, String code);

  boolean resetPassword(Long userId, String password);
}
