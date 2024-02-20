package com.locarie.backend.controllers.post.create;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.locarie.backend.datacreators.post.PostDtoCreator;
import com.locarie.backend.datacreators.user.UserEntityCreator;
import com.locarie.backend.domain.dto.post.PostDto;
import com.locarie.backend.domain.dto.user.UserDto;
import com.locarie.backend.domain.entities.UserEntity;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PostCreateControllerTest {
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

  @Test
  void testCreateReturnsPost() throws Exception {
    PostDto dto = givenPostDto();
    MockMultipartHttpServletRequestBuilder request = givenCreatePostRequest(dto);
    ResultActions result = mockMvc.perform(request);
    thenResultDataShouldBeCorrect(result, dto);
  }

  private PostDto givenPostDto() {
    PostDto postDto = PostDtoCreator.postDtoJoleneHornsey1();
    postDto.setId(null);
    postDto.setUser(userDto);
    return postDto;
  }

  private MockMultipartHttpServletRequestBuilder givenCreatePostRequest(PostDto dto)
      throws JsonProcessingException {
    return MockMvcRequestBuilders.multipart("/api/v1/posts")
        .file(postImage)
        .part(createPostPart(dto));
  }

  private void thenResultDataShouldBeCorrect(ResultActions result, PostDto dto) throws Exception {
    result
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.data.id").exists())
        .andExpect(jsonPath("$.data.content").value(dto.getContent()));
  }

  private MockPart createPostPart(PostDto postDto) throws JsonProcessingException {
    String postJson = objectMapper.writeValueAsString(postDto);
    MockPart postPart = new MockPart("post", postJson.getBytes());
    postPart.getHeaders().setContentType(MediaType.APPLICATION_JSON);
    return postPart;
  }
}
