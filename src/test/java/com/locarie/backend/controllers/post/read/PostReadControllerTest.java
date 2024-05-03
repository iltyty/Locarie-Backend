package com.locarie.backend.controllers.post.read;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.locarie.backend.datacreators.post.PostTestsDataCreator;
import com.locarie.backend.domain.dto.post.PostDto;
import com.locarie.backend.utils.LocationBoundUtil;
import com.locarie.backend.utils.expecters.ResultExpectUtil;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PostReadControllerTest {
  private static final String List_WITHIN_URl = "/api/v1/posts/within";

  @Autowired private MockMvc mockMvc;
  @Autowired private PostTestsDataCreator dataCreator;
  @Autowired private ResultExpectUtil expectUtil;

  @Test
  void testListReturnsHttpOk() throws Exception {
    dataCreator.givenPostDtosJoleneHornseyAfterCreated();
    MockHttpServletRequestBuilder request = givenListPostRequest();
    mockMvc.perform(request).andExpect(status().isOk());
  }

  @Test
  void testListReturnsEmptyList() throws Exception {
    MockHttpServletRequestBuilder request = givenListPostRequest();
    ResultActions result = mockMvc.perform(request);
    expectUtil.thenResultShouldBeOk(result);
    thenResultShouldBeEmpty(result);
  }

  @Test
  void testListReturnsPosts() throws Exception {
    MockHttpServletRequestBuilder request = givenListPostRequest();
    PostDto post1 = dataCreator.givenPostDtoJoleneHornsey1AfterCreated();
    PostDto post2 = dataCreator.givenPostDtoJoleneHornsey2AfterCreated();
    ResultActions result = mockMvc.perform(request);
    expectUtil.thenResultShouldBeOk(result);
    thenResultShouldBeList(result, List.of(post2, post1));
  }

  @Test
  void testGetReturnsHttpOk() throws Exception {
    PostDto dto = dataCreator.givenPostDtoJoleneHornsey1AfterCreated();
    MockHttpServletRequestBuilder request = givenGetPostRequest(dto.getId());
    ResultActions result = mockMvc.perform(request);
    expectUtil.thenResultShouldBeOk(result);
  }

  @Test
  void testGetReturnsHttpNotFound() throws Exception {
    MockHttpServletRequestBuilder request = givenGetPostRequest(0);
    ResultActions result = mockMvc.perform(request);
    expectUtil.thenResultStatusShouldBeNotFound(result);
  }

  @Test
  void testGetReturnsPost() throws Exception {
    PostDto post = dataCreator.givenPostDtoJoleneHornsey2AfterCreated();
    MockHttpServletRequestBuilder request = givenGetPostRequest(post.getId());
    ResultActions result = mockMvc.perform(request);
    expectUtil.thenResultShouldBeOk(result);
    thenResultShouldBe(result, post);
  }

  @Test
  void testListUserPostsReturnCorrectData() throws Exception {
    List<PostDto> posts = dataCreator.givenPostDtosJoleneHornseyAfterCreated();
    MockHttpServletRequestBuilder request =
        givenListUserPostsRequest(posts.getFirst().getUser().getId());
    ResultActions result = mockMvc.perform(request);
    expectUtil.thenResultShouldBeOk(result);
    thenResultShouldBeList(result, posts.reversed());
  }

  @Test
  void testListPostsWithinBoundShouldReturnCorrectResult() throws Exception {
    List<PostDto> posts1 = dataCreator.givenPostDtosShreejiAfterCreated();
    List<PostDto> posts2 = dataCreator.givenPostDtosJoleneHornseyAfterCreated();
    Point[] bound = LocationBoundUtil.postLocationBound(posts1.getLast(), posts2.getLast());
    MockHttpServletRequestBuilder request = givenListWithinRequest(bound);
    List<PostDto> expect = List.of(posts2.getLast(), posts1.getLast());

    ResultActions result = mockMvc.perform(request);
    expectUtil.thenResultShouldBeOk(result);
    thenResultShouldBeList(result, expect);
  }

  private MockHttpServletRequestBuilder givenListPostRequest() {
    return MockMvcRequestBuilders.get("/api/v1/posts");
  }

  private MockHttpServletRequestBuilder givenListUserPostsRequest(long id) {
    return MockMvcRequestBuilders.get("/api/v1/posts/user/" + id);
  }

  private MockHttpServletRequestBuilder givenGetPostRequest(long id) {
    return MockMvcRequestBuilders.get("/api/v1/posts/" + id);
  }

  private MockHttpServletRequestBuilder givenListWithinRequest(Point[] bound) {
    return MockMvcRequestBuilders.get(List_WITHIN_URl).params(prepareListWithinParams(bound));
  }

  private MultiValueMap<String, String> prepareListWithinParams(Point[] bound) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("minLatitude", String.valueOf(bound[1].getY()));
    params.add("maxLatitude", String.valueOf(bound[0].getY()));
    params.add("minLongitude", String.valueOf(bound[0].getX()));
    params.add("maxLongitude", String.valueOf(bound[1].getX()));
    return params;
  }

  private void thenResultShouldBeEmpty(ResultActions result) throws Exception {
    result.andExpect(jsonPath("$.data").isArray()).andExpect(jsonPath("$.data").isEmpty());
  }

  private void thenResultShouldBeNull(ResultActions result) throws Exception {
    result.andExpect(jsonPath("$.data").doesNotExist());
  }

  private void thenResultShouldBeList(ResultActions result, List<PostDto> expect) throws Exception {
    result
        .andExpect(jsonPath("$.data").isArray())
        .andExpect(jsonPath("$.data.length()").value(expect.size()));
    for (int i = 0; i < expect.size(); i++) {
      PostDto dto = expect.get(i);
      result
          .andExpect(jsonPath("$.data[" + i + "].id").value(dto.getId()))
          .andExpect(jsonPath("$.data[" + i + "].content").value(dto.getContent()));
    }
  }

  private void thenResultShouldBe(ResultActions result, PostDto expect) throws Exception {
    result
        .andExpect(jsonPath("$.data.id").value(expect.getId()))
        .andExpect(jsonPath("$.data.content").value(expect.getContent()));
  }
}
