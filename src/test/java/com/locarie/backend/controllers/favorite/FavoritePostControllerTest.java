package com.locarie.backend.controllers.favorite;

import static com.locarie.backend.utils.matchers.ControllerResultMatcherUtil.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.locarie.backend.datacreators.post.PostTestsDataCreator;
import com.locarie.backend.datacreators.user.UserTestsDataCreator;
import com.locarie.backend.domain.dto.post.PostDto;
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
@Transactional
@AutoConfigureMockMvc
public class FavoritePostControllerTest {
  private static final String FAVORITE_ENDPOINT = "/api/v1/favorite";
  private static final String UNFAVORITE_ENDPOINT = "/api/v1/unfavorite";

  private static String listEndpoint(Long userId) {
    return String.format("/api/v1/favorite?userId=%d", userId);
  }

  @Autowired MockMvc mockMvc;
  @Autowired UserTestsDataCreator userTestsDataCreator;
  @Autowired PostTestsDataCreator postTestsDataCreator;

  @Test
  void testFavoritePostShouldSucceed() throws Exception {
    Long userId = userTestsDataCreator.givenBusinessUserShreejiIdAfterCreated();
    Long postId = postTestsDataCreator.givenPostDtoShreeji1AfterCreated().getId();
    MockHttpServletRequestBuilder request = givenFavoritePostRequest(userId, postId);
    ResultActions result = whenPerformRequest(request);
    thenResultShouldBeCreated(result);
  }

  @Test
  void testRepeatFavoriteShouldSucceed() throws Exception {
    Long userId = userTestsDataCreator.givenBusinessUserShreejiIdAfterCreated();
    Long postId = postTestsDataCreator.givenPostDtoShreeji1AfterCreated().getId();
    MockHttpServletRequestBuilder request = givenFavoritePostRequest(userId, postId);
    whenPerformRequest(request);
    ResultActions result = whenPerformRequest(request);
    thenResultShouldBeCreated(result);
  }

  @Test
  void testUnfavoriteAfterFavoriteShouldSucceed() throws Exception {
    Long userId = userTestsDataCreator.givenBusinessUserShreejiIdAfterCreated();
    Long postId = postTestsDataCreator.givenPostDtoShreeji1AfterCreated().getId();
    MockHttpServletRequestBuilder favoriteRequest = givenFavoritePostRequest(userId, postId);
    whenPerformRequest(favoriteRequest);

    MockHttpServletRequestBuilder unfavoriteRequest = givenUnfavoritePostRequest(userId, postId);
    ResultActions result = whenPerformRequest(unfavoriteRequest);
    thenResultShouldBeCreated(result);
  }

  @Test
  void testRepeatUnfavoriteShouldSucceed() throws Exception {
    Long userId = userTestsDataCreator.givenBusinessUserShreejiIdAfterCreated();
    Long postId = postTestsDataCreator.givenPostDtoShreeji1AfterCreated().getId();
    MockHttpServletRequestBuilder favoriteRequest = givenFavoritePostRequest(userId, postId);
    whenPerformRequest(favoriteRequest);

    MockHttpServletRequestBuilder unfavoriteRequest = givenUnfavoritePostRequest(userId, postId);
    whenPerformRequest(unfavoriteRequest);
    ResultActions result = whenPerformRequest(unfavoriteRequest);
    thenResultShouldBeCreated(result);
  }

  @Test
  void testListAfterFavoriteShouldReturnCorrectData() throws Exception {
    Long userId = userTestsDataCreator.givenBusinessUserShreejiIdAfterCreated();
    PostDto post = postTestsDataCreator.givenPostDtoShreeji1AfterCreated();
    MockHttpServletRequestBuilder favoriteRequest = givenFavoritePostRequest(userId, post.getId());
    whenPerformRequest(favoriteRequest);

    MockHttpServletRequestBuilder listRequest = givenListFavoriteRequest(userId);
    ResultActions result = whenPerformRequest(listRequest);
    thenResultShouldBeOk(result);
    thenListResultShouldBeExact(result, post);
  }

  @Test
  void testListAfterUnfavoriteShouldReturnEmptyData() throws Exception {
    Long userId = userTestsDataCreator.givenBusinessUserShreejiIdAfterCreated();
    Long postId = postTestsDataCreator.givenPostDtoShreeji1AfterCreated().getId();
    MockHttpServletRequestBuilder favoriteRequest = givenFavoritePostRequest(userId, postId);
    whenPerformRequest(favoriteRequest);

    MockHttpServletRequestBuilder unfavoriteRequest = givenUnfavoritePostRequest(userId, postId);
    whenPerformRequest(unfavoriteRequest);

    MockHttpServletRequestBuilder listRequest = givenListFavoriteRequest(userId);
    ResultActions result = whenPerformRequest(listRequest);
    thenResultShouldBeOk(result);
    thenListResultShouldBeEmpty(result);
  }

  private MockHttpServletRequestBuilder givenFavoritePostRequest(Long userId, Long postId) {
    MultiValueMap<String, String> params = preparePostParams(userId, postId);
    return MockMvcRequestBuilders.post(FAVORITE_ENDPOINT)
        .params(params)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED);
  }

  private MockHttpServletRequestBuilder givenUnfavoritePostRequest(Long userId, Long postId) {
    return MockMvcRequestBuilders.post(UNFAVORITE_ENDPOINT)
        .params(preparePostParams(userId, postId))
        .contentType(MediaType.APPLICATION_FORM_URLENCODED);
  }

  private MockHttpServletRequestBuilder givenListFavoriteRequest(Long userId) {
    String endpoint = listEndpoint(userId);
    return MockMvcRequestBuilders.get(endpoint);
  }

  private MultiValueMap<String, String> preparePostParams(Long userId, Long postId) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("userId", userId.toString());
    params.add("postId", postId.toString());
    return params;
  }

  private ResultActions whenPerformRequest(MockHttpServletRequestBuilder request) throws Exception {
    return mockMvc.perform(request);
  }

  private void thenResultShouldBeCreated(ResultActions result) throws Exception {
    result.andExpect(resultStatusShouldBeCreated());
    thenResultShouldBeSuccess(result);
  }

  private void thenResultShouldBeOk(ResultActions result) throws Exception {
    result.andExpect(resultStatusShouldBeOk());
    thenResultShouldBeSuccess(result);
  }

  private void thenResultShouldBeSuccess(ResultActions result) throws Exception {
    result.andExpect(resultStatusCodeShouldBeSuccess()).andExpect(resultMessageShouldBeSuccess());
  }

  private void thenListResultShouldBeExact(ResultActions result, PostDto post) throws Exception {
    result
        .andExpect(jsonPath("$.data").isArray())
        .andExpect(jsonPath("$.data[0].id").value(post.getId()))
        .andExpect(jsonPath("$.data[0].title").value(post.getTitle()))
        .andExpect(jsonPath("$.data[0].content").value(post.getContent()));
  }

  private void thenListResultShouldBeEmpty(ResultActions result) throws Exception {
    result.andExpect(jsonPath("$.data").isArray()).andExpect(jsonPath("$.data").isEmpty());
  }
}
