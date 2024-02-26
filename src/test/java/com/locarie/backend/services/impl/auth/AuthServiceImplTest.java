package com.locarie.backend.services.impl.auth;

import static org.assertj.core.api.Assertions.*;

import com.locarie.backend.datacreators.user.UserTestsDataCreator;
import com.locarie.backend.domain.redis.ResetPasswordEntry;
import com.locarie.backend.exceptions.NotAuthorizedOperationException;
import com.locarie.backend.exceptions.UserNotFoundException;
import com.locarie.backend.repositories.redis.ResetPasswordEntryRepository;
import com.locarie.backend.services.auth.impl.AuthServiceImpl;
import jakarta.transaction.Transactional;
import java.util.Optional;
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
    Long userId = dataCreator.givenBusinessUserJoleneHornseyAfterCreated().getId();
    boolean result = underTests.forgotPassword(userId);
    assertThat(result).isTrue();
    thenResetPasswordCodeShouldBeNotValidated(userId);
  }

  @Test
  void testForgotPasswordForNonExistingUserShouldThrow() {
    assertThatThrownBy(() -> underTests.forgotPassword(0L))
        .isInstanceOf(UserNotFoundException.class);
  }

  @Test
  void testValidateForgotPasswordCodeAfterGenerationShouldSucceed() {
    Long userId = dataCreator.givenBusinessUserJoleneHornseyAfterCreated().getId();
    underTests.forgotPassword(userId);

    String code = assertAndGetResetPasswordCode(userId);
    boolean result = underTests.validateForgotPasswordCode(userId, code);
    assertThat(result).isTrue();
    thenResetPasswordCodeShouldBeValidated(userId);
  }

  private String assertAndGetResetPasswordCode(Long userId) {
    Optional<ResetPasswordEntry> optionalEntry = repository.findById(userId);
    assertThat(optionalEntry.isPresent()).isTrue();
    return optionalEntry.get().getCode();
  }

  @Test
  void testValidateForgotPasswordCodeAfterExpiredShouldFail() {
    Long userId = dataCreator.givenBusinessUserJoleneHornseyAfterCreated().getId();
    underTests.forgotPassword(userId);

    String code = assertAndGetResetPasswordCode(userId);
    expireCode(userId);

    boolean result = underTests.validateForgotPasswordCode(userId, code);
    assertThat(result).isFalse();
  }

  @Test
  void testResetPasswordAfterValidatedShouldSucceed() {
    Long userId = dataCreator.givenBusinessUserJoleneHornseyAfterCreated().getId();
    underTests.forgotPassword(userId);

    String code = assertAndGetResetPasswordCode(userId);
    underTests.validateForgotPasswordCode(userId, code);

    String password = "88888888";
    boolean result = underTests.resetPassword(userId, password);

    assertThat(result).isTrue();
    assertThat(repository.existsById(userId)).isFalse();
  }

  @Test
  void testResetPasswordWithoutForgotShouldFail() {
    Long userId = dataCreator.givenBusinessUserJoleneHornseyAfterCreated().getId();
    String password = "88888888";
    assertThatThrownBy(() -> underTests.resetPassword(userId, password))
        .isInstanceOf(NotAuthorizedOperationException.class);
  }

  @Test
  void testResetPasswordWithoutValidationShouldFail() {
    Long userId = dataCreator.givenBusinessUserJoleneHornseyAfterCreated().getId();
    underTests.forgotPassword(userId);

    String password = "88888888";
    assertThatThrownBy(() -> underTests.resetPassword(userId, password))
        .isInstanceOf(NotAuthorizedOperationException.class);
  }

  private void thenResetPasswordCodeShouldBeNotValidated(Long userId) {
    thenResetPasswordEntryShouldExist(userId);
    Optional<ResetPasswordEntry> optional = repository.findById(userId);
    assertThat(optional.isPresent()).isTrue();
    assertThat(optional.get().isValidated()).isFalse();
  }

  private void thenResetPasswordCodeShouldBeValidated(Long userId) {
    thenResetPasswordEntryShouldExist(userId);
    Optional<ResetPasswordEntry> optional = repository.findById(userId);
    assertThat(optional.isPresent()).isTrue();
    assertThat(optional.get().isValidated()).isTrue();
  }

  private void thenResetPasswordEntryShouldExist(Long userId) {
    assertThat(repository.existsById(userId)).isTrue();
  }

  private void expireCode(Long userId) {
    repository.deleteById(userId);
  }
}
