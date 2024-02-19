package com.locarie.backend.controllers.user.read;

import static com.locarie.backend.utils.matchers.ControllerResultMatcherUtil.resultStatusCodeShouldBeSuccess;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.locarie.backend.datacreators.user.UserEntityCreator;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.repositories.user.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class UserGetControllerTest {
  @Autowired private MockMvc mockMvc;
  @Autowired private UserRepository userRepository;

  @Test
  void testGetExistedUserShouldSucceed() throws Exception {
    UserEntity userEntity = givenUserEntityAfterCreated();
    MockHttpServletRequestBuilder request = givenGetUserRequest(userEntity.getId());
    ResultActions result = whenPerformGetUserRequest(request);
    thenGetResultShouldBeSuccess(result);
    thenGetResultShouldContainUser(result, userEntity);
  }

  @Test
  void testGetNonExistedUserShouldFail() throws Exception {
    MockHttpServletRequestBuilder request = givenGetUserRequest(0L);
    ResultActions result = whenPerformGetUserRequest(request);
    thenGetResultShouldBeNotFound(result);
  }

  private UserEntity givenUserEntityAfterCreated() {
    UserEntity userEntity = UserEntityCreator.plainUserEntity();
    return userRepository.save(userEntity);
  }

  private MockHttpServletRequestBuilder givenGetUserRequest(Long userId) {
    String endpoint = getEndpointByUserId(userId);
    return MockMvcRequestBuilders.get(endpoint);
  }

  private ResultActions whenPerformGetUserRequest(MockHttpServletRequestBuilder request)
      throws Exception {
    return mockMvc.perform(request);
  }

  private void thenGetResultShouldBeSuccess(ResultActions result) throws Exception {
    result
        .andExpect(resultStatusCodeShouldBeSuccess())
        .andExpect(resultStatusCodeShouldBeSuccess());
  }

  private void thenGetResultShouldContainUser(ResultActions result, UserEntity userEntity)
      throws Exception {
    result
        .andDo(
            result1 -> {
              System.out.println(result1.getResponse().getContentAsString());
            })
        .andExpect(jsonPath("$.data.id").value(userEntity.getId()));
  }

  private void thenGetResultShouldBeNotFound(ResultActions result) throws Exception {
    result.andExpect(status().isNotFound());
  }

  private static String getEndpointByUserId(Long userId) {
    return "/api/v1/users/" + userId;
  }
}
