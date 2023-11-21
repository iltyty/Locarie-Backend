package com.locarie.backend.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.locarie.backend.TestDataUtil;
import com.locarie.backend.domain.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void testUserSerialization() throws JsonProcessingException {
        User user = TestDataUtil.newBusinessUserJoleneHornsey();
        String userJson = mapper.writeValueAsString(user);
        User deserializedUser = mapper.readValue(userJson, User.class);
        assertThat(user).isEqualTo(deserializedUser);
    }

    @Test
    void testCreateUserReturnsHttpCreated() throws Exception {
        User user = TestDataUtil.newBusinessUserJoleneHornsey();
        String userJson = mapper.writeValueAsString(user);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    void testCreateUserReturnsUser() throws Exception {
        User user = TestDataUtil.newBusinessUserJoleneHornsey();
        user.setId(null);
        String userJson = mapper.writeValueAsString(user);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.username").value("Jolene Hornsey")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.location.latitude")
                        .value(is(user.getLocation().getY()), Double.class)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.location.longitude")
                        .value(is(user.getLocation().getX()), Double.class)
        );
    }
}