package com.locarie.backend.services.impl.user;

import com.locarie.backend.domain.dto.user.UserDto;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.mapper.Mapper;
import com.locarie.backend.repositories.user.UserRepository;
import com.locarie.backend.services.user.UserGetService;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UserGetServiceImpl implements UserGetService {

  private final UserRepository repository;
  private final Mapper<UserEntity, UserDto> mapper;

  public UserGetServiceImpl(UserRepository repository, Mapper<UserEntity, UserDto> mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public Optional<UserDto> get(Long id) {
    Optional<UserEntity> result = repository.findById(id);
    return result.map(mapper::mapTo);
  }
}
