package com.locarie.backend.controllers.user.delete;

import com.locarie.backend.utils.matchers.ResultMatcherUtil;
import static org.assertj.core.api.Assertions.assertThat;

import com.locarie.backend.datacreators.user.UserTestsDataCreator;
import com.locarie.backend.repositories.user.UserRepository;
import com.locarie.backend.utils.expecters.ResultExpectUtil;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserDeleteControllerTest {
  private static String getEndpoint(Long id) {
    return "/api/v1/users/" + id;
  }

  @Autowired private MockMvc mockMvc;
  @Autowired private UserRepository repository;
  @Autowired private UserTestsDataCreator dataCreator;
  @Autowired private ResultExpectUtil expectUtil;

  @Test
  void testDeleteUserAfterCreatedShouldSucceed() throws Exception {
    Long id = dataCreator.givenPlainUserAfterCreated().getId();
    MockHttpServletRequestBuilder request = givenDeleteRequest(id);
    ResultActions result = mockMvc.perform(request);
    expectUtil.thenResultShouldBeOk(result);
    thenUserShouldNotExists(id);
  }

  @Test
  void testDeleteNonExistentUserShouldReturnStatusOk() throws Exception {
    MockHttpServletRequestBuilder request = givenDeleteRequest(0L);
    ResultActions result = mockMvc.perform(request);
    result.andExpect(status().isOk());
    expectUtil.thenResultShouldBeUserNotFound(result);
  }

  private MockHttpServletRequestBuilder givenDeleteRequest(Long id) {
    String endpoint = getEndpoint(id);
    return MockMvcRequestBuilders.delete(endpoint);
  }

  private void thenUserShouldNotExists(Long id) {
    assertThat(repository.findById(id).isPresent()).isFalse();
  }
}
