package com.locarie.backend.services.impl.user.avatar;

import static org.assertj.core.api.Assertions.assertThat;

import com.locarie.backend.datacreators.image.MockAvatarCreator;
import com.locarie.backend.datacreators.user.UserEntityCreator;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.repositories.UserRepository;
import com.locarie.backend.services.impl.user.UserAvatarServiceImpl;
import jakarta.transaction.Transactional;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
@Transactional
public class UserAvatarServiceImplIntegrationTest {
  @Autowired private UserAvatarServiceImpl underTests;
  @Autowired private UserRepository userRepository;

  @Test
  void testUploadAndGetAvatarShouldSucceed() throws IOException {
    Long userId = givenUserIdAfterCreated();
    MultipartFile avatar = givenAvatar();
    byte[] result = whenGetAvatarAfterUploaded(userId, avatar);
    thenResultShouldEqualToOriginalAvatarBytes(result, avatar);
  }

  private Long givenUserIdAfterCreated() {
    UserEntity userEntity = UserEntityCreator.plainUserEntity();
    return userRepository.save(userEntity).getId();
  }

  private MultipartFile givenAvatar() {
    return MockAvatarCreator.pngAvatar();
  }

  private byte[] whenGetAvatarAfterUploaded(Long userId, MultipartFile avatar) {
    underTests.update(userId, avatar);
    return underTests.getAvatar(userId);
  }

  private void thenResultShouldEqualToOriginalAvatarBytes(byte[] result, MultipartFile avatar)
      throws IOException {
    assertThat(result).isEqualTo(avatar.getBytes());
  }
}
