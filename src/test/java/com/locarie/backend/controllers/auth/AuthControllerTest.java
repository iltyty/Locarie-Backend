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
    expectUtil.thenResultShouldBeCreated(result);
    thenResultDataShouldBeTrue(result);
    thenRedisShouldContainKey(userId.toString());
  }

  private MockHttpServletRequestBuilder givenForgotPasswordRequest(Long userId) {
    return MockMvcRequestBuilders.post(FORGOT_PASSWORD_ENDPOINT)
        .params(prepareForgotPasswordParams(userId));
  }

  private MultiValueMap<String, String> prepareForgotPasswordParams(Long userId) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("userId", userId.toString());
    return params;
  }

  private void thenResultDataShouldBeTrue(ResultActions result) throws Exception {
    result.andExpect(jsonPath("$.data").value("true"));
  }

  private void thenRedisShouldContainKey(String key) {
    assertThat(redis.hasKey(key)).isTrue();
  }
}
