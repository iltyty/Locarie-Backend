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
class PostControllerTests {

    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private PostDto createPostJoleneHornsey1() throws Exception {
        UserDto userDto = TestDataUtil.newBusinessUserDtoJoleneHornsey();
        userDto.setId(null);
        PostDto postDto = TestDataUtil.newPostDtoJoleneHornsey1(userDto);
        postDto.setId(null);
        String postJson = objectMapper.writeValueAsString(postDto);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/posts")
                        .contentType("application/json")
                        .content(postJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        ).andDo(
                result -> {
                    String content = result.getResponse().getContentAsString();
                    PostDto savedPostDto = objectMapper.readValue(content, PostDto.class);
                    postDto.setId(savedPostDto.getId());
                }
        );
        return postDto;
    }

    private PostDto createPostJoleneHornsey2() throws Exception {
        UserDto userDto = TestDataUtil.newBusinessUserDtoJoleneHornsey();
        userDto.setId(null);
        PostDto postDto = TestDataUtil.newPostDtoJoleneHornsey2(userDto);
        postDto.setId(null);
        String postJson = objectMapper.writeValueAsString(postDto);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/posts")
                        .contentType("application/json")
                        .content(postJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        ).andDo(
                result -> {
                    String content = result.getResponse().getContentAsString();
                    PostDto savedPostDto = objectMapper.readValue(content, PostDto.class);
                    postDto.setId(savedPostDto.getId());
                }
        );
        return postDto;
    }

    @Test
    void testCreateReturnsHttpCreated() throws Exception {
        createPostJoleneHornsey1();
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
    void testListReturnsHttpOk() throws Exception {
        createPostJoleneHornsey1();
        createPostJoleneHornsey2();
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/posts")
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    void testListReturnsEmptyList() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/posts")
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$").isEmpty()
        );
    }

    @Test
    void testListReturnsPosts() throws Exception {
        PostDto postDto1 = createPostJoleneHornsey1();
        PostDto postDto2 = createPostJoleneHornsey2();
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/posts")
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].id").value(postDto1.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].title").value(postDto1.getTitle())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].content").value(postDto1.getContent())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[1].id").value(postDto2.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[1].title").value(postDto2.getTitle())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[1].content").value(postDto2.getContent())
        );
    }

    @Test
    void testGetReturnsHttpOk() throws Exception {
        PostDto postDto = createPostJoleneHornsey2();
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/posts/" + postDto.getId())
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
        PostDto postDto = createPostJoleneHornsey2();
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/posts/" + postDto.getId())
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(postDto.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(postDto.getTitle())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content").value(postDto.getContent()));
    }
}