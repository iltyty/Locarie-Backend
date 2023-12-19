package com.locarie.backend.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.locarie.backend.domain.dto.PostDto;
import com.locarie.backend.domain.dto.UserDto;
import com.locarie.backend.domain.dto.UserRegistrationDto;
import com.locarie.backend.global.ResultCode;
import com.locarie.backend.services.user.UserService;
import com.locarie.backend.utils.post.PostDtoCreator;
import com.locarie.backend.utils.user.UserRegistrationDtoCreator;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PostControllerTest {

  private static UserDto userDto;
  private static final ObjectMapper objectMapper = new ObjectMapper();
  private static final MockMultipartFile avatar =
      new MockMultipartFile("avatar", "avatar.jpg", "image/jpeg", new byte[1]);
  private static final MockMultipartFile postImage =
      new MockMultipartFile("images", "image.jpg", "image/jpeg", new byte[1]);
  @Autowired private MockMvc mockMvc;
  @Autowired private UserService userService;

  @BeforeEach
  public void registerBusinessUserJoleneHornsey() {
    UserRegistrationDto userRegistrationDto =
        UserRegistrationDtoCreator.businessUserRegistrationDtoJoleneHornsey();
    userRegistrationDto.setId(null);
    userDto = userService.register(userRegistrationDto, avatar);
  }

  private static MockPart createPostPart(PostDto postDto) throws JsonProcessingException {
    String postJson = objectMapper.writeValueAsString(postDto);
    MockPart postPart = new MockPart("post", postJson.getBytes());
    postPart.getHeaders().setContentType(MediaType.APPLICATION_JSON);
    return postPart;
  }

  private PostDto createPostJoleneHornsey1() throws Exception {
    PostDto postDto = PostDtoCreator.postDtoJoleneHornsey1();
    postDto.setId(null);
    postDto.setUser(userDto);
    mockMvc
        .perform(
            MockMvcRequestBuilders.multipart("/api/v1/posts")
                .file(postImage)
                .part(createPostPart(postDto)))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andDo(
            result -> {
              String content = result.getResponse().getContentAsString();
              JsonNode dataNode = objectMapper.readTree(content).get("data");
              PostDto savedPostDto = objectMapper.treeToValue(dataNode, PostDto.class);
              postDto.setId(savedPostDto.getId());
            });
    return postDto;
  }

  private PostDto createPostJoleneHornsey2() throws Exception {
    PostDto postDto = PostDtoCreator.postDtoJoleneHornsey2();
    postDto.setId(null);
    postDto.setUser(userDto);
    mockMvc
        .perform(
            MockMvcRequestBuilders.multipart("/api/v1/posts")
                .file(postImage)
                .part(createPostPart(postDto)))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andDo(
            result -> {
              String content = result.getResponse().getContentAsString();
              JsonNode dataNode = objectMapper.readTree(content).get("data");
              PostDto savedPostDto = objectMapper.treeToValue(dataNode, PostDto.class);
              postDto.setId(savedPostDto.getId());
            });
    return postDto;
  }

  @Test
  void testCreateReturnsHttpCreated() throws Exception {
    createPostJoleneHornsey1();
    createPostJoleneHornsey2();
  }

  @Test
  void testCreateReturnsPost() throws Exception {
    PostDto postDto = PostDtoCreator.postDtoJoleneHornsey1();
    postDto.setId(null);
    postDto.setUser(userDto);

    mockMvc
        .perform(
            MockMvcRequestBuilders.multipart("/api/v1/posts")
                .file(postImage)
                .part(createPostPart(postDto)))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.title").value(postDto.getTitle()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.content").value(postDto.getContent()));
  }

  @Test
  void testListReturnsHttpOk() throws Exception {
    createPostJoleneHornsey1();
    createPostJoleneHornsey2();
    mockMvc
        .perform(MockMvcRequestBuilders.get("/api/v1/posts"))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  void testListReturnsEmptyList() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.get("/api/v1/posts"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(ResultCode.SUCCESS.getCode()))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.message").value(ResultCode.SUCCESS.getMessage()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
  }

  @Test
  void testListReturnsPosts() throws Exception {
    PostDto postDto1 = createPostJoleneHornsey1();
    PostDto postDto2 = createPostJoleneHornsey2();
    mockMvc
        .perform(MockMvcRequestBuilders.get("/api/v1/posts"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(ResultCode.SUCCESS.getCode()))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.message").value(ResultCode.SUCCESS.getMessage()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(postDto1.getId()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].title").value(postDto1.getTitle()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].content").value(postDto1.getContent()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].id").value(postDto2.getId()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].title").value(postDto2.getTitle()))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.data[1].content").value(postDto2.getContent()));
  }

  @Test
  void testGetReturnsHttpOk() throws Exception {
    PostDto postDto = createPostJoleneHornsey2();
    mockMvc
        .perform(MockMvcRequestBuilders.get("/api/v1/posts/" + postDto.getId()))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  void testGetReturnsHttpNotFound() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.get("/api/v1/posts/0"))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  void testGetReturnsPost() throws Exception {
    PostDto postDto = createPostJoleneHornsey2();
    mockMvc
        .perform(MockMvcRequestBuilders.get("/api/v1/posts/" + postDto.getId()))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(ResultCode.SUCCESS.getCode()))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.message").value(ResultCode.SUCCESS.getMessage()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(postDto.getId()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.title").value(postDto.getTitle()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.content").value(postDto.getContent()));
  }
}
