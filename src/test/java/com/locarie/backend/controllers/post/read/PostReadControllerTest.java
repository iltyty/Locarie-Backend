package com.locarie.backend.controllers.post.read;

import com.locarie.backend.datacreators.post.PostTestsDataCreator;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
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
    PostDto dto1 = dataCreator.givenPostDtoJoleneHornsey1AfterCreated();
    PostDto dto2 = dataCreator.givenPostDtoJoleneHornsey2AfterCreated();
    mockMvc
        .perform(MockMvcRequestBuilders.get("/api/v1/posts"))
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
    mockMvc
        .perform(MockMvcRequestBuilders.get("/api/v1/posts/" + dto.getId()))
        .andExpect(status().isOk());
  }

  @Test
  void testGetReturnsHttpNotFound() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/posts/0")).andExpect(status().isNotFound());
  }

  @Test
  void testGetReturnsPost() throws Exception {
    PostDto dto = dataCreator.givenPostDtoJoleneHornsey2AfterCreated();
    mockMvc
        .perform(MockMvcRequestBuilders.get("/api/v1/posts/" + dto.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(ResultCode.SUCCESS.getCode()))
        .andExpect(jsonPath("$.message").value(ResultCode.SUCCESS.getMessage()))
        .andExpect(jsonPath("$.data.id").value(dto.getId()))
        .andExpect(jsonPath("$.data.title").value(dto.getTitle()))
        .andExpect(jsonPath("$.data.content").value(dto.getContent()));
  }
}
