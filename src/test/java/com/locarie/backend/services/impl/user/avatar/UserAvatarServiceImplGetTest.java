package com.locarie.backend.services.impl.user.avatar;

import static org.assertj.core.api.Assertions.assertThat;

import com.locarie.backend.datacreators.user.UserEntityCreator;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.repositories.UserRepository;
import com.locarie.backend.services.impl.user.UserAvatarServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class UserAvatarServiceImplGetTest {
  @Autowired private UserAvatarServiceImpl underTests;
  @Autowired private UserRepository userRepository;

  @Test
  void testGetUserAvatarShouldSucceed() {
    Long userId = givenUserIdAfterCreated();
    byte[] avatar = whenGetAvatar(userId);
    thenResultShouldBeEmpty(avatar);
  }

  private Long givenUserIdAfterCreated() {
    UserEntity userEntity = UserEntityCreator.plainUserEntity();
    return userRepository.save(userEntity).getId();
  }

  private byte[] whenGetAvatar(Long userId) {
    return underTests.getAvatar(userId);
  }

  private void thenResultShouldBeEmpty(byte[] avatar) {
    assertThat(avatar).isEmpty();
  }
}
