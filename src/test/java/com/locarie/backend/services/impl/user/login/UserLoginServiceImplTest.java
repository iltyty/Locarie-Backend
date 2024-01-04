package com.locarie.backend.services.impl.user.login;

import static org.assertj.core.api.Assertions.assertThat;

import com.locarie.backend.datacreators.user.UserEntityCreator;
import com.locarie.backend.datacreators.user.UserLoginRequestDtoCreator;
import com.locarie.backend.domain.dto.user.UserLoginRequestDto;
import com.locarie.backend.domain.dto.user.UserLoginResponseDto;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.repositories.UserRepository;
import com.locarie.backend.services.user.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class UserLoginServiceImplTest {
  @Autowired private UserRepository userRepository;
  @Autowired private UserService underTests;

  @Test
  void loginWithCorrectCredentialShouldSucceed() {
    UserLoginRequestDto loginDto = givenCorrectLoginCredentialAfterCreated();
    UserLoginResponseDto result = whenLogin(loginDto);
    thenLoginResultShouldBeSuccess(result);
  }

  @Test
  void loginWithIncorrectCredentialShouldFail() {
    UserLoginRequestDto loginDto = givenIncorrectLoginCredential();
    UserLoginResponseDto result = whenLogin(loginDto);
    thenLoginResultShouldBeFailure(result);
  }

  private UserLoginRequestDto givenCorrectLoginCredentialAfterCreated() {
    UserEntity userEntity = UserEntityCreator.plainUserEntity();
    userRepository.save(userEntity);
    return UserLoginRequestDtoCreator.plainUserLoginRequestDto();
  }

  private UserLoginRequestDto givenIncorrectLoginCredential() {
    return UserLoginRequestDtoCreator.incorrectUserLoginRequestDto();
  }

  private UserLoginResponseDto whenLogin(UserLoginRequestDto loginDto) {
    return underTests.login(loginDto);
  }

  private void thenLoginResultShouldBeSuccess(UserLoginResponseDto result) {
    assertThat(result).isNotNull();
    assertThat(result.getId()).isPositive();
    assertThat(result.getUsername()).isNotEmpty();
    assertThat(result.getJwtToken()).isNotEmpty();
  }

  private void thenLoginResultShouldBeFailure(UserLoginResponseDto result) {
    assertThat(result).isNull();
  }
}
