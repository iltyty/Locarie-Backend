package com.locarie.backend.services.impl.user;

import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.exceptions.UserNotFoundException;
import com.locarie.backend.repositories.user.UserRepository;
import com.locarie.backend.services.user.UserProfileImagesService;
import com.locarie.backend.services.utils.UserFindUtils;
import com.locarie.backend.storage.StorageService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserProfileImagesServiceImpl implements UserProfileImagesService {
  private final UserFindUtils userFindUtils;
  private final StorageService storageService;
  private final UserRepository userRepository;

  public UserProfileImagesServiceImpl(
      UserFindUtils userFindUtils, StorageService storageService, UserRepository userRepository) {
    this.userFindUtils = userFindUtils;
    this.storageService = storageService;
    this.userRepository = userRepository;
  }

  @Override
  public java.util.List<String> uploadProfileImages(Long id, MultipartFile[] images) {
    try {
      UserEntity userEntity = userFindUtils.findUserById(id);
      List<String> profileImageUrls = storeProfileImages(id, images);
      updateProfileImages(userEntity, profileImageUrls);
      return profileImageUrls;
    } catch (UserNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  private List<String> storeProfileImages(Long userId, MultipartFile[] images) {
    String dirname = String.format("user_%d/profile_images", userId);
    return Arrays.stream(images)
        .map(image -> storageService.store(image, dirname))
        .collect(Collectors.toCollection(ArrayList::new));
  }

  private void updateProfileImages(UserEntity userEntity, List<String> profileImageUrls) {
    userEntity.setProfileImageUrls(profileImageUrls);
    userRepository.save(userEntity);
  }
}
