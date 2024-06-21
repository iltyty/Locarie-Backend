package com.locarie.backend.services.user.impl;

import com.locarie.backend.domain.dto.user.UserDto;
import com.locarie.backend.domain.dto.user.UserRegistrationDto;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.mapper.Mapper;
import com.locarie.backend.repositories.user.UserRepository;
import com.locarie.backend.services.mail.MailService;
import com.locarie.backend.services.user.UserRegisterService;
import org.springframework.stereotype.Service;

@Service
public class UserRegisterServiceImpl implements UserRegisterService {
  private final UserRepository repository;
  private final Mapper<UserEntity, UserDto> mapper;
  private final MailService emailService;

  public UserRegisterServiceImpl(UserRepository repository, Mapper<UserEntity, UserDto> mapper, MailService emailService) {
    this.repository = repository;
    this.mapper = mapper;
    this.emailService = emailService;
  }

  @Override
  public UserDto register(UserRegistrationDto dto) {
    if (repository.findByEmail(dto.getEmail()).isPresent()) {
      return null;
    }
    UserEntity user = mapper.mapFrom(dto);
    UserEntity savedUser = repository.save(user);
    sendRegistrationSuccessEmail(user.getEmail());
    return mapper.mapTo(savedUser);
  }

  private void sendRegistrationSuccessEmail(String email) {
    emailService.sendMail(email, "Registration Success", "Welcome to Locarie!");
  }
}
