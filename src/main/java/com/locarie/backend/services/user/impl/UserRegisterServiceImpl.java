package com.locarie.backend.services.user.impl;

import com.locarie.backend.domain.dto.user.UserDto;
import com.locarie.backend.domain.dto.user.UserRegistrationDto;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.mapper.Mapper;
import com.locarie.backend.repositories.user.UserRepository;
import com.locarie.backend.services.mail.MailService;
import com.locarie.backend.services.user.UserRegisterService;
import lombok.extern.log4j.Log4j2;
import org.checkerframework.checker.units.qual.A;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
@Log4j2
public class UserRegisterServiceImpl implements UserRegisterService {
  private final UserRepository repository;
  private final Mapper<UserEntity, UserDto> mapper;
  private final MailService emailService;
  private final ResourceLoader resourceLoader;

  public UserRegisterServiceImpl(UserRepository repository, Mapper<UserEntity, UserDto> mapper, MailService emailService, ResourceLoader resourceLoader) {
    this.repository = repository;
    this.mapper = mapper;
    this.emailService = emailService;
    this.resourceLoader = resourceLoader;
  }

  @Override
  public UserDto register(UserRegistrationDto dto) {
    if (repository.findByEmail(dto.getEmail()).isPresent()) {
      return null;
    }
    UserEntity user = mapper.mapFrom(dto);
    UserEntity savedUser = repository.save(user);
    sendRegistrationSuccessEmail(user.getEmail(), user.getFirstName());
    return mapper.mapTo(savedUser);
  }

  private void sendRegistrationSuccessEmail(String email, String firstName) {
    String subject = String.format("Welcome to Locarie, %s!", firstName);
    String body = readWelcomeText(firstName);
    emailService.sendMail(email, subject, body);
  }

  private String readWelcomeText(String firstName) {
    try {
      Resource resource = resourceLoader.getResource("classpath:welcome.txt");
      String body  = new String(Files.readAllBytes(Paths.get(resource.getURI())));
      return String.format(body, firstName, firstName);
    } catch (IOException e) {
      log.error(e.getMessage());
      return "Welcome to Locarie!";
    }
  }
}
