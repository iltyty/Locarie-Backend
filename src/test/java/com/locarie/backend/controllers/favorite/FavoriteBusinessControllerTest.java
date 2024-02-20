package com.locarie.backend.controllers.favorite;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.locarie.backend.datacreators.user.UserTestsDataCreator;
import com.locarie.backend.domain.dto.user.UserDto;
import com.locarie.backend.utils.expecters.ResultExpectUtil;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class FavoriteBusinessControllerTest {
  private static final String FAVORITE_ENDPOINT = "/api/v1/users/favorite";
  private static final String UNFAVORITE_ENDPOINT = "/api/v1/users/unfavorite";

  private static String listFavoriteEndpoint(Long useId) {
    return String.format("/api/v1/users/favorite?userId=%d", useId);
  }

  private static String listFavoredByEndpoint(Long businessId) {
    return String.format("/api/v1/users/favored-by?businessId=%d", businessId);
  }

  @Autowired private MockMvc mockMvc;
  @Autowired private UserTestsDataCreator userTestsDataCreator;
  @Autowired private ResultExpectUtil resultExpectUtil;

  @Test
  void testFavoriteBusinessShouldSucceed() throws Exception {
    UserDto user = userTestsDataCreator.givenPlainUserAfterCreated();
    UserDto businessUser = userTestsDataCreator.givenBusinessUserJoleneHornseyAfterCreated();
    MockHttpServletRequestBuilder request =
        givenFavoriteBusinessRequest(user.getId(), businessUser.getId());
    ResultActions result = whenPerformRequest(request);
    resultExpectUtil.thenResultShouldBeOk(result);
  }

  @Test
  void testRepeatFavoriteShouldSucceed() throws Exception {
    UserDto user = userTestsDataCreator.givenPlainUserAfterCreated();
    UserDto businessUser = userTestsDataCreator.givenBusinessUserJoleneHornseyAfterCreated();
    MockHttpServletRequestBuilder request =
        givenFavoriteBusinessRequest(user.getId(), businessUser.getId());
    whenPerformRequest(request);

    ResultActions result = whenPerformRequest(request);
    resultExpectUtil.thenResultShouldBeOk(result);
  }

  @Test
  void testUnfavoriteAfterFavoriteShouldSucceed() throws Exception {
    UserDto user = userTestsDataCreator.givenPlainUserAfterCreated();
    UserDto businessUser = userTestsDataCreator.givenBusinessUserJoleneHornseyAfterCreated();
    MockHttpServletRequestBuilder favoriteRequest =
        givenFavoriteBusinessRequest(user.getId(), businessUser.getId());
    whenPerformRequest(favoriteRequest);

    MockHttpServletRequestBuilder unfavoriteRequest =
        givenUnfavoriteBusinessRequest(user.getId(), businessUser.getId());
    ResultActions result = whenPerformRequest(unfavoriteRequest);
    resultExpectUtil.thenResultShouldBeOk(result);
  }

  @Test
  void testRepeatUnfavoriteShouldSucceed() throws Exception {
    UserDto user = userTestsDataCreator.givenPlainUserAfterCreated();
    UserDto businessUser = userTestsDataCreator.givenBusinessUserJoleneHornseyAfterCreated();
    MockHttpServletRequestBuilder favoriteRequest =
        givenFavoriteBusinessRequest(user.getId(), businessUser.getId());
    whenPerformRequest(favoriteRequest);

    MockHttpServletRequestBuilder unfavoriteRequest =
        givenUnfavoriteBusinessRequest(user.getId(), businessUser.getId());
    whenPerformRequest(unfavoriteRequest);

    ResultActions result = whenPerformRequest(unfavoriteRequest);
    resultExpectUtil.thenResultShouldBeOk(result);
  }

  @Test
  void testListAfterFavoriteShouldReturnCorrectData() throws Exception {
    UserDto user = userTestsDataCreator.givenPlainUserAfterCreated();
    UserDto businessUser = userTestsDataCreator.givenBusinessUserJoleneHornseyAfterCreated();
    MockHttpServletRequestBuilder favoriteRequest =
        givenFavoriteBusinessRequest(user.getId(), businessUser.getId());
    whenPerformRequest(favoriteRequest);

    MockHttpServletRequestBuilder listRequest = givenListFavoriteRequest(user.getId());
    ResultActions result = whenPerformRequest(listRequest);
    thenListResultShouldBeExact(result, businessUser);
  }

  @Test
  void testListFavoriteAfterUnfavoriteShouldReturnEmptyData() throws Exception {
    UserDto user = userTestsDataCreator.givenPlainUserAfterCreated();
    UserDto businessUser = userTestsDataCreator.givenBusinessUserJoleneHornseyAfterCreated();
    MockHttpServletRequestBuilder favoriteRequest =
        givenFavoriteBusinessRequest(user.getId(), businessUser.getId());
    whenPerformRequest(favoriteRequest);

    MockHttpServletRequestBuilder unfavoriteRequest =
        givenUnfavoriteBusinessRequest(user.getId(), businessUser.getId());
    whenPerformRequest(unfavoriteRequest);

    MockHttpServletRequestBuilder listRequest = givenListFavoriteRequest(user.getId());
    ResultActions result = whenPerformRequest(listRequest);
    resultExpectUtil.thenResultShouldBeOk(result);
    thenListResultShouldBeEmpty(result);
  }

  @Test
  void testListFavoredByAfterFavoriteShouldReturnCorrectData() throws Exception {
    UserDto user = userTestsDataCreator.givenPlainUserAfterCreated();
    UserDto businessUser = userTestsDataCreator.givenBusinessUserJoleneHornseyAfterCreated();
    MockHttpServletRequestBuilder favoriteRequest =
        givenFavoriteBusinessRequest(user.getId(), businessUser.getId());
    whenPerformRequest(favoriteRequest);

    MockHttpServletRequestBuilder listRequest = givenListFavoredByRequest(businessUser.getId());
    ResultActions result = whenPerformRequest(listRequest);
    thenListResultShouldBeExact(result, user);
  }

  @Test
  void testListFavoredByAfterUnfavoriteShouldReturnEmptyData() throws Exception {
    UserDto user = userTestsDataCreator.givenPlainUserAfterCreated();
    UserDto businessUser = userTestsDataCreator.givenBusinessUserJoleneHornseyAfterCreated();
    MockHttpServletRequestBuilder favoriteRequest =
        givenFavoriteBusinessRequest(user.getId(), businessUser.getId());
    whenPerformRequest(favoriteRequest);

    MockHttpServletRequestBuilder unfavoriteRequest =
        givenUnfavoriteBusinessRequest(user.getId(), businessUser.getId());
    whenPerformRequest(unfavoriteRequest);

    MockHttpServletRequestBuilder listRequest = givenListFavoredByRequest(businessUser.getId());
    ResultActions result = whenPerformRequest(listRequest);
    resultExpectUtil.thenResultShouldBeOk(result);
    thenListResultShouldBeEmpty(result);
  }

  private MockHttpServletRequestBuilder givenFavoriteBusinessRequest(Long userId, Long businessId) {
    return MockMvcRequestBuilders.post(FAVORITE_ENDPOINT)
        .params(preparePostParams(userId, businessId))
        .contentType(MediaType.APPLICATION_FORM_URLENCODED);
  }

  private MockHttpServletRequestBuilder givenUnfavoriteBusinessRequest(
      Long userId, Long businessId) {
    return MockMvcRequestBuilders.post(UNFAVORITE_ENDPOINT)
        .params(preparePostParams(userId, businessId))
        .contentType(MediaType.APPLICATION_FORM_URLENCODED);
  }

  private MockHttpServletRequestBuilder givenListFavoriteRequest(Long userId) {
    return MockMvcRequestBuilders.get(listFavoriteEndpoint(userId));
  }

  private MockHttpServletRequestBuilder givenListFavoredByRequest(Long businessId) {
    return MockMvcRequestBuilders.get(listFavoredByEndpoint(businessId));
  }

  private ResultActions whenPerformRequest(MockHttpServletRequestBuilder request) throws Exception {
    return mockMvc.perform(request);
  }

  private void thenListResultShouldBeExact(ResultActions result, UserDto user) throws Exception {
    result
        .andExpect(jsonPath("$.data").isArray())
        .andExpect(jsonPath("$.data[0].id").value(user.getId()))
        .andExpect(jsonPath("$.data[0].username").value(user.getUsername()))
        .andExpect(jsonPath("$.data[0].firstName").value(user.getFirstName()))
        .andExpect(jsonPath("$.data[0].lastName").value(user.getLastName()))
        .andExpect(jsonPath("$.data[0].email").value(user.getEmail()))
        .andExpect(jsonPath("$.data[0].businessName").value(user.getBusinessName()));
  }

  private void thenListResultShouldBeEmpty(ResultActions result) throws Exception {
    result.andExpect(jsonPath("$.data").isArray());
    result.andExpect(jsonPath("$.data").isEmpty());
  }

  private MultiValueMap<String, String> preparePostParams(Long userId, Long businessId) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("userId", userId.toString());
    params.add("businessId", businessId.toString());
    return params;
  }
}
