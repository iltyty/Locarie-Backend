package com.locarie.backend.controllers.post.read;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.locarie.backend.datacreators.post.PostTestsDataCreator;
import com.locarie.backend.domain.dto.post.PostDto;
import com.locarie.backend.global.ResultCode;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PostReadControllerTest {
  @Autowired private MockMvc mockMvc;
  @Autowired private PostTestsDataCreator dataCreator;

  @Test
  void testListReturnsHttpOk() throws Exception {
    dataCreator.givenPostDtosJoleneHornseyAfterCreated();
    MockHttpServletRequestBuilder request = givenListPostRequest();
    mockMvc.perform(request).andExpect(status().isOk());
  }

  @Test
  void testListReturnsEmptyList() throws Exception {
    MockHttpServletRequestBuilder request = givenListPostRequest();
    mockMvc
        .perform(request)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(ResultCode.SUCCESS.getCode()))
        .andExpect(jsonPath("$.message").value(ResultCode.SUCCESS.getMessage()))
        .andExpect(jsonPath("$.data").isEmpty());
  }

  @Test
  void testListReturnsPosts() throws Exception {
    MockHttpServletRequestBuilder request = givenListPostRequest();
    PostDto dto1 = dataCreator.givenPostDtoJoleneHornsey1AfterCreated();
    PostDto dto2 = dataCreator.givenPostDtoJoleneHornsey2AfterCreated();
    mockMvc
        .perform(request)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(ResultCode.SUCCESS.getCode()))
        .andExpect(jsonPath("$.message").value(ResultCode.SUCCESS.getMessage()))
        .andExpect(jsonPath("$.data[0].id").value(dto1.getId()))
        .andExpect(jsonPath("$.data[0].title").value(dto1.getTitle()))
        .andExpect(jsonPath("$.data[0].content").value(dto1.getContent()))
        .andExpect(jsonPath("$.data[1].id").value(dto2.getId()))
        .andExpect(jsonPath("$.data[1].title").value(dto2.getTitle()))
        .andExpect(jsonPath("$.data[1].content").value(dto2.getContent()));
  }

  @Test
  void testGetReturnsHttpOk() throws Exception {
    PostDto dto = dataCreator.givenPostDtoJoleneHornsey1AfterCreated();
    MockHttpServletRequestBuilder request = givenGetPostRequest(dto.getId());
    mockMvc.perform(request).andExpect(status().isOk());
  }

  @Test
  void testGetReturnsHttpNotFound() throws Exception {
    MockHttpServletRequestBuilder request = givenGetPostRequest(0);
    mockMvc.perform(request).andExpect(status().isNotFound());
  }

  @Test
  void testGetReturnsPost() throws Exception {
    PostDto dto = dataCreator.givenPostDtoJoleneHornsey2AfterCreated();
    MockHttpServletRequestBuilder request = givenGetPostRequest(dto.getId());
    mockMvc
        .perform(request)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(ResultCode.SUCCESS.getCode()))
        .andExpect(jsonPath("$.message").value(ResultCode.SUCCESS.getMessage()))
        .andExpect(jsonPath("$.data.id").value(dto.getId()))
        .andExpect(jsonPath("$.data.title").value(dto.getTitle()))
        .andExpect(jsonPath("$.data.content").value(dto.getContent()));
  }

  @Test
  void testListUserPostsReturnCorrectData() throws Exception {
    List<PostDto> dtos = dataCreator.givenPostDtosJoleneHornseyAfterCreated();
    MockHttpServletRequestBuilder request =
        givenListUserPostsRequest(dtos.getFirst().getUser().getId());
    mockMvc
        .perform(request)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(ResultCode.SUCCESS.getCode()))
        .andExpect(jsonPath("$.message").value(ResultCode.SUCCESS.getMessage()))
        .andExpect(jsonPath("$.data[0].id").value(dtos.get(0).getId()))
        .andExpect(jsonPath("$.data[0].title").value(dtos.get(0).getTitle()))
        .andExpect(jsonPath("$.data[0].content").value(dtos.get(0).getContent()))
        .andExpect(jsonPath("$.data[1].id").value(dtos.get(1).getId()))
        .andExpect(jsonPath("$.data[1].title").value(dtos.get(1).getTitle()))
        .andExpect(jsonPath("$.data[1].content").value(dtos.get(1).getContent()));
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
}
