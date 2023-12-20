package com.locarie.backend.services.impl.user;

import com.locarie.backend.domain.dto.EmptyUserDto;
import com.locarie.backend.domain.dto.UserDto;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.exceptions.UserNotFoundException;
import com.locarie.backend.mapper.Mapper;
import com.locarie.backend.repositories.UserRepository;
import com.locarie.backend.services.user.UserAvatarService;
import com.locarie.backend.storage.StorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.Optional;

@Service("UserAvatarService")
public class UserAvatarServiceImpl implements UserAvatarService {
  private final UserRepository userRepository;
  private final StorageService storageService;
  private final Mapper<UserEntity, UserDto> mapper;

  public UserAvatarServiceImpl(UserRepository userRepository, StorageService storageService, Mapper<UserEntity, UserDto> mapper) {
    this.userRepository = userRepository;
    this.storageService = storageService;
    this.mapper = mapper;
  }

  @Override
  public UserDto updateAvatar(Long userId, MultipartFile avatar) {
    try {
      UserEntity userEntity = findUserById(userId);
      Path avatarPath = storeAvatar(userId, avatar);
      updateUserAvatarUrl(userEntity, avatarPath);
      return entityToDto(userEntity);
    } catch (UserNotFoundException e) {
      return handleUserNotFound();
    }
  }

  private UserEntity findUserById(Long userId) {
    Optional<UserEntity> optionalUserEntity = userRepository.findById(userId);
    return optionalUserEntity.orElseThrow(() -> new UserNotFoundException("User with id" + userId + " not found"));
  }

  private Path storeAvatar(Long userId, MultipartFile avatar) {
    String dirname = String.format("user_%d/avatar", userId);
    return storageService.store(avatar, dirname);
  }

  private void updateUserAvatarUrl(UserEntity userEntity, Path avatarPath) {
    userEntity.setAvatarUrl(avatarPath.toString());
    userRepository.save(userEntity);
  }

  private UserDto entityToDto(UserEntity userEntity) {
    return mapper.mapTo(userEntity);
  }

  private UserDto handleUserNotFound() {
    return new EmptyUserDto();
  }
}
