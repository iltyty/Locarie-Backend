package com.locarie.backend.services.auth;

public interface AuthService {
  boolean forgotPassword(String email);

  boolean validateForgotPasswordCode(String email, String code);

  boolean resetPassword(String email, String password);

  boolean validatePassword(String email, String password);
}
