package com.locarie.backend.datacreators.user;

import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.repositories.user.UserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserTestsDataCreator {
  @Autowired private UserRepository userRepository;

  public UserEntity givenPlainUserAfterCreated() {
    UserEntity userEntity = UserEntityCreator.plainUserEntity();
    userEntity.setId(null);
    if (!userRepository.existsByEmail(userEntity.getEmail())) {
      return userRepository.save(userEntity);
    }
    Optional<UserEntity> optionalUserEntity = userRepository.findByEmail(userEntity.getEmail());
    assert optionalUserEntity.isPresent();
    return optionalUserEntity.get();
  }

  public UserEntity givenBusinessUserJoleneHornseyAfterCreated() {
    UserEntity userEntity = UserEntityCreator.businessUserEntityJoleneHornsey();
    userEntity.setId(null);
    if (!userRepository.existsByEmail(userEntity.getEmail())) {
      return userRepository.save(userEntity);
    }
    Optional<UserEntity> optionalUserEntity = userRepository.findByEmail(userEntity.getEmail());
    assert optionalUserEntity.isPresent();
    return optionalUserEntity.get();
  }

  public UserEntity givenBusinessUserShreejiAfterCreated() {
    UserEntity userEntity = UserEntityCreator.businessUserEntityShreeji();
    userEntity.setId(null);
    if (!userRepository.existsByEmail(userEntity.getEmail())) {
      return userRepository.save(userEntity);
    }
    Optional<UserEntity> optionalUserEntity = userRepository.findByEmail(userEntity.getEmail());
    assert optionalUserEntity.isPresent();
    return optionalUserEntity.get();
  }
}
