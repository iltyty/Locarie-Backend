package com.locarie.backend.services.impl.user.avatar;

import static org.assertj.core.api.Assertions.assertThat;

import com.locarie.backend.domain.dto.UserDto;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.repositories.UserRepository;
import com.locarie.backend.services.impl.user.UserAvatarServiceImpl;
import com.locarie.backend.utils.MockAvatarCreator;
import com.locarie.backend.utils.user.UserEntityCreator;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

@SpringBootTest
@Transactional
public class UserAvatarServiceImplTest {
  @Autowired private UserAvatarServiceImpl underTests;
  @Autowired private UserRepository userRepository;

  @Test
  void testUploadAvatarShouldSucceed() {
    Long userId = givenUserIdAfterCreated();
    MockMultipartFile avatar = givenAvatar();
    UserDto userDto = whenUpdateAvatar(userId, avatar);
    thenResultShouldContainUpdatedAvatarUrl(userDto, avatar);
  }

  private Long givenUserIdAfterCreated() {
    UserEntity userEntity = UserEntityCreator.plainUserEntity();
    return userRepository.save(userEntity).getId();
  }

  private MockMultipartFile givenAvatar() {
    return MockAvatarCreator.jpgAvatar();
  }

  private UserDto whenUpdateAvatar(Long userId, MockMultipartFile avatar) {
    return underTests.update(userId, avatar);
  }

  private void thenResultShouldContainUpdatedAvatarUrl(UserDto userDto, MockMultipartFile avatar) {
    assertThat(userDto).isNotNull();
    assertThat(userDto.getAvatarUrl()).isNotEmpty();
    assertThat(userDto.getAvatarUrl()).contains(avatar.getOriginalFilename());
  }
}
