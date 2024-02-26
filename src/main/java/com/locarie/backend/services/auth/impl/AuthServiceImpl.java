package com.locarie.backend.services.auth.impl;

import com.locarie.backend.domain.redis.ResetPasswordEntry;
import com.locarie.backend.exceptions.UserNotFoundException;
import com.locarie.backend.repositories.redis.ResetPasswordEntryRepository;
import com.locarie.backend.repositories.user.UserRepository;
import com.locarie.backend.services.auth.AuthService;
import com.locarie.backend.services.redis.RedisService;
import java.util.Optional;
import java.util.Random;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
  @Value("${validation.code.expire:5}")
  private long expire;

  private final UserRepository userRepository;
  private final ResetPasswordEntryRepository resetPasswordEntryRepository;

  public AuthServiceImpl(
      UserRepository repository,
      RedisService redis,
      ResetPasswordEntryRepository resetPasswordEntryRepository) {
    this.userRepository = repository;
    this.resetPasswordEntryRepository = resetPasswordEntryRepository;
  }

  @Override
  public boolean forgotPassword(Long userId) {
    if (!userRepository.existsById(userId)) {
      throw new UserNotFoundException("user with id " + userId + " not found");
    }
    generateForgotPasswordValidationCode(userId);
    return true;
  }

  private void generateForgotPasswordValidationCode(Long userId) {
    String key = userId.toString();
    ResetPasswordEntry entry =
        ResetPasswordEntry.builder()
            .id(userId)
            .code(randomCode())
            .validated(false)
            .ttl(expire)
            .build();
    resetPasswordEntryRepository.save(entry);
  }

  private String randomCode() {
    Random random = new Random();
    long code = random.nextLong(900000) + 100000;
    return Long.toString(code);
  }

  @Override
  public boolean validateForgotPasswordCode(Long userId, String code) {
    if (!userRepository.existsById(userId)) {
      throw new UserNotFoundException("user with id " + userId + " not found");
    }
    return doValidateForgotPasswordCode(userId, code);
  }

  private boolean doValidateForgotPasswordCode(Long userId, String code) {
    Optional<ResetPasswordEntry> optionalEntry = resetPasswordEntryRepository.findById(userId);
    if (optionalEntry.isEmpty()) {
      return false;
    }
    ResetPasswordEntry entry = optionalEntry.get();
    if (isCodeCorrect(code, entry)) {
      setEntryCodeValidated(entry);
      return true;
    }
    return false;
  }

  private boolean isCodeCorrect(String code, ResetPasswordEntry entry) {
    return entry.getCode().equals(code);
  }

  private void setEntryCodeValidated(ResetPasswordEntry entry) {
    entry.setValidated(true);
    resetPasswordEntryRepository.save(entry);
  }
}
