package com.locarie.backend.services.impl.user;

import com.locarie.backend.domain.dto.UserDto;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.exceptions.UserNotFoundException;
import com.locarie.backend.mapper.Mapper;
import com.locarie.backend.repositories.UserRepository;
import com.locarie.backend.services.user.UserAvatarService;
import com.locarie.backend.services.utils.UserFindUtils;
import com.locarie.backend.storage.StorageService;
import com.locarie.backend.storage.exceptions.StorageException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service("UserAvatarService")
public class UserAvatarServiceImpl implements UserAvatarService {
  private final UserRepository userRepository;
  private final UserFindUtils userFindUtils;
  private final StorageService storageService;
  private final Mapper<UserEntity, UserDto> mapper;

  public UserAvatarServiceImpl(
      UserRepository userRepository,
      UserFindUtils userFindUtils,
      StorageService storageService,
      Mapper<UserEntity, UserDto> mapper) {
    this.userRepository = userRepository;
    this.userFindUtils = userFindUtils;
    this.storageService = storageService;
    this.mapper = mapper;
  }

  @Override
  public UserDto update(Long userId, MultipartFile avatar)
      throws StorageException, UserNotFoundException {
    try {
      UserEntity userEntity = userFindUtils.findUserById(userId);
      Path avatarPath = storeAvatar(userId, avatar);
      updateUserAvatarUrl(userEntity, avatarPath);
      return entityToDto(userEntity);
    } catch (UserNotFoundException e) {
      throw handleUserNotFoundException(e);
    }
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

  @Override
  public byte[] getAvatar(Long userId) throws UserNotFoundException {
    try {
      UserEntity userEntity = userFindUtils.findUserById(userId);
      Path avatarPath = getAvatarPath(userEntity);
      return readAvatar(avatarPath);
    } catch (UserNotFoundException e) {
      throw handleUserNotFoundException(e);
    } catch (IOException e) {
      return handleReadAvatarException();
    }
  }

  private Path getAvatarPath(UserEntity userEntity) {
    return Path.of(userEntity.getAvatarUrl());
  }

  private byte[] readAvatar(Path avatarPath) throws IOException {
    return Files.readAllBytes(avatarPath);
  }

  private UserNotFoundException handleUserNotFoundException(UserNotFoundException e) {
    throw e;
  }

  private byte[] handleReadAvatarException() {
    return new byte[0];
  }
}
