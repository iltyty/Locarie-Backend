package com.locarie.backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.locarie.backend.TestDataUtil;
import com.locarie.backend.domain.dto.PostDto;
import com.locarie.backend.domain.dto.UserDto;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testCreateReturnsHttpCreated() throws Exception {
        // data preparation
        UserDto userDto = TestDataUtil.newBusinessUserDtoJoleneHornsey();
        userDto.setId(null);
        PostDto postDto = TestDataUtil.newPostDtoJoleneHornsey1(userDto);
        postDto.setId(null);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/posts")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(postDto))
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    void testCreateReturnsPost() throws Exception {
        UserDto userDto = TestDataUtil.newBusinessUserDtoJoleneHornsey();
        userDto.setId(null);
        PostDto postDto = TestDataUtil.newPostDtoJoleneHornsey1(userDto);
        postDto.setId(null);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/posts")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(postDto))
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").exists()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(postDto.getTitle())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content").value(postDto.getContent())
        );
    }

    @Test
    void testGetReturnsHttpOk() throws Exception {
        // data preparation
        UserDto userDto = TestDataUtil.newBusinessUserDtoJoleneHornsey();
        userDto.setId(null);
        PostDto postDto = TestDataUtil.newPostDtoJoleneHornsey2(userDto);
        postDto.setId(null);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/posts")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(postDto))
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/posts/1")
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    void testGetReturnsHttpNotFound() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/posts/1")
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    void testGetReturnsPost() throws Exception {
        UserDto userDto = TestDataUtil.newBusinessUserDtoJoleneHornsey();
        userDto.setId(null);
        PostDto postDto = TestDataUtil.newPostDtoJoleneHornsey2(userDto);
        postDto.setId(null);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/posts")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(postDto))
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/posts/11")
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").exists()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(postDto.getTitle())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content").value(postDto.getContent()));
    }
}