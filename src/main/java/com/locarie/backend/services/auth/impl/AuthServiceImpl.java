package com.locarie.backend.services.auth.impl;

import com.locarie.backend.domain.redis.ResetPasswordEntry;
import com.locarie.backend.exceptions.NotAuthorizedOperationException;
import com.locarie.backend.exceptions.UserNotFoundException;
import com.locarie.backend.repositories.redis.ResetPasswordEntryRepository;
import com.locarie.backend.repositories.user.UserRepository;
import com.locarie.backend.services.auth.AuthService;
import com.locarie.backend.services.mail.MailService;
import java.util.Optional;
import java.util.Random;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
  @Value("${validation.code.expire:300}")
  private long expire;

  private final UserRepository userRepository;
  private final ResetPasswordEntryRepository resetPasswordEntryRepository;
  private final MailService mailService;

  public AuthServiceImpl(
      UserRepository repository,
      ResetPasswordEntryRepository resetPasswordEntryRepository,
      MailService mailService) {
    this.userRepository = repository;
    this.resetPasswordEntryRepository = resetPasswordEntryRepository;
    this.mailService = mailService;
  }

  @Override
  public boolean forgotPassword(String email) {
    if (!userRepository.existsByEmail(email)) {
      throw new UserNotFoundException("user with email " + email + " not found");
    }
    String code = generateAndSaveRandomCode(email, false);
    sendValidationCodeEmail(email, code);
    return true;
  }

  private String generateAndSaveRandomCode(String email, boolean validated) {
    String code = randomCode();
    ResetPasswordEntry entry =
        ResetPasswordEntry.builder()
            .email(email)
            .code(code)
            .validated(validated)
            .ttl(expire)
            .build();
    resetPasswordEntryRepository.save(entry);
    return code;
  }

  private String randomCode() {
    Random random = new Random();
    long code = random.nextLong(900000) + 100000;
    return Long.toString(code);
  }

  @Override
  public boolean validateForgotPasswordCode(String email, String code) {
    throwIfUserNotExists(email);
    return doValidateForgotPasswordCode(email, code);
  }

  private void throwIfUserNotExists(String email) {
    if (!userRepository.existsByEmail(email)) {
      throw new UserNotFoundException("user with email " + email + " not found");
    }
  }

  private boolean doValidateForgotPasswordCode(String email, String code) {
    Optional<ResetPasswordEntry> optional = resetPasswordEntryRepository.findById(email);
    if (optional.isEmpty()) {
      return false;
    }
    if (!isCodeCorrect(code, optional.get())) {
      return false;
    }
    setEntryCodeValidated(optional.get());
    return true;
  }

  private boolean isCodeCorrect(String code, ResetPasswordEntry entry) {
    return entry.getCode().equals(code);
  }

  private void setEntryCodeValidated(ResetPasswordEntry entry) {
    entry.setValidated(true);
    resetPasswordEntryRepository.save(entry);
  }

  private void sendValidationCodeEmail(String email, String code) {
    mailService.sendMail(
        email, "Validation code from locarie.", "Hello, your validation code is: " + code + ".");
  }

  @Override
  public boolean resetPassword(String email, String password) {
    throwIfUserNotExists(email);
    throwIfResetPasswordEntryCodeNotValidated(email);
    doResetPassword(email, password);
    deleteResetPasswordEntry(email);
    return true;
  }

  @Override
  public boolean validatePassword(String email, String password) {
    throwIfUserNotExists(email);
    if (!userRepository.existsByEmailAndPassword(email, password)) {
      return false;
    }
    generateAndSaveRandomCode(email, true);
    return true;
  }

  private void throwIfResetPasswordEntryCodeNotValidated(String email) {
    if (!checkResetPasswordEntryCodeValidated(email)) {
      throw new NotAuthorizedOperationException("unauthorized operation");
    }
  }

  private boolean checkResetPasswordEntryCodeValidated(String email) {
    Optional<ResetPasswordEntry> optional = resetPasswordEntryRepository.findById(email);
    return optional.map(ResetPasswordEntry::isValidated).orElse(false);
  }

  private void doResetPassword(String email, String password) {
    userRepository.updatePassword(email, password);
  }

  private void deleteResetPasswordEntry(String email) {
    resetPasswordEntryRepository.deleteById(email);
  }
}
