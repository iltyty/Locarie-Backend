package com.locarie.backend.datacreators.user;

import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.repositories.UserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserTestsDataCreator {
  @Autowired private UserRepository userRepository;

  public Long givenBusinessUserJoleneHornseyIdAfterCreated() {
    UserEntity userEntity = UserEntityCreator.businessUserEntityJoleneHornsey();
    userEntity.setId(null);
    if (!userRepository.existsByEmail(userEntity.getEmail())) {
      return userRepository.save(userEntity).getId();
    }
    Optional<UserEntity> optionalUserEntity = userRepository.findByEmail(userEntity.getEmail());
    assert optionalUserEntity.isPresent();
    return optionalUserEntity.get().getId();
  }

  public Long givenBusinessUserShreejiIdAfterCreated() {
    UserEntity userEntity = UserEntityCreator.businessUserEntityShreeji();
    userEntity.setId(null);
    if (!userRepository.existsByEmail(userEntity.getEmail())) {
      return userRepository.save(userEntity).getId();
    }
    Optional<UserEntity> optionalUserEntity = userRepository.findByEmail(userEntity.getEmail());
    assert optionalUserEntity.isPresent();
    return optionalUserEntity.get().getId();
  }
}
