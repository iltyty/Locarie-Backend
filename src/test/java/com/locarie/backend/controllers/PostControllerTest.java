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
}