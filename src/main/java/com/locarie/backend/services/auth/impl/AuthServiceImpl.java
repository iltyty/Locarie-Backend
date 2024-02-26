package com.locarie.backend.services.auth.impl;

import com.locarie.backend.exceptions.UserNotFoundException;
import com.locarie.backend.repositories.user.UserRepository;
import com.locarie.backend.services.auth.AuthService;
import com.locarie.backend.services.redis.RedisService;
import java.util.Random;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
  @Value("${validation.code.expire:5}")
  private long expire;

  private final UserRepository repository;
  private final RedisService redis;

  public AuthServiceImpl(UserRepository repository, RedisService redis) {
    this.repository = repository;
    this.redis = redis;
  }

  @Override
  public boolean forgotPassword(Long userId) {
    if (!repository.existsById(userId)) {
      throw new UserNotFoundException("user with id " + userId + " not found");
    }
    generateForgotPasswordValidationCode(userId);
    return true;
  }

  private void generateForgotPasswordValidationCode(Long userId) {
    String key = userId.toString();
    redis.set(key, randomCode());
    redis.setExpireInMinutes(key, expire);
  }

  private String randomCode() {
    Random random = new Random();
    long code = random.nextLong(900000) + 100000;
    return Long.toString(code);
  }

  @Override
  public boolean validateForgotPassword(Long userId, String code) {
    if (!repository.existsById(userId)) {
      throw new UserNotFoundException("user with id " + userId + " not found");
    }
    return validateForgotPasswordCode(userId, code);
  }

  private boolean validateForgotPasswordCode(Long userId, String code) {
    String existingCode = (String) redis.get(userId.toString());
    if (existingCode == null) {
      return false;
    }
    return existingCode.equals(code);
  }
}
