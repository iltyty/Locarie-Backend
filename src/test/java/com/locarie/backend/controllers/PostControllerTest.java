package com.locarie.backend.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.locarie.backend.TestDataUtil;
import com.locarie.backend.domain.dto.PostDto;
import com.locarie.backend.domain.dto.UserDto;
import com.locarie.backend.domain.dto.UserRegistrationDto;
import com.locarie.backend.global.ResultCode;
import com.locarie.backend.services.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PostControllerTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final MockMultipartFile avatar =
            new MockMultipartFile("avatar", "avatar.jpg", "image/jpeg", new byte[1]);
    @Autowired private MockMvc mockMvc;
    @Autowired private UserService userService;

    private UserDto registerBusinessUserJoleneHornsey() {
        UserRegistrationDto userDto = TestDataUtil.newBusinessUserRegistrationDtoJoleneHornsey();
        return userService.register(userDto, avatar);
    }

    private PostDto createPostJoleneHornsey1(final UserDto userDto) throws Exception {
        PostDto postDto = TestDataUtil.newPostDtoJoleneHornsey1(userDto);
        postDto.setId(null);
        String postJson = objectMapper.writeValueAsString(postDto);
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/posts")
                                .contentType("application/json")
                                .content(postJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(
                        result -> {
                            String content = result.getResponse().getContentAsString();
                            JsonNode dataNode = objectMapper.readTree(content).get("data");
                            PostDto savedPostDto =
                                    objectMapper.treeToValue(dataNode, PostDto.class);
                            postDto.setId(savedPostDto.getId());
                        });
        return postDto;
    }

    private PostDto createPostJoleneHornsey2(final UserDto userDto) throws Exception {
        PostDto postDto = TestDataUtil.newPostDtoJoleneHornsey2(userDto);
        postDto.setId(null);
        String postJson = objectMapper.writeValueAsString(postDto);
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/posts")
                                .contentType("application/json")
                                .content(postJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(
                        result -> {
                            String content = result.getResponse().getContentAsString();
                            JsonNode dataNode = objectMapper.readTree(content).get("data");
                            PostDto savedPostDto =
                                    objectMapper.treeToValue(dataNode, PostDto.class);
                            postDto.setId(savedPostDto.getId());
                        });
        return postDto;
    }

    @Test
    void testCreateReturnsHttpCreated() throws Exception {
        UserDto userDto = registerBusinessUserJoleneHornsey();
        createPostJoleneHornsey1(userDto);
        createPostJoleneHornsey2(userDto);
    }

    @Test
    void testCreateReturnsPost() throws Exception {
        UserDto userDto = registerBusinessUserJoleneHornsey();
        PostDto postDto = TestDataUtil.newPostDtoJoleneHornsey1(userDto);
        postDto.setId(null);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/posts")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(postDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.title").value(postDto.getTitle()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.data.content")
                                .value(postDto.getContent()));
    }

    @Test
    void testListReturnsHttpOk() throws Exception {
        UserDto userDto = registerBusinessUserJoleneHornsey();
        createPostJoleneHornsey1(userDto);
        createPostJoleneHornsey2(userDto);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/posts"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testListReturnsEmptyList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/posts"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.status")
                                .value(ResultCode.SUCCESS.getCode()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.message")
                                .value(ResultCode.SUCCESS.getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
    }

    @Test
    void testListReturnsPosts() throws Exception {
        UserDto userDto = registerBusinessUserJoleneHornsey();
        PostDto postDto1 = createPostJoleneHornsey1(userDto);
        PostDto postDto2 = createPostJoleneHornsey2(userDto);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/posts"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.status")
                                .value(ResultCode.SUCCESS.getCode()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.message")
                                .value(ResultCode.SUCCESS.getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(postDto1.getId()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.data[0].title")
                                .value(postDto1.getTitle()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.data[0].content")
                                .value(postDto1.getContent()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].id").value(postDto2.getId()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.data[1].title")
                                .value(postDto2.getTitle()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.data[1].content")
                                .value(postDto2.getContent()));
    }

    @Test
    void testGetReturnsHttpOk() throws Exception {
        UserDto userDto = registerBusinessUserJoleneHornsey();
        PostDto postDto = createPostJoleneHornsey2(userDto);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/posts/" + postDto.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testGetReturnsHttpNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/posts/0"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testGetReturnsPost() throws Exception {
        UserDto userDto = registerBusinessUserJoleneHornsey();
        PostDto postDto = createPostJoleneHornsey2(userDto);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/posts/" + postDto.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.status")
                                .value(ResultCode.SUCCESS.getCode()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.message")
                                .value(ResultCode.SUCCESS.getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(postDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.title").value(postDto.getTitle()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.data.content")
                                .value(postDto.getContent()));
    }
}
