package com.locarie.backend.services.user.impl;

import com.locarie.backend.domain.dto.user.UserDto;
import com.locarie.backend.domain.dto.user.UserRegistrationDto;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.mapper.Mapper;
import com.locarie.backend.repositories.user.UserRepository;
import com.locarie.backend.services.mail.MailService;
import com.locarie.backend.services.user.UserRegisterService;
import java.io.IOException;
import java.io.InputStream;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class UserRegisterServiceImpl implements UserRegisterService {
  private final UserRepository repository;
  private final Mapper<UserEntity, UserDto> mapper;
  private final MailService emailService;

  public UserRegisterServiceImpl(
      UserRepository repository, Mapper<UserEntity, UserDto> mapper, MailService emailService) {
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
    if (user.getType() != UserEntity.Type.BUSINESS) {
      sendRegistrationSuccessEmail(user.getEmail(), user.getFirstName());
    }
    return mapper.mapTo(savedUser);
  }

  private void sendRegistrationSuccessEmail(String email, String firstName) {
    String subject = String.format("Welcome to Locarie, %s!", firstName);
    String body = readWelcomeText(firstName);
    emailService.sendMail(email, subject, body);
  }

  private String readWelcomeText(String firstName) {
    try {
      ClassPathResource resource = new ClassPathResource("classpath:welcome.txt");
      InputStream inputStream = resource.getInputStream();
      String body = new String(inputStream.readAllBytes());
      return String.format(body, firstName);
    } catch (IOException e) {
      log.error(e.getMessage());
      return "Welcome to Locarie!";
    }
  }
}
