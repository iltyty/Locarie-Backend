package com.locarie.backend.services.utils;

import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.exceptions.UserNotFoundException;
import com.locarie.backend.repositories.user.UserRepository;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class UserFindUtils {
  private final UserRepository userRepository;

  public UserFindUtils(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public UserEntity findUserById(Long userId) throws UserNotFoundException {
    Optional<UserEntity> optionalUserEntity = userRepository.findById(userId);
    return optionalUserEntity.orElseThrow(
        () -> new UserNotFoundException("User with id" + userId + " not found"));
  }
}
