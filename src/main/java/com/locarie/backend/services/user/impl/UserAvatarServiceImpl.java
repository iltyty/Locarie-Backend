package com.locarie.backend.services.user.impl;

import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.exceptions.UserNotFoundException;
import com.locarie.backend.repositories.user.UserRepository;
import com.locarie.backend.services.user.UserAvatarService;
import com.locarie.backend.services.utils.UserFindUtils;
import com.locarie.backend.storage.StorageService;
import com.locarie.backend.storage.exceptions.StorageException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service("UserAvatarService")
public class UserAvatarServiceImpl implements UserAvatarService {
  private final UserRepository userRepository;
  private final UserFindUtils userFindUtils;
  private final StorageService storageService;

  public UserAvatarServiceImpl(
      UserRepository userRepository, UserFindUtils userFindUtils, StorageService storageService) {
    this.userRepository = userRepository;
    this.userFindUtils = userFindUtils;
    this.storageService = storageService;
  }

  @Override
  public String updateAvatar(Long userId, MultipartFile avatar)
      throws StorageException, UserNotFoundException {
    UserEntity userEntity = userFindUtils.findUserById(userId);
    String avatarUrl = storeAvatar(userId, avatar);
    updateUserAvatarUrl(userEntity, avatarUrl);
    return avatarUrl;
  }

  private String storeAvatar(Long userId, MultipartFile avatar) {
    String dirname = String.format("user_%d/avatar", userId);
    return storageService.store(avatar, dirname);
  }

  private void updateUserAvatarUrl(UserEntity userEntity, String avatarPath) {
    userEntity.setAvatarUrl(avatarPath);
    userRepository.save(userEntity);
  }
}
