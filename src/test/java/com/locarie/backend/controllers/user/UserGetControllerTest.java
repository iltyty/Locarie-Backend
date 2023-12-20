package com.locarie.backend.controllers.user;

import com.locarie.backend.datacreators.user.UserEntityCreator;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.repositories.UserRepository;
import com.locarie.backend.utils.UserControllerResultMatcherUtil;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class UserGetControllerTest {
  @Autowired private MockMvc mockMvc;

  @Autowired UserRepository userRepository;

  @Test
  void testGetExistedUserShouldSucceed() throws Exception {
    UserEntity userEntity = givenUserEntityAfterCreated();
    MockHttpServletRequestBuilder request = givenGetUserRequest(userEntity.getId());
    ResultActions result = whenPerformGetUserRequest(request);
    thenGetResultShouldBeSuccess(result);
    thenGetResultShouldContainUserEntity(result, userEntity);
  }

  @Test
  void testGetNonExistedUserShouldFail() throws Exception {
    MockHttpServletRequestBuilder request = givenGetUserRequest(0L);
    ResultActions result = whenPerformGetUserRequest(request);
    thenGetResultShouldBeNotFound(result);
  }

  UserEntity givenUserEntityAfterCreated() {
    UserEntity userEntity = UserEntityCreator.plainUserEntity();
    return userRepository.save(userEntity);
  }

  MockHttpServletRequestBuilder givenGetUserRequest(Long userId) {
    String endpoint = getEndpointByUserId(userId);
    return MockMvcRequestBuilders.get(endpoint);
  }

  private ResultActions whenPerformGetUserRequest(MockHttpServletRequestBuilder request)
      throws Exception {
    return mockMvc.perform(request);
  }

  private void thenGetResultShouldBeSuccess(ResultActions result) throws Exception {
    result
        .andExpect(UserControllerResultMatcherUtil.resultStatusCodeShouldBeSuccess())
        .andExpect(UserControllerResultMatcherUtil.resultStatusCodeShouldBeSuccess());
  }

  private void thenGetResultShouldContainUserEntity(ResultActions result, UserEntity userEntity)
      throws Exception {
    userEntity.setPassword(null);
    result.andExpect(MockMvcResultMatchers.jsonPath("$.data").value(userEntity));
  }

  private void thenGetResultShouldBeNotFound(ResultActions result) throws Exception {
    result.andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  private static String getEndpointByUserId(Long userId) {
    return "/api/v1/users/" + userId;
  }
}
