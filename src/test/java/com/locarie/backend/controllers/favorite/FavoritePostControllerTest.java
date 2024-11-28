package com.locarie.backend.controllers.favorite;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.locarie.backend.datacreators.post.PostTestsDataCreator;
import com.locarie.backend.datacreators.user.UserTestsDataCreator;
import com.locarie.backend.domain.dto.post.PostDto;
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
@Transactional
@AutoConfigureMockMvc
public class FavoritePostControllerTest {
  private static final String FAVORITE_ENDPOINT = "/api/v1/posts/favorite";
  private static final String UNFAVORITE_ENDPOINT = "/api/v1/posts/unfavorite";

  private static String listFavoriteEndpoint(Long userId) {
    return String.format("/api/v1/posts/favorite?userId=%d", userId);
  }

  private static String listFavoredByEndpoint(Long postId) {
    return String.format("/api/v1/posts/favored-by?postId=%d", postId);
  }

  private static String countFavoriteEndpoint(Long userId) {
    return String.format("/api/v1/posts/favorite/count?userId=%d", userId);
  }

  private static String countFavoredByEndpoint(Long postId) {
    return String.format("/api/v1/posts/favored-by/count?postId=%d", postId);
  }

  private static String checkHasBeenSavedEndpoint(Long userId, Long postId) {
    return String.format("/api/v1/posts/is-favored-by?userId=%d&postId=%d", userId, postId);
  }

  @Autowired private MockMvc mockMvc;
  @Autowired private UserTestsDataCreator userTestsDataCreator;
  @Autowired private PostTestsDataCreator postTestsDataCreator;
  @Autowired private ResultExpectUtil resultExpectUtil;

  @Test
  void testFavoritePostShouldSucceed() throws Exception {
    Long userId = userTestsDataCreator.givenBusinessUserShreejiAfterCreated().getId();
    Long postId = postTestsDataCreator.givenPostDtoShreeji1AfterCreated().getId();
    MockHttpServletRequestBuilder request = givenFavoritePostRequest(userId, postId);
    ResultActions result = whenPerformRequest(request);
    resultExpectUtil.thenResultShouldBeOk(result);
  }

  @Test
  void testRepeatFavoriteShouldSucceed() throws Exception {
    Long userId = userTestsDataCreator.givenBusinessUserShreejiAfterCreated().getId();
    Long postId = postTestsDataCreator.givenPostDtoShreeji1AfterCreated().getId();
    MockHttpServletRequestBuilder request = givenFavoritePostRequest(userId, postId);
    whenPerformRequest(request);
    ResultActions result = whenPerformRequest(request);
    resultExpectUtil.thenResultShouldBeOk(result);
  }

  @Test
  void testUnfavoriteAfterFavoriteShouldSucceed() throws Exception {
    Long userId = userTestsDataCreator.givenBusinessUserShreejiAfterCreated().getId();
    Long postId = postTestsDataCreator.givenPostDtoShreeji1AfterCreated().getId();
    MockHttpServletRequestBuilder favoriteRequest = givenFavoritePostRequest(userId, postId);
    whenPerformRequest(favoriteRequest);

    MockHttpServletRequestBuilder unfavoriteRequest = givenUnfavoritePostRequest(userId, postId);
    ResultActions result = whenPerformRequest(unfavoriteRequest);
    resultExpectUtil.thenResultShouldBeOk(result);
  }

  @Test
  void testRepeatUnfavoriteShouldSucceed() throws Exception {
    Long userId = userTestsDataCreator.givenBusinessUserShreejiAfterCreated().getId();
    Long postId = postTestsDataCreator.givenPostDtoShreeji1AfterCreated().getId();
    MockHttpServletRequestBuilder favoriteRequest = givenFavoritePostRequest(userId, postId);
    whenPerformRequest(favoriteRequest);

    MockHttpServletRequestBuilder unfavoriteRequest = givenUnfavoritePostRequest(userId, postId);
    whenPerformRequest(unfavoriteRequest);
    ResultActions result = whenPerformRequest(unfavoriteRequest);
    resultExpectUtil.thenResultShouldBeOk(result);
  }

  @Test
  void testListFavoritePostsAfterFavoriteShouldReturnCorrectResult() throws Exception {
    Long userId = userTestsDataCreator.givenBusinessUserShreejiAfterCreated().getId();
    PostDto post = postTestsDataCreator.givenPostDtoShreeji1AfterCreated();
    MockHttpServletRequestBuilder favoriteRequest = givenFavoritePostRequest(userId, post.getId());
    whenPerformRequest(favoriteRequest);

    MockHttpServletRequestBuilder listRequest = givenListFavoriteRequest(userId);
    ResultActions result = whenPerformRequest(listRequest);
    resultExpectUtil.thenResultShouldBeOk(result);
    thenListFavoritePostsResultShouldBeExact(result, post);
  }

  @Test
  void testListFavoritePostsAfterUnfavoriteShouldReturnEmptyResult() throws Exception {
    Long userId = userTestsDataCreator.givenBusinessUserShreejiAfterCreated().getId();
    Long postId = postTestsDataCreator.givenPostDtoShreeji1AfterCreated().getId();
    MockHttpServletRequestBuilder favoriteRequest = givenFavoritePostRequest(userId, postId);
    whenPerformRequest(favoriteRequest);

    MockHttpServletRequestBuilder unfavoriteRequest = givenUnfavoritePostRequest(userId, postId);
    whenPerformRequest(unfavoriteRequest);

    MockHttpServletRequestBuilder listRequest = givenListFavoriteRequest(userId);
    ResultActions result = whenPerformRequest(listRequest);
    resultExpectUtil.thenResultShouldBeOk(result);
    thenListResultShouldBeEmpty(result);
  }

  @Test
  void testListFavoredByAfterFavoriteShouldReturnCorrectData() throws Exception {
    UserDto user = userTestsDataCreator.givenBusinessUserShreejiAfterCreated();
    PostDto post = postTestsDataCreator.givenPostDtoShreeji1AfterCreated();
    MockHttpServletRequestBuilder favoriteRequest =
        givenFavoritePostRequest(user.getId(), post.getId());
    whenPerformRequest(favoriteRequest);

    MockHttpServletRequestBuilder listRequest = givenListFavoredByRequest(post.getId());
    ResultActions result = whenPerformRequest(listRequest);
    resultExpectUtil.thenResultShouldBeOk(result);
    thenListFavoredByUsersResultShouldBeExact(result, user);
  }

  @Test
  void testListFavoredByAfterUnfavoriteShouldReturnEmptyData() throws Exception {
    UserDto user = userTestsDataCreator.givenBusinessUserShreejiAfterCreated();
    PostDto post = postTestsDataCreator.givenPostDtoShreeji1AfterCreated();
    MockHttpServletRequestBuilder favoriteRequest =
        givenFavoritePostRequest(user.getId(), post.getId());
    whenPerformRequest(favoriteRequest);

    MockHttpServletRequestBuilder unfavoriteRequest =
        givenUnfavoritePostRequest(user.getId(), post.getId());
    whenPerformRequest(unfavoriteRequest);

    MockHttpServletRequestBuilder listRequest = givenListFavoredByRequest(post.getId());
    ResultActions result = whenPerformRequest(listRequest);
    resultExpectUtil.thenResultShouldBeOk(result);
    thenListResultShouldBeEmpty(result);
  }

  @Test
  void testCountFavoritePostsAfterFavoriteShouldReturnCorrectResult() throws Exception {
    Long userId = userTestsDataCreator.givenBusinessUserShreejiAfterCreated().getId();
    Long postId = postTestsDataCreator.givenPostDtoShreeji1AfterCreated().getId();
    MockHttpServletRequestBuilder favoriteRequest = givenFavoritePostRequest(userId, postId);
    whenPerformRequest(favoriteRequest);

    MockHttpServletRequestBuilder countRequest = givenCountFavoriteRequest(userId);
    ResultActions result = whenPerformRequest(countRequest);
    resultExpectUtil.thenResultShouldBeOk(result);
    thenCountResultShouldBeOne(result);
  }

  @Test
  void testCountFavoredByAfterFavoriteShouldReturnCorrectResult() throws Exception {
    Long userId = userTestsDataCreator.givenBusinessUserShreejiAfterCreated().getId();
    Long postId = postTestsDataCreator.givenPostDtoShreeji1AfterCreated().getId();
    MockHttpServletRequestBuilder favoriteRequest = givenFavoritePostRequest(userId, postId);
    whenPerformRequest(favoriteRequest);

    MockHttpServletRequestBuilder countRequest = givenCountFavoredByRequest(postId);
    ResultActions result = whenPerformRequest(countRequest);
    resultExpectUtil.thenResultShouldBeOk(result);
    thenCountResultShouldBeOne(result);
  }

  @Test
  void testIsFavoredByAfterFavoriteShouldReturnTrue() throws Exception {
    Long userId = userTestsDataCreator.givenBusinessUserShreejiAfterCreated().getId();
    Long postId = postTestsDataCreator.givenPostDtoShreeji1AfterCreated().getId();
    MockHttpServletRequestBuilder favoriteRequest = givenFavoritePostRequest(userId, postId);
    whenPerformRequest(favoriteRequest);

    MockHttpServletRequestBuilder hasBeenSavedRequest = givenHasBeenSavedRequest(userId, postId);
    ResultActions result = whenPerformRequest(hasBeenSavedRequest);
    resultExpectUtil.thenResultShouldBeOk(result);
    thenDataShouldBeTrue(result);
  }

  @Test
  void testIsFavoredByAfterUnFavoriteShouldReturnTrue() throws Exception {
    Long userId = userTestsDataCreator.givenBusinessUserShreejiAfterCreated().getId();
    Long postId = postTestsDataCreator.givenPostDtoShreeji1AfterCreated().getId();
    MockHttpServletRequestBuilder favoriteRequest = givenFavoritePostRequest(userId, postId);
    whenPerformRequest(favoriteRequest);

    MockHttpServletRequestBuilder unfavoriteRequest = givenUnfavoritePostRequest(userId, postId);
    whenPerformRequest(unfavoriteRequest);

    MockHttpServletRequestBuilder hasBeenSavedRequest = givenHasBeenSavedRequest(userId, postId);
    ResultActions result = whenPerformRequest(hasBeenSavedRequest);
    resultExpectUtil.thenResultShouldBeOk(result);
    thenDataShouldBeFalse(result);
  }

  private MockHttpServletRequestBuilder givenFavoritePostRequest(Long userId, Long postId) {
    return MockMvcRequestBuilders.post(FAVORITE_ENDPOINT)
        .params(preparePostParams(userId, postId))
        .contentType(MediaType.APPLICATION_FORM_URLENCODED);
  }

  private MockHttpServletRequestBuilder givenUnfavoritePostRequest(Long userId, Long postId) {
    return MockMvcRequestBuilders.post(UNFAVORITE_ENDPOINT)
        .params(preparePostParams(userId, postId))
        .contentType(MediaType.APPLICATION_FORM_URLENCODED);
  }

  private MockHttpServletRequestBuilder givenListFavoriteRequest(Long userId) {
    String endpoint = listFavoriteEndpoint(userId);
    return MockMvcRequestBuilders.get(endpoint);
  }

  private MockHttpServletRequestBuilder givenListFavoredByRequest(Long postId) {
    String endpoint = listFavoredByEndpoint(postId);
    return MockMvcRequestBuilders.get(endpoint);
  }

  private MockHttpServletRequestBuilder givenCountFavoriteRequest(Long userId) {
    String endpoint = countFavoriteEndpoint(userId);
    return MockMvcRequestBuilders.get(endpoint);
  }

  private MockHttpServletRequestBuilder givenCountFavoredByRequest(Long postId) {
    String endpoint = countFavoredByEndpoint(postId);
    return MockMvcRequestBuilders.get(endpoint);
  }

  private MockHttpServletRequestBuilder givenHasBeenSavedRequest(Long userId, Long postId) {
    String endpoint = checkHasBeenSavedEndpoint(userId, postId);
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

  private void thenListFavoritePostsResultShouldBeExact(ResultActions result, PostDto post)
      throws Exception {
    result
        .andExpect(jsonPath("$.data.content").isArray())
        .andExpect(jsonPath("$.data.content.[0].id").value(post.getId()))
        .andExpect(jsonPath("$.data.content.[0].content").value(post.getContent()));
  }

  private void thenListFavoredByUsersResultShouldBeExact(ResultActions result, UserDto user)
      throws Exception {
    result
        .andExpect(jsonPath("$.data.content").isArray())
        .andExpect(jsonPath("$.data.content.[0].id").value(user.getId()))
        .andExpect(jsonPath("$.data.content.[0].username").value(user.getUsername()))
        .andExpect(jsonPath("$.data.content.[0].type").value(user.getType().toString()))
        .andExpect(jsonPath("$.data.content.[0].firstName").value(user.getFirstName()))
        .andExpect(jsonPath("$.data.content.[0].lastName").value(user.getLastName()));
  }

  private void thenListResultShouldBeEmpty(ResultActions result) throws Exception {
    result
        .andExpect(jsonPath("$.data.content").isArray())
        .andExpect(jsonPath("$.data.content").isEmpty());
  }

  private void thenCountResultShouldBeOne(ResultActions result) throws Exception {
    result.andExpect(jsonPath("$.data").value(1));
  }

  private void thenDataShouldBeTrue(ResultActions result) throws Exception {
    result.andExpect(jsonPath("$.data").value(true));
  }

  private void thenDataShouldBeFalse(ResultActions result) throws Exception {
    result.andExpect(jsonPath("$.data").value(false));
  }
}
