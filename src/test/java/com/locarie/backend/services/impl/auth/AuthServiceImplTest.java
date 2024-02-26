package com.locarie.backend.services.impl.auth;

import static org.assertj.core.api.Assertions.assertThat;

import com.locarie.backend.datacreators.user.UserTestsDataCreator;
import com.locarie.backend.services.auth.impl.AuthServiceImpl;
import com.locarie.backend.services.redis.RedisService;
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
  private static GenericContainer<?> container;

  @Autowired private AuthServiceImpl underTests;
  @Autowired private RedisService redis;
  @Autowired private UserTestsDataCreator dataCreator;

  @BeforeAll
  static void startRedisContainer() {
    container =
        new GenericContainer<>(DockerImageName.parse("redis:6.0-alpine")).withExposedPorts(6379);
    container.start();
    System.setProperty("spring.data.redis.host", container.getHost());
    System.setProperty("spring.data.redis.port", container.getMappedPort(6379).toString());
  }

  @Test
  void testForgotPasswordShouldGenerateOneCodeInRedis() {
    Long userId = dataCreator.givenBusinessUserJoleneHornseyAfterCreated().getId();
    boolean result = underTests.forgotPassword(userId);
    System.out.println(container.getLogs());
    assertThat(result).isTrue();
    assertThat(redis.hasKey(userId.toString())).isTrue();
  }
}
