package com.locarie.backend.datacreators.user;

import com.locarie.backend.domain.dto.user.UserDto;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.mapper.Mapper;
import com.locarie.backend.repositories.user.UserRepository;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class UserTestsDataCreator {
  private final UserRepository repository;
  private final Mapper<UserEntity, UserDto> mapper;

  public UserTestsDataCreator(UserRepository userRepository, Mapper<UserEntity, UserDto> mapper) {
    this.repository = userRepository;
    this.mapper = mapper;
  }

  public UserDto givenPlainUserAfterCreated() {
    UserEntity userEntity = UserEntityCreator.plainUserEntity();
    userEntity.setId(null);
    if (!repository.existsByEmail(userEntity.getEmail())) {
      return mapper.mapTo(repository.save(userEntity));
    }
    Optional<UserEntity> optionalUserEntity = repository.findByEmail(userEntity.getEmail());
    assert optionalUserEntity.isPresent();
    return mapper.mapTo(optionalUserEntity.get());
  }

  public UserDto givenBusinessUserJoleneHornseyAfterCreated() {
    UserEntity userEntity = UserEntityCreator.businessUserEntityJoleneHornsey();
    userEntity.setId(null);
    if (!repository.existsByEmail(userEntity.getEmail())) {
      return mapper.mapTo(repository.save(userEntity));
    }
    Optional<UserEntity> optionalUserEntity = repository.findByEmail(userEntity.getEmail());
    assert optionalUserEntity.isPresent();
    return mapper.mapTo(optionalUserEntity.get());
  }

  public UserDto givenBusinessUserShreejiAfterCreated() {
    UserEntity userEntity = UserEntityCreator.businessUserEntityShreeji();
    userEntity.setId(null);
    if (!repository.existsByEmail(userEntity.getEmail())) {
      return mapper.mapTo(repository.save(userEntity));
    }
    Optional<UserEntity> optionalUserEntity = repository.findByEmail(userEntity.getEmail());
    assert optionalUserEntity.isPresent();
    return mapper.mapTo(optionalUserEntity.get());
  }
}
