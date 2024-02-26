package com.locarie.backend.controllers.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.locarie.backend.datacreators.user.UserTestsDataCreator;
import com.locarie.backend.domain.redis.ResetPasswordEntry;
import com.locarie.backend.repositories.redis.ResetPasswordEntryRepository;
import com.locarie.backend.utils.expecters.ResultExpectUtil;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuthControllerTest {
  private static final String FORGOT_PASSWORD_ENDPOINT = "/api/v1/auth/forgot-password";
  private static final String VALIDATE_FORGOT_PASSWORD_ENDPOINT =
      "/api/v1/auth/forgot-password/validate";
  private static final String RESET_PASSWORD_ENDPOINT = "/api/v1/auth/reset-password";

  @Autowired private MockMvc mockMvc;
  @Autowired private ResultExpectUtil expectUtil;
  @Autowired private UserTestsDataCreator dataCreator;
  @Autowired private ResetPasswordEntryRepository repository;

  @BeforeEach
  void startRedisContainer() {
    GenericContainer<?> container =
        new GenericContainer<>(DockerImageName.parse("redis:6.0-alpine")).withExposedPorts(6379);
    container.start();
    System.setProperty("spring.data.redis.host", container.getHost());
    System.setProperty("spring.data.redis.port", container.getMappedPort(6379).toString());
  }

  @Test
  void testForgotPasswordShouldGenerateOneCodeInRedis() throws Exception {
    String email = dataCreator.givenBusinessUserJoleneHornseyAfterCreated().getEmail();
    MockHttpServletRequestBuilder request = givenForgotPasswordRequest(email);

    ResultActions result = mockMvc.perform(request);

    thenResetPasswordCodeShouldBeNotValidated(email);
    expectUtil.thenResultShouldBeCreated(result);
    thenResultDataShouldBeTrue(result);
  }

  @Test
  void testForgotPasswordForNonExistentUserShouldFail() throws Exception {
    MockHttpServletRequestBuilder request = givenForgotPasswordRequest("non-existent@email.com");
    ResultActions result = mockMvc.perform(request);
    expectUtil.thenResultShouldBeUserNotFound(result);
  }

  @Test
  void testValidateAfterForgotPasswordShouldSucceed() throws Exception {
    String email = dataCreator.givenBusinessUserJoleneHornseyAfterCreated().getEmail();
    MockHttpServletRequestBuilder forgotPasswordRequest = givenForgotPasswordRequest(email);
    mockMvc.perform(forgotPasswordRequest);

    String code = repository.findById(email).get().getCode();
    MockHttpServletRequestBuilder validateRequest = givenValidateForgotPasswordRequest(email, code);
    ResultActions result = mockMvc.perform(validateRequest);

    thenResetPasswordCodeShouldBeValidated(email);
    expectUtil.thenResultShouldBeCreated(result);
    thenResultDataShouldBeTrue(result);
  }

  @Test
  void testValidateForgotPasswordForNonExistentUserShouldFail() throws Exception {
    MockHttpServletRequestBuilder request =
        givenValidateForgotPasswordRequest("non-existent@email.com", "");
    ResultActions result = mockMvc.perform(request);
    expectUtil.thenResultShouldBeUserNotFound(result);
  }

  @Test
  void testValidateForgotPasswordAfterCodeExpiredShouldFail() throws Exception {
    String email = dataCreator.givenBusinessUserJoleneHornseyAfterCreated().getEmail();
    MockHttpServletRequestBuilder forgotPasswordRequest = givenForgotPasswordRequest(email);
    mockMvc.perform(forgotPasswordRequest);

    String code = repository.findById(email).get().getCode();
    expireForgotPasswordCode(email);

    MockHttpServletRequestBuilder validateRequest = givenValidateForgotPasswordRequest(email, code);
    ResultActions result = mockMvc.perform(validateRequest);

    expectUtil.thenResultShouldBeCreated(result);
    thenResultDataShouldBeFalse(result);
  }

  @Test
  void testResetPasswordAfterValidatedShouldSucceed() throws Exception {
    String email = dataCreator.givenBusinessUserJoleneHornseyAfterCreated().getEmail();
    MockHttpServletRequestBuilder forgotPasswordRequest = givenForgotPasswordRequest(email);
    mockMvc.perform(forgotPasswordRequest);

    String code = repository.findById(email).get().getCode();
    MockHttpServletRequestBuilder validateRequest = givenValidateForgotPasswordRequest(email, code);
    mockMvc.perform(validateRequest);

    String password = "88888888";
    MockHttpServletRequestBuilder resetPasswordRequest = givenResetPasswordRequest(email, password);
    ResultActions result = mockMvc.perform(resetPasswordRequest);

    expectUtil.thenResultShouldBeCreated(result);
    thenResultDataShouldBeTrue(result);
    assertThat(repository.existsById(email)).isFalse();
  }

  @Test
  void testResetPasswordWithoutForgotShouldFail() throws Exception {
    String email = dataCreator.givenBusinessUserJoleneHornseyAfterCreated().getEmail();
    String password = "88888888";
    MockHttpServletRequestBuilder resetPasswordRequest = givenResetPasswordRequest(email, password);
    ResultActions result = mockMvc.perform(resetPasswordRequest);
    expectUtil.thenResultShouldBeUnauthorized(result);
  }

  @Test
  void testResetPasswordWithoutValidationShouldFail() throws Exception {
    String email = dataCreator.givenBusinessUserJoleneHornseyAfterCreated().getEmail();
    MockHttpServletRequestBuilder forgotPasswordRequest = givenForgotPasswordRequest(email);
    mockMvc.perform(forgotPasswordRequest);

    String password = "88888888";
    MockHttpServletRequestBuilder resetPasswordRequest = givenResetPasswordRequest(email, password);
    ResultActions result = mockMvc.perform(resetPasswordRequest);

    expectUtil.thenResultShouldBeUnauthorized(result);
  }

  private void expireForgotPasswordCode(String email) {
    repository.deleteById(email);
  }

  private MockHttpServletRequestBuilder givenForgotPasswordRequest(String email) {
    return MockMvcRequestBuilders.post(FORGOT_PASSWORD_ENDPOINT)
        .params(prepareForgotPasswordParams(email));
  }

  private MockHttpServletRequestBuilder givenValidateForgotPasswordRequest(
      String email, String code) {
    return MockMvcRequestBuilders.post(VALIDATE_FORGOT_PASSWORD_ENDPOINT)
        .params(prepareValidateForgotPasswordParams(email, code));
  }

  private MockHttpServletRequestBuilder givenResetPasswordRequest(String email, String password) {
    return MockMvcRequestBuilders.post(RESET_PASSWORD_ENDPOINT)
        .params(prepareResetPasswordParams(email, password));
  }

  private MultiValueMap<String, String> prepareForgotPasswordParams(String email) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("email", email);
    return params;
  }

  private MultiValueMap<String, String> prepareValidateForgotPasswordParams(
      String email, String code) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("email", email);
    params.add("code", code);
    return params;
  }

  private MultiValueMap<String, String> prepareResetPasswordParams(String email, String password) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("email", email);
    params.add("password", password);
    return params;
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

  private void thenResultDataShouldBeTrue(ResultActions result) throws Exception {
    result.andExpect(jsonPath("$.data").value(true));
  }

  private void thenResultDataShouldBeFalse(ResultActions result) throws Exception {
    result.andExpect(jsonPath("$.data").value(false));
  }
}
