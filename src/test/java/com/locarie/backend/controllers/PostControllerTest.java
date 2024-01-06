package com.locarie.backend.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.locarie.backend.datacreators.post.PostDtoCreator;
import com.locarie.backend.datacreators.user.UserEntityCreator;
import com.locarie.backend.domain.dto.post.PostDto;
import com.locarie.backend.domain.dto.user.UserDto;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.global.ResultCode;
import com.locarie.backend.mapper.Mapper;
import com.locarie.backend.repositories.user.UserRepository;
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

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PostControllerTest {
  private static UserDto userDto;
  private static final MockMultipartFile postImage =
      new MockMultipartFile("images", "image.jpg", "image/jpeg", new byte[1]);

  @Autowired private MockMvc mockMvc;
  @Autowired private UserRepository userRepository;
  @Autowired private ObjectMapper objectMapper;
  @Autowired private Mapper<UserEntity, UserDto> mapper;

  @BeforeEach
  public void createBusinessUserJoleneHornsey() {
    UserEntity userEntity = UserEntityCreator.businessUserEntityJoleneHornsey();
    UserEntity savedUserEntity = userRepository.save(userEntity);
    userDto = mapper.mapTo(savedUserEntity);
  }

  private MockPart createPostPart(PostDto postDto) throws JsonProcessingException {
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
        .andExpect(status().isCreated())
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
        .andExpect(status().isCreated())
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
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.data.id").exists())
        .andExpect(jsonPath("$.data.title").value(postDto.getTitle()))
        .andExpect(jsonPath("$.data.content").value(postDto.getContent()));
  }

  @Test
  void testListReturnsHttpOk() throws Exception {
    createPostJoleneHornsey1();
    createPostJoleneHornsey2();
    mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/posts")).andExpect(status().isOk());
  }

  @Test
  void testListReturnsEmptyList() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.get("/api/v1/posts"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(ResultCode.SUCCESS.getCode()))
        .andExpect(jsonPath("$.message").value(ResultCode.SUCCESS.getMessage()))
        .andExpect(jsonPath("$.data").isEmpty());
  }

  @Test
  void testListReturnsPosts() throws Exception {
    PostDto postDto1 = createPostJoleneHornsey1();
    PostDto postDto2 = createPostJoleneHornsey2();
    mockMvc
        .perform(MockMvcRequestBuilders.get("/api/v1/posts"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(ResultCode.SUCCESS.getCode()))
        .andExpect(jsonPath("$.message").value(ResultCode.SUCCESS.getMessage()))
        .andExpect(jsonPath("$.data[0].id").value(postDto1.getId()))
        .andExpect(jsonPath("$.data[0].title").value(postDto1.getTitle()))
        .andExpect(jsonPath("$.data[0].content").value(postDto1.getContent()))
        .andExpect(jsonPath("$.data[1].id").value(postDto2.getId()))
        .andExpect(jsonPath("$.data[1].title").value(postDto2.getTitle()))
        .andExpect(jsonPath("$.data[1].content").value(postDto2.getContent()));
  }

  @Test
  void testGetReturnsHttpOk() throws Exception {
    PostDto postDto = createPostJoleneHornsey2();
    mockMvc
        .perform(MockMvcRequestBuilders.get("/api/v1/posts/" + postDto.getId()))
        .andExpect(status().isOk());
  }

  @Test
  void testGetReturnsHttpNotFound() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/posts/0")).andExpect(status().isNotFound());
  }

  @Test
  void testGetReturnsPost() throws Exception {
    PostDto postDto = createPostJoleneHornsey2();
    mockMvc
        .perform(MockMvcRequestBuilders.get("/api/v1/posts/" + postDto.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(ResultCode.SUCCESS.getCode()))
        .andExpect(jsonPath("$.message").value(ResultCode.SUCCESS.getMessage()))
        .andExpect(jsonPath("$.data.id").value(postDto.getId()))
        .andExpect(jsonPath("$.data.title").value(postDto.getTitle()))
        .andExpect(jsonPath("$.data.content").value(postDto.getContent()));
  }
}
