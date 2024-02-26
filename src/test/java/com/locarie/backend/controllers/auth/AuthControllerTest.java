package com.locarie.backend.controllers.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.locarie.backend.datacreators.user.UserTestsDataCreator;
import com.locarie.backend.services.redis.RedisService;
import com.locarie.backend.utils.expecters.ResultExpectUtil;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
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
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class AuthControllerTest {
  private static final String FORGOT_PASSWORD_ENDPOINT = "/api/v1/auth/forgot-password";
  private static final String VALIDATE_FORGOT_PASSWORD_ENDPOINT =
      "/api/v1/auth/forgot-password/validate";

  @Autowired private MockMvc mockMvc;
  @Autowired private RedisService redis;
  @Autowired private UserTestsDataCreator dataCreator;
  @Autowired private ResultExpectUtil expectUtil;

  @BeforeAll
  static void startRedisContainer() {
    GenericContainer<?> container =
        new GenericContainer<>(DockerImageName.parse("redis:6.0-alpine")).withExposedPorts(6379);
    container.start();
    System.setProperty("spring.data.redis.host", container.getHost());
    System.setProperty("spring.data.redis.port", container.getMappedPort(6379).toString());
  }

  @Test
  void testForgotPasswordShouldGenerateOneCodeInRedis() throws Exception {
    Long userId = dataCreator.givenBusinessUserJoleneHornseyAfterCreated().getId();
    MockHttpServletRequestBuilder request = givenForgotPasswordRequest(userId);

    ResultActions result = mockMvc.perform(request);

    thenRedisShouldContainKey(userId.toString());
    expectUtil.thenResultShouldBeCreated(result);
    thenResultDataShouldBeTrue(result);
  }

  @Test
  void testForgotPasswordForNonExistentUserShouldFail() throws Exception {
    MockHttpServletRequestBuilder request = givenForgotPasswordRequest(0L);
    ResultActions result = mockMvc.perform(request);
    expectUtil.thenResultShouldBeUserNotFound(result);
  }

  @Test
  void testValidateAfterForgotPasswordShouldSucceed() throws Exception {
    Long userId = dataCreator.givenBusinessUserJoleneHornseyAfterCreated().getId();
    MockHttpServletRequestBuilder forgotPasswordRequest = givenForgotPasswordRequest(userId);
    mockMvc.perform(forgotPasswordRequest);

    String code = (String) redis.get(userId.toString());
    MockHttpServletRequestBuilder validateRequest =
        givenValidateForgotPasswordRequest(userId, code);
    ResultActions result = mockMvc.perform(validateRequest);

    expectUtil.thenResultShouldBeOk(result);
    thenResultDataShouldBeTrue(result);
  }

  @Test
  void testValidateForgotPasswordForNonExistentUserShouldFail() throws Exception {
    MockHttpServletRequestBuilder request = givenValidateForgotPasswordRequest(0L, "");
    ResultActions result = mockMvc.perform(request);
    expectUtil.thenResultShouldBeUserNotFound(result);
  }

  @Test
  void testValidateForgotPasswordAfterCodeExpiredShouldFail() throws Exception {
    Long userId = dataCreator.givenBusinessUserJoleneHornseyAfterCreated().getId();
    MockHttpServletRequestBuilder forgotPasswordRequest = givenForgotPasswordRequest(userId);
    mockMvc.perform(forgotPasswordRequest);

    String code = (String) redis.get(userId.toString());
    expireForgotPasswordCode(userId);

    MockHttpServletRequestBuilder validateRequest =
        givenValidateForgotPasswordRequest(userId, code);
    ResultActions result = mockMvc.perform(validateRequest);

    expectUtil.thenResultShouldBeOk(result);
    thenResultDataShouldBeFalse(result);
  }

  private void expireForgotPasswordCode(Long userId) {
    String key = userId.toString();
    redis.delete(key);
  }

  private MockHttpServletRequestBuilder givenForgotPasswordRequest(Long userId) {
    return MockMvcRequestBuilders.post(FORGOT_PASSWORD_ENDPOINT)
        .params(prepareForgotPasswordParams(userId));
  }

  private MockHttpServletRequestBuilder givenValidateForgotPasswordRequest(
      Long userId, String code) {
    return MockMvcRequestBuilders.get(VALIDATE_FORGOT_PASSWORD_ENDPOINT)
        .params(prepareValidateForgotPasswordParams(userId, code));
  }

  private MultiValueMap<String, String> prepareForgotPasswordParams(Long userId) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("userId", userId.toString());
    return params;
  }

  private MultiValueMap<String, String> prepareValidateForgotPasswordParams(
      Long userId, String code) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("userId", userId.toString());
    params.add("code", code);
    return params;
  }

  private void thenRedisShouldContainKey(String key) {
    assertThat(redis.hasKey(key)).isTrue();
  }

  private void thenResultDataShouldBeTrue(ResultActions result) throws Exception {
    result.andExpect(jsonPath("$.data").value(true));
  }

  private void thenResultDataShouldBeFalse(ResultActions result) throws Exception {
    result.andExpect(jsonPath("$.data").value(false));
  }
}
