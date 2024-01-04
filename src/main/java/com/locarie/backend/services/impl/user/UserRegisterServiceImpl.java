package com.locarie.backend.services.impl.user;

import com.locarie.backend.domain.dto.user.UserDto;
import com.locarie.backend.domain.dto.user.UserRegistrationDto;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.mapper.Mapper;
import com.locarie.backend.repositories.UserRepository;
import com.locarie.backend.services.user.UserRegisterService;
import org.springframework.stereotype.Service;

@Service
public class UserRegisterServiceImpl implements UserRegisterService {
  private final UserRepository repository;
  private final Mapper<UserEntity, UserDto> mapper;

  public UserRegisterServiceImpl(UserRepository repository, Mapper<UserEntity, UserDto> mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public UserDto register(UserRegistrationDto dto) {
    if (repository.findByEmail(dto.getEmail()).isPresent()) {
      return null;
    }
    UserEntity user = mapper.mapFrom(dto);
    UserEntity savedUser = repository.save(user);
    return mapper.mapTo(savedUser);
  }
}
