package com.locarie.backend.services.impl.user.avatar;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.locarie.backend.datacreators.image.MockAvatarCreator;
import com.locarie.backend.datacreators.user.UserEntityCreator;
import com.locarie.backend.domain.dto.user.UserDto;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.exceptions.UserNotFoundException;
import com.locarie.backend.repositories.UserRepository;
import com.locarie.backend.services.impl.user.UserAvatarServiceImpl;
import com.locarie.backend.storage.exceptions.StorageException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

@SpringBootTest
@Transactional
public class UserAvatarServiceImplUploadTest {
  @Autowired private UserAvatarServiceImpl underTests;
  @Autowired private UserRepository userRepository;

  @Test
  void testUploadAvatarShouldSucceed() {
    Long userId = givenUserIdAfterCreated();
    MockMultipartFile avatar = givenAvatar();
    UserDto userDto = whenUpdateAvatar(userId, avatar);
    thenResultShouldContainUpdatedAvatarUrl(userDto, avatar);
  }

  @Test
  void testUploadAvatarOfNonExistentUserShouldFail() {
    Long userId = givenNonExistentUserId();
    MockMultipartFile avatar = givenAvatar();
    thenResultShouldThrowUserNotFoundExceptionWhenUpdateAvatar(userId, avatar);
  }

  @Test
  void testUploadAvatarWithNoFilenameShouldFail() {
    Long userId = givenUserIdAfterCreated();
    MockMultipartFile avatar = MockAvatarCreator.avatarWithNoFilename();
    thenResultShouldThrowStorageExceptionWhenUpdateAvatar(userId, avatar);
  }

  @Test
  void testUploadEmptyAvatarShouldFail() {
    Long userId = givenUserIdAfterCreated();
    MockMultipartFile avatar = MockAvatarCreator.emptyAvatar();
    thenResultShouldThrowStorageExceptionWhenUpdateAvatar(userId, avatar);
  }

  private Long givenUserIdAfterCreated() {
    UserEntity userEntity = UserEntityCreator.plainUserEntity();
    return userRepository.save(userEntity).getId();
  }

  private Long givenNonExistentUserId() {
    return 0L;
  }

  private MockMultipartFile givenAvatar() {
    return MockAvatarCreator.jpgAvatar();
  }

  private UserDto whenUpdateAvatar(Long userId, MockMultipartFile avatar) {
    return underTests.updateAvatar(userId, avatar);
  }

  private void thenResultShouldContainUpdatedAvatarUrl(UserDto userDto, MockMultipartFile avatar) {
    assertThat(userDto).isNotNull();
    assertThat(userDto.getAvatarUrl()).isNotEmpty();
    assertThat(userDto.getAvatarUrl()).contains(avatar.getOriginalFilename());
  }

  private void thenResultShouldThrowUserNotFoundExceptionWhenUpdateAvatar(
      Long userId, MockMultipartFile avatar) {
    assertThatExceptionOfType(UserNotFoundException.class)
        .isThrownBy(
            () -> {
              whenUpdateAvatar(userId, avatar);
            });
  }

  private void thenResultShouldThrowStorageExceptionWhenUpdateAvatar(
      Long userId, MockMultipartFile avatar) {
    assertThatExceptionOfType(StorageException.class)
        .isThrownBy(
            () -> {
              whenUpdateAvatar(userId, avatar);
            });
  }
}
