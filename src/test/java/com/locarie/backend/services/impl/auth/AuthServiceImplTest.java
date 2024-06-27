package com.locarie.backend.services.impl.auth;

import com.locarie.backend.datacreators.user.UserEntityCreator;
import com.locarie.backend.domain.dto.user.UserDto;
import com.locarie.backend.domain.entities.UserEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.locarie.backend.datacreators.user.UserTestsDataCreator;
import com.locarie.backend.domain.redis.ResetPasswordEntry;
import com.locarie.backend.exceptions.NotAuthorizedOperationException;
import com.locarie.backend.exceptions.UserNotFoundException;
import com.locarie.backend.repositories.redis.ResetPasswordEntryRepository;
import com.locarie.backend.services.auth.impl.AuthServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class AuthServiceImplTest {
  @Autowired private AuthServiceImpl underTests;
  @Autowired private ResetPasswordEntryRepository repository;
  @Autowired private UserTestsDataCreator dataCreator;

  @BeforeAll
  static void startRedisContainer() {
    GenericContainer<?> container =
        new GenericContainer<>(DockerImageName.parse("redis:6.0-alpine")).withExposedPorts(6379);
    container.start();
    System.setProperty("spring.data.redis.host", container.getHost());
    System.setProperty("spring.data.redis.port", container.getMappedPort(6379).toString());
  }

  @Test
  void testForgotPasswordShouldGenerateOneCodeInRedis() {
    String email = dataCreator.givenBusinessUserJoleneHornseyAfterCreated().getEmail();
    boolean result = underTests.forgotPassword(email);
    assertThat(result).isTrue();
    thenResetPasswordCodeShouldBeNotValidated(email);
  }

  @Test
  void testForgotPasswordForNonExistingUserShouldThrow() {
    assertThatThrownBy(() -> underTests.forgotPassword("non-existent@email.com"))
        .isInstanceOf(UserNotFoundException.class);
  }

  @Test
  void testValidateForgotPasswordCodeAfterGenerationShouldSucceed() {
    String email = dataCreator.givenBusinessUserJoleneHornseyAfterCreated().getEmail();
    underTests.forgotPassword(email);

    String code = repository.findById(email).get().getCode();
    boolean result = underTests.validateForgotPasswordCode(email, code);
    assertThat(result).isTrue();
    thenResetPasswordCodeShouldBeValidated(email);
  }

  @Test
  void testValidateForgotPasswordCodeAfterExpiredShouldFail() {
    String email = dataCreator.givenBusinessUserJoleneHornseyAfterCreated().getEmail();
    underTests.forgotPassword(email);

    String code = repository.findById(email).get().getCode();
    expireCode(email);

    boolean result = underTests.validateForgotPasswordCode(email, code);
    assertThat(result).isFalse();
  }

  @Test
  void testResetPasswordAfterValidatedShouldSucceed() {
    String email = dataCreator.givenBusinessUserJoleneHornseyAfterCreated().getEmail();
    underTests.forgotPassword(email);

    String code = repository.findById(email).get().getCode();
    underTests.validateForgotPasswordCode(email, code);

    String password = "88888888";
    boolean result = underTests.resetPassword(email, password);

    assertThat(result).isTrue();
    assertThat(repository.existsById(email)).isFalse();
  }

  @Test
  void testResetPasswordWithoutForgotShouldFail() {
    String email = dataCreator.givenBusinessUserJoleneHornseyAfterCreated().getEmail();
    String password = "88888888";
    assertThatThrownBy(() -> underTests.resetPassword(email, password))
        .isInstanceOf(NotAuthorizedOperationException.class);
  }

  @Test
  void testResetPasswordWithoutForgotValidationShouldFail() {
    String email = dataCreator.givenBusinessUserJoleneHornseyAfterCreated().getEmail();
    underTests.forgotPassword(email);

    String password = "88888888";
    assertThatThrownBy(() -> underTests.resetPassword(email, password))
        .isInstanceOf(NotAuthorizedOperationException.class);
  }

  @Test
  void testValidateCorrectPasswordShouldSucceed() {
    UserEntity userEntity = UserEntityCreator.businessUserEntityJoleneHornsey();
    UserDto userDto = dataCreator.givenBusinessUserJoleneHornseyAfterCreated();
    String result = underTests.validatePassword(userDto.getEmail(), userEntity.getPassword());
    assertThat(result).isNotEmpty();
  }

  @Test
  void testValidateIncorrectPasswordShouldFail() {
    UserDto userDto = dataCreator.givenBusinessUserJoleneHornseyAfterCreated();
    String result = underTests.validatePassword(userDto.getEmail(), "incorrect-password");
    assertThat(result).isEmpty();
  }

  @Test
  void testResetPasswordWithPasswordValidationShouldSucceed() {
    String email = dataCreator.givenBusinessUserJoleneHornseyAfterCreated().getEmail();
    String code = underTests.validatePassword(email, "88888888");
    boolean result = underTests.resetPassword(email, code);
    assertThat(result).isTrue();
  }

  @Test
  void testResetPasswordWithoutPasswordValidationShouldFail() {
    String email = dataCreator.givenBusinessUserJoleneHornseyAfterCreated().getEmail();
    assertThatThrownBy(() -> underTests.resetPassword(email, "88888888"))
        .isInstanceOf(NotAuthorizedOperationException.class);
  }

  private void thenResetPasswordCodeShouldBeNotValidated(String email) {
    thenResetPasswordEntryShouldExist(email);
    ResetPasswordEntry entry = repository.findById(email).get();
    assertThat(entry.isValidated()).isFalse();
  }

  private void thenResetPasswordCodeShouldBeValidated(String email) {
    thenResetPasswordEntryShouldExist(email);
    ResetPasswordEntry entry = repository.findById(email).get();
    assertThat(entry.isValidated()).isTrue();
  }

  private void thenResetPasswordEntryShouldExist(String email) {
    assertThat(repository.existsById(email)).isTrue();
  }

  private void expireCode(String email) {
    repository.deleteById(email);
  }
}
